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
import com.netbrasoft.gnuob.generic.contract.Contract;
import com.netbrasoft.gnuob.generic.customer.Customer;
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeService;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "PayPalExpressCheckOutWebServiceImpl")
@Interceptors(value = { AppSimonInterceptor.class })
public class PayPalExpressCheckOutWebServiceImpl<O extends Order> implements CheckOutWebService<O> {

   @EJB(beanName = "PayPalExpressCheckOutServiceImpl")
   private CheckOutService<O> checkOutService;

   @EJB(beanName = "SecuredGenericTypeServiceImpl")
   private SecuredGenericTypeService<O> securedGenericOrderService;

   @EJB(beanName = "SecuredGenericTypeServiceImpl")
   private SecuredGenericTypeService<Contract> securedGenericContractService;

   @EJB(beanName = "SecuredGenericTypeServiceImpl")
   private SecuredGenericTypeService<Customer> securedGenericCustomerService;

   @Override
   @WebMethod(operationName = "doCheckout")
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
         } else {
            throw new GNUOpenBusinessServiceException("Exception from PayPal Notification, no order found for the given notification code.");
         }
         return order;
      } catch (final Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "doRefundTransaction")
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
