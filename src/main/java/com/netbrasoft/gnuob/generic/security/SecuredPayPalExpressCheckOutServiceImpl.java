package com.netbrasoft.gnuob.generic.security;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import com.netbrasoft.gnuob.generic.order.Order;
import com.netbrasoft.gnuob.generic.order.PayPalExpressCheckOutServiceImpl;
import com.netbrasoft.gnuob.generic.security.Rule.Operation;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@Stateless(name = SecuredPayPalExpressCheckOutServiceImpl.SECURED_PAY_PAL_EXPRESS_CHECK_OUT_SERVICE_IMPL_NAME,
    mappedName = SecuredPayPalExpressCheckOutServiceImpl.SECURED_PAY_PAL_EXPRESS_CHECK_OUT_SERVICE_IMPL_NAME)
@Interceptors(value = {AccessControl.class, AppSimonInterceptor.class})
public class SecuredPayPalExpressCheckOutServiceImpl<T extends Order> extends PayPalExpressCheckOutServiceImpl<T> implements SecuredGenericTypeCheckOutService<T> {

  public static final String SECURED_PAY_PAL_EXPRESS_CHECK_OUT_SERVICE_IMPL_NAME = "SecuredPayPalExpressCheckOutServiceImpl";

  @Override
  @OperationAccess(operation = Operation.UPDATE)
  public void doCheckout(final MetaData metaData, final T type) {
    super.doCheckout(type);
  }

  @Override
  @OperationAccess(operation = Operation.UPDATE)
  public void doCheckoutDetails(final MetaData metaData, final T type) {
    super.doCheckoutDetails(type);
  }

  @Override
  @OperationAccess(operation = Operation.UPDATE)
  public void doCheckoutPayment(final MetaData metaData, final T type) {
    super.doCheckoutPayment(type);
  }

  @Override
  @OperationAccess(operation = Operation.UPDATE)
  public T doNotification(final MetaData metaData, final T type) {
    return super.doNotification(type);
  }

  @Override
  @OperationAccess(operation = Operation.UPDATE)
  public void doRefundTransaction(final MetaData metaData, final T type) {
    super.doRefundTransaction(type);
  }

  @Override
  @OperationAccess(operation = Operation.UPDATE)
  public void doTransactionDetails(final MetaData metaData, final T type) {
    super.doTransactionDetails(type);
  }
}
