package com.netbrasoft.gnuob.generic;

import java.util.List;

public interface GenericTypeService<T> {

	long count(T type, Parameter... param);

	void disableFilter(String filterName);

	void enableFilter(String filterName, Parameter... param);

	T find(T type, long id);

	List<T> find(T type, Paging paging, OrderBy orderBy, Parameter... param);

	void merge(T type);

	void persist(T type);

	void refresh(T type);

	void remove(T type);
}
