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

package br.com.netbrasoft.gnuob.generic.security;

import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.COUNT_USER_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.FIND_USER_BY_ID_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.FIND_USER_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.MERGE_USER_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.META_DATA_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_BY_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAGING_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PERSIST_USER_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.REFRESH_USER_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.REMOVE_USER_OPERATION_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.USER_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.USER_RESTFUL_SERVICE_IMPL;
import static br.com.netbrasoft.gnuob.generic.factory.MessageCreaterFactory.createMessage;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Set;

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
import br.com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@Path(USER_RESTFUL_SERVICE_IMPL)
@Consumes({APPLICATION_JSON})
@Produces({APPLICATION_JSON})
@Interceptors(value = {AppSimonInterceptor.class})
public class UserRESTfulServiceImpl implements IGenericTypeWebService<User> {

  private static final Logger LOGGER = getLogger(UserRESTfulServiceImpl.class);

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<User> securedGenericUserService;

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<Group> securedGenericGroupService;

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<Site> securedGenericSiteService;

  public UserRESTfulServiceImpl() {
    // This constructor will be used by the EJB container.
  }

  UserRESTfulServiceImpl(final ISecuredGenericTypeService<User> securedGenericUserService,
      final ISecuredGenericTypeService<Group> securedGenericGroupService,
      final ISecuredGenericTypeService<Site> securedGenericSiteService) {
    this.securedGenericUserService = securedGenericUserService;
    this.securedGenericGroupService = securedGenericGroupService;
    this.securedGenericSiteService = securedGenericSiteService;
  }

  @Override
  @GET
  @Path(COUNT_USER_OPERATION_NAME)
  public long count(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(USER_PARAM_NAME) final User type) {
    try {
      return processCount(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(COUNT_USER_OPERATION_NAME, credentials, type));
    }
  }

  private long processCount(final MetaData credentials, final User type) {
    readSites(credentials, type.getSites());
    readGroups(credentials, type.getGroups());
    return securedGenericUserService.count(credentials, type);
  }

  private void readSites(final MetaData credentials, final Set<Site> sites) {
    if (sites != null) {
      sites.stream().filter(e -> e.isDetached()).forEach(e -> securedGenericSiteService.read(credentials, e));
    }
  }

  private void readGroups(final MetaData credentials, final Set<Group> groups) {
    if (groups != null) {
      groups.stream().filter(e -> e.isDetached()).forEach(e -> securedGenericGroupService.read(credentials, e));
    }
  }

  @Override
  @GET
  @Path(FIND_USER_BY_ID_OPERATION_NAME)
  public User find(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(USER_PARAM_NAME) final User type) {
    try {
      return processFindById(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(FIND_USER_BY_ID_OPERATION_NAME, credentials, type));
    }
  }

  private User processFindById(final MetaData credentials, final User type) {
    readSites(credentials, type.getSites());
    readGroups(credentials, type.getGroups());
    return securedGenericUserService.find(credentials, type, type.getId());
  }

  @Override
  @GET
  @Path(FIND_USER_OPERATION_NAME)
  public List<User> find(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(USER_PARAM_NAME) final User type, @QueryParam(PAGING_PARAM_NAME) final Paging paging,
      @QueryParam(ORDER_BY_PARAM_NAME) final OrderByEnum orderingProperty) {
    try {
      return processFind(credentials, type, paging, orderingProperty);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(FIND_USER_OPERATION_NAME, credentials, type, paging, orderingProperty));
    }
  }

  private List<User> processFind(final MetaData credentials, final User type, final Paging paging,
      final OrderByEnum orderingProperty) {
    readSites(credentials, type.getSites());
    readGroups(credentials, type.getGroups());
    return securedGenericUserService.find(credentials, type, paging, orderingProperty);
  }

  @Override
  @POST
  @Path(MERGE_USER_OPERATION_NAME)
  public User merge(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(USER_PARAM_NAME) final User type) {
    try {
      return processMerge(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(MERGE_USER_OPERATION_NAME, credentials, type));
    }
  }

  private User processMerge(final MetaData credentials, final User type) {
    createUpdateSites(credentials, type.getSites());
    createUpdateGroups(credentials, type.getGroups());
    return securedGenericUserService.merge(credentials, type);
  }

  private void createUpdateSites(final MetaData credentials, final Set<Site> sites) {
    if (sites != null) {
      sites.stream().filter(e -> !e.isDetached()).forEach(e -> securedGenericSiteService.create(credentials, e));
      sites.stream().filter(e -> e.isDetached()).forEach(e -> securedGenericSiteService.update(credentials, e));
    }
  }

  private void createUpdateGroups(final MetaData credentials, final Set<Group> groups) {
    if (groups != null) {
      groups.stream().filter(e -> !e.isDetached()).forEach(e -> securedGenericGroupService.create(credentials, e));
      groups.stream().filter(e -> e.isDetached()).forEach(e -> securedGenericGroupService.update(credentials, e));
    }
  }

  @Override
  @PUT
  @Path(PERSIST_USER_OPERATION_NAME)
  public User persist(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(USER_PARAM_NAME) final User type) {
    try {
      return processPersist(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(PERSIST_USER_OPERATION_NAME, credentials, type));
    }
  }

  private User processPersist(final MetaData credentials, final User type) {
    createUpdateSites(credentials, type.getSites());
    createUpdateGroups(credentials, type.getGroups());
    return mergeOrPersistUser(credentials, type);
  }

  private User mergeOrPersistUser(final MetaData credentials, final User type) {
    if (type.isDetached()) {
      return securedGenericUserService.merge(credentials, type);
    }
    securedGenericUserService.persist(credentials, type);
    return type;
  }

  @Override
  @POST
  @Path(REFRESH_USER_OPERATION_NAME)
  public User refresh(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(USER_PARAM_NAME) final User type) {
    try {
      return processRefresh(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(REFRESH_USER_OPERATION_NAME, credentials, type));
    }
  }

  private User processRefresh(final MetaData credentials, final User type) {
    readSites(credentials, type.getSites());
    readGroups(credentials, type.getGroups());
    return securedGenericUserService.refresh(credentials, type, type.getId());
  }

  @Override
  @DELETE
  @Path(REMOVE_USER_OPERATION_NAME)
  public void remove(@QueryParam(META_DATA_PARAM_NAME) final MetaData credentials,
      @QueryParam(USER_PARAM_NAME) final User type) {
    try {
      processRemove(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      LOGGER.debug(createMessage(REMOVE_USER_OPERATION_NAME, credentials, type));
    }
  }

  private void processRemove(final MetaData credentials, final User type) {
    deleteSites(credentials, type.getSites());
    deleteGroups(credentials, type.getGroups());
    securedGenericUserService.remove(credentials, type);
  }

  private void deleteSites(final MetaData credentials, final Set<Site> sites) {
    if (sites != null) {
      sites.stream().filter(e -> e.isDetached()).forEach(e -> securedGenericSiteService.delete(credentials, e));
    }
  }

  private void deleteGroups(final MetaData credentials, final Set<Group> groups) {
    if (groups != null) {
      groups.stream().filter(e -> e.isDetached()).forEach(e -> securedGenericGroupService.delete(credentials, e));
    }
  }
}
