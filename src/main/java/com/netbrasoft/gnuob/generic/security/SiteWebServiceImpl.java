package com.netbrasoft.gnuob.generic.security;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.GenericTypeWebService;
import com.netbrasoft.gnuob.generic.OrderBy;
import com.netbrasoft.gnuob.generic.Paging;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "SiteWebServiceImpl")
@Interceptors(value = {AppSimonInterceptor.class})
public class SiteWebServiceImpl<S extends Site> implements GenericTypeWebService<S> {

  @EJB(beanName = "SecuredGenericTypeServiceImpl")
  private SecuredGenericTypeService<S> securedGenericSiteService;

  @Override
  @WebMethod(operationName = "countSite")
  public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "site") S type) {
    try {
      return securedGenericSiteService.count(metadata, type);
    } catch (Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findSiteById")
  public S find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "site") S type) {
    try {
      return securedGenericSiteService.find(metadata, type, type.getId());
    } catch (Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findSite")
  public List<S> find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "site") S type, @WebParam(name = "paging") Paging paging,
      @WebParam(name = "orderBy") OrderBy orderBy) {
    try {
      return securedGenericSiteService.find(metadata, type, paging, orderBy);
    } catch (Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "mergeSite")
  public S merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "site") S type) {
    try {
      securedGenericSiteService.merge(metadata, type);
      return type;
    } catch (Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "persistSite")
  public S persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "site") S type) {
    try {

      securedGenericSiteService.persist(metadata, type);
      return type;
    } catch (Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "refreshSite")
  public S refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "site") S type) {
    try {
      return securedGenericSiteService.refresh(metadata, type, type.getId());
    } catch (Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "removeSite")
  public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "site") S type) {
    try {
      securedGenericSiteService.remove(metadata, type);
    } catch (Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

}
