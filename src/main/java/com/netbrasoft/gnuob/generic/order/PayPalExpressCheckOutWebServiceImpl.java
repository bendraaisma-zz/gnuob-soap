package com.netbrasoft.gnuob.generic.order;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.contract.Contract;
import com.netbrasoft.gnuob.generic.customer.Customer;
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeService;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "PayPalExpressCheckOutWebServiceImpl")
public class PayPalExpressCheckOutWebServiceImpl<O extends Order> implements CheckOutWebService<O> {

	@EJB(beanName = "PayPalExpressCheckOutServiceImpl")
	private CheckOutService<O> checkOutService;

	@EJB(beanName = "SecuredGenericTypeServiceImpl")
	private SecuredGenericTypeService<O> securedGenericOrderService;

	@EJB(beanName = "SecuredGenericTypeServiceImpl")
	private SecuredGenericTypeService<Contract> securedGenericContractService;

	@EJB(beanName = "SecuredGenericTypeServiceImpl")
	private SecuredGenericTypeService<Customer> securedGenericCustomerService;

	@Override
	@WebMethod(operationName = "doCheckout")
	public O doCheckout(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O order) throws GNUOpenBusinessServiceException {
		try {
			checkOutService.doCheckout(order);
			securedGenericCustomerService.merge(metadata, order.getContract().getCustomer());
			securedGenericContractService.merge(metadata, order.getContract());
			securedGenericOrderService.merge(metadata, order);
			return order;
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "doCheckoutDetails")
	public O doCheckoutDetails(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O order) throws GNUOpenBusinessServiceException {
		try {
			checkOutService.doCheckoutDetails(order);
			securedGenericOrderService.merge(metadata, order);
			return order;
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "doCheckoutPayment")
	public O doCheckoutPayment(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "order") O order) throws GNUOpenBusinessServiceException {
		try {
			checkOutService.doCheckoutPayment(order);
			securedGenericOrderService.merge(metadata, order);
			return order;
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

}
