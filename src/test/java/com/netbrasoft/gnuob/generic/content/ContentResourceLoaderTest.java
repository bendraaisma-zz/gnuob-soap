package com.netbrasoft.gnuob.generic.content;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
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
      ve = new VelocityEngine("/home/bernard/Projects/gnuob/gnuob-soap/src/main/resources/velocity.properties");

      ve.addProperty("resource.loader", "content");
      ve.addProperty("content.resource.loader.class", "com.netbrasoft.gnuob.generic.content.ContentResourceLoader");
      ve.addProperty("content.resource.loader.description", "Velocity Content Resource Loader");
      ve.addProperty("content.resource.loader.cache", "false");
      ve.addProperty("content.resource.loader.modificationCheckInterval", "60");
      ve.addProperty("content.resource.loader.user", "guest");
      ve.addProperty("content.resource.loader.password", "guest");
      ve.addProperty("content.resource.loader.site", "localhost");
      ve.init();
   }

   @Test
   public void testEmailContentTemplate() {
      final Template template = ve.getTemplate("mytemplate.vm");

      final VelocityContext context = new VelocityContext();

      context.put("name", new String("Velocity"));
      final StringWriter sw = new StringWriter();

      template.merge(context, sw);
   }
}
