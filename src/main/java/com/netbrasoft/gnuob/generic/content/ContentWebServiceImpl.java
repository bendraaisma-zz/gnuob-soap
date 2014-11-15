package com.netbrasoft.gnuob.generic.content;

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
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeService;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "ContentWebServiceImpl")
@Interceptors(value = { SimonInterceptor.class })
public class ContentWebServiceImpl<C extends Content> implements GenericTypeWebService<C> {

   @EJB(beanName = "SecuredGenericTypeServiceImpl")
   private SecuredGenericTypeService<C> securedGenericContentService;

   @Override
   @WebMethod(operationName = "countContent")
   public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "content") C type) {
      try {
         return securedGenericContentService.count(metadata, type);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "findContentById")
   public C find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "content") C type) {
      try {
         return securedGenericContentService.find(metadata, type, type.getId());
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "findContent")
   public List<C> find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "content") C type, @WebParam(name = "paging") Paging paging, @WebParam(name = "orderBy") OrderBy orderBy) {
      try {
         return securedGenericContentService.find(metadata, type, paging, orderBy);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "mergeContent")
   public C merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "content") C type) {
      try {
         securedGenericContentService.merge(metadata, type);
         return type;
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "persistContent")
   public C persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "content") C type) {
      try {
         securedGenericContentService.persist(metadata, type);
         return type;
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "refreshContent")
   public C refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "content") C type) {
      try {
         return securedGenericContentService.refresh(metadata, type, type.getId());
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "removeContent")
   public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "content") C type) {
      try {
         securedGenericContentService.remove(metadata, type);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

}
