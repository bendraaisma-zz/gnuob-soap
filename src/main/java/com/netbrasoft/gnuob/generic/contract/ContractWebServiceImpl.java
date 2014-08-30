package com.netbrasoft.gnuob.generic.contract;

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
import com.netbrasoft.gnuob.generic.customer.Customer;
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeService;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "ContractWebServiceImpl")
public class ContractWebServiceImpl<C extends Contract> implements GenericTypeWebService<C> {

	@EJB(beanName = "SecuredGenericTypeServiceImpl")
	private SecuredGenericTypeService<C> securedGenericContractService;

	@EJB(beanName = "SecuredGenericTypeServiceImpl")
	private SecuredGenericTypeService<Customer> securedGenericCustomerService;

	@Override
	@WebMethod(operationName = "countContract")
	public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C type) throws GNUOpenBusinessServiceException {
		try {
			return securedGenericContractService.count(metadata, type);
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "findContractById")
	public C find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C type) throws GNUOpenBusinessServiceException {
		try {
			return securedGenericContractService.find(metadata, type, type.getId());
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "findContract")
	public List<C> find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C type, @WebParam(name = "paging") Paging paging, @WebParam(name = "orderBy") OrderBy orderBy) throws GNUOpenBusinessServiceException {
		try {
			return securedGenericContractService.find(metadata, type, paging, orderBy);
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "mergeContract")
	public C merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C type) throws GNUOpenBusinessServiceException {
		try {
			securedGenericContractService.merge(metadata, type);
			return type;
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "persistContract")
	public C persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C type) throws GNUOpenBusinessServiceException {
		try {
			securedGenericCustomerService.persist(metadata, type.getCustomer());
			securedGenericContractService.persist(metadata, type);
			return type;
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "refreshContract")
	public C refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C type) throws GNUOpenBusinessServiceException {
		try {
			return securedGenericContractService.refresh(metadata, type, type.getId());
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "removeContract")
	public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "contract") C type) throws GNUOpenBusinessServiceException {
		try {
			securedGenericContractService.remove(metadata, type);
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

}
