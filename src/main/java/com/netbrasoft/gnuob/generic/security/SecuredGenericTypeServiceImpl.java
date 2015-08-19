package com.netbrasoft.gnuob.generic.security;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import com.netbrasoft.gnuob.generic.GenericTypeServiceImpl;
import com.netbrasoft.gnuob.generic.OrderBy;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.Parameter;
import com.netbrasoft.gnuob.generic.security.Rule.Operation;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@Stateless(name = "SecuredGenericTypeServiceImpl", mappedName = "SecuredGenericTypeServiceImpl")
@Interceptors(value = { AccessControl.class, AppSimonInterceptor.class })
public class SecuredGenericTypeServiceImpl<T> extends GenericTypeServiceImpl<T>implements SecuredGenericTypeService<T> {

   @Override
   @OperationAccess(operation = Operation.READ)
   public long count(MetaData metadata, T type, Parameter... param) {
      return super.count(type, param);
   }

   @Override
   @OperationAccess(operation = Operation.CREATE)
   public void create(MetaData metadata, T type) {
      // Method is used by access control to test if type can be created.
   }

   @Override
   @OperationAccess(operation = Operation.DELETE)
   public void delete(MetaData metadata, T type) {
      // Method is used by access control to test if type can be deleted.
   }

   @Override
   @OperationAccess(operation = Operation.READ)
   public T find(MetaData metadata, T type, long id) {
      return super.find(type, id);
   }

   @Override
   @OperationAccess(operation = Operation.READ)
   public List<T> find(MetaData metadata, T type, Paging paging, OrderBy orderBy, Parameter... param) {
      return super.find(type, paging, orderBy, param);
   }

   @Override
   @OperationAccess(operation = Operation.UPDATE)
   public void merge(MetaData metadata, T type) {
      super.merge(type);
   }

   @Override
   @OperationAccess(operation = Operation.CREATE)
   public void persist(MetaData metadata, T type) {
      super.persist(type);
   }

   @Override
   @OperationAccess(operation = Operation.READ)
   public void read(MetaData metadata, T type) {
      // Method is used by access control to test if type is readable.
   }

   @Override
   @OperationAccess(operation = Operation.READ)
   public T refresh(MetaData metadata, T type, long id) {
      return super.refresh(type, id);
   }

   @Override
   @OperationAccess(operation = Operation.DELETE)
   public void remove(MetaData metadata, T type) {
      super.remove(type);
   }

   @Override
   @OperationAccess(operation = Operation.UPDATE)
   public void update(MetaData metadata, T type) {
      // Method is used by access control to test if type is updatable.
   }
}
