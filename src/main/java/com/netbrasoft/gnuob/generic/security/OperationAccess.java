package com.netbrasoft.gnuob.generic.security;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ METHOD })
@Retention(RUNTIME)
public @interface OperationAccess {

   public Rule.Operation operation() default Rule.Operation.READ;
}
