package com.netbrasoft.gnuob.generic.security;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import com.netbrasoft.gnuob.generic.order.Order;
import com.netbrasoft.gnuob.generic.order.PayPalExpressCheckOutServiceImpl;
import com.netbrasoft.gnuob.generic.security.Rule.Operation;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@Stateless(name = "SecuredPayPalExpressCheckOutServiceImpl", mappedName = "SecuredPayPalExpressCheckOutServiceImpl")
@Interceptors(value = {AccessControl.class, AppSimonInterceptor.class})
public class SecuredPayPalExpressCheckOutServiceImpl<T extends Order> extends PayPalExpressCheckOutServiceImpl<T> implements SecuredGenericTypeCheckOutService<T> {

  @Override
  @OperationAccess(operation = Operation.UPDATE)
  public void doCheckout(MetaData metadata, T type) {
    super.doCheckout(type);
  }

  @Override
  @OperationAccess(operation = Operation.UPDATE)
  public void doCheckoutDetails(MetaData metadata, T type) {
    super.doCheckoutDetails(type);
  }

  @Override
  @OperationAccess(operation = Operation.UPDATE)
  public void doCheckoutPayment(MetaData metadata, T type) {
    super.doCheckoutPayment(type);
  }

  @Override
  @OperationAccess(operation = Operation.UPDATE)
  public T doNotification(MetaData metadata, T type) {
    return super.doNotification(type);
  }

  @Override
  @OperationAccess(operation = Operation.UPDATE)
  public void doRefundTransaction(MetaData metadata, T type) {
    super.doRefundTransaction(type);
  }

  @Override
  @OperationAccess(operation = Operation.UPDATE)
  public void doTransactionDetails(MetaData metadata, T type) {
    super.doTransactionDetails(type);
  }
}
