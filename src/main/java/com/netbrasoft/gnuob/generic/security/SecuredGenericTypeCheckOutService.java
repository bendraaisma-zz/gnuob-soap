package com.netbrasoft.gnuob.generic.security;

public interface SecuredGenericTypeCheckOutService<T> {

  void doCheckout(MetaData metaData, T type);

  void doCheckoutDetails(MetaData metaData, T type);

  void doCheckoutPayment(MetaData metaData, T type);

  T doNotification(MetaData metaData, T type);

  void doRefundTransaction(MetaData metaData, T type);

  void doTransactionDetails(MetaData metaData, T type);
}
