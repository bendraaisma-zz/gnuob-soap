package com.netbrasoft.gnuob.generic;

import javax.persistence.LockModeType;

import org.hibernate.Session;

public interface GenericTypeDao<T> {

   boolean contains(T type);

   void disableFilter(String filterName);

   void enableFilter(String filterName, Parameter... param);

   T find(T type, long id, LockModeType lockModeType);

   void flush();

   Session getDelegate();

   LockModeType getLockModeTypeOfType(T type);

   boolean isOpen();

   void lock(T type, LockModeType lockModeType);

   void merge(T type);

   void persist(T type);

   T refresh(T type, long id, LockModeType lockModeType);

   void remove(T type);
}
