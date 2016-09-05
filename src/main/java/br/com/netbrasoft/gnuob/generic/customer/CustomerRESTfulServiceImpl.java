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

package br.com.netbrasoft.gnuob.generic.customer;

import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.COUNT_CUSTOMER_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CUSTOMER_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CUSTOMER_RESTFUL_SERVICE_IMPL;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.FIND_CUSTOMER_BY_ID_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.FIND_CUSTOMER_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.MERGE_CUSTOMER_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.META_DATA_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_BY_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAGING_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PERSIST_CUSTOMER_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.REFRESH_CUSTOMER_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.REMOVE_CUSTOMER_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME;
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
import br.com.netbrasoft.gnuob.generic.security.ISecuredGenericTypeService;
import br.com.netbrasoft.gnuob.generic.security.MetaData;
import br.com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@Path(CUSTOMER_RESTFUL_SERVICE_IMPL)
@Consumes({APPLICATION_JSON})
@Produces({APPLICATION_JSON})
@Interceptors(value = {AppSimonInterceptor.class})
public class CustomerRESTfulServiceImpl implements IGenericTypeWebService<Customer> {

  private static final Logger LOGGER = getLogger(CustomerRESTfulServiceImpl.class);

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<Customer> securedGenericCustomerService;

  public CustomerRESTfulServiceImpl() {
    // This constructor will be used by the EJB container.
  }

  CustomerRESTfulServiceImpl(final ISecuredGenericTypeService<Customer> securedGenericCustomerService) {
    this.securedGenericCustomerService = securedGenericCustomerService;
  }

  @Override
  @GET
  @Path(COUNT_CUSTOMER_OPERATION_NAME)
  public long count(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(CUSTOMER_PARAM_NAME) final Customer type) {
    try {
      return securedGenericCustomerService.count(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(COUNT_CUSTOMER_OPERATION_NAME, credentials, type));
    }
  }

  @Override
  @GET
  @Path(FIND_CUSTOMER_BY_ID_OPERATION_NAME)
  public Customer find(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(CUSTOMER_PARAM_NAME) final Customer type) {
    try {
      return securedGenericCustomerService.find(credentials, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(FIND_CUSTOMER_BY_ID_OPERATION_NAME, credentials, type));
    }
  }

  @Override
  @GET
  @Path(FIND_CUSTOMER_OPERATION_NAME)
  public List<Customer> find(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(CUSTOMER_PARAM_NAME) final Customer type, @QueryParam(PAGING_PARAM_NAME) final Paging paging,
      @QueryParam(ORDER_BY_PARAM_NAME) final OrderByEnum orderingProperty) {
    try {
      return securedGenericCustomerService.find(credentials, type, paging, orderingProperty);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(FIND_CUSTOMER_OPERATION_NAME, credentials, type, paging, orderingProperty));
    }
  }

  @Override
  @POST
  @Path(MERGE_CUSTOMER_OPERATION_NAME)
  public Customer merge(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(CUSTOMER_PARAM_NAME) final Customer type) {
    try {
      return securedGenericCustomerService.merge(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(MERGE_CUSTOMER_OPERATION_NAME, credentials, type));
    }
  }

  @Override
  @PUT
  @Path(PERSIST_CUSTOMER_OPERATION_NAME)
  public Customer persist(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(CUSTOMER_PARAM_NAME) final Customer type) {
    try {
      return persistMergeCustomer(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(PERSIST_CUSTOMER_OPERATION_NAME, credentials, type));
    }
  }

  private Customer persistMergeCustomer(final MetaData credentials, final Customer type) {
    if (type.isDetached()) {
      return securedGenericCustomerService.merge(credentials, type);
    }
    securedGenericCustomerService.persist(credentials, type);
    return type;
  }

  @Override
  @POST
  @Path(REFRESH_CUSTOMER_OPERATION_NAME)
  public Customer refresh(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(CUSTOMER_PARAM_NAME) final Customer type) {
    try {
      return securedGenericCustomerService.refresh(credentials, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(REFRESH_CUSTOMER_OPERATION_NAME, credentials, type));
    }
  }

  @Override
  @DELETE
  @Path(REMOVE_CUSTOMER_OPERATION_NAME)
  public void remove(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(CUSTOMER_PARAM_NAME) final Customer type) {
    try {
      securedGenericCustomerService.remove(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(REMOVE_CUSTOMER_OPERATION_NAME, credentials, type));
    }
  }
}
