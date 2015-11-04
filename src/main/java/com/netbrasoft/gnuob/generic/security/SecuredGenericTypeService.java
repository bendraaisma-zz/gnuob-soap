package com.netbrasoft.gnuob.generic.security;

import java.util.List;

import com.netbrasoft.gnuob.generic.OrderBy;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.Parameter;

public interface SecuredGenericTypeService<T> {

  long count(MetaData metaData, T type, Parameter... param);

  void create(MetaData metaData, T type);

  void delete(MetaData metaData, T type);

  T find(MetaData metaData, T type, long id);

  List<T> find(MetaData metaData, T type, Paging paging, OrderBy orderBy, Parameter... param);

  void flush();

  T merge(MetaData metaData, T type);

  void persist(MetaData metaData, T type);

  void read(MetaData metaData, T type);

  T refresh(MetaData metaData, T type, long id);

  void remove(MetaData metaData, T type);

  void update(MetaData metaData, T type);
}
