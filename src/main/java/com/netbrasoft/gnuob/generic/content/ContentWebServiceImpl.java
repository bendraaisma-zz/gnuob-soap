package com.netbrasoft.gnuob.generic.content;

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
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeService;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeServiceImpl;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = ContentWebServiceImpl.CONTENT_WEB_SERVICE_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class})
public class ContentWebServiceImpl<T extends Content> implements GenericTypeWebService<T> {

  protected static final String CONTENT_WEB_SERVICE_IMPL_NAME = "ContentWebServiceImpl";

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<T> securedGenericContentService;

  @Override
  @WebMethod(operationName = "countContent")
  public long count(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "content") final T type) {
    try {
      return securedGenericContentService.count(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findContentById")
  public T find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "content") final T type) {
    try {
      return securedGenericContentService.find(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findContent")
  public List<T> find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "content") final T type, @WebParam(name = "paging") final Paging paging,
      @WebParam(name = "orderBy") final OrderBy orderBy) {
    try {
      return securedGenericContentService.find(metaData, type, paging, orderBy);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "mergeContent")
  public T merge(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "content") final T type) {
    try {
      return securedGenericContentService.merge(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "persistContent")
  public T persist(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "content") final T type) {
    try {
      if (type.isDetached()) {
        return securedGenericContentService.merge(metaData, type);
      }
      securedGenericContentService.persist(metaData, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "refreshContent")
  public T refresh(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "content") final T type) {
    try {
      return securedGenericContentService.refresh(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "removeContent")
  public void remove(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "content") final T type) {
    try {
      securedGenericContentService.remove(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
