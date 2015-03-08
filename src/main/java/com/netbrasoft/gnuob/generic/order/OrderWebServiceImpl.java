package com.netbrasoft.gnuob.generic.order;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.GenericTypeWebService;
import com.netbrasoft.gnuob.generic.OrderBy;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.contract.Contract;
import com.netbrasoft.gnuob.generic.customer.Customer;
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeService;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "OrderWebServiceImpl")
@Interceptors(value = { AppSimonInterceptor.class })
public class OrderWebServiceImpl<O extends Order> implements GenericTypeWebService<O> {

   @EJB(beanName = "SecuredGenericTypeServiceImpl")
   private SecuredGenericTypeService<O> securedGenericOrderService;

   @EJB(beanName = "SecuredGenericTypeServiceImpl")
   private SecuredGenericTypeService<Contract> securedGenericContractService;

   @EJB(beanName = "SecuredGenericTypeServiceImpl")
   private SecuredGenericTypeService<Customer> securedGenericCustomerService;

   @Override
   @WebMethod(operationName = "countOrder")
   public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O order) {
      try {
         if (order.getContract() != null) {
            securedGenericCustomerService.read(metadata, order.getContract().getCustomer());
            securedGenericContractService.read(metadata, order.getContract());
         }
         return securedGenericOrderService.count(metadata, order);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "findOrderById")
   public O find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O order) {
      try {
         if (order.getContract() != null) {
            securedGenericCustomerService.read(metadata, order.getContract().getCustomer());
            securedGenericContractService.read(metadata, order.getContract());
         }
         return securedGenericOrderService.find(metadata, order, order.getId());
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "findOrder")
   public List<O> find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O order, @WebParam(name = "paging") Paging paging, @WebParam(name = "orderBy") OrderBy orderBy) {
      try {
         if (order.getContract() != null) {
            securedGenericCustomerService.read(metadata, order.getContract().getCustomer());
            securedGenericContractService.read(metadata, order.getContract());
         }
         return securedGenericOrderService.find(metadata, order, paging, orderBy);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "mergeOrder")
   public O merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O order) {
      try {
         if (order.getContract() != null) {
            securedGenericCustomerService.update(metadata, order.getContract().getCustomer());
            securedGenericContractService.update(metadata, order.getContract());
         }
         securedGenericOrderService.merge(metadata, order);
         return order;
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "persistOrder")
   public O persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O order) {
      try {
         if (order.getContract() != null) {
            securedGenericCustomerService.persist(metadata, order.getContract().getCustomer());
            securedGenericContractService.persist(metadata, order.getContract());
         }
         securedGenericOrderService.persist(metadata, order);
         return order;
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "refreshOrder")
   public O refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O order) {
      try {
         if (order.getContract() != null) {
            securedGenericCustomerService.read(metadata, order.getContract().getCustomer());
            securedGenericContractService.read(metadata, order.getContract());
         }
         return securedGenericOrderService.refresh(metadata, order, order.getId());
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "removeOrder")
   public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O order) {
      try {
         if (order.getContract() != null) {
            securedGenericCustomerService.delete(metadata, order.getContract().getCustomer());
            securedGenericContractService.delete(metadata, order.getContract());
         }
         securedGenericOrderService.remove(metadata, order);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

}
