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
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "CategoryWebServiceImpl")
@Interceptors(value = {AppSimonInterceptor.class})
public class CategoryWebServiceImpl<C extends Category> implements GenericTypeWebService<C> {

  @EJB(beanName = "SecuredGenericTypeServiceImpl")
  private SecuredGenericTypeService<C> securedGenericCategoryService;

  @EJB(beanName = "SecuredGenericTypeServiceImpl")
  private SecuredGenericTypeService<Content> securedGenericContentService;

  @Override
  @WebMethod(operationName = "countCategory")
  public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "category") C type) {
    try {
      readContents(metadata, type.getContents());
      readSubCategoryContents(metadata, type.getSubCategories());
      if (!type.getSubCategories().isEmpty()) {
        final List<Example> examples = getSubCategoryExamples(type.getSubCategories());
        final Parameter parameter = new Parameter("subCategories", Restrictions.or(examples.toArray(new Example[examples.size()])));
        return securedGenericCategoryService.count(metadata, type, parameter);
      }
      return securedGenericCategoryService.count(metadata, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void createUpdateContents(MetaData metadata, Set<Content> contents) {
    if (contents != null && !contents.isEmpty()) {
      for (final Content content : contents) {
        if (content.getId() == 0) {
          securedGenericContentService.create(metadata, content);
        } else {
          securedGenericContentService.update(metadata, content);
        }
      }
    }
  }

  private void createUpdateSubCategoryContents(MetaData metadata, Set<SubCategory> subCategories) {
    if (subCategories != null && !subCategories.isEmpty()) {
      for (final SubCategory subCategory : subCategories) {
        createUpdateContents(metadata, subCategory.getContents());
        createUpdateSubCategoryContents(metadata, subCategory.getSubCategories());
      }
    }
  }

  private void deleteContents(MetaData metadata, Set<Content> contents) {
    if (contents != null && !contents.isEmpty()) {
      for (final Content content : contents) {
        if (content.getId() > 0) {
          securedGenericContentService.delete(metadata, content);
        }
      }
    }
  }

  private void deleteSubCategoryContents(MetaData metadata, Set<SubCategory> subCategories) {
    if (subCategories != null && !subCategories.isEmpty()) {
      for (final SubCategory subCategory : subCategories) {
        deleteContents(metadata, subCategory.getContents());
        deleteSubCategoryContents(metadata, subCategory.getSubCategories());
      }
    }
  }

  @Override
  @WebMethod(operationName = "findCategoryById")
  public C find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "category") C type) {
    try {
      readContents(metadata, type.getContents());
      readSubCategoryContents(metadata, type.getSubCategories());
      return securedGenericCategoryService.find(metadata, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findCategory")
  public List<C> find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "category") C type, @WebParam(name = "paging") Paging paging,
      @WebParam(name = "orderBy") OrderBy orderBy) {
    try {
      readContents(metadata, type.getContents());
      readSubCategoryContents(metadata, type.getSubCategories());
      if (!type.getSubCategories().isEmpty()) {
        final List<Example> examples = getSubCategoryExamples(type.getSubCategories());
        final Parameter parameter = new Parameter("subCategories", Restrictions.or(examples.toArray(new Example[examples.size()])));
        return securedGenericCategoryService.find(metadata, type, paging, orderBy, parameter);
      }
      return securedGenericCategoryService.find(metadata, type, paging, orderBy);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private List<Example> getSubCategoryExamples(Set<SubCategory> subCategories) {
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
  public C merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "category") C type) {
    try {
      createUpdateContents(metadata, type.getContents());
      createUpdateSubCategoryContents(metadata, type.getSubCategories());
      return securedGenericCategoryService.merge(metadata, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "persistCategory")
  public C persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "category") C type) {
    try {
      createUpdateContents(metadata, type.getContents());
      createUpdateSubCategoryContents(metadata, type.getSubCategories());
      if (type.isDetached()) {
        return securedGenericCategoryService.merge(metadata, type);
      }
      securedGenericCategoryService.persist(metadata, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void readContents(MetaData metadata, Set<Content> contents) {
    if (contents != null && !contents.isEmpty()) {
      for (final Content content : contents) {
        if (content.getId() > 0) {
          securedGenericContentService.read(metadata, content);
        }
      }
    }
  }

  private void readSubCategoryContents(MetaData metadata, Set<SubCategory> subCategories) {
    if (subCategories != null && !subCategories.isEmpty()) {
      for (final SubCategory subCategory : subCategories) {
        readContents(metadata, subCategory.getContents());
        readSubCategoryContents(metadata, subCategory.getSubCategories());
      }
    }
  }

  @Override
  @WebMethod(operationName = "refreshCategory")
  public C refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "category") C type) {
    try {
      readContents(metadata, type.getContents());
      readSubCategoryContents(metadata, type.getSubCategories());
      return securedGenericCategoryService.refresh(metadata, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "removeCategory")
  public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "category") C type) {
    try {
      deleteContents(metadata, type.getContents());
      deleteSubCategoryContents(metadata, type.getSubCategories());
      securedGenericCategoryService.remove(metadata, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
