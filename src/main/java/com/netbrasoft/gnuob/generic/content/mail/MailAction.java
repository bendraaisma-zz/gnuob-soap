package com.netbrasoft.gnuob.generic.content.mail;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({METHOD})
@Retention(RUNTIME)
public @interface MailAction {

  public Mail operation() default Mail.NO_MAIL;
}
