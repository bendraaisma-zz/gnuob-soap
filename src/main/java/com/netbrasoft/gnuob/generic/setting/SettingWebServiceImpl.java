package com.netbrasoft.gnuob.generic.setting;

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
@Stateless(name = "SettingWebServiceImpl")
@Interceptors(value = { SimonInterceptor.class })
public class SettingWebServiceImpl<S extends Setting> implements GenericTypeWebService<S> {

   @EJB(beanName = "SecuredGenericTypeServiceImpl")
   private SecuredGenericTypeService<S> securedGenericSettingService;

   @Override
   @WebMethod(operationName = "countSetting")
   public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "Setting") S type) {
      try {
         return securedGenericSettingService.count(metadata, type);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "findSettingById")
   public S find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "Setting") S type) {
      try {
         return securedGenericSettingService.find(metadata, type, type.getId());
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "findSetting")
   public List<S> find(@WebParam(name = "metaData", header = true) MetaData metadata,
         @WebParam(name = "Setting") S type, @WebParam(name = "paging") Paging paging,
         @WebParam(name = "orderBy") OrderBy orderBy) {
      try {
         return securedGenericSettingService.find(metadata, type, paging, orderBy);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "mergeSetting")
   public S merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "Setting") S type) {
      try {
         securedGenericSettingService.merge(metadata, type);
         return type;
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "persistSetting")
   public S persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "Setting") S type) {
      try {
         securedGenericSettingService.persist(metadata, type);
         return type;
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "refreshSetting")
   public S refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "Setting") S type) {
      try {
         return securedGenericSettingService.refresh(metadata, type, type.getId());
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "removeSetting")
   public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "Setting") S type) {
      try {
         securedGenericSettingService.remove(metadata, type);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

}
