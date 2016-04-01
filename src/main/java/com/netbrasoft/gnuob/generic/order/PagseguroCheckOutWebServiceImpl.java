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

import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DO_CHECKOUT_DETAILS_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DO_CHECKOUT_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DO_CHECKOUT_PAYMENT_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DO_NOTIFICATION_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DO_REFUND_TRANSACTION_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DO_TRANSACTION_DETAILS_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GNUOB_WEB_SERVICE_TARGET_NAMESPACE;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.META_DATA_PARAM_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_PARAM_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAGSEGURO_CHECK_OUT_WEB_SERVICE_IMPL_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SECURED_PAGSEGURO_CHECK_OUT_SERVICE_IMPL_NAME;

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
import com.netbrasoft.gnuob.generic.security.ISecuredGenericTypeCheckOutService;
import com.netbrasoft.gnuob.generic.security.ISecuredGenericTypeService;
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = GNUOB_WEB_SERVICE_TARGET_NAMESPACE)
@Stateless(name = PAGSEGURO_CHECK_OUT_WEB_SERVICE_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class, MailControl.class})
public class PagseguroCheckOutWebServiceImpl<T extends Order> implements ICheckOutWebService<T> {

  @EJB(beanName = SECURED_PAGSEGURO_CHECK_OUT_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeCheckOutService<T> securedGenericTypeCheckOutService;

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<T> securedGenericOrderService;

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<Contract> securedGenericContractService;

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<Customer> securedGenericCustomerService;

  public PagseguroCheckOutWebServiceImpl() {}

  PagseguroCheckOutWebServiceImpl(ISecuredGenericTypeService<T> securedGenericOrderService,
      ISecuredGenericTypeService<Contract> securedGenericContractService,
      ISecuredGenericTypeService<Customer> securedGenericCustomerService) {
    this.securedGenericOrderService = securedGenericOrderService;
    this.securedGenericContractService = securedGenericContractService;
    this.securedGenericCustomerService = securedGenericCustomerService;
  }

  @Override
  @WebMethod(operationName = DO_CHECKOUT_OPERATION_NAME)
  @MailAction(operation = MailEnum.NO_MAIL)
  public T doCheckout(
      @WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = ORDER_PARAM_NAME) final T type) {
    try {
      createUpdateContract(credentials, type.getContract());
      securedGenericTypeCheckOutService.doCheckout(credentials, type);
      return mergePersistOrder(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void createUpdateContract(final MetaData credentials, final Contract contract) {
    if (contract != null) {
      if (contract.isDetached()) {
        securedGenericContractService.update(credentials, contract);
      } else {
        securedGenericContractService.create(credentials, contract);
      }
      createUpdateCustomer(credentials, contract.getCustomer());
    }
  }

  private void createUpdateCustomer(final MetaData credentials, final Customer customer) {
    if (customer != null) {
      if (customer.isDetached()) {
        securedGenericCustomerService.update(credentials, customer);
      } else {
        securedGenericCustomerService.create(credentials, customer);
      }
    }
  }

  private T mergePersistOrder(final MetaData credentials, final T type) {
    if (type.isDetached()) {
      return securedGenericOrderService.merge(credentials, type);
    }
    securedGenericOrderService.persist(credentials, type);
    return type;
  }

  @Override
  @WebMethod(operationName = DO_CHECKOUT_DETAILS_OPERATION_NAME)
  @MailAction(operation = MailEnum.NO_MAIL)
  public T doCheckoutDetails(
      @WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = ORDER_PARAM_NAME) final T type) {
    try {
      createUpdateContract(credentials, type.getContract());
      securedGenericTypeCheckOutService.doCheckoutDetails(credentials, type);
      return mergePersistOrder(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = DO_CHECKOUT_PAYMENT_OPERATION_NAME)
  @MailAction(operation = MailEnum.CONFIRMATION_NEW_ORDER_MAIL)
  public T doCheckoutPayment(
      @WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = ORDER_PARAM_NAME) final T type) {
    try {
      createUpdateContract(credentials, type.getContract());
      securedGenericTypeCheckOutService.doCheckoutPayment(credentials, type);
      return mergePersistOrder(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = DO_NOTIFICATION_OPERATION_NAME)
  @MailAction(operation = MailEnum.NO_MAIL)
  public T doNotification(
      @WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = ORDER_PARAM_NAME) T type) {
    try {
      // Lege order met getNotificationId code gezet

      type = securedGenericTypeCheckOutService.doNotification(credentials, type);

      // Lege order met getNotificationId + TransactionId + OrderId

      final String transactionId = type.getTransactionId();

      type.setActive(true);
      type.setTransactionId(null);
      type.setNotificationId(null);

      if (securedGenericOrderService.count(credentials, type) > 0) {

        final Iterator<T> iterator = securedGenericOrderService
            .find(credentials, type, Paging.getInstance(0, 1), OrderByEnum.NONE).iterator();


        type = iterator.next();
        type.setTransactionId(transactionId);

        createUpdateContract(credentials, type.getContract());
        securedGenericTypeCheckOutService.doTransactionDetails(credentials, type);
        return mergePersistOrder(credentials, type);
      } else {
        throw new GNUOpenBusinessServiceException(
            "No order available based on the received notification code.");
      }
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = DO_REFUND_TRANSACTION_OPERATION_NAME)
  @MailAction(operation = MailEnum.NO_MAIL)
  public T doRefundTransaction(
      @WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = ORDER_PARAM_NAME) final T type) {
    try {
      createUpdateContract(credentials, type.getContract());
      securedGenericTypeCheckOutService.doRefundTransaction(credentials, type);
      return mergePersistOrder(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = DO_TRANSACTION_DETAILS_OPERATION_NAME)
  @MailAction(operation = MailEnum.NO_MAIL)
  public T doTransactionDetails(
      @WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = ORDER_PARAM_NAME) final T type) {
    try {
      createUpdateContract(credentials, type.getContract());
      securedGenericTypeCheckOutService.doTransactionDetails(credentials, type);
      return mergePersistOrder(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
