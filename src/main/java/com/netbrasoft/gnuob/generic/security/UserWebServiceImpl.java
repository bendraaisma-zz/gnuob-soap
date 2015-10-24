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
@Stateless(name = "UserWebServiceImpl")
@Interceptors(value = {AppSimonInterceptor.class})
public class UserWebServiceImpl<U extends User> implements GenericTypeWebService<U> {

  @EJB(beanName = "SecuredGenericTypeServiceImpl")
  private SecuredGenericTypeService<U> securedGenericUserService;

  @EJB(beanName = "SecuredGenericTypeServiceImpl")
  private SecuredGenericTypeService<Group> securedGenericGroupService;

  @EJB(beanName = "SecuredGenericTypeServiceImpl")
  private SecuredGenericTypeService<Site> securedGenericSiteService;

  @Override
  @WebMethod(operationName = "countUser")
  public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type) {
    try {
      readSites(metadata, type.getSites());
      readGroups(metadata, type.getGroups());
      return securedGenericUserService.count(metadata, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void createUpdateGroups(MetaData metadata, Set<Group> groups) {
    if (groups != null && !groups.isEmpty()) {
      for (final Group group : groups) {
        if (group.getId() == 0) {
          securedGenericGroupService.create(metadata, group);
        } else {
          securedGenericGroupService.update(metadata, group);
        }
      }
    }
  }

  private void createUpdateSites(MetaData metadata, Set<Site> sites) {
    if (sites != null && !sites.isEmpty()) {
      for (final Site site : sites) {
        if (site.getId() == 0) {
          securedGenericSiteService.create(metadata, site);
        } else {
          securedGenericSiteService.update(metadata, site);
        }
      }
    }
  }

  private void deleteGroups(MetaData metadata, Set<Group> groups) {
    if (groups != null && !groups.isEmpty()) {
      for (final Group group : groups) {
        if (group.getId() > 0) {
          securedGenericGroupService.delete(metadata, group);
        }
      }
    }
  }

  private void deleteSites(MetaData metadata, Set<Site> sites) {
    if (sites != null && !sites.isEmpty()) {
      for (final Site site : sites) {
        if (site.getId() > 0) {
          securedGenericSiteService.delete(metadata, site);
        }
      }
    }
  }

  @Override
  @WebMethod(operationName = "findUserById")
  public U find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type) {
    try {
      readSites(metadata, type.getSites());
      readGroups(metadata, type.getGroups());
      return securedGenericUserService.find(metadata, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findUser")
  public List<U> find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type, @WebParam(name = "paging") Paging paging,
      @WebParam(name = "orderBy") OrderBy orderBy) {
    try {
      readSites(metadata, type.getSites());
      readGroups(metadata, type.getGroups());
      return securedGenericUserService.find(metadata, type, paging, orderBy);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "mergeUser")
  public U merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type) {
    try {
      createUpdateSites(metadata, type.getSites());
      createUpdateGroups(metadata, type.getGroups());
      return securedGenericUserService.merge(metadata, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "persistUser")
  public U persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type) {
    try {
      createUpdateSites(metadata, type.getSites());
      createUpdateGroups(metadata, type.getGroups());
      if (type.isDetached()) {
        return securedGenericUserService.merge(metadata, type);
      }
      securedGenericUserService.persist(metadata, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void readGroups(MetaData metadata, Set<Group> groups) {
    if (groups != null && !groups.isEmpty()) {
      for (final Group group : groups) {
        if (group.getId() > 0) {
          securedGenericGroupService.read(metadata, group);
        }
      }
    }
  }

  private void readSites(MetaData metadata, Set<Site> sites) {
    if (sites != null && !sites.isEmpty()) {
      for (final Site site : sites) {
        if (site.getId() > 0) {
          securedGenericSiteService.read(metadata, site);
        }
      }
    }
  }

  @Override
  @WebMethod(operationName = "refreshUser")
  public U refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type) {
    try {
      readSites(metadata, type.getSites());
      readGroups(metadata, type.getGroups());
      return securedGenericUserService.refresh(metadata, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "removeUser")
  public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type) {
    try {
      deleteGroups(metadata, type.getGroups());
      deleteSites(metadata, type.getSites());
      securedGenericUserService.remove(metadata, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
