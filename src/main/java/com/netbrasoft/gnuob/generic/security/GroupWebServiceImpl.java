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
@Stateless(name = GroupWebServiceImpl.GROUP_WEB_SERVICE_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class})
public class GroupWebServiceImpl<T extends Group> implements GenericTypeWebService<T> {

  protected static final String GROUP_WEB_SERVICE_IMPL_NAME = "GroupWebServiceImpl";

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<T> securedGenericGroupService;

  @Override
  @WebMethod(operationName = "countGroup")
  public long count(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "group") final T type) {
    try {
      return securedGenericGroupService.count(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findGroupById")
  public T find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "group") final T type) {
    try {
      return securedGenericGroupService.find(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findGroup")
  public List<T> find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "group") final T type, @WebParam(name = "paging") final Paging paging,
      @WebParam(name = "orderBy") final OrderBy orderBy) {
    try {
      return securedGenericGroupService.find(metaData, type, paging, orderBy);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "mergeGroup")
  public T merge(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "group") final T type) {
    try {
      return securedGenericGroupService.merge(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "persistGroup")
  public T persist(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "group") final T type) {
    try {
      if (type.isDetached()) {
        return securedGenericGroupService.merge(metaData, type);
      }
      securedGenericGroupService.persist(metaData, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "refreshGroup")
  public T refresh(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "group") final T type) {
    try {
      return securedGenericGroupService.refresh(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "removeGroup")
  public void remove(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "group") final T type) {
    try {
      securedGenericGroupService.remove(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
