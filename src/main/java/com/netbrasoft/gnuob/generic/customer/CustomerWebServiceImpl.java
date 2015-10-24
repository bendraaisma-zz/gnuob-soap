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
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "CustomerWebServiceImpl")
@Interceptors(value = {AppSimonInterceptor.class})
public class CustomerWebServiceImpl<C extends Customer> implements GenericTypeWebService<C> {

  @EJB(beanName = "SecuredGenericTypeServiceImpl")
  private SecuredGenericTypeService<C> securedGenericCustomerService;

  @Override
  @WebMethod(operationName = "countCustomer")
  public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "customer") C type) {
    try {
      return securedGenericCustomerService.count(metadata, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findCustomerById")
  public C find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "customer") C type) {
    try {
      return securedGenericCustomerService.find(metadata, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findCustomer")
  public List<C> find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "customer") C type, @WebParam(name = "paging") Paging paging,
      @WebParam(name = "orderBy") OrderBy orderBy) {
    try {
      return securedGenericCustomerService.find(metadata, type, paging, orderBy);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "mergeCustomer")
  public C merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "customer") C type) {
    try {
      return securedGenericCustomerService.merge(metadata, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "persistCustomer")
  public C persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "customer") C type) {
    try {
      if (type.isDetached()) {
        return securedGenericCustomerService.merge(metadata, type);
      }
      securedGenericCustomerService.persist(metadata, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "refreshCustomer")
  public C refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "customer") C type) {
    try {
      return securedGenericCustomerService.refresh(metadata, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "removeCustomer")
  public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "customer") C type) {
    try {
      securedGenericCustomerService.remove(metadata, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
