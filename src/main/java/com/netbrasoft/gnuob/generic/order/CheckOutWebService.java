package com.netbrasoft.gnuob.generic.order;

import com.netbrasoft.gnuob.generic.security.MetaData;

public interface CheckOutWebService<O extends Order> {

    O doCheckout(MetaData metadata, O order);

    O doCheckoutDetails(MetaData metadata, O order);

    O doCheckoutPayment(MetaData metadata, O order);
}
