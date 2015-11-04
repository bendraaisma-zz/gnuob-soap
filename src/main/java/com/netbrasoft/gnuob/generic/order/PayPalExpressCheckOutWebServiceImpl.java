package com.netbrasoft.gnuob.generic.order;

import java.util.Iterator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.OrderBy;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.content.mail.Mail;
import com.netbrasoft.gnuob.generic.content.mail.MailAction;
import com.netbrasoft.gnuob.generic.content.mail.MailControl;
import com.netbrasoft.gnuob.generic.contract.Contract;
import com.netbrasoft.gnuob.generic.customer.Customer;
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeCheckOutService;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeService;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeServiceImpl;
import com.netbrasoft.gnuob.generic.security.SecuredPayPalExpressCheckOutServiceImpl;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = PayPalExpressCheckOutWebServiceImpl.PAY_PAL_EXPRESS_CHECK_OUT_WEB_SERVICE_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class, MailControl.class})
public class PayPalExpressCheckOutWebServiceImpl<T extends Order> implements CheckOutWebService<T> {

  protected static final String PAY_PAL_EXPRESS_CHECK_OUT_WEB_SERVICE_IMPL_NAME = "PayPalExpressCheckOutWebServiceImpl";

  @EJB(beanName = SecuredPayPalExpressCheckOutServiceImpl.SECURED_PAY_PAL_EXPRESS_CHECK_OUT_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeCheckOutService<T> securedGenericTypeCheckOutService;

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<T> securedGenericOrderService;

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<Contract> securedGenericContractService;

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<Customer> securedGenericCustomerService;

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
  @MailAction(operation = Mail.NO_MAIL)
  public T doCheckout(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "order") final T type) {
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
  @MailAction(operation = Mail.NO_MAIL)
  public T doCheckoutDetails(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "order") final T type) {
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
  @MailAction(operation = Mail.CONFIRMATION_NEW_ORDER_MAIL)
  public T doCheckoutPayment(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "order") final T type) {
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
  @MailAction(operation = Mail.NO_MAIL)
  public T doNotification(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "order") T type) {
    try {
      type = securedGenericTypeCheckOutService.doNotification(metaData, type);

      final String transactionId = type.getTransactionId();

      type.setActive(true);
      type.setTransactionId(null);
      type.setNotificationId(null);

      final Iterator<T> iterator = securedGenericOrderService.find(metaData, type, new Paging(0, 1), OrderBy.NONE).iterator();

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
        throw new GNUOpenBusinessServiceException("Exception from PayPal Notification, no order found for the given notification code.");
      }
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "doRefundTransaction")
  @MailAction(operation = Mail.NO_MAIL)
  public T doRefundTransaction(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "order") final T type) {
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
  @MailAction(operation = Mail.NO_MAIL)
  public T doTransactionDetails(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "order") final T type) {
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
