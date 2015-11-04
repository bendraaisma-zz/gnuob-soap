package com.netbrasoft.gnuob.generic.security;

import java.util.List;
import java.util.Set;

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
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = UserWebServiceImpl.USER_WEB_SERVICE_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class})
public class UserWebServiceImpl<T extends User> implements GenericTypeWebService<T> {

  protected static final String USER_WEB_SERVICE_IMPL_NAME = "UserWebServiceImpl";

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<T> securedGenericUserService;

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<Group> securedGenericGroupService;

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<Site> securedGenericSiteService;

  @Override
  @WebMethod(operationName = "countUser")
  public long count(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "user") final T type) {
    try {
      readSites(metaData, type.getSites());
      readGroups(metaData, type.getGroups());
      return securedGenericUserService.count(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void createUpdateGroups(final MetaData metaData, final Set<Group> groups) {
    if (groups != null && !groups.isEmpty()) {
      for (final Group group : groups) {
        if (group.getId() == 0) {
          securedGenericGroupService.create(metaData, group);
        } else {
          securedGenericGroupService.update(metaData, group);
        }
      }
    }
  }

  private void createUpdateSites(final MetaData metaData, final Set<Site> sites) {
    if (sites != null && !sites.isEmpty()) {
      for (final Site site : sites) {
        if (site.getId() == 0) {
          securedGenericSiteService.create(metaData, site);
        } else {
          securedGenericSiteService.update(metaData, site);
        }
      }
    }
  }

  private void deleteGroups(final MetaData metaData, final Set<Group> groups) {
    if (groups != null && !groups.isEmpty()) {
      for (final Group group : groups) {
        if (group.getId() > 0) {
          securedGenericGroupService.delete(metaData, group);
        }
      }
    }
  }

  private void deleteSites(final MetaData metaData, final Set<Site> sites) {
    if (sites != null && !sites.isEmpty()) {
      for (final Site site : sites) {
        if (site.getId() > 0) {
          securedGenericSiteService.delete(metaData, site);
        }
      }
    }
  }

  @Override
  @WebMethod(operationName = "findUserById")
  public T find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "user") final T type) {
    try {
      readSites(metaData, type.getSites());
      readGroups(metaData, type.getGroups());
      return securedGenericUserService.find(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findUser")
  public List<T> find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "user") final T type, @WebParam(name = "paging") final Paging paging,
      @WebParam(name = "orderBy") final OrderBy orderBy) {
    try {
      readSites(metaData, type.getSites());
      readGroups(metaData, type.getGroups());
      return securedGenericUserService.find(metaData, type, paging, orderBy);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "mergeUser")
  public T merge(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "user") final T type) {
    try {
      createUpdateSites(metaData, type.getSites());
      createUpdateGroups(metaData, type.getGroups());
      return securedGenericUserService.merge(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "persistUser")
  public T persist(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "user") final T type) {
    try {
      createUpdateSites(metaData, type.getSites());
      createUpdateGroups(metaData, type.getGroups());
      if (type.isDetached()) {
        return securedGenericUserService.merge(metaData, type);
      }
      securedGenericUserService.persist(metaData, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void readGroups(final MetaData metaData, final Set<Group> groups) {
    if (groups != null && !groups.isEmpty()) {
      for (final Group group : groups) {
        if (group.getId() > 0) {
          securedGenericGroupService.read(metaData, group);
        }
      }
    }
  }

  private void readSites(final MetaData metaData, final Set<Site> sites) {
    if (sites != null && !sites.isEmpty()) {
      for (final Site site : sites) {
        if (site.getId() > 0) {
          securedGenericSiteService.read(metaData, site);
        }
      }
    }
  }

  @Override
  @WebMethod(operationName = "refreshUser")
  public T refresh(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "user") final T type) {
    try {
      readSites(metaData, type.getSites());
      readGroups(metaData, type.getGroups());
      return securedGenericUserService.refresh(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "removeUser")
  public void remove(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "user") final T type) {
    try {
      deleteGroups(metaData, type.getGroups());
      deleteSites(metaData, type.getSites());
      securedGenericUserService.remove(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
