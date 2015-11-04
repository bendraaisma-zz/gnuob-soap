package com.netbrasoft.gnuob.generic.category;

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
import com.netbrasoft.gnuob.generic.GenericTypeWebService;
import com.netbrasoft.gnuob.generic.OrderBy;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.Parameter;
import com.netbrasoft.gnuob.generic.content.Content;
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeService;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeServiceImpl;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = CategoryWebServiceImpl.CATEGORY_WEB_SERVICE_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class})
public class CategoryWebServiceImpl<T extends Category> implements GenericTypeWebService<T> {

  protected static final String CATEGORY_WEB_SERVICE_IMPL_NAME = "CategoryWebServiceImpl";

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<T> securedGenericCategoryService;

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<Content> securedGenericContentService;

  @Override
  @WebMethod(operationName = "countCategory")
  public long count(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "category") final T type) {
    try {
      readContents(metaData, type.getContents());
      readSubCategoryContents(metaData, type.getSubCategories());
      if (!type.getSubCategories().isEmpty()) {
        final List<Example> examples = getSubCategoryExamples(type.getSubCategories());
        final Parameter parameter = new Parameter("subCategories", Restrictions.or(examples.toArray(new Example[examples.size()])));
        return securedGenericCategoryService.count(metaData, type, parameter);
      }
      return securedGenericCategoryService.count(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void createUpdateContents(final MetaData metaData, final Set<Content> contents) {
    if (contents != null && !contents.isEmpty()) {
      for (final Content content : contents) {
        if (content.getId() == 0) {
          securedGenericContentService.create(metaData, content);
        } else {
          securedGenericContentService.update(metaData, content);
        }
      }
    }
  }

  private void createUpdateSubCategoryContents(final MetaData metaData, final Set<SubCategory> subCategories) {
    if (subCategories != null && !subCategories.isEmpty()) {
      for (final SubCategory subCategory : subCategories) {
        createUpdateContents(metaData, subCategory.getContents());
        createUpdateSubCategoryContents(metaData, subCategory.getSubCategories());
      }
    }
  }

  private void deleteContents(final MetaData metaData, final Set<Content> contents) {
    if (contents != null && !contents.isEmpty()) {
      for (final Content content : contents) {
        if (content.getId() > 0) {
          securedGenericContentService.delete(metaData, content);
        }
      }
    }
  }

  private void deleteSubCategoryContents(final MetaData metaData, final Set<SubCategory> subCategories) {
    if (subCategories != null && !subCategories.isEmpty()) {
      for (final SubCategory subCategory : subCategories) {
        deleteContents(metaData, subCategory.getContents());
        deleteSubCategoryContents(metaData, subCategory.getSubCategories());
      }
    }
  }

  @Override
  @WebMethod(operationName = "findCategoryById")
  public T find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "category") final T type) {
    try {
      readContents(metaData, type.getContents());
      readSubCategoryContents(metaData, type.getSubCategories());
      return securedGenericCategoryService.find(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findCategory")
  public List<T> find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "category") final T type,
      @WebParam(name = "paging") final Paging paging, @WebParam(name = "orderBy") final OrderBy orderBy) {
    try {
      readContents(metaData, type.getContents());
      readSubCategoryContents(metaData, type.getSubCategories());
      if (!type.getSubCategories().isEmpty()) {
        final List<Example> examples = getSubCategoryExamples(type.getSubCategories());
        final Parameter parameter = new Parameter("subCategories", Restrictions.or(examples.toArray(new Example[examples.size()])));
        return securedGenericCategoryService.find(metaData, type, paging, orderBy, parameter);
      }
      return securedGenericCategoryService.find(metaData, type, paging, orderBy);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private List<Example> getSubCategoryExamples(final Set<SubCategory> subCategories) {
    final List<Example> examples = new ArrayList<Example>();
    if (subCategories != null) {
      for (final SubCategory subCategory : subCategories) {
        examples.add(Example.create(subCategory));
      }
    }
    return examples;
  }

  @Override
  @WebMethod(operationName = "mergeCategory")
  public T merge(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "category") final T type) {
    try {
      createUpdateContents(metaData, type.getContents());
      createUpdateSubCategoryContents(metaData, type.getSubCategories());
      return securedGenericCategoryService.merge(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "persistCategory")
  public T persist(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "category") final T type) {
    try {
      createUpdateContents(metaData, type.getContents());
      createUpdateSubCategoryContents(metaData, type.getSubCategories());
      if (type.isDetached()) {
        return securedGenericCategoryService.merge(metaData, type);
      }
      securedGenericCategoryService.persist(metaData, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void readContents(final MetaData metaData, final Set<Content> contents) {
    if (contents != null && !contents.isEmpty()) {
      for (final Content content : contents) {
        if (content.getId() > 0) {
          securedGenericContentService.read(metaData, content);
        }
      }
    }
  }

  private void readSubCategoryContents(final MetaData metaData, final Set<SubCategory> subCategories) {
    if (subCategories != null && !subCategories.isEmpty()) {
      for (final SubCategory subCategory : subCategories) {
        readContents(metaData, subCategory.getContents());
        readSubCategoryContents(metaData, subCategory.getSubCategories());
      }
    }
  }

  @Override
  @WebMethod(operationName = "refreshCategory")
  public T refresh(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "category") final T type) {
    try {
      readContents(metaData, type.getContents());
      readSubCategoryContents(metaData, type.getSubCategories());
      return securedGenericCategoryService.refresh(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "removeCategory")
  public void remove(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "category") final T type) {
    try {
      deleteContents(metaData, type.getContents());
      deleteSubCategoryContents(metaData, type.getSubCategories());
      securedGenericCategoryService.remove(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
