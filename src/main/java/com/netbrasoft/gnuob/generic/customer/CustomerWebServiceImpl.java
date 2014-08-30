package com.netbrasoft.gnuob.generic.customer;

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
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeService;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "CustomerWebServiceImpl")
public class CustomerWebServiceImpl<C extends Customer> implements GenericTypeWebService<C> {

	@EJB(beanName = "SecuredGenericTypeServiceImpl")
	private SecuredGenericTypeService<C> securedGenericCustomerService;

	@Override
	@WebMethod(operationName = "countCustomer")
	public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "customer") C type) throws GNUOpenBusinessServiceException {
		try {
			return securedGenericCustomerService.count(metadata, type);
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "findCustomerById")
	public C find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "customer") C type) throws GNUOpenBusinessServiceException {
		try {
			return securedGenericCustomerService.find(metadata, type, type.getId());
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "findCustomer")
	public List<C> find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "customer") C type, @WebParam(name = "paging") Paging paging, @WebParam(name = "orderBy") OrderBy orderBy) throws GNUOpenBusinessServiceException {
		try {
			return securedGenericCustomerService.find(metadata, type, paging, orderBy);
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "mergeCustomer")
	public C merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "customer") C type) throws GNUOpenBusinessServiceException {
		try {
			securedGenericCustomerService.merge(metadata, type);
			return type;
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "persistCustomer")
	public C persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "customer") C type) throws GNUOpenBusinessServiceException {
		try {
			securedGenericCustomerService.persist(metadata, type);
			return type;
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "refreshCustomer")
	public C refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "customer") C type) throws GNUOpenBusinessServiceException {
		try {
			return securedGenericCustomerService.refresh(metadata, type, type.getId());
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "removeCustomer")
	public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "customer") C type) throws GNUOpenBusinessServiceException {
		try {
			securedGenericCustomerService.remove(metadata, type);
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

}
