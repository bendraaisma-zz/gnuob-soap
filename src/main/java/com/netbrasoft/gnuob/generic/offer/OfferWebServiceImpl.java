package com.netbrasoft.gnuob.generic.offer;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.GenericTypeWebService;
import com.netbrasoft.gnuob.generic.OrderBy;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.Parameter;
import com.netbrasoft.gnuob.generic.contract.Contract;
import com.netbrasoft.gnuob.generic.customer.Customer;
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeService;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeServiceImpl;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = OfferWebServiceImpl.OFFER_WEB_SERVICE_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class})
public class OfferWebServiceImpl<T extends Offer> implements GenericTypeWebService<T> {

  protected static final String OFFER_WEB_SERVICE_IMPL_NAME = "OfferWebServiceImpl";

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<T> securedGenericOfferService;

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<Contract> securedGenericContractService;

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<Customer> securedGenericCustomerService;

  @Override
  @WebMethod(operationName = "countOffer")
  public long count(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "offer") final T type) {
    try {
      readUpdateContractCustomer(metaData, type.getContract());
      if (type.getContract() != null) {
        final Parameter parameter = new Parameter("contract", Restrictions.or(Example.create(type.getContract())));
        return securedGenericOfferService.count(metaData, type, parameter);
      }
      return securedGenericOfferService.count(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void createUpdateContractCustomer(final MetaData metaData, final Contract contract) {
    if (contract != null) {
      if (contract.getId() == 0) {
        securedGenericContractService.create(metaData, contract);
      } else {
        securedGenericContractService.update(metaData, contract);
      }
      createUpdateCustomer(metaData, contract.getCustomer());
    }
  }

  private void createUpdateCustomer(final MetaData metaData, final Customer customer) {
    if (customer != null) {
      if (customer.getId() == 0) {
        securedGenericCustomerService.create(metaData, customer);
      } else {
        securedGenericCustomerService.update(metaData, customer);
      }
    }
  }

  private void deleteUpdateContractCustomer(final MetaData metaData, final Contract contract) {
    if (contract != null) {
      if (contract.getId() > 0) {
        securedGenericContractService.delete(metaData, contract);
      }
      deletUpdateCustomer(metaData, contract.getCustomer());
    }
  }

  private void deletUpdateCustomer(final MetaData metaData, final Customer customer) {
    if (customer != null && customer.getId() > 0) {
      securedGenericCustomerService.delete(metaData, customer);
    }
  }

  @Override
  @WebMethod(operationName = "findOfferById")
  public T find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "offer") final T type) {
    try {
      readUpdateContractCustomer(metaData, type.getContract());
      return securedGenericOfferService.find(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findOffer")
  public List<T> find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "offer") final T type, @WebParam(name = "paging") final Paging paging,
      @WebParam(name = "orderBy") final OrderBy orderBy) {
    try {
      readUpdateContractCustomer(metaData, type.getContract());
      if (type.getContract() != null) {
        final Parameter parameter = new Parameter("contract", Restrictions.or(Example.create(type.getContract())));
        return securedGenericOfferService.find(metaData, type, paging, orderBy, parameter);
      }
      return securedGenericOfferService.find(metaData, type, paging, orderBy);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "mergeOffer")
  public T merge(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "offer") final T type) {
    try {
      createUpdateContractCustomer(metaData, type.getContract());
      return securedGenericOfferService.merge(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "persistOffer")
  public T persist(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "offer") final T type) {
    try {
      createUpdateContractCustomer(metaData, type.getContract());
      if (type.isDetached()) {
        return securedGenericOfferService.merge(metaData, type);
      }
      securedGenericOfferService.persist(metaData, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void readUpdateContractCustomer(final MetaData metaData, final Contract contract) {
    if (contract != null) {
      if (contract.getId() > 0) {
        securedGenericContractService.read(metaData, contract);
      }
      readUpdateCustomer(metaData, contract.getCustomer());
    }
  }

  private void readUpdateCustomer(final MetaData metaData, final Customer customer) {
    if (customer != null && customer.getId() > 0) {
      securedGenericCustomerService.read(metaData, customer);
    }
  }

  @Override
  @WebMethod(operationName = "refreshOffer")
  public T refresh(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "offer") final T type) {
    try {
      readUpdateContractCustomer(metaData, type.getContract());
      return securedGenericOfferService.refresh(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "removeOffer")
  public void remove(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "offer") final T type) {
    try {
      deleteUpdateContractCustomer(metaData, type.getContract());
      securedGenericOfferService.remove(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
