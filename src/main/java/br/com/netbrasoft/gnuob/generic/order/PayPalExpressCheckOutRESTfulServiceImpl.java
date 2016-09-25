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

package br.com.netbrasoft.gnuob.generic.order;

import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DO_CHECKOUT_DETAILS_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DO_CHECKOUT_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DO_CHECKOUT_PAYMENT_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DO_NOTIFICATION_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DO_REFUND_TRANSACTION_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DO_TRANSACTION_DETAILS_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.META_DATA_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAY_PAL_EXPRESS_CHECK_OUT_RESTFUL_SERVICE_IMPL;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SECURED_PAY_PAL_EXPRESS_CHECK_OUT_SERVICE_IMPL_NAME;
import static br.com.netbrasoft.gnuob.generic.OrderByEnum.NONE;
import static br.com.netbrasoft.gnuob.generic.content.mail.MailEnum.CONFIRMATION_NEW_ORDER_MAIL;
import static br.com.netbrasoft.gnuob.generic.content.mail.MailEnum.NO_MAIL;
import static br.com.netbrasoft.gnuob.generic.factory.MessageCreaterFactory.createMessage;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.slf4j.LoggerFactory.getLogger;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;

import br.com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import br.com.netbrasoft.gnuob.generic.Paging;
import br.com.netbrasoft.gnuob.generic.content.mail.MailAction;
import br.com.netbrasoft.gnuob.generic.content.mail.MailControl;
import br.com.netbrasoft.gnuob.generic.contract.Contract;
import br.com.netbrasoft.gnuob.generic.customer.Customer;
import br.com.netbrasoft.gnuob.generic.security.ISecuredGenericTypeCheckOutService;
import br.com.netbrasoft.gnuob.generic.security.ISecuredGenericTypeService;
import br.com.netbrasoft.gnuob.generic.security.MetaData;
import br.com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@Path(PAY_PAL_EXPRESS_CHECK_OUT_RESTFUL_SERVICE_IMPL)
@Consumes({APPLICATION_JSON})
@Produces({APPLICATION_JSON})
@Interceptors(value = {AppSimonInterceptor.class, MailControl.class})
public class PayPalExpressCheckOutRESTfulServiceImpl implements ICheckOutWebService<Order> {

  private static final Logger LOGGER = getLogger(PayPalExpressCheckOutRESTfulServiceImpl.class);

