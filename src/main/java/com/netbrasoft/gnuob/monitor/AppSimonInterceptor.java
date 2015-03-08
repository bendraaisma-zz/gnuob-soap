package com.netbrasoft.gnuob.monitor;

import javax.interceptor.InvocationContext;

public class AppSimonInterceptor extends org.javasimon.javaee.SimonInterceptor {

   @Override
   protected String getSimonName(InvocationContext context) {
      String className = context.getMethod().getDeclaringClass().getName();
      String methodName = context.getMethod().getName();
      return className + "." + methodName;
   }
}
