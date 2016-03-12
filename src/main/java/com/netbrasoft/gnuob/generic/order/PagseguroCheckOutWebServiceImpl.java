/*
 * Copyright 2016 Netbrasoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.netbrasoft.gnuob.generic.order;

import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME;

import java.util.Iterator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.OrderByEnum;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.content.mail.MailAction;
import com.netbrasoft.gnuob.generic.content.mail.MailControl;
import com.netbrasoft.gnuob.generic.content.mail.MailEnum;
import com.netbrasoft.gnuob.generic.contract.Contract;
import com.netbrasoft.gnuob.generic.customer.Customer;
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.ISecuredGenericTypeCheckOutService;
import com.netbrasoft.gnuob.generic.security.ISecuredGenericTypeService;
import com.netbrasoft.gnuob.generic.security.SecuredPagseguroCheckOutServiceImpl;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = PagseguroCheckOutWebServiceImpl.PAGSEGURO_CHECK_OUT_WEB_SERVICE_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class, MailControl.class})
public class PagseguroCheckOutWebServiceImpl<T extends Order> implements ICheckOutWebService<T> {

  protected static final String PAGSEGURO_CHECK_OUT_WEB_SERVICE_IMPL_NAME = "PagseguroCheckOutWebServiceImpl";

  @EJB(beanName = SecuredPagseguroCheckOutServiceImpl.SECURED_PAGSEGURO_CHECK_OUT_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeCheckOutService<T> securedGenericTypeCheckOutService;

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<T> securedGenericOrderService;

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<Contract> securedGenericContractService;

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<Customer> securedGenericCustomerService;

  private void createUpdateContractCustomer(final MetaData metaData, final Contract contract) {
    if (contract != null) {
      if (contract.getId() == 0) {
        securedGenericContractService.create(metaData, contract);
      } else {
        securedGenericContractService.update(metaData, contract);
      }
      createUpdateCustomer(metaData, contract.getCustomer());
    }
  }

  private void createUpdateCustomer(final MetaData metaData, final Customer customer) {
    if (customer != null) {
      if (customer.getId() == 0) {
        securedGenericCustomerService.create(metaData, customer);
      } else {
        securedGenericCustomerService.update(metaData, customer);
      }
    }
  }

  @Override
  @WebMethod(operationName = "doCheckout")
  @MailAction(operation = MailEnum.NO_MAIL)
  public T doCheckout(@WebParam(name = "metaData", header = true) final MetaData metaData,
      @WebParam(name = "order") final T type) {
    try {
      createUpdateContractCustomer(metaData, type.getContract());
      securedGenericTypeCheckOutService.doCheckout(metaData, type);
      if (type.isDetached()) {
        return securedGenericOrderService.merge(metaData, type);
      }
      securedGenericOrderService.persist(metaData, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "doCheckoutDetails")
  @MailAction(operation = MailEnum.NO_MAIL)
  public T doCheckoutDetails(@WebParam(name = "metaData", header = true) final MetaData metaData,
      @WebParam(name = "order") final T type) {
    try {
      createUpdateContractCustomer(metaData, type.getContract());
      securedGenericTypeCheckOutService.doCheckoutDetails(metaData, type);
      if (type.isDetached()) {
        return securedGenericOrderService.merge(metaData, type);
      }
      securedGenericOrderService.persist(metaData, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "doCheckoutPayment")
  @MailAction(operation = MailEnum.CONFIRMATION_NEW_ORDER_MAIL)
  public T doCheckoutPayment(@WebParam(name = "metaData", header = true) final MetaData metaData,
      @WebParam(name = "order") final T type) {
    try {
      createUpdateContractCustomer(metaData, type.getContract());
      securedGenericTypeCheckOutService.doCheckoutPayment(metaData, type);
      if (type.isDetached()) {
        return securedGenericOrderService.merge(metaData, type);
      }
      securedGenericOrderService.persist(metaData, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "doNotification")
  @MailAction(operation = MailEnum.NO_MAIL)
  public T doNotification(@WebParam(name = "metaData", header = true) final MetaData metaData,
      @WebParam(name = "order") T type) {
    try {
      type = securedGenericTypeCheckOutService.doNotification(metaData, type);

      final String transactionId = type.getTransactionId();

      type.setActive(true);
      type.setTransactionId(null);
      type.setNotificationId(null);

      final Iterator<T> iterator =
          securedGenericOrderService.find(metaData, type, Paging.getInstance(0, 1), OrderByEnum.NONE).iterator();

      if (iterator.hasNext()) {
        type = iterator.next();
        type.setTransactionId(transactionId);

        createUpdateContractCustomer(metaData, type.getContract());
        securedGenericTypeCheckOutService.doTransactionDetails(metaData, type);
        if (type.isDetached()) {
          return securedGenericOrderService.merge(metaData, type);
        }
        securedGenericOrderService.persist(metaData, type);
        return type;
      } else {
        throw new GNUOpenBusinessServiceException(
            "Exception from Pagseguro Notification, no order found for the given notification code.");
      }
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "doRefundTransaction")
  @MailAction(operation = MailEnum.NO_MAIL)
  public T doRefundTransaction(@WebParam(name = "metaData", header = true) final MetaData metaData,
      @WebParam(name = "order") final T type) {
    try {
      createUpdateContractCustomer(metaData, type.getContract());
      securedGenericTypeCheckOutService.doRefundTransaction(metaData, type);
      if (type.isDetached()) {
        return securedGenericOrderService.merge(metaData, type);
      }
      securedGenericOrderService.persist(metaData, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "doTransactionDetails")
  @MailAction(operation = MailEnum.NO_MAIL)
  public T doTransactionDetails(@WebParam(name = "metaData", header = true) final MetaData metaData,
      @WebParam(name = "order") final T type) {
    try {
      createUpdateContractCustomer(metaData, type.getContract());
      securedGenericTypeCheckOutService.doTransactionDetails(metaData, type);
      if (type.isDetached()) {
        return securedGenericOrderService.merge(metaData, type);
      }
      securedGenericOrderService.persist(metaData, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