  @EJB(beanName = SECURED_PAY_PAL_EXPRESS_CHECK_OUT_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeCheckOutService<Order> securedGenericTypeCheckOutService;

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<Order> securedGenericOrderService;

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<Contract> securedGenericContractService;

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<Customer> securedGenericCustomerService;

  public PayPalExpressCheckOutRESTfulServiceImpl() {
    // This constructor will be used by the EJB container.
  }

  PayPalExpressCheckOutRESTfulServiceImpl(
      final ISecuredGenericTypeCheckOutService<Order> securedGenericTypeCheckOutService,
      final ISecuredGenericTypeService<Order> securedGenericOrderService,
      final ISecuredGenericTypeService<Contract> securedGenericContractService,
      final ISecuredGenericTypeService<Customer> securedGenericCustomerService) {
    this.securedGenericTypeCheckOutService = securedGenericTypeCheckOutService;
    this.securedGenericOrderService = securedGenericOrderService;
    this.securedGenericContractService = securedGenericContractService;
    this.securedGenericCustomerService = securedGenericCustomerService;
  }

  @Override
  @POST
  @Path(DO_CHECKOUT_OPERATION_NAME)
  @MailAction(operation = NO_MAIL)
  public Order doCheckout(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(ORDER_PARAM_NAME) final Order type) {
    try {
      return processCheckout(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(DO_CHECKOUT_OPERATION_NAME, credentials, type));
    }
  }

  private Order processCheckout(final MetaData credentials, final Order type) {
    createUpdateContract(credentials, type.getContract());
    securedGenericTypeCheckOutService.doCheckout(credentials, type);
    return mergePersistOrder(credentials, type);
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

  private Order mergePersistOrder(final MetaData credentials, final Order type) {
    if (type.isDetached()) {
      return securedGenericOrderService.merge(credentials, type);
    }
    securedGenericOrderService.persist(credentials, type);
    return type;
  }

  @Override
  @POST
  @Path(DO_CHECKOUT_DETAILS_OPERATION_NAME)
  @MailAction(operation = NO_MAIL)
  public Order doCheckoutDetails(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(ORDER_PARAM_NAME) final Order type) {
    try {
      return processCheckoutDetails(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(DO_CHECKOUT_DETAILS_OPERATION_NAME, credentials, type));
    }
  }

  private Order processCheckoutDetails(final MetaData credentials, final Order type) {
    createUpdateContract(credentials, type.getContract());
    securedGenericTypeCheckOutService.doCheckoutDetails(credentials, type);
    return mergePersistOrder(credentials, type);
  }

  @Override
  @POST
  @Path(DO_CHECKOUT_PAYMENT_OPERATION_NAME)
  @MailAction(operation = CONFIRMATION_NEW_ORDER_MAIL)
  public Order doCheckoutPayment(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(ORDER_PARAM_NAME) final Order type) {
    try {
      return processCheckoutPayment(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(DO_CHECKOUT_PAYMENT_OPERATION_NAME, credentials, type));
    }
  }

  private Order processCheckoutPayment(final MetaData credentials, final Order type) {
    createUpdateContract(credentials, type.getContract());
    securedGenericTypeCheckOutService.doCheckoutPayment(credentials, type);
    return mergePersistOrder(credentials, type);
  }

  @Override
  @POST
  @Path(DO_NOTIFICATION_OPERATION_NAME)
  @MailAction(operation = NO_MAIL)
  public Order doNotification(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(ORDER_PARAM_NAME) final Order type) {
    try {
      return processNotification(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(DO_NOTIFICATION_OPERATION_NAME, credentials, type));
    }
  }

  private Order processNotification(final MetaData credentials, Order type) {
    type = securedGenericTypeCheckOutService.doNotification(credentials, type);
    final String transactionId = type.getTransactionId();
    type.setTransactionId(null);
    if (containsOrder(credentials, type)) {
      return mergePersistOrder(credentials,
          doTransactionDetails(credentials, findOrder(credentials, type), transactionId));
    } else {
      throw new GNUOpenBusinessServiceException("No order available based on the received notification code.");
    }
  }

  private boolean containsOrder(final MetaData credentials, final Order type) {
    return securedGenericOrderService.count(credentials, type) > 0;
  }

  private Order doTransactionDetails(final MetaData credentials, final Order type, final String transactionId) {
    type.setTransactionId(transactionId);
    createUpdateContract(credentials, type.getContract());
    securedGenericTypeCheckOutService.doTransactionDetails(credentials, type);
    return type;
  }

  private Order findOrder(final MetaData credentials, final Order type) {
    return securedGenericOrderService.find(credentials, type, Paging.getInstance(0, 1), NONE).iterator().next();
  }

  @Override
  @POST
  @Path(DO_REFUND_TRANSACTION_OPERATION_NAME)
  @MailAction(operation = NO_MAIL)
  public Order doRefundTransaction(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(ORDER_PARAM_NAME) final Order type) {
    try {
      return processRefundTransaction(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(DO_REFUND_TRANSACTION_OPERATION_NAME, credentials, type));
    }
  }

  private Order processRefundTransaction(final MetaData credentials, final Order type) {
    createUpdateContract(credentials, type.getContract());
    securedGenericTypeCheckOutService.doRefundTransaction(credentials, type);
    return mergePersistOrder(credentials, type);
  }

  @Override
  @POST
  @Path(DO_TRANSACTION_DETAILS_OPERATION_NAME)
  @MailAction(operation = NO_MAIL)
  public Order doTransactionDetails(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(ORDER_PARAM_NAME) final Order type) {
    try {
      return processTransactionDetails(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(DO_TRANSACTION_DETAILS_OPERATION_NAME, credentials, type));
    }
  }

  private Order processTransactionDetails(final MetaData credentials, final Order type) {
    createUpdateContract(credentials, type.getContract());
    securedGenericTypeCheckOutService.doTransactionDetails(credentials, type);
    return mergePersistOrder(credentials, type);
  }
}
