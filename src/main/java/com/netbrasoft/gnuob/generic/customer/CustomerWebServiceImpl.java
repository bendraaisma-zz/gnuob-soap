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
@Stateless(name = CustomerWebServiceImpl.CUSTOMER_WEB_SERVICE_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class})
public class CustomerWebServiceImpl<T extends Customer> implements GenericTypeWebService<T> {

  protected static final String CUSTOMER_WEB_SERVICE_IMPL_NAME = "CustomerWebServiceImpl";

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<T> securedGenericCustomerService;

  @Override
  @WebMethod(operationName = "countCustomer")
  public long count(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "customer") final T type) {
    try {
      return securedGenericCustomerService.count(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findCustomerById")
  public T find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "customer") final T type) {
    try {
      return securedGenericCustomerService.find(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findCustomer")
  public List<T> find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "customer") final T type,
      @WebParam(name = "paging") final Paging paging, @WebParam(name = "orderBy") final OrderBy orderBy) {
    try {
      return securedGenericCustomerService.find(metaData, type, paging, orderBy);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "mergeCustomer")
  public T merge(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "customer") final T type) {
    try {
      return securedGenericCustomerService.merge(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "persistCustomer")
  public T persist(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "customer") final T type) {
    try {
      if (type.isDetached()) {
        return securedGenericCustomerService.merge(metaData, type);
      }
      securedGenericCustomerService.persist(metaData, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "refreshCustomer")
  public T refresh(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "customer") final T type) {
    try {
      return securedGenericCustomerService.refresh(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "removeCustomer")
  public void remove(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "customer") final T type) {
    try {
      securedGenericCustomerService.remove(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
