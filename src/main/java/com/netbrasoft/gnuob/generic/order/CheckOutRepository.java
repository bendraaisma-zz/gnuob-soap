package com.netbrasoft.gnuob.generic.order;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.security.MetaData;

public class CheckOutRepository<O extends Order> implements CheckOutWebService<O> {

	private final CheckOutWebService<O> checkOutWebService;

	@SuppressWarnings("unchecked")
	public CheckOutRepository(String jndiName) {
		InitialContext context;
		try {
			context = new InitialContext();
			checkOutWebService = (CheckOutWebService<O>) context.lookup(jndiName);
		} catch (NamingException e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	public O doCheckout(MetaData metadata, O order) throws GNUOpenBusinessServiceException {
		return checkOutWebService.doCheckout(metadata, order);
	}

	@Override
	public O doCheckoutDetails(MetaData metadata, O order) throws GNUOpenBusinessServiceException {
		return checkOutWebService.doCheckoutDetails(metadata, order);
	}

	@Override
	public O doCheckoutPayment(MetaData metadata, O order) throws GNUOpenBusinessServiceException {
		return checkOutWebService.doCheckoutPayment(metadata, order);
	}
}
