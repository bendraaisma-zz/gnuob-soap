package com.netbrasoft.gnuob.generic.order;

import com.netbrasoft.gnuob.generic.security.MetaData;

public interface CheckOutWebService<O extends Order> {

  O doCheckout(MetaData metadata, O type);

  O doCheckoutDetails(MetaData metadata, O type);

  O doCheckoutPayment(MetaData metadata, O type);

  O doNotification(MetaData metadata, O type);

  O doRefundTransaction(MetaData metadata, O type);

  O doTransactionDetails(MetaData metadata, O type);
}
