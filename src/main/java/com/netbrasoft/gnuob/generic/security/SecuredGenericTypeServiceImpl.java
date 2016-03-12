/*
 * Copyright 2016 Netbrasoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.netbrasoft.gnuob.generic.security;

import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import com.netbrasoft.gnuob.generic.GenericTypeServiceImpl;
import com.netbrasoft.gnuob.generic.OrderByEnum;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.Parameter;
import com.netbrasoft.gnuob.generic.security.Rule.Operation;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@Stateless(name = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME, mappedName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
@Interceptors(value = {AccessControl.class, AppSimonInterceptor.class})
public class SecuredGenericTypeServiceImpl<T> extends GenericTypeServiceImpl<T>
    implements ISecuredGenericTypeService<T> {

  @Override
  @OperationAccess(operation = Operation.READ)
  public long count(final MetaData credentials, final T type, final Parameter... param) {
    return super.count(type, param);
  }

  /*@Override
  @OperationAccess(operation = Operation.CREATE)
  public void create(final MetaData credentials, final T type) {
    // Method is used by access control to test if type can be created.
  }*/

  /*@Override
  @OperationAccess(operation = Operation.DELETE)
  public void delete(final MetaData credentials, final T type) {
    // Method is used by access control to test if type can be deleted.
  }*/

  @Override
  @OperationAccess(operation = Operation.READ)
  public T find(final MetaData credentials, final T type, final long id) {
    return super.find(type, id);
  }

  @Override
  @OperationAccess(operation = Operation.READ)
  public List<T> find(final MetaData credentials, final T type, final Paging paging, final OrderByEnum orderingProperty,
      final Parameter... param) {
    return super.find(type, paging, orderingProperty, param);
  }

  @Override
  @OperationAccess(operation = Operation.UPDATE)
  public T merge(final MetaData credentials, final T type) {
    return super.merge(type);
  }

  @Override
  @OperationAccess(operation = Operation.CREATE)
  public void persist(final MetaData credentials, final T type) {
    super.persist(type);
  }

  /*@Override
  @OperationAccess(operation = Operation.READ)
  public void read(final MetaData credentials, final T type) {
    // Method is used by access control to test if type is readable.
  }*/

  @Override
  @OperationAccess(operation = Operation.READ)
  public T refresh(final MetaData credentials, final T type, final long id) {
    return super.refresh(type, id);
  }

  @Override
  @OperationAccess(operation = Operation.DELETE)
  public void remove(final MetaData credentials, final T type) {
    super.remove(type);
  }

  /*@Override
  @OperationAccess(operation = Operation.UPDATE)
  public void update(final MetaData credentials, final T type) {
    // Method is used by access control to test if type is updatable.
  }*/
}
