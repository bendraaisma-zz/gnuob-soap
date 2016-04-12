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

import java.util.List;

import com.netbrasoft.gnuob.generic.OrderByEnum;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.Parameter;
import com.netbrasoft.gnuob.generic.security.Rule.Operation;

public abstract interface ISecuredGenericTypeService<T> {

  public abstract long count(MetaData credentials, T type, Parameter... param);

  @OperationAccess(operation = Operation.CREATE)
  public default void create(MetaData credentials, T type) {}

  @OperationAccess(operation = Operation.DELETE)
  public default void delete(MetaData credentials, T type) {}

  public abstract T find(MetaData credentials, T type, long id);

  public abstract List<T> find(MetaData credentials, T type, Paging paging, OrderByEnum orderingProperty,
      Parameter... param);

  public abstract void flush();

  public abstract T merge(MetaData credentials, T type);

  public abstract void persist(MetaData credentials, T type);

  @OperationAccess(operation = Operation.READ)
  public default void read(MetaData credentials, T type) {}

  public abstract T refresh(MetaData credentials, T type, long id);

  public abstract void remove(MetaData credentials, T type);

  @OperationAccess(operation = Operation.UPDATE)
  public default void update(MetaData credentials, T type) {}
}
