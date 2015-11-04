package com.netbrasoft.gnuob.generic.order;

import com.netbrasoft.gnuob.generic.security.MetaData;

public interface CheckOutWebService<T extends Order> {

  T doCheckout(MetaData metadata, T type);

  T doCheckoutDetails(MetaData metadata, T type);

  T doCheckoutPayment(MetaData metadata, T type);

  T doNotification(MetaData metadata, T type);

  T doRefundTransaction(MetaData metadata, T type);

  T doTransactionDetails(MetaData metadata, T type);
}
