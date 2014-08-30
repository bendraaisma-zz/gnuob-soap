package com.netbrasoft.gnuob.generic.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
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
import com.netbrasoft.gnuob.generic.category.SubCategory;
import com.netbrasoft.gnuob.generic.content.Content;
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeService;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "ProductWebServiceImpl")
public class ProductWebServiceImpl<P extends Product> implements GenericTypeWebService<P> {

	@EJB(beanName = "SecuredGenericTypeServiceImpl")
	private SecuredGenericTypeService<P> securedGenericProductService;

	@EJB(beanName = "SecuredGenericTypeServiceImpl")
	private SecuredGenericTypeService<Content> securedGenericContentService;

	@Override
	@WebMethod(operationName = "countProduct")
	public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "product") P type) throws GNUOpenBusinessServiceException {
		try {
			if (!type.getSubCategories().isEmpty()) {
				List<Example> examples = getSubCategoryExamples(type.getSubCategories());
				Parameter parameter = Parameter.getInstance("subCategories", Restrictions.or(examples.toArray(new Example[examples.size()])));
				return securedGenericProductService.count(metadata, type, parameter);
			}
			return securedGenericProductService.count(metadata, type);
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "findProductById")
	public P find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "product") P type) throws GNUOpenBusinessServiceException {
		try {
			return securedGenericProductService.find(metadata, type, type.getId());
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "findProduct")
	public List<P> find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "product") P type, @WebParam(name = "paging") Paging paging, @WebParam(name = "orderBy") OrderBy orderBy) throws GNUOpenBusinessServiceException {
		try {
			List<Example> examples = getSubCategoryExamples(type.getSubCategories());
			Parameter parameter = Parameter.getInstance("subCategories", Restrictions.or(examples.toArray(new Example[examples.size()])));
			return securedGenericProductService.find(metadata, type, paging, orderBy, parameter);
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	private List<Example> getSubCategoryExamples(Set<SubCategory> subCategories) {

		List<Example> examples = new ArrayList<Example>();

		if (subCategories != null) {
			for (SubCategory subCategory : subCategories) {
				examples.add(Example.create(subCategory));
			}
		}

		return examples;
	}

	@Override
	@WebMethod(operationName = "mergeProduct")
	public P merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "product") P type) throws GNUOpenBusinessServiceException {
		try {
			persistMergeContents(metadata, type.getContents());
			persistMergeSubCategoryContents(metadata, type.getSubCategories());
			securedGenericProductService.merge(metadata, type);
			return type;
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "persistProduct")
	public P persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "product") P type) throws GNUOpenBusinessServiceException {
		try {
			persistMergeContents(metadata, type.getContents());
			persistMergeSubCategoryContents(metadata, type.getSubCategories());
			securedGenericProductService.persist(metadata, type);
			return type;
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	private void persistMergeContents(MetaData metadata, Set<Content> contents) {
		if (contents != null && !contents.isEmpty()) {
			for (Content content : contents) {
				if (content.getId() == 0) {
					securedGenericContentService.persist(metadata, content);
				}
			}
		}
	}

	private void persistMergeSubCategoryContents(MetaData metadata, Set<SubCategory> subCategories) {
		if (subCategories != null && !subCategories.isEmpty()) {
			for (SubCategory subCategory : subCategories) {
				persistMergeContents(metadata, subCategory.getContents());
				persistMergeSubCategoryContents(metadata, subCategory.getSubCategories());
			}
		}
	}

	@Override
	@WebMethod(operationName = "refreshProduct")
	public P refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "product") P type) throws GNUOpenBusinessServiceException {
		try {
			return securedGenericProductService.refresh(metadata, type, type.getId());
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

	@Override
	@WebMethod(operationName = "removeProduct")
	public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "product") P type) throws GNUOpenBusinessServiceException {
		try {
			securedGenericProductService.remove(metadata, type);
		} catch (Exception e) {
			throw new GNUOpenBusinessServiceException(e.getMessage(), e);
		}
	}

}
