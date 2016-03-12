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

package com.netbrasoft.gnuob.generic.category;

import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CATEGORY_PARAM_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CATEGORY_WEB_SERVICE_IMPL_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.COUNT_CATEGORY_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.FIND_CATEGORY_BY_ID_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.FIND_CATEGORY_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GNUOB_WEB_SERVICE_TARGET_NAMESPACE;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.MERGE_CATEGORY_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.META_DATA_PARAM_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_BY_PARAM_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAGING_PARAM_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PERSIST_CATEGORY_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.REFRESH_CATEGORY_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.REMOVE_CATEGORY_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SUB_CATEGORIES_PROPERTY_NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.IGenericTypeWebService;
import com.netbrasoft.gnuob.generic.OrderByEnum;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.Parameter;
import com.netbrasoft.gnuob.generic.content.Content;
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.ISecuredGenericTypeService;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = GNUOB_WEB_SERVICE_TARGET_NAMESPACE)
@Stateless(name = CATEGORY_WEB_SERVICE_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class})
public class CategoryWebServiceImpl<T extends Category> implements IGenericTypeWebService<T> {

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<T> securedGenericCategoryService;

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<Content> securedGenericContentService;

  public CategoryWebServiceImpl() {}

  CategoryWebServiceImpl(final ISecuredGenericTypeService<T> securedGenericCategoryService,
      final ISecuredGenericTypeService<Content> securedGenericContentService) {
    this.securedGenericCategoryService = securedGenericCategoryService;
    this.securedGenericContentService = securedGenericContentService;
  }

