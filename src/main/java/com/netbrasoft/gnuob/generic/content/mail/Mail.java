package com.netbrasoft.gnuob.generic.content.mail;

public enum Mail {

   WELCOME_NEW_CUSTOMER_MAIL(Subject.CONFIRMATION_NEW_ORDER_SUBJECT, Body.CONFIRMATION_NEW_ORDER_BODY), CONFIRMATION_NEW_ORDER_MAIL(Subject.CONFIRMATION_NEW_ORDER_SUBJECT, Body.CONFIRMATION_NEW_ORDER_BODY), NO_MAIL(Subject.NONE, Body.NONE);

   public enum Body {
      CONFIRMATION_NEW_CUSTOMER_BODY("confirmation_new_customer_body.vm"), CONFIRMATION_NEW_ORDER_BODY("confirmation_new_order_body.vm"), NONE("");

      private String template;

      private Body(String template) {
         setTemplate(template);
      }

      public String getTemplate() {
         return template;
      }

      public void setTemplate(String template) {
         this.template = template;
      }
   }

   public enum Subject {
      CONFIRMATION_NEW_CUSTOMER_SUBJECT("confirmation_new_customer_subject.vm"), CONFIRMATION_NEW_ORDER_SUBJECT("confirmation_new_order_subject.vm"), NONE("");

      private String template;

      private Subject(String template) {
         setTemplate(template);
      }

      public String getTemplate() {
         return template;
      }

      public void setTemplate(String template) {
         this.template = template;
      }
   }

   private Subject subject;

   private Body body;

   private Mail(Subject subject, Body body) {
      setSubject(subject);
      setBody(body);
   }

   public Body getBody() {
      return body;
   }

   public Subject getSubject() {
      return subject;
   }

   public void setBody(Body body) {
      this.body = body;
   }

   public void setSubject(Subject subject) {
      this.subject = subject;
   }
}
