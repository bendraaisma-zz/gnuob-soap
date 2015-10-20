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
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeService;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "PagseguroCheckOutWebServiceImpl")
@Interceptors(value = {AppSimonInterceptor.class, MailControl.class})
public class PagseguroCheckOutWebServiceImpl<O extends Order> implements CheckOutWebService<O> {

  @EJB(beanName = "PagseguroCheckOutServiceImpl")
  private CheckOutService<O> checkOutService;

  @EJB(beanName = "SecuredGenericTypeServiceImpl")
  private SecuredGenericTypeService<O> securedGenericOrderService;

  @EJB(beanName = "SecuredGenericTypeServiceImpl")
  private SecuredGenericTypeService<Contract> securedGenericContractService;

  @EJB(beanName = "SecuredGenericTypeServiceImpl")
  private SecuredGenericTypeService<Customer> securedGenericCustomerService;

  @Override
  @WebMethod(operationName = "doCheckout")
  @MailAction(operation = Mail.NO_MAIL)
  public O doCheckout(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O order) {
    try {
      securedGenericCustomerService.update(metadata, order.getContract().getCustomer());
      securedGenericContractService.update(metadata, order.getContract());
      securedGenericOrderService.update(metadata, order);
      checkOutService.doCheckout(order);
      securedGenericOrderService.merge(metadata, order);
      return order;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "doCheckoutDetails")
  @MailAction(operation = Mail.NO_MAIL)
  public O doCheckoutDetails(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O order) {
    try {
      securedGenericCustomerService.update(metadata, order.getContract().getCustomer());
      securedGenericContractService.update(metadata, order.getContract());
      securedGenericOrderService.update(metadata, order);
      checkOutService.doCheckoutDetails(order);
      securedGenericOrderService.merge(metadata, order);
      return order;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "doCheckoutPayment")
  @MailAction(operation = Mail.CONFIRMATION_NEW_ORDER_MAIL)
  public O doCheckoutPayment(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O order) {
    try {
      securedGenericCustomerService.update(metadata, order.getContract().getCustomer());
      securedGenericContractService.update(metadata, order.getContract());
      securedGenericOrderService.update(metadata, order);
      checkOutService.doCheckoutPayment(order);
      securedGenericOrderService.merge(metadata, order);
      return order;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "doNotification")
  @MailAction(operation = Mail.NO_MAIL)
  public O doNotification(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O order) {
    try {
      order = checkOutService.doNotification(order);

      final String transactionId = order.getTransactionId();

      order.setActive(true);
      order.setTransactionId(null);
      order.setNotificationId(null);

      final Iterator<O> iterator = securedGenericOrderService.find(metadata, order, new Paging(0, 1), OrderBy.NONE).iterator();

      if (iterator.hasNext()) {
        order = iterator.next();
        order.setTransactionId(transactionId);

        securedGenericCustomerService.update(metadata, order.getContract().getCustomer());
        securedGenericContractService.update(metadata, order.getContract());
        securedGenericOrderService.update(metadata, order);
        checkOutService.doTransactionDetails(order);
        securedGenericOrderService.merge(metadata, order);

        return order;
      } else {
        throw new GNUOpenBusinessServiceException("Exception from Pagseguro Notification, no order found for the given notification code.");
      }
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "doRefundTransaction")
  @MailAction(operation = Mail.NO_MAIL)
  public O doRefundTransaction(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O order) {
    try {
      securedGenericCustomerService.update(metadata, order.getContract().getCustomer());
      securedGenericContractService.update(metadata, order.getContract());
      securedGenericOrderService.update(metadata, order);
      checkOutService.doRefundTransaction(order);
      securedGenericOrderService.merge(metadata, order);
      return order;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "doTransactionDetails")
  @MailAction(operation = Mail.NO_MAIL)
  public O doTransactionDetails(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O order) {
    try {
      securedGenericCustomerService.update(metadata, order.getContract().getCustomer());
      securedGenericContractService.update(metadata, order.getContract());
      securedGenericOrderService.update(metadata, order);
      checkOutService.doTransactionDetails(order);
      securedGenericOrderService.merge(metadata, order);
      return order;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
