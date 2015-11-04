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
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeServiceImpl;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = ContractWebServiceImpl.CONTRACT_WEB_SERVICE_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class})
public class ContractWebServiceImpl<T extends Contract> implements GenericTypeWebService<T> {

  protected static final String CONTRACT_WEB_SERVICE_IMPL_NAME = "ContractWebServiceImpl";

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<T> securedGenericContractService;

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<Customer> securedGenericCustomerService;

  @Override
  @WebMethod(operationName = "countContract")
  public long count(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "contract") final T type) {
    try {
      readCustomer(metaData, type.getCustomer());
      return securedGenericContractService.count(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void createUpdateCustomer(final MetaData metaData, final Customer customer) {
    if (customer.getId() == 0) {
      securedGenericCustomerService.create(metaData, customer);
    } else {
      securedGenericCustomerService.update(metaData, customer);
    }
  }

  private void deleteCustomer(final MetaData metaData, final Customer customer) {
    if (customer != null && customer.getId() > 0) {
      securedGenericCustomerService.delete(metaData, customer);
    }
  }

  @Override
  @WebMethod(operationName = "findContractById")
  public T find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "contract") final T type) {
    try {
      readCustomer(metaData, type.getCustomer());
      return securedGenericContractService.find(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findContract")
  public List<T> find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "contract") final T type,
      @WebParam(name = "paging") final Paging paging, @WebParam(name = "orderBy") final OrderBy orderBy) {
    try {
      readCustomer(metaData, type.getCustomer());
      return securedGenericContractService.find(metaData, type, paging, orderBy);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "mergeContract")
  public T merge(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "contract") final T type) {
    try {
      createUpdateCustomer(metaData, type.getCustomer());
      return securedGenericContractService.merge(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "persistContract")
  @MailAction(operation = Mail.WELCOME_NEW_CUSTOMER_MAIL)
  public T persist(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "contract") final T type) {
    try {
      createUpdateCustomer(metaData, type.getCustomer());
      if (type.isDetached()) {
        return securedGenericContractService.merge(metaData, type);
      }
      securedGenericContractService.persist(metaData, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void readCustomer(final MetaData metaData, final Customer customer) {
    if (customer != null && customer.getId() > 0) {
      securedGenericCustomerService.read(metaData, customer);
    }
  }

  @Override
  @WebMethod(operationName = "refreshContract")
  public T refresh(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "contract") final T type) {
    try {
      readCustomer(metaData, type.getCustomer());
      return securedGenericContractService.refresh(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "removeContract")
  public void remove(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "contract") final T type) {
    try {
      deleteCustomer(metaData, type.getCustomer());
      securedGenericContractService.remove(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
