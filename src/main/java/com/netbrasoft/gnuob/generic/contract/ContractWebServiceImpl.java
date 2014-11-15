package com.netbrasoft.gnuob.generic.contract;

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
import com.netbrasoft.gnuob.generic.customer.Customer;
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeService;
import com.netbrasoft.gnuob.monitor.SimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "ContractWebServiceImpl")
@Interceptors(value = { SimonInterceptor.class })
public class ContractWebServiceImpl<C extends Contract> implements GenericTypeWebService<C> {
   
   @EJB(beanName = "SecuredGenericTypeServiceImpl")
   private SecuredGenericTypeService<C> securedGenericContractService;
   
   @EJB(beanName = "SecuredGenericTypeServiceImpl")
   private SecuredGenericTypeService<Customer> securedGenericCustomerService;
   
   @Override
   @WebMethod(operationName = "countContract")
   public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C contract) {
      try {
         securedGenericCustomerService.read(metadata, contract.getCustomer());
         return securedGenericContractService.count(metadata, contract);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }
   
   @Override
   @WebMethod(operationName = "findContractById")
   public C find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C contract) {
      try {
         securedGenericCustomerService.read(metadata, contract.getCustomer());
         return securedGenericContractService.find(metadata, contract, contract.getId());
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }
   
   @Override
   @WebMethod(operationName = "findContract")
   public List<C> find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C contract, @WebParam(name = "paging") Paging paging, @WebParam(name = "orderBy") OrderBy orderBy) {
      try {
         securedGenericCustomerService.read(metadata, contract.getCustomer());
         return securedGenericContractService.find(metadata, contract, paging, orderBy);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }
   
   @Override
   @WebMethod(operationName = "mergeContract")
   public C merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C contract) {
      try {
         securedGenericCustomerService.update(metadata, contract.getCustomer());
         securedGenericContractService.merge(metadata, contract);
         return contract;
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }
   
   @Override
   @WebMethod(operationName = "persistContract")
   public C persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C contract) {
      try {
         securedGenericCustomerService.persist(metadata, contract.getCustomer());
         securedGenericContractService.persist(metadata, contract);
         return contract;
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }
   
   @Override
   @WebMethod(operationName = "refreshContract")
   public C refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C contract) {
      try {
         securedGenericCustomerService.read(metadata, contract.getCustomer());
         return securedGenericContractService.refresh(metadata, contract, contract.getId());
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }
   
   @Override
   @WebMethod(operationName = "removeContract")
   public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C contract) {
      try {
         securedGenericCustomerService.delete(metadata, contract.getCustomer());
         securedGenericContractService.remove(metadata, contract);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }
   
}
