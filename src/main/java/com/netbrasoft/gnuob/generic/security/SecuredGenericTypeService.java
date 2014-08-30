package com.netbrasoft.gnuob.generic.security;

import java.util.List;

import com.netbrasoft.gnuob.generic.OrderBy;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.Parameter;

public interface SecuredGenericTypeService<T> {

	long count(MetaData metadata, T type, Parameter... param);

	T find(MetaData metadata, T type, long id);

	List<T> find(MetaData metadata, T type, Paging paging, OrderBy orderBy, Parameter... param);

	void merge(MetaData metadata, T type);

	void persist(MetaData metadata, T type);

	T refresh(MetaData metadata, T type, long id);

	void remove(MetaData metadata, T type);
}
