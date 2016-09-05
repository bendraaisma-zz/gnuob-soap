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

package br.com.netbrasoft.gnuob.generic.content;

import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CONTENT_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CONTENT_RESTFUL_SERVICE_IMPL;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.COUNT_CONTENT_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.FIND_CONTENT_BY_ID_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.FIND_CONTENT_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.MERGE_CONTENT_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.META_DATA_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_BY_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAGING_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PERSIST_CONTENT_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.REFRESH_CONTENT_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.REMOVE_CONTENT_OPERATION_NAME;
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

@Path(CONTENT_RESTFUL_SERVICE_IMPL)
@Consumes({APPLICATION_JSON})
@Produces({APPLICATION_JSON})
@Interceptors(value = {AppSimonInterceptor.class})
public class ContentRESTfulServiceImpl implements IGenericTypeWebService<Content> {

  private static final Logger LOGGER = getLogger(ContentRESTfulServiceImpl.class);

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<Content> securedGenericContentService;

  public ContentRESTfulServiceImpl() {
    // This constructor will be used by the EJB container.
  }

  ContentRESTfulServiceImpl(final ISecuredGenericTypeService<Content> securedGenericContentService) {
    this.securedGenericContentService = securedGenericContentService;
  }

  @Override
  @GET
  @Path(COUNT_CONTENT_OPERATION_NAME)
  public long count(@QueryParam(META_DATA_PARAM_NAME) MetaData credentials,
      @QueryParam(CONTENT_PARAM_NAME) Content type) {
    try {
      return securedGenericContentService.count(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(COUNT_CONTENT_OPERATION_NAME, credentials, type));
    }
  }

  @Override
  @GET
  @Path(FIND_CONTENT_BY_ID_OPERATION_NAME)
  public Content find(@QueryParam(META_DATA_PARAM_NAME) MetaData credentials,
      @QueryParam(CONTENT_PARAM_NAME) Content type) {
    try {
      return securedGenericContentService.find(credentials, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(FIND_CONTENT_BY_ID_OPERATION_NAME, credentials, type));
    }
  }

  @Override
  @GET
  @Path(FIND_CONTENT_OPERATION_NAME)
  public List<Content> find(@QueryParam(META_DATA_PARAM_NAME) MetaData credentials,
      @QueryParam(CONTENT_PARAM_NAME) Content type, @QueryParam(PAGING_PARAM_NAME) final Paging paging,
      @QueryParam(ORDER_BY_PARAM_NAME) OrderByEnum orderingProperty) {
    try {
      return securedGenericContentService.find(credentials, type, paging, orderingProperty);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(FIND_CONTENT_OPERATION_NAME, credentials, type, paging, orderingProperty));
    }
  }

  @Override
  @POST
  @Path(MERGE_CONTENT_OPERATION_NAME)
  public Content merge(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(CONTENT_PARAM_NAME) final Content type) {
    try {
      return securedGenericContentService.merge(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(MERGE_CONTENT_OPERATION_NAME, credentials, type));
    }
  }

  @Override
  @PUT
  @Path(PERSIST_CONTENT_OPERATION_NAME)
  public Content persist(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(CONTENT_PARAM_NAME) final Content type) {
    try {
      return persitMergeContent(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(PERSIST_CONTENT_OPERATION_NAME, credentials, type));
    }
  }

  private Content persitMergeContent(final MetaData credentials, final Content type) {
    if (type.isDetached()) {
      return securedGenericContentService.merge(credentials, type);
    }
    securedGenericContentService.persist(credentials, type);
    return type;
  }

  @Override
  @POST
  @Path(REFRESH_CONTENT_OPERATION_NAME)
  public Content refresh(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(CONTENT_PARAM_NAME) final Content type) {
    try {
      return securedGenericContentService.refresh(credentials, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(REFRESH_CONTENT_OPERATION_NAME, credentials, type));
    }
  }

  @Override
  @DELETE
  @Path(REMOVE_CONTENT_OPERATION_NAME)
  public void remove(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(CONTENT_PARAM_NAME) final Content type) {
    try {
      securedGenericContentService.remove(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(REMOVE_CONTENT_OPERATION_NAME, credentials, type));
    }
  }
}
