package com.netbrasoft.gnuob.generic.security;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.netbrasoft.gnuob.monitor.SimonInterceptor;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.GenericTypeWebService;
import com.netbrasoft.gnuob.generic.OrderBy;
import com.netbrasoft.gnuob.generic.Paging;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "GroupWebServiceImpl")
@Interceptors(value = { SimonInterceptor.class })
public class GroupWebServiceImpl<G extends Group> implements GenericTypeWebService<G> {

   @EJB(beanName = "SecuredGenericTypeServiceImpl")
   private SecuredGenericTypeService<G> securedGenericGroupService;

   @Override
   @WebMethod(operationName = "countGroup")
   public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "group") G type) {
      try {
         return securedGenericGroupService.count(metadata, type);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "findGroupById")
   public G find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "group") G type) {
      try {
         return securedGenericGroupService.find(metadata, type, type.getId());
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "findGroup")
   public List<G> find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "group") G type,
         @WebParam(name = "paging") Paging paging, @WebParam(name = "orderBy") OrderBy orderBy) {
      try {
         return securedGenericGroupService.find(metadata, type, paging, orderBy);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "mergeGroup")
   public G merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "group") G type) {
      try {
         securedGenericGroupService.merge(metadata, type);
         return type;
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "persistGroup")
   public G persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "group") G type) {
      try {

         securedGenericGroupService.persist(metadata, type);
         return type;
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "refreshGroup")
   public G refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "group") G type) {
      try {
         return securedGenericGroupService.refresh(metadata, type, type.getId());
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "removeGroup")
   public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "group") G type) {
      try {
         securedGenericGroupService.remove(metadata, type);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

}
