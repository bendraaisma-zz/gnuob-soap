package com.netbrasoft.gnuob.generic.offer;

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
import com.netbrasoft.gnuob.monitor.SimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "OfferWebServiceImpl")
@Interceptors(value = { SimonInterceptor.class })
public class OfferWebServiceImpl<O extends Offer> implements GenericTypeWebService<O> {

   @EJB(beanName = "SecuredGenericTypeServiceImpl")
   private SecuredGenericTypeService<O> securedGenericOfferService;

   @EJB(beanName = "SecuredGenericTypeServiceImpl")
   private SecuredGenericTypeService<Contract> securedGenericContractService;

   @EJB(beanName = "SecuredGenericTypeServiceImpl")
   private SecuredGenericTypeService<Customer> securedGenericCustomerService;

   @Override
   @WebMethod(operationName = "countOffer")
   public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "offer") O offer) {
      try {
         if (offer.getContract() != null) {
            securedGenericCustomerService.read(metadata, offer.getContract().getCustomer());
            securedGenericContractService.read(metadata, offer.getContract());
         }
         return securedGenericOfferService.count(metadata, offer);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "findOfferById")
   public O find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "offer") O offer) {
      try {
         if (offer.getContract() != null) {
            securedGenericCustomerService.read(metadata, offer.getContract().getCustomer());
            securedGenericContractService.read(metadata, offer.getContract());
         }
         return securedGenericOfferService.find(metadata, offer, offer.getId());
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "findOffer")
   public List<O> find(@WebParam(name = "metaData", header = true) MetaData metadata,
         @WebParam(name = "offer") O offer, @WebParam(name = "paging") Paging paging,
         @WebParam(name = "orderBy") OrderBy orderBy) {
      try {
         if (offer.getContract() != null) {
            securedGenericCustomerService.read(metadata, offer.getContract().getCustomer());
            securedGenericContractService.read(metadata, offer.getContract());
         }
         return securedGenericOfferService.find(metadata, offer, paging, orderBy);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "mergeOffer")
   public O merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "offer") O offer) {
      try {
         if (offer.getContract() != null) {
            securedGenericCustomerService.update(metadata, offer.getContract().getCustomer());
            securedGenericContractService.update(metadata, offer.getContract());
         }
         securedGenericOfferService.merge(metadata, offer);
         return offer;
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "persistOffer")
   public O persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "offer") O offer) {
      try {
         if (offer.getContract() != null) {
            securedGenericCustomerService.persist(metadata, offer.getContract().getCustomer());
            securedGenericContractService.persist(metadata, offer.getContract());
         }
         securedGenericOfferService.persist(metadata, offer);
         return offer;
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "refreshOffer")
   public O refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "offer") O offer) {
      try {
         if (offer.getContract() != null) {
            securedGenericCustomerService.read(metadata, offer.getContract().getCustomer());
            securedGenericContractService.read(metadata, offer.getContract());
         }
         return securedGenericOfferService.refresh(metadata, offer, offer.getId());
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "removeOffer")
   public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "offer") O offer) {
      try {
         if (offer.getContract() != null) {
            securedGenericCustomerService.delete(metadata, offer.getContract().getCustomer());
            securedGenericContractService.delete(metadata, offer.getContract());
         }
         securedGenericOfferService.remove(metadata, offer);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

}
