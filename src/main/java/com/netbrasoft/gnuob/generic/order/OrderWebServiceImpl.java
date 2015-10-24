package com.netbrasoft.gnuob.generic.order;

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
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "OrderWebServiceImpl")
@Interceptors(value = {AppSimonInterceptor.class})
public class OrderWebServiceImpl<O extends Order> implements GenericTypeWebService<O> {

  @EJB(beanName = "SecuredGenericTypeServiceImpl")
  private SecuredGenericTypeService<O> securedGenericOrderService;

  @EJB(beanName = "SecuredGenericTypeServiceImpl")
  private SecuredGenericTypeService<Contract> securedGenericContractService;

  @EJB(beanName = "SecuredGenericTypeServiceImpl")
  private SecuredGenericTypeService<Customer> securedGenericCustomerService;

  @Override
  @WebMethod(operationName = "countOrder")
  public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type) {
    try {
      readUpdateContractCustomer(metadata, type.getContract());
      if (type.getContract() != null) {
        final Parameter parameter = new Parameter("contract", Restrictions.or(Example.create(type.getContract())));
        return securedGenericOrderService.count(metadata, type, parameter);
      }
      return securedGenericOrderService.count(metadata, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void createUpdateContractCustomer(MetaData metadata, Contract contract) {
    if (contract != null) {
      if (contract.getId() == 0) {
        securedGenericContractService.create(metadata, contract);
      } else {
        securedGenericContractService.update(metadata, contract);
      }
      createUpdateCustomer(metadata, contract.getCustomer());
    }
  }

  private void createUpdateCustomer(MetaData metadata, Customer customer) {
    if (customer != null) {
      if (customer.getId() == 0) {
        securedGenericCustomerService.create(metadata, customer);
      } else {
        securedGenericCustomerService.update(metadata, customer);
      }
    }
  }

  private void deleteUpdateContractCustomer(MetaData metadata, Contract contract) {
    if (contract != null) {
      if (contract.getId() > 0) {
        securedGenericContractService.delete(metadata, contract);
      }
      deletUpdateCustomer(metadata, contract.getCustomer());
    }
  }

  private void deletUpdateCustomer(MetaData metadata, Customer customer) {
    if (customer != null) {
      if (customer.getId() > 0) {
        securedGenericCustomerService.delete(metadata, customer);
      }
    }
  }

  @Override
  @WebMethod(operationName = "findOrderById")
  public O find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type) {
    try {
      readUpdateContractCustomer(metadata, type.getContract());
      return securedGenericOrderService.find(metadata, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findOrder")
  public List<O> find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type, @WebParam(name = "paging") Paging paging,
      @WebParam(name = "orderBy") OrderBy orderBy) {
    try {
      readUpdateContractCustomer(metadata, type.getContract());
      if (type.getContract() != null) {
        final Parameter parameter = new Parameter("contract", Restrictions.or(Example.create(type.getContract())));
        return securedGenericOrderService.find(metadata, type, paging, orderBy, parameter);
      }
      return securedGenericOrderService.find(metadata, type, paging, orderBy);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "mergeOrder")
  public O merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type) {
    try {
      createUpdateContractCustomer(metadata, type.getContract());
      return securedGenericOrderService.merge(metadata, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "persistOrder")
  public O persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type) {
    try {
      createUpdateContractCustomer(metadata, type.getContract());
      if (type.isDetached()) {
        return securedGenericOrderService.merge(metadata, type);
      }
      securedGenericOrderService.persist(metadata, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void readUpdateContractCustomer(MetaData metadata, Contract contract) {
    if (contract != null) {
      if (contract.getId() > 0) {
        securedGenericContractService.read(metadata, contract);
      }
      readUpdateCustomer(metadata, contract.getCustomer());
    }
  }

  private void readUpdateCustomer(MetaData metadata, Customer customer) {
    if (customer != null) {
      if (customer.getId() > 0) {
        securedGenericCustomerService.read(metadata, customer);
      }
    }
  }

  @Override
  @WebMethod(operationName = "refreshOrder")
  public O refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type) {
    try {
      readUpdateContractCustomer(metadata, type.getContract());
      return securedGenericOrderService.refresh(metadata, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "removeOrder")
  public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type) {
    try {
      deleteUpdateContractCustomer(metadata, type.getContract());
      securedGenericOrderService.remove(metadata, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
