package com.netbrasoft.gnuob.generic.security;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.GenericTypeWebService;
import com.netbrasoft.gnuob.generic.OrderBy;
import com.netbrasoft.gnuob.generic.Paging;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "UserWebServiceImpl")
public class UserWebServiceImpl<U extends User> implements GenericTypeWebService<U> {

	@EJB(beanName = "SecuredGenericTypeServiceImpl")
	private SecuredGenericTypeService<U> securedGenericUserService;

	@EJB(beanName = "SecuredGenericTypeServiceImpl")
	private SecuredGenericTypeService<Group> securedGenericGroupService;

	@EJB(beanName = "SecuredGenericTypeServiceImpl")
	private SecuredGenericTypeService<Site> securedGenericSiteService;

	@Override
	@WebMethod(operationName = "countUser")
	public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type) throws GNUOpenBusinessServiceException {
		try {
			return securedGenericUserService.count(metadata, type);
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "findUserById")
	public U find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type) throws GNUOpenBusinessServiceException {
		try {
			return securedGenericUserService.find(metadata, type, type.getId());
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "findUser")
	public List<U> find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type, @WebParam(name = "paging") Paging paging, @WebParam(name = "orderBy") OrderBy orderBy) throws GNUOpenBusinessServiceException {
		try {
			return securedGenericUserService.find(metadata, type, paging, orderBy);
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "mergeUser")
	public U merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type) throws GNUOpenBusinessServiceException {
		try {
			securedGenericUserService.merge(metadata, type);
			return type;
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "persistUser")
	public U persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type) throws GNUOpenBusinessServiceException {
		try {
			persistUserSites(metadata, type);
			persistUserGroups(metadata, type);
			securedGenericUserService.persist(metadata, type);
			return type;
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	private void persistUserGroups(MetaData metadata, U type) {
		if (!type.getGroups().isEmpty()) {
			for (Group group : type.getGroups()) {
				securedGenericGroupService.persist(metadata, group);
			}
		}
	}

	private void persistUserSites(MetaData metadata, U type) {
		if (!type.getSites().isEmpty()) {
			for (Site site : type.getSites()) {
				securedGenericSiteService.persist(metadata, site);
			}
		}
	}

	@Override
	@WebMethod(operationName = "refreshUser")
	public U refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type) throws GNUOpenBusinessServiceException {
		try {
			securedGenericUserService.refresh(metadata, type);
			return type;
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "removeUser")
	public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "user") U type) throws GNUOpenBusinessServiceException {
		try {
			securedGenericUserService.remove(metadata, type);
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

}
