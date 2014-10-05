package com.netbrasoft.gnuob.generic.order;

public interface CheckOutService<O extends Order> {

    void doCheckout(O order);

    void doCheckoutDetails(O order);

    void doCheckoutPayment(O order);
}
