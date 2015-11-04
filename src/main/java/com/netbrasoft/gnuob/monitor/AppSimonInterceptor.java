package com.netbrasoft.gnuob.monitor;

import javax.interceptor.InvocationContext;

public class AppSimonInterceptor extends org.javasimon.javaee.SimonInterceptor {

  @Override
  protected String getSimonName(final InvocationContext context) {
    final String className = context.getMethod().getDeclaringClass().getName();
    final String methodName = context.getMethod().getName();
    return className + "." + methodName;
  }
}
