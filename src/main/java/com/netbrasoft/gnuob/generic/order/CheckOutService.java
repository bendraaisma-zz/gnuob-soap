package com.netbrasoft.gnuob.generic.order;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;

public interface CheckOutService<O extends Order> {

	void doCheckout(O order) throws GNUOpenBusinessServiceException;

	void doCheckoutDetails(O order) throws GNUOpenBusinessServiceException;

	void doCheckoutPayment(O order) throws GNUOpenBusinessServiceException;
}
