package com.netbrasoft.gnuob.generic;

import java.util.List;

import javax.persistence.LockModeType;

public interface GenericTypeService<T> {

  long count(T type, Parameter... param);

  void disableFilter(String filterName);

  void enableFilter(String filterName, Parameter... param);

  T find(T type, long id);

  T find(T type, long id, LockModeType lockModeType);

  List<T> find(T type, Paging paging, OrderBy orderBy, Parameter... param);

  void flush();

  void merge(T type);

  void persist(T type);

  T refresh(T type, long id);

  void remove(T type);
}
