package com.netbrasoft.gnuob.generic.security;

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
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = SiteWebServiceImpl.SITE_WEB_SERVICE_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class})
public class SiteWebServiceImpl<T extends Site> implements GenericTypeWebService<T> {

  protected static final String SITE_WEB_SERVICE_IMPL_NAME = "SiteWebServiceImpl";

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<T> securedGenericSiteService;

  @Override
  @WebMethod(operationName = "countSite")
  public long count(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "site") final T type) {
    try {
      return securedGenericSiteService.count(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findSiteById")
  public T find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "site") final T type) {
    try {
      return securedGenericSiteService.find(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findSite")
  public List<T> find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "site") final T type, @WebParam(name = "paging") final Paging paging,
      @WebParam(name = "orderBy") final OrderBy orderBy) {
    try {
      return securedGenericSiteService.find(metaData, type, paging, orderBy);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "mergeSite")
  public T merge(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "site") final T type) {
    try {
      return securedGenericSiteService.merge(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "persistSite")
  public T persist(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "site") final T type) {
    try {
      if (type.isDetached()) {
        return securedGenericSiteService.merge(metaData, type);
      }
      securedGenericSiteService.persist(metaData, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "refreshSite")
  public T refresh(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "site") final T type) {
    try {
      return securedGenericSiteService.refresh(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "removeSite")
  public void remove(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "site") final T type) {
    try {
      securedGenericSiteService.remove(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
