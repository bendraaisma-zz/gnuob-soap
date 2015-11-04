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

@Stateless(name = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME, mappedName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
@Interceptors(value = {AccessControl.class, AppSimonInterceptor.class})
public class SecuredGenericTypeServiceImpl<T> extends GenericTypeServiceImpl<T> implements SecuredGenericTypeService<T> {

  public static final String SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME = "SecuredGenericTypeServiceImpl";

  @Override
  @OperationAccess(operation = Operation.READ)
  public long count(final MetaData metaData, final T type, final Parameter... param) {
    return super.count(type, param);
  }

  @Override
  @OperationAccess(operation = Operation.CREATE)
  public void create(final MetaData metaData, final T type) {
    // Method is used by access control to test if type can be created.
  }

  @Override
  @OperationAccess(operation = Operation.DELETE)
  public void delete(final MetaData metaData, final T type) {
    // Method is used by access control to test if type can be deleted.
  }

  @Override
  @OperationAccess(operation = Operation.READ)
  public T find(final MetaData metaData, final T type, final long id) {
    return super.find(type, id);
  }

  @Override
  @OperationAccess(operation = Operation.READ)
  public List<T> find(final MetaData metaData, final T type, final Paging paging, final OrderBy orderBy, final Parameter... param) {
    return super.find(type, paging, orderBy, param);
  }

  @Override
  @OperationAccess(operation = Operation.UPDATE)
  public T merge(final MetaData metaData, final T type) {
    return super.merge(type);
  }

  @Override
  @OperationAccess(operation = Operation.CREATE)
  public void persist(final MetaData metaData, final T type) {
    super.persist(type);
  }

  @Override
  @OperationAccess(operation = Operation.READ)
  public void read(final MetaData metaData, final T type) {
    // Method is used by access control to test if type is readable.
  }

  @Override
  @OperationAccess(operation = Operation.READ)
  public T refresh(final MetaData metaData, final T type, final long id) {
    return super.refresh(type, id);
  }

  @Override
  @OperationAccess(operation = Operation.DELETE)
  public void remove(final MetaData metaData, final T type) {
    super.remove(type);
  }

  @Override
  @OperationAccess(operation = Operation.UPDATE)
  public void update(final MetaData metaData, final T type) {
    // Method is used by access control to test if type is updatable.
  }
}
