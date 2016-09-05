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

package br.com.netbrasoft.gnuob.generic.contract;

import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CONTRACT_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CONTRACT_RESTFUL_SERVICE_IMPL;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.COUNT_CONTRACT_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.FIND_CONTRACT_BY_ID_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.FIND_CONTRACT_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.MERGE_CONTRACT_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.META_DATA_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_BY_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAGING_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PERSIST_CONTRACT_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.REFRESH_CONTRACT_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.REMOVE_CONTRACT_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME;
import static br.com.netbrasoft.gnuob.generic.content.mail.MailEnum.WELCOME_NEW_CUSTOMER_MAIL;
import static br.com.netbrasoft.gnuob.generic.factory.MessageCreaterFactory.createMessage;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
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
import br.com.netbrasoft.gnuob.generic.content.mail.MailAction;
import br.com.netbrasoft.gnuob.generic.customer.Customer;
import br.com.netbrasoft.gnuob.generic.security.ISecuredGenericTypeService;
import br.com.netbrasoft.gnuob.generic.security.MetaData;
import br.com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@Path(CONTRACT_RESTFUL_SERVICE_IMPL)
@Consumes({APPLICATION_JSON})
@Produces({APPLICATION_JSON})
@Interceptors(value = {AppSimonInterceptor.class})
public class ContractRESTfulServiceImpl implements IGenericTypeWebService<Contract> {

  private static final Logger LOGGER = getLogger(ContractRESTfulServiceImpl.class);

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<Contract> securedGenericContractService;

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<Customer> securedGenericCustomerService;

  public ContractRESTfulServiceImpl() {
    // This constructor will be used by the EJB container.
  }

  ContractRESTfulServiceImpl(final ISecuredGenericTypeService<Contract> securedGenericContractService,
      final ISecuredGenericTypeService<Customer> securedGenericCustomerService) {
    this.securedGenericContractService = securedGenericContractService;
    this.securedGenericCustomerService = securedGenericCustomerService;
  }

  @Override
  @GET
  @Path(COUNT_CONTRACT_OPERATION_NAME)
  public long count(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(CONTRACT_PARAM_NAME) final Contract type) {
    try {
      return processCount(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(COUNT_CONTRACT_OPERATION_NAME, credentials, type));
    }
  }

  private long processCount(final MetaData credentials, final Contract type) {
    readCustomer(credentials, type.getCustomer());
    return securedGenericContractService.count(credentials, type);
  }

  private void readCustomer(final MetaData credentials, final Customer customer) {
    if (customer != null && customer.isDetached()) {
      securedGenericCustomerService.read(credentials, customer);
    }
  }

  @Override
  @GET
  @Path(FIND_CONTRACT_OPERATION_NAME)
  public List<Contract> find(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(CONTRACT_PARAM_NAME) final Contract type, @QueryParam(PAGING_PARAM_NAME) final Paging paging,
      @QueryParam(ORDER_BY_PARAM_NAME) final OrderByEnum orderingProperty) {
    try {
      return processFind(credentials, type, paging, orderingProperty);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(FIND_CONTRACT_OPERATION_NAME, credentials, type, paging, orderingProperty));
    }
  }

  private List<Contract> processFind(final MetaData credentials, final Contract type, final Paging paging,
      final OrderByEnum orderingProperty) {
    readCustomer(credentials, type.getCustomer());
    return securedGenericContractService.find(credentials, type, paging, orderingProperty);
  }

  @Override
  @GET
  @Path(FIND_CONTRACT_BY_ID_OPERATION_NAME)
  public Contract find(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(CONTRACT_PARAM_NAME) final Contract type) {
    try {
      return processFindById(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(FIND_CONTRACT_BY_ID_OPERATION_NAME, credentials, type));
    }
  }

  private Contract processFindById(final MetaData credentials, final Contract type) {
    readCustomer(credentials, type.getCustomer());
    return securedGenericContractService.find(credentials, type, type.getId());
  }

  @Override
  @POST
  @Path(MERGE_CONTRACT_OPERATION_NAME)
  public Contract merge(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(CONTRACT_PARAM_NAME) final Contract type) {
    try {
      return processMerge(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(MERGE_CONTRACT_OPERATION_NAME, credentials, type));
    }
  }

  private Contract processMerge(final MetaData credentials, final Contract type) {
    createUpdateCustomer(credentials, type.getCustomer());
    return securedGenericContractService.merge(credentials, type);
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
  @Path(PERSIST_CONTRACT_OPERATION_NAME)
  @MailAction(operation = WELCOME_NEW_CUSTOMER_MAIL)
  public Contract persist(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(CONTRACT_PARAM_NAME) final Contract type) {
    try {
      return processPersist(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(PERSIST_CONTRACT_OPERATION_NAME, credentials, type));
    }
  }

  private Contract processPersist(final MetaData credentials, final Contract type) {
    createUpdateCustomer(credentials, type.getCustomer());
    return mergePersistContract(credentials, type);
  }

  private Contract mergePersistContract(final MetaData credentials, final Contract type) {
    if (type.isDetached()) {
      return securedGenericContractService.merge(credentials, type);
    }
    securedGenericContractService.persist(credentials, type);
    return type;
  }

  @Override
  @POST
  @Path(REFRESH_CONTRACT_OPERATION_NAME)
  public Contract refresh(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(CONTRACT_PARAM_NAME) final Contract type) {
    try {
      return processRefresh(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(REFRESH_CONTRACT_OPERATION_NAME, credentials, type));
    }
  }

  private Contract processRefresh(final MetaData credentials, final Contract type) {
    readCustomer(credentials, type.getCustomer());
    return securedGenericContractService.refresh(credentials, type, type.getId());
  }

  @Override
  @DELETE
  @Path(REMOVE_CONTRACT_OPERATION_NAME)
  public void remove(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(CONTRACT_PARAM_NAME) final Contract type) {
    try {
      processRemove(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(REMOVE_CONTRACT_OPERATION_NAME, credentials, type));
    }
  }

  private void processRemove(final MetaData credentials, final Contract type) {
    deleteCustomer(credentials, type.getCustomer());
    securedGenericContractService.remove(credentials, type);
  }

  private void deleteCustomer(final MetaData credentials, final Customer customer) {
    if (customer != null && customer.isDetached()) {
      securedGenericCustomerService.delete(credentials, customer);
    }
  }
}
