package com.netbrasoft.gnuob.generic;

import java.util.List;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.security.MetaData;

public interface GenericTypeWebService<T> {

	long count(MetaData metaData, T type) throws GNUOpenBusinessServiceException;

	List<T> find(MetaData metaData, T type, Paging paging, OrderBy orderBy) throws GNUOpenBusinessServiceException;

	T find(MetaData metaData, T type) throws GNUOpenBusinessServiceException;

	T merge(MetaData metaData, T type) throws GNUOpenBusinessServiceException;

	T persist(MetaData metaData, T type) throws GNUOpenBusinessServiceException;

	T refresh(MetaData metaData, T type) throws GNUOpenBusinessServiceException;

	void remove(MetaData metaData, T type) throws GNUOpenBusinessServiceException;
}
