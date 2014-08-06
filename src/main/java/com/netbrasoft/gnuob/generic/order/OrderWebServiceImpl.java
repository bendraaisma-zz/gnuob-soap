package com.netbrasoft.gnuob.generic.order;

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
import com.netbrasoft.gnuob.generic.contract.Contract;
import com.netbrasoft.gnuob.generic.customer.Customer;
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeService;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "OrderWebServiceImpl")
public class OrderWebServiceImpl<O extends Order> implements GenericTypeWebService<O> {

	@EJB(beanName = "SecuredGenericTypeServiceImpl")
	private SecuredGenericTypeService<O> securedGenericOrderService;

	@EJB(beanName = "SecuredGenericTypeServiceImpl")
	private SecuredGenericTypeService<Contract> securedGenericContractService;

	@EJB(beanName = "SecuredGenericTypeServiceImpl")
	private SecuredGenericTypeService<Customer> securedGenericCustomerService;

	@Override
	@WebMethod(operationName = "countOrder")
	public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type) throws GNUOpenBusinessServiceException {
		try {
			return securedGenericOrderService.count(metadata, type);
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "findOrderById")
	public O find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type) throws GNUOpenBusinessServiceException {
		try {
			return securedGenericOrderService.find(metadata, type, type.getId());
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "findOrder")
	public List<O> find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type, @WebParam(name = "paging") Paging paging, @WebParam(name = "orderBy") OrderBy orderBy) throws GNUOpenBusinessServiceException {
		try {
			return securedGenericOrderService.find(metadata, type, paging, orderBy);
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "mergeOrder")
	public O merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type) throws GNUOpenBusinessServiceException {
		try {
			securedGenericCustomerService.merge(metadata, type.getContract().getCustomer());
			securedGenericContractService.merge(metadata, type.getContract());
			securedGenericOrderService.merge(metadata, type);
			return type;
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "persistOrder")
	public O persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type) throws GNUOpenBusinessServiceException {
		try {
			securedGenericCustomerService.persist(metadata, type.getContract().getCustomer());
			securedGenericContractService.persist(metadata, type.getContract());
			securedGenericOrderService.persist(metadata, type);
			return type;
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "refreshOrder")
	public O refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type) throws GNUOpenBusinessServiceException {
		try {
			securedGenericOrderService.refresh(metadata, type);
			return type;
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "removeOrder")
	public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O type) throws GNUOpenBusinessServiceException {
		try {
			securedGenericOrderService.remove(metadata, type);
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

}
