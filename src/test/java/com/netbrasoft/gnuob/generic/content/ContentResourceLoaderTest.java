package com.netbrasoft.gnuob.generic.content;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.netbrasoft.gnuob.soap.utils.Utils;

@RunWith(Arquillian.class)
public class ContentResourceLoaderTest {

   @Deployment(testable = true)
   public static Archive<?> createDeployment() {
      return Utils.createDeployment();
   }

   VelocityEngine ve;

   @Before
   public void testBefore() {
      final VelocityEngine ve = new VelocityEngine();
      ve.setProperty("content.resource.user", "guest");
      ve.setProperty("content.resource.password", "guest");
      ve.setProperty("content.resource.site", "localhost");
      ve.init("/home/bernard/Projects/gnuob/gnuob-soap/src/main/resources/velocity.properties");
   }

   @Test
   public void testEmailContentTemplate() {
      final Template template = Velocity.getTemplate("mytemplate.vm");

      final VelocityContext context = new VelocityContext();

      context.put("name", new String("Velocity"));
      final StringWriter sw = new StringWriter();

      template.merge(context, sw);
   }
}
