/*
 * Copyright 2016 Netbrasoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package br.com.netbrasoft.gnuob.generic.order;

import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CONTRACT_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.COUNT_ORDER_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.FIND_ORDER_BY_ID_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.FIND_ORDER_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.MERGE_ORDER_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.META_DATA_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_BY_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_RESTFUL_SERVICE_IMPL;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAGING_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PERSIST_ORDER_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.REFRESH_ORDER_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.REMOVE_ORDER_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME;
import static br.com.netbrasoft.gnuob.generic.factory.MessageCreaterFactory.createMessage;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hibernate.criterion.Example.create;
import static org.hibernate.criterion.Restrictions.or;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;

import br.com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import br.com.netbrasoft.gnuob.generic.IGenericTypeWebService;
import br.com.netbrasoft.gnuob.generic.OrderByEnum;
import br.com.netbrasoft.gnuob.generic.Paging;
import br.com.netbrasoft.gnuob.generic.Parameter;
import br.com.netbrasoft.gnuob.generic.contract.Contract;
import br.com.netbrasoft.gnuob.generic.customer.Customer;
import br.com.netbrasoft.gnuob.generic.security.ISecuredGenericTypeService;
import br.com.netbrasoft.gnuob.generic.security.MetaData;
import br.com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@Path(ORDER_RESTFUL_SERVICE_IMPL)
@Consumes({APPLICATION_JSON})
@Produces({APPLICATION_JSON})
@Interceptors(value = {AppSimonInterceptor.class})
public class OrderRESTfulServiceImpl implements IGenericTypeWebService<Order> {

  private static final Logger LOGGER = getLogger(OrderRESTfulServiceImpl.class);

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<Order> securedGenericOrderService;

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<Contract> securedGenericContractService;

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<Customer> securedGenericCustomerService;

  public OrderRESTfulServiceImpl() {
    // This constructor will be used by the EJB container.
  }

  OrderRESTfulServiceImpl(final ISecuredGenericTypeService<Order> securedGenericOrderService,
      final ISecuredGenericTypeService<Contract> securedGenericContractService,
      final ISecuredGenericTypeService<Customer> securedGenericCustomerService) {
    this.securedGenericOrderService = securedGenericOrderService;
    this.securedGenericContractService = securedGenericContractService;
    this.securedGenericCustomerService = securedGenericCustomerService;
  }

  @Override
  @GET
  @Path(COUNT_ORDER_OPERATION_NAME)
  public long count(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(ORDER_PARAM_NAME) final Order type) {
    try {
      return processCount(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(COUNT_ORDER_OPERATION_NAME, credentials, type));
    }
  }

  private long processCount(final MetaData credentials, final Order type) {
    readContract(credentials, type.getContract());
    return countByOrder(credentials, type);
  }

  private void readContract(final MetaData credentials, final Contract contract) {
    if (contract != null) {
      if (contract.isDetached()) {
        securedGenericContractService.read(credentials, contract);
      }
      readCustomer(credentials, contract.getCustomer());
    }
  }

  private void readCustomer(final MetaData credentials, final Customer customer) {
    if (customer != null && customer.isDetached()) {
      securedGenericCustomerService.read(credentials, customer);
    }
  }

  private long countByOrder(final MetaData credentials, final Order type) {
    if (type.getContract() != null) {
      return securedGenericOrderService.count(credentials, type,
          Parameter.getInstance(CONTRACT_PARAM_NAME, or(create(type.getContract()))));
    }
    return securedGenericOrderService.count(credentials, type);
  }

  @Override
  @GET
  @Path(FIND_ORDER_OPERATION_NAME)
  public List<Order> find(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(ORDER_PARAM_NAME) final Order type, @QueryParam(PAGING_PARAM_NAME) final Paging paging,
      @QueryParam(ORDER_BY_PARAM_NAME) final OrderByEnum orderingProperty) {
    try {
      return processFind(credentials, type, paging, orderingProperty);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(FIND_ORDER_OPERATION_NAME, credentials, type, paging, orderingProperty));
    }
  }

  private List<Order> processFind(final MetaData credentials, final Order type, final Paging paging,
      final OrderByEnum orderingProperty) {
    readContract(credentials, type.getContract());
    return findByOrder(credentials, type, paging, orderingProperty);
  }

  private List<Order> findByOrder(final MetaData credentials, final Order type, final Paging paging,
      final OrderByEnum orderingProperty) {
    if (type.getContract() != null) {
      return findByOrderAndContract(credentials, type, paging, orderingProperty);
    }
    return securedGenericOrderService.find(credentials, type, paging, orderingProperty);
  }

  private List<Order> findByOrderAndContract(final MetaData credentials, final Order type, final Paging paging,
      final OrderByEnum orderingProperty) {
    return securedGenericOrderService.find(credentials, type, paging, orderingProperty,
        Parameter.getInstance(CONTRACT_PARAM_NAME, or(create(type.getContract()))));
  }

  @Override
  @GET
  @Path(FIND_ORDER_BY_ID_OPERATION_NAME)
  public Order find(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(ORDER_PARAM_NAME) final Order type) {
    try {
      return processFindById(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(FIND_ORDER_BY_ID_OPERATION_NAME, credentials, type));
    }
  }

  private Order processFindById(final MetaData credentials, final Order type) {
    readContract(credentials, type.getContract());
    return securedGenericOrderService.find(credentials, type, type.getId());
  }

  @Override
  @POST
  @Path(MERGE_ORDER_OPERATION_NAME)
  public Order merge(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(ORDER_PARAM_NAME) final Order type) {
    try {
      createUpdateContract(credentials, type.getContract());
      return securedGenericOrderService.merge(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(MERGE_ORDER_OPERATION_NAME, credentials, type));
    }
  }

  private void createUpdateContract(final MetaData credentials, final Contract contract) {
    if (contract != null) {
      if (contract.isDetached()) {
        securedGenericContractService.update(credentials, contract);
      } else {
        securedGenericContractService.create(credentials, contract);
      }
      createUpdateCustomer(credentials, contract.getCustomer());
    }
  }

  private void createUpdateCustomer(final MetaData credentials, final Customer customer) {
    if (customer != null) {
      if (customer.isDetached()) {
        securedGenericCustomerService.update(credentials, customer);
      } else {
        securedGenericCustomerService.create(credentials, customer);
      }
    }
  }

  @Override
  @PUT
  @Path(PERSIST_ORDER_OPERATION_NAME)
  public Order persist(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(ORDER_PARAM_NAME) final Order type) {
    try {
      return processPersist(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(PERSIST_ORDER_OPERATION_NAME, credentials, type));
    }
  }

  private Order processPersist(final MetaData credentials, final Order type) {
    createUpdateContract(credentials, type.getContract());
    return mergePersistOrderType(credentials, type);
  }

  private Order mergePersistOrderType(final MetaData credentials, final Order type) {
    if (type.isDetached()) {
      return securedGenericOrderService.merge(credentials, type);
    }
    securedGenericOrderService.persist(credentials, type);
    return type;
  }

  @Override
  @POST
  @Path(REFRESH_ORDER_OPERATION_NAME)
  public Order refresh(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(ORDER_PARAM_NAME) final Order type) {
    try {
      return processRefresh(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(REFRESH_ORDER_OPERATION_NAME, credentials, type));
    }
  }

  private Order processRefresh(final MetaData credentials, final Order type) {
    readContract(credentials, type.getContract());
    return securedGenericOrderService.refresh(credentials, type, type.getId());
  }

  @Override
  @DELETE
  @Path(REMOVE_ORDER_OPERATION_NAME)
  public void remove(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(ORDER_PARAM_NAME) final Order type) {
    try {
      processRemove(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(REMOVE_ORDER_OPERATION_NAME, credentials, type));
    }
  }

  private void processRemove(final MetaData credentials, final Order type) {
    deleteContract(credentials, type.getContract());
    securedGenericOrderService.remove(credentials, type);
  }

  private void deleteContract(final MetaData credentials, final Contract contract) {
    if (contract != null) {
      if (contract.isDetached()) {
        securedGenericContractService.delete(credentials, contract);
      }
      deleteCustomer(credentials, contract.getCustomer());
    }
  }

  private void deleteCustomer(final MetaData credentials, final Customer customer) {
    if (customer != null && customer.isDetached()) {
      securedGenericCustomerService.delete(credentials, customer);
    }
  }
}
