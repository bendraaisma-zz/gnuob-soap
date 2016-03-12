/*
 * Copyright 2016 Netbrasoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.netbrasoft.gnuob.generic.content;

import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.JAVA_MODULE;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ONE;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PASSWORD_PARAM_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SITE_PARAM_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.UNCHECKED_VALUE;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.USER_PARAM_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ZERO;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.OrderByEnum;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.security.ISecuredGenericTypeService;
import com.netbrasoft.gnuob.generic.security.MetaData;

public class ContentResourceLoader<T extends Content> extends ResourceLoader {

  private static final String COULDN_T_FIND_CONTENT_WITH_GIVEN_NAME = "Couldn't find content with given name: ";

  private MetaData credentials = MetaData.getInstance();
  private ISecuredGenericTypeService<T> securedGenericContentService;

  @SuppressWarnings(UNCHECKED_VALUE)
  public ContentResourceLoader() throws NamingException {
    this((ISecuredGenericTypeService<T>) InitialContext.doLookup(JAVA_MODULE + SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME));
  }

  ContentResourceLoader(final ISecuredGenericTypeService<T> securedGenericContentService) {
    this.securedGenericContentService = securedGenericContentService;
  }

  @Override
  public long getLastModified(final Resource resource) {
    return getContentLastModificationTimeByName(resource.getName());
  }

  private long getContentLastModificationTimeByName(final String contentName) {
    if (containContentByName(contentName)) {
      return getContentModificationTimeByName(contentName);
    }
    throw new GNUOpenBusinessServiceException(COULDN_T_FIND_CONTENT_WITH_GIVEN_NAME + contentName);
  }

  @SuppressWarnings(UNCHECKED_VALUE)
  private boolean containContentByName(final String contentName) {
    return securedGenericContentService.count(credentials, (T) Content.getInstance(contentName)) > ZERO;
  }

  @SuppressWarnings(UNCHECKED_VALUE)
  private long getContentModificationTimeByName(final String contentName) {
    return securedGenericContentService
        .find(credentials, (T) Content.getInstance(contentName), Paging.getInstance(ZERO, ONE), OrderByEnum.NONE)
        .iterator().next().getModification().getTime();
  }

  @Override
  public InputStream getResourceStream(final String sourceName) {
    return getContentResourceInputStreamByName(sourceName);
  }

  private InputStream getContentResourceInputStreamByName(final String sourceName) {
    if (containContentByName(sourceName)) {
      return getContentResourceBufferedInputStreamByName(sourceName);
    }
    throw new GNUOpenBusinessServiceException(COULDN_T_FIND_CONTENT_WITH_GIVEN_NAME + sourceName);
  }

  @SuppressWarnings(UNCHECKED_VALUE)
  private InputStream getContentResourceBufferedInputStreamByName(final String sourceName) {
    return new BufferedInputStream(new ByteArrayInputStream(securedGenericContentService
        .find(credentials, (T) Content.getInstance(sourceName), Paging.getInstance(ZERO, ONE), OrderByEnum.NONE)
        .iterator().next().getData()));
  }

  @Override
  public void init(final ExtendedProperties configuration) {
    credentials = MetaData.getInstance(configuration.getString(SITE_PARAM_NAME),
        configuration.getString(USER_PARAM_NAME), configuration.getString(PASSWORD_PARAM_NAME));
  }

  @Override
  public boolean isSourceModified(final Resource resource) {
    return resource.getLastModified() != getLastModified(resource);
  }
}
