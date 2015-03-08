package com.netbrasoft.gnuob.generic.security;

import java.util.List;
import java.util.Set;

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
@Stateless(name = "UserWebServiceImpl")
@Interceptors(value = { SimonInterceptor.class })
public class UserWebServiceImpl<U extends User> implements GenericTypeWebService<U> {

   @EJB(beanName = "SecuredGenericTypeServiceImpl")
   private SecuredGenericTypeService<U> securedGenericUserService;

   @EJB(beanName = "SecuredGenericTypeServiceImpl")
   private SecuredGenericTypeService<Group> securedGenericGroupService;

   @EJB(beanName = "SecuredGenericTypeServiceImpl")
   private SecuredGenericTypeService<Site> securedGenericSiteService;

   @Override
   @WebMethod(operationName = "countUser")
   public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type) {
      try {
         return securedGenericUserService.count(metadata, type);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "findUserById")
   public U find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type) {
      try {
         return securedGenericUserService.find(metadata, type, type.getId());
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "findUser")
   public List<U> find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type,
         @WebParam(name = "paging") Paging paging, @WebParam(name = "orderBy") OrderBy orderBy) {
      try {
         return securedGenericUserService.find(metadata, type, paging, orderBy);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "mergeUser")
   public U merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type) {
      try {
         persistMergeSites(metadata, type.getSites());
         persistMergeGroups(metadata, type.getGroups());
         securedGenericUserService.merge(metadata, type);
         return type;
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "persistUser")
   public U persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type) {
      try {
         persistMergeSites(metadata, type.getSites());
         persistMergeGroups(metadata, type.getGroups());
         securedGenericUserService.persist(metadata, type);
         return type;
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   private void persistMergeGroups(MetaData metadata, Set<Group> groups) {
      if (groups != null && !groups.isEmpty()) {
         for (Group group : groups) {
            if (group.getId() == 0) {
               securedGenericGroupService.persist(metadata, group);
            }
         }
      }
   }

   private void persistMergeSites(MetaData metadata, Set<Site> sites) {
      if (sites != null && !sites.isEmpty()) {
         for (Site site : sites) {
            if (site.getId() == 0) {
               securedGenericSiteService.persist(metadata, site);
            }
         }
      }
   }

   @Override
   @WebMethod(operationName = "refreshUser")
   public U refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type) {
      try {
         return securedGenericUserService.refresh(metadata, type, type.getId());
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

   @Override
   @WebMethod(operationName = "removeUser")
   public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type) {
      try {
         securedGenericUserService.remove(metadata, type);
      } catch (Exception e) {
         throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
   }

}
