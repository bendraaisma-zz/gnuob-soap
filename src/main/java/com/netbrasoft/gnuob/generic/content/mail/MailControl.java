package com.netbrasoft.gnuob.generic.content.mail;

import java.io.StringWriter;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.LockModeType;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.javasimon.SimonManager;
import org.javasimon.Split;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.GenericTypeService;
import com.netbrasoft.gnuob.generic.content.contexts.ContextElement;
import com.netbrasoft.gnuob.generic.content.contexts.ContextVisitor;
import com.netbrasoft.gnuob.generic.content.mail.Mail.Body;
import com.netbrasoft.gnuob.generic.content.mail.Mail.Subject;
import com.netbrasoft.gnuob.generic.security.Access;
import com.netbrasoft.gnuob.generic.security.MetaData;

public class MailControl<A extends Access> {

   @EJB(beanName = "GenericTypeServiceImpl")
   private GenericTypeService<A> accessTypeService;

   @EJB(beanName = "ContextVisitorImpl")
   private ContextVisitor contextVisitor;

   @Resource(mappedName = "java:jboss/mail/Default")
   private Session session;

   private MetaData getMetaData(InvocationContext ctx) {
      final MetaData metaData = new MetaData();

      for (final Object parameter : ctx.getParameters()) {
         if (parameter instanceof MetaData) {
            return (MetaData) parameter;
         }
      }
      return metaData;
   }

   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception {

      final MailAction mailAcction = ctx.getMethod().getAnnotation(MailAction.class);
      final Object object = ctx.proceed();

      if (mailAcction != null) {
         if (!mailAcction.operation().equals(Mail.NO_MAIL)) {
            processMailAction(ctx, getMetaData(ctx), mailAcction.operation().getSubject(), mailAcction.operation().getBody());
         }
         return object;
      } else {
         throw new GNUOpenBusinessServiceException("Mail action is not set on this method, no mail can be sent.");
      }
   }

   private void processMailAction(InvocationContext ctx, MetaData metadata, Subject subject, Body body) throws Exception {
      final Split split = SimonManager.getStopwatch("com.netbrasoft.gnuob.generic.content.mail.MailControl.sendMailAction").start();

      for (final Object parameter : ctx.getParameters()) {

         if (parameter instanceof Access) {

            final long accessId = ((Access) parameter).getId();

            @SuppressWarnings("unchecked")
            final Access access = accessTypeService.find((A) parameter, accessId, LockModeType.NONE);

            if (access != null) {

               final VelocityEngine ve = new VelocityEngine();
               final StringWriter subjectWriter = new StringWriter();
               final StringWriter bodyWriter = new StringWriter();

               ve.addProperty("resource.loader", "content");
               ve.addProperty("content.resource.loader.class", "com.netbrasoft.gnuob.generic.content.ContentResourceLoader");
               ve.addProperty("content.resource.loader.description", "Velocity Content Resource Loader");
               ve.addProperty("content.resource.loader.cache", "false");
               ve.addProperty("content.resource.loader.modificationCheckInterval", "60");
               ve.addProperty("content.resource.loader.user", metadata.getUser());
               ve.addProperty("content.resource.loader.password", metadata.getPassword());
               ve.addProperty("content.resource.loader.site", metadata.getSite());
               ve.init();

               try {
                  final Template subjectTemplate = ve.getTemplate(subject.getTemplate());
                  final Template bodyTemplate = ve.getTemplate(body.getTemplate());
                  final Message message = new MimeMessage(session);
                  final Address from = new InternetAddress(System.getProperty("gnuob." + metadata.getSite() + ".email"));
                  // TODO BD: get email from customer object using a email adress template.
                  final Address[] to = new InternetAddress[] {new InternetAddress("bendraaisma@gmail.com") };

                  subjectTemplate.merge(((ContextElement) access).accept(contextVisitor), subjectWriter);
                  bodyTemplate.merge(((ContextElement) access).accept(contextVisitor), bodyWriter);

                  message.setFrom(from);
                  message.setRecipients(Message.RecipientType.TO, to);
                  message.setSentDate(new Date());
                  message.setSubject(subjectWriter.toString());
                  message.setContent(bodyWriter.toString(), "text/html;charset=UTF-8");
                  Transport.send(message);
               } catch (final MessagingException e) {
                  split.stop();
                  throw new GNUOpenBusinessServiceException("Failed to send mail, no mail can be sent.", e);
               }
            } else {
               split.stop();
               throw new GNUOpenBusinessServiceException("Enity object is not found in database, no mail wil be sent.");
            }
         }
      }
   }
}
