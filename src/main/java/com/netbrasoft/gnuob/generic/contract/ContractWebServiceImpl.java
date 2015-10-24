package com.netbrasoft.gnuob.generic.contract;

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
import com.netbrasoft.gnuob.generic.content.mail.Mail;
import com.netbrasoft.gnuob.generic.content.mail.MailAction;
import com.netbrasoft.gnuob.generic.customer.Customer;
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeService;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "ContractWebServiceImpl")
@Interceptors(value = {AppSimonInterceptor.class})
public class ContractWebServiceImpl<C extends Contract> implements GenericTypeWebService<C> {

  @EJB(beanName = "SecuredGenericTypeServiceImpl")
  private SecuredGenericTypeService<C> securedGenericContractService;

  @EJB(beanName = "SecuredGenericTypeServiceImpl")
  private SecuredGenericTypeService<Customer> securedGenericCustomerService;

  @Override
  @WebMethod(operationName = "countContract")
  public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C type) {
    try {
      readCustomer(metadata, type.getCustomer());
      return securedGenericContractService.count(metadata, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void createUpdateCustomer(MetaData metadata, Customer customer) {
    if (customer.getId() == 0) {
      securedGenericCustomerService.create(metadata, customer);
    } else {
      securedGenericCustomerService.update(metadata, customer);
    }
  }

  private void deleteCustomer(MetaData metadata, Customer customer) {
    if (customer != null && customer.getId() > 0) {
      securedGenericCustomerService.delete(metadata, customer);
    }
  }

  @Override
  @WebMethod(operationName = "findContractById")
  public C find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C type) {
    try {
      readCustomer(metadata, type.getCustomer());
      return securedGenericContractService.find(metadata, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findContract")
  public List<C> find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C type, @WebParam(name = "paging") Paging paging,
      @WebParam(name = "orderBy") OrderBy orderBy) {
    try {
      readCustomer(metadata, type.getCustomer());
      return securedGenericContractService.find(metadata, type, paging, orderBy);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "mergeContract")
  public C merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C type) {
    try {
      createUpdateCustomer(metadata, type.getCustomer());
      return securedGenericContractService.merge(metadata, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "persistContract")
  @MailAction(operation = Mail.WELCOME_NEW_CUSTOMER_MAIL)
  public C persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C type) {
    try {
      createUpdateCustomer(metadata, type.getCustomer());
      if (type.isDetached()) {
        return securedGenericContractService.merge(metadata, type);
      }
      securedGenericContractService.persist(metadata, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void readCustomer(MetaData metadata, Customer customer) {
    if (customer != null && customer.getId() > 0) {
      securedGenericCustomerService.read(metadata, customer);
    }
  }

  @Override
  @WebMethod(operationName = "refreshContract")
  public C refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C type) {
    try {
      readCustomer(metadata, type.getCustomer());
      return securedGenericContractService.refresh(metadata, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "removeContract")
  public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C type) {
    try {
      deleteCustomer(metadata, type.getCustomer());
      securedGenericContractService.remove(metadata, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
