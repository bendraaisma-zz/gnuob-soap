package com.netbrasoft.gnuob.generic.order;

public interface CheckOutService<T extends Order> {

  void doCheckout(T type);

  void doCheckoutDetails(T type);

  void doCheckoutPayment(T type);

  T doNotification(T type);

  void doRefundTransaction(T type);

  void doTransactionDetails(T type);
}
