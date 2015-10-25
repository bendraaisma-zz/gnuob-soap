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
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "PayPalExpressCheckOutWebServiceImpl")
@Interceptors(value = {AppSimonInterceptor.class, MailControl.class})
public class PayPalExpressCheckOutWebServiceImpl<O extends Order> implements CheckOutWebService<O> {

  @EJB(beanName = "SecuredPayPalExpressCheckOutServiceImpl")
  private SecuredGenericTypeCheckOutService<O> securedGenericTypeCheckOutService;

  @EJB(beanName = "SecuredGenericTypeServiceImpl")
  private SecuredGenericTypeService<O> securedGenericOrderService;

  @EJB(beanName = "SecuredGenericTypeServiceImpl")
  private SecuredGenericTypeService<Contract> securedGenericContractService;

  @EJB(beanName = "SecuredGenericTypeServiceImpl")
  private SecuredGenericTypeService<Customer> securedGenericCustomerService;

  private void createUpdateContractCustomer(MetaData metadata, Contract contract) {
    if (contract != null) {
      if (contract.getId() == 0) {
        securedGenericContractService.create(metadata, contract);
      } else {
        securedGenericContractService.update(metadata, contract);
      }
      createUpdateCustomer(metadata, contract.getCustomer());
    }
  }

  private void createUpdateCustomer(MetaData metadata, Customer customer) {
    if (customer != null) {
      if (customer.getId() == 0) {
        securedGenericCustomerService.create(metadata, customer);
      } else {
        securedGenericCustomerService.update(metadata, customer);
      }
    }
  }

  @Override
  @WebMethod(operationName = "doCheckout")
  @MailAction(operation = Mail.NO_MAIL)
  public O doCheckout(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type) {
    try {
      createUpdateContractCustomer(metadata, type.getContract());
      securedGenericTypeCheckOutService.doCheckout(metadata, type);
      if (type.isDetached()) {
        return securedGenericOrderService.merge(metadata, type);
      }
      securedGenericOrderService.persist(metadata, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "doCheckoutDetails")
  @MailAction(operation = Mail.NO_MAIL)
  public O doCheckoutDetails(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type) {
    try {
      createUpdateContractCustomer(metadata, type.getContract());
      securedGenericTypeCheckOutService.doCheckoutDetails(metadata, type);
      if (type.isDetached()) {
        return securedGenericOrderService.merge(metadata, type);
      }
      securedGenericOrderService.persist(metadata, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "doCheckoutPayment")
  @MailAction(operation = Mail.CONFIRMATION_NEW_ORDER_MAIL)
  public O doCheckoutPayment(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type) {
    try {
      createUpdateContractCustomer(metadata, type.getContract());
      securedGenericTypeCheckOutService.doCheckoutPayment(metadata, type);
      if (type.isDetached()) {
        return securedGenericOrderService.merge(metadata, type);
      }
      securedGenericOrderService.persist(metadata, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "doNotification")
  @MailAction(operation = Mail.NO_MAIL)
  public O doNotification(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type) {
    try {
      type = securedGenericTypeCheckOutService.doNotification(metadata, type);

      final String transactionId = type.getTransactionId();

      type.setActive(true);
      type.setTransactionId(null);
      type.setNotificationId(null);

      final Iterator<O> iterator = securedGenericOrderService.find(metadata, type, new Paging(0, 1), OrderBy.NONE).iterator();

      if (iterator.hasNext()) {
        type = iterator.next();
        type.setTransactionId(transactionId);

        createUpdateContractCustomer(metadata, type.getContract());
        securedGenericTypeCheckOutService.doTransactionDetails(metadata, type);
        if (type.isDetached()) {
          return securedGenericOrderService.merge(metadata, type);
        }
        securedGenericOrderService.persist(metadata, type);
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
  public O doRefundTransaction(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type) {
    try {
      createUpdateContractCustomer(metadata, type.getContract());
      securedGenericTypeCheckOutService.doRefundTransaction(metadata, type);
      if (type.isDetached()) {
        return securedGenericOrderService.merge(metadata, type);
      }
      securedGenericOrderService.persist(metadata, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "doTransactionDetails")
  @MailAction(operation = Mail.NO_MAIL)
  public O doTransactionDetails(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type) {
    try {
      createUpdateContractCustomer(metadata, type.getContract());
      securedGenericTypeCheckOutService.doTransactionDetails(metadata, type);
      if (type.isDetached()) {
        return securedGenericOrderService.merge(metadata, type);
      }
      securedGenericOrderService.persist(metadata, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
