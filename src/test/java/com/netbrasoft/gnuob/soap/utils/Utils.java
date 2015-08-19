package com.netbrasoft.gnuob.soap.utils;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;

public final class Utils {

   public static Archive<?> createDeployment() {

      final PomEquippedResolveStage pom = Maven.resolver().loadPomFromFile("pom.xml");

      final JavaArchive[] hibernateCore = pom.resolve("org.hibernate:hibernate-core").withoutTransitivity().as(JavaArchive.class);
      final JavaArchive[] pagSeguroApi = pom.resolve("pagseguro-api:pagseguro-api").withoutTransitivity().as(JavaArchive.class);
      final JavaArchive[] paypalService = pom.resolve("com.paypal:paypal-service").withoutTransitivity().as(JavaArchive.class);
      final JavaArchive[] javasimonJavaee = pom.resolve("org.javasimon:javasimon-javaee").withoutTransitivity().as(JavaArchive.class);
      final JavaArchive[] javasimonCore = pom.resolve("org.javasimon:javasimon-core").withoutTransitivity().as(JavaArchive.class);
      final JavaArchive[] pbkdf2 = pom.resolve("de.rtner:PBKDF2").withoutTransitivity().as(JavaArchive.class);

      return ShrinkWrap.create(WebArchive.class, "gnuob-test-application.war").addPackages(true, "com.netbrasoft.gnuob").addAsResource("META-INF/MANIFEST.MF", "META-INF/MANIFEST.MF").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsResource("velocity.properties", "velocity.properties").addAsResource("META-INF/persistence-test.xml", "META-INF/persistence.xml").addAsLibraries(hibernateCore, pagSeguroApi, paypalService, javasimonJavaee, javasimonCore, pbkdf2);
   }
}