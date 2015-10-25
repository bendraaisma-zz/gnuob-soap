package com.netbrasoft.gnuob.generic.security;

public interface SecuredGenericTypeCheckOutService<T> {

  void doCheckout(MetaData metadata, T type);

  void doCheckoutDetails(MetaData metadata, T type);

  void doCheckoutPayment(MetaData metadata, T type);

  T doNotification(MetaData metadata, T type);

  void doRefundTransaction(MetaData metadata, T type);

  void doTransactionDetails(MetaData metadata, T type);
}
