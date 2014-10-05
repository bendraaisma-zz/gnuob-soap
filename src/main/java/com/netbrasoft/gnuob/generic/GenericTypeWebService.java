package com.netbrasoft.gnuob.generic;

import java.util.List;

import com.netbrasoft.gnuob.generic.security.MetaData;

public interface GenericTypeWebService<T> {

    long count(MetaData metaData, T type);

    T find(MetaData metaData, T type);

    List<T> find(MetaData metaData, T type, Paging paging, OrderBy orderBy);

    T merge(MetaData metaData, T type);

    T persist(MetaData metaData, T type);

    T refresh(MetaData metaData, T type);

    void remove(MetaData metaData, T type);
}
