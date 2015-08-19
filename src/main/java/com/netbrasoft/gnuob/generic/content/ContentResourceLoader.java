package com.netbrasoft.gnuob.generic.content;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;

import javax.naming.InitialContext;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import com.netbrasoft.gnuob.generic.OrderBy;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeService;

public class ContentResourceLoader<C extends Content> extends ResourceLoader {

   private SecuredGenericTypeService<C> securedGenericContentService;

   private final MetaData metaData = new MetaData();

   @SuppressWarnings("unchecked")
   public ContentResourceLoader() {
      try {
         securedGenericContentService = (SecuredGenericTypeService<C>) InitialContext.doLookup("java:module/SecuredGenericTypeServiceImpl");
      } catch (final Exception e) {
         throw new RuntimeException("ContentResourceLoader not properly initialized. No SecuredGenericTypeService was identified.", e);
      }
   }

   @Override
   @SuppressWarnings("unchecked")
   public long getLastModified(Resource resource) {
      final Content content = new Content();
      content.setActive(true);
      content.setName(resource.getName());

      final Iterator<C> iterator = securedGenericContentService.find(metaData, (C) content, new Paging(0, 1), OrderBy.NONE).iterator();

      if (iterator.hasNext()) {
         return iterator.next().getModification().getTime();
      } else {
         throw new ResourceNotFoundException("ContentResourceLoader: could not find resource '" + resource.getName() + "'");
      }
   }

   @Override
   @SuppressWarnings("unchecked")
   public InputStream getResourceStream(String source) throws ResourceNotFoundException {
      final Content content = new Content();
      content.setActive(true);
      content.setName(source);

      final Iterator<C> iterator = securedGenericContentService.find(metaData, (C) content, new Paging(0, 1), OrderBy.NONE).iterator();

      if (iterator.hasNext()) {
         return new BufferedInputStream(new ByteArrayInputStream(iterator.next().getData()));
      } else {
         throw new ResourceNotFoundException("ContentResourceLoader: could not find resource '" + source + "'");
      }
   }

   @Override
   public void init(ExtendedProperties configuration) {
      metaData.setUser(configuration.getString("user"));
      metaData.setPassword(configuration.getString("password"));
      metaData.setSite(configuration.getString("site"));
   }

   @Override
   public boolean isSourceModified(Resource resource) {
      return resource.getLastModified() != getLastModified(resource);
   }
}
