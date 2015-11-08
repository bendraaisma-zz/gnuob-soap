package com.netbrasoft.gnuob.generic.customer;

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
@Stateless(name = PostalCodeWebServiceImpl.POSTAL_CODE_WEB_SERVICE_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class})
public class PostalCodeWebServiceImpl<T extends PostalCode> implements GenericTypeWebService<T> {

  protected static final String POSTAL_CODE_WEB_SERVICE_IMPL_NAME = "PostalCodeWebServiceImpl";

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<T> securedGenericPostalCodeService;

  @Override
  @WebMethod(operationName = "countPostalCode")
  public long count(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "postalCode") final T type) {
    try {
      return securedGenericPostalCodeService.count(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findPostalCodeById")
  public T find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "postalCode") final T type) {
    try {
      return securedGenericPostalCodeService.find(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findPostalCode")
  public List<T> find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "postalCode") final T type,
      @WebParam(name = "paging") final Paging paging, @WebParam(name = "orderBy") final OrderBy orderBy) {
    try {
      return securedGenericPostalCodeService.find(metaData, type, paging, orderBy);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "mergePostalCode")
  public T merge(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "postalCode") final T type) {
    try {
      return securedGenericPostalCodeService.merge(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "persistPostalCode")
  public T persist(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "postalCode") final T type) {
    try {
      if (type.isDetached()) {
        return securedGenericPostalCodeService.merge(metaData, type);
      }
      securedGenericPostalCodeService.persist(metaData, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "refreshPostalCode")
  public T refresh(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "postalCode") final T type) {
    try {
      return securedGenericPostalCodeService.refresh(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "removePostalCode")
  public void remove(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "postalCode") final T type) {
    try {
      securedGenericPostalCodeService.remove(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