  @Override
  @WebMethod(operationName = COUNT_CATEGORY_OPERATION_NAME)
  public long count(@WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = CATEGORY_PARAM_NAME) final T type) {
    try {
      readContents(credentials, type);
      return countByCategory(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void readContents(final MetaData credentials, final T type) {
    readContents(credentials, type.getContents());
    readSubCategoryContents(credentials, type.getSubCategories());
  }

  private void readContents(final MetaData credentials, final Set<Content> contents) {
    if (contents != null) {
      contents.stream().filter(e -> e.isDetached()).forEach(e -> securedGenericContentService.read(credentials, e));
    }
  }

  private void readSubCategoryContents(final MetaData credentials, final Set<SubCategory> subCategories) {
    if (subCategories != null) {
      subCategories.stream().forEach(e -> {
        readContents(credentials, e.getContents());
        readSubCategoryContents(credentials, e.getSubCategories());
      });
    }
  }

  private long countByCategory(final MetaData credentials, final T type) {
    if (type.getSubCategories() != null && !type.getSubCategories().isEmpty()) {
      return countByCategoryAndSubCategories(credentials, type);
    }
    return securedGenericCategoryService.count(credentials, type);
  }

  private long countByCategoryAndSubCategories(final MetaData credentials, final T type) {
    final List<Example> examples = getSubCategoryExamples(type.getSubCategories());
    return securedGenericCategoryService.count(credentials, type,
        new Parameter(SUB_CATEGORIES_PROPERTY_NAME, Restrictions.or(examples.toArray(new Example[examples.size()]))));
  }

  private List<Example> getSubCategoryExamples(final Set<SubCategory> subCategories) {
    final List<Example> examples = new ArrayList<Example>();
    subCategories.stream().forEach(e -> examples.add(Example.create(e)));
    return examples;
  }

  @Override
  @WebMethod(operationName = FIND_CATEGORY_OPERATION_NAME)
  public List<T> find(@WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = CATEGORY_PARAM_NAME) final T type, @WebParam(name = PAGING_PARAM_NAME) final Paging paging,
      @WebParam(name = ORDER_BY_PARAM_NAME) final OrderByEnum orderingByProperty) {
    try {
      readContents(credentials, type);
      return findByCategory(credentials, type, paging, orderingByProperty);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private List<T> findByCategory(final MetaData credentials, final T type, final Paging paging,
      final OrderByEnum orderingByProperty) {
    if (type.getSubCategories() != null && !type.getSubCategories().isEmpty()) {
      return findByCategoryAndSubCategories(credentials, type, paging, orderingByProperty);
    }
    return securedGenericCategoryService.find(credentials, type, paging, orderingByProperty);
  }

  private List<T> findByCategoryAndSubCategories(final MetaData credentials, final T type, final Paging paging,
      final OrderByEnum orderingByProperty) {
    final List<Example> examples = getSubCategoryExamples(type.getSubCategories());
    return securedGenericCategoryService.find(credentials, type, paging, orderingByProperty,
        new Parameter(SUB_CATEGORIES_PROPERTY_NAME, Restrictions.or(examples.toArray(new Example[examples.size()]))));
  }

  @Override
  @WebMethod(operationName = FIND_CATEGORY_BY_ID_OPERATION_NAME)
  public T find(@WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = CATEGORY_PARAM_NAME) final T type) {
    try {
      readContents(credentials, type);
      return securedGenericCategoryService.find(credentials, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = MERGE_CATEGORY_OPERATION_NAME)
  public T merge(@WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = CATEGORY_PARAM_NAME) final T type) {
    try {
      createUpdateContents(credentials, type);
      return securedGenericCategoryService.merge(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void createUpdateContents(final MetaData credentials, final T type) {
    createUpdateContents(credentials, type.getContents());
    createUpdateSubCategoryContents(credentials, type.getSubCategories());
  }

  private void createUpdateContents(final MetaData credentials, final Set<Content> contents) {
    if (contents != null) {
      contents.stream().filter(e -> !e.isDetached()).forEach(e -> securedGenericContentService.create(credentials, e));
      contents.stream().filter(e -> e.isDetached()).forEach(e -> securedGenericContentService.update(credentials, e));
    }
  }

  private void createUpdateSubCategoryContents(final MetaData credentials, final Set<SubCategory> subCategories) {
    if (subCategories != null) {
      subCategories.stream().forEach(e -> {
        createUpdateContents(credentials, e.getContents());
        createUpdateSubCategoryContents(credentials, e.getSubCategories());
      });
    }
  }

  @Override
  @WebMethod(operationName = PERSIST_CATEGORY_OPERATION_NAME)
  public T persist(@WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = CATEGORY_PARAM_NAME) final T type) {
    try {
      createUpdateContents(credentials, type);
      return mergePersistCategory(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private T mergePersistCategory(final MetaData credentials, final T type) {
    if (type.isDetached()) {
      return securedGenericCategoryService.merge(credentials, type);
    }
    securedGenericCategoryService.persist(credentials, type);
    return type;
  }

  @Override
  @WebMethod(operationName = REFRESH_CATEGORY_OPERATION_NAME)
  public T refresh(@WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = CATEGORY_PARAM_NAME) final T type) {
    try {
      readContents(credentials, type);
      return securedGenericCategoryService.refresh(credentials, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = REMOVE_CATEGORY_OPERATION_NAME)
  public void remove(@WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = CATEGORY_PARAM_NAME) final T type) {
    try {
      deleteContents(credentials, type);
      securedGenericCategoryService.remove(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void deleteContents(final MetaData credentials, final T type) {
    deleteContents(credentials, type.getContents());
    deleteSubCategoryContents(credentials, type.getSubCategories());
  }

  private void deleteContents(final MetaData credentials, final Set<Content> contents) {
    if (contents != null) {
      contents.stream().filter(e -> e.isDetached()).forEach(e -> securedGenericContentService.delete(credentials, e));
    }
  }

  private void deleteSubCategoryContents(final MetaData credentials, final Set<SubCategory> subCategories) {
    if (subCategories != null) {
      subCategories.stream().forEach(e -> {
        deleteContents(credentials, e.getContents());
        deleteSubCategoryContents(credentials, e.getSubCategories());
      });
    }
  }
}
