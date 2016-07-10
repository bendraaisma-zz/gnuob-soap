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

package br.com.netbrasoft.gnuob.generic;

import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CREATION_PROPERTY_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GENERIC_TYPE_DAO_IMPL_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GENERIC_TYPE_SERVICE_IMPL_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ID_PROPERTY_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.MODIFICATION_PROPERTY_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.UNCHECKED_VALUE;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ZERO;

import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.LockModeType;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import br.com.netbrasoft.gnuob.generic.security.OperationAccess;
import br.com.netbrasoft.gnuob.generic.security.Rule.Operation;
import br.com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@Stateless(name = GENERIC_TYPE_SERVICE_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class})
public class GenericTypeServiceImpl<T> implements IGenericTypeService<T> {

  @EJB(beanName = GENERIC_TYPE_DAO_IMPL_NAME)
  private IGenericTypeDao<T> genericTypeDao;

  public GenericTypeServiceImpl() {
    // This constructor will be used by the EBJ container.
  }

  GenericTypeServiceImpl(final IGenericTypeDao<T> genericTypeDao) {
    this.genericTypeDao = genericTypeDao;
  }

  @Override
  @OperationAccess(operation = Operation.NONE)
  public long count(final T exampleType, final Parameter... param) {
    return (long) setParameters(createCriteriaByType(exampleType).add(Example.create(exampleType)
        .excludeProperty(CREATION_PROPERTY_NAME).excludeProperty(MODIFICATION_PROPERTY_NAME)), param)
            .setProjection(
                Projections.projectionList().add(Projections.property(ID_PROPERTY_NAME)).add(Projections.rowCount()))
            .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).setCacheable(true).uniqueResult();
  }

  private Criteria createCriteriaByType(final T type) {
    return genericTypeDao.getDelegate().createCriteria(type.getClass());
  }

  private Criteria setParameters(final Criteria criteria, final Parameter... param) {
    Arrays.asList(param).stream().forEach(e -> criteria.createCriteria(e.getName()).add((Criterion) e.getValue()));
    return criteria;
  }

  @Override
  @SuppressWarnings(UNCHECKED_VALUE)
  @OperationAccess(operation = Operation.NONE)
  public List<T> find(final T type, final Paging paging, final OrderByEnum orderingProperty, final Parameter... param) {
    return setOrderingProperty(orderingProperty,
        createCriteriaByType(type).add(Restrictions.in(ID_PROPERTY_NAME, findIds(type, paging, param))))
            .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).setCacheable(true).list();
  }

  private Criteria setOrderingProperty(final OrderByEnum orderingProperty, final Criteria criteria) {
    switch (orderingProperty.getOrder()) {
      case ASC:
        return criteria.addOrder(Order.asc(orderingProperty.getColumn()));
      case DESC:
        return criteria.addOrder(Order.desc(orderingProperty.getColumn()));
      case NONE:
      default:
        return criteria;
    }
  }

  @SuppressWarnings(UNCHECKED_VALUE)
  @OperationAccess(operation = Operation.NONE)
  private List<Long> findIds(final T exampleType, final Paging paging, final Parameter... param) {
    return setParameters(setFirst(paging,
        getMax(paging,
            createCriteriaByType(exampleType).add(Example.create(exampleType).excludeZeroes().enableLike()
                .excludeProperty(CREATION_PROPERTY_NAME).excludeProperty(MODIFICATION_PROPERTY_NAME)))),
        param).setProjection(Projections.projectionList().add(Projections.property(ID_PROPERTY_NAME)))
            .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).setCacheable(true).list();
  }

  private Criteria setFirst(final Paging paging, final Criteria criteria) {
    return paging.getFirst() > ZERO ? criteria.setFirstResult(paging.getFirst()) : criteria;
  }

  private Criteria getMax(final Paging paging, final Criteria criteria) {
    return paging.getMax() > ZERO ? criteria.setMaxResults(paging.getMax()) : criteria;
  }

  @Override
  @OperationAccess(operation = Operation.NONE)
  public T find(final T type, final long id, final LockModeType lockModeType) {
    return genericTypeDao.find(type, id, lockModeType);
  }

  @Override
  @OperationAccess(operation = Operation.NONE)
  public T find(final T type, final long id) {
    final List<T> list = findTypeById(type, id);
    if (list.isEmpty()) {
      throw new GNUOpenBusinessServiceException("No entity found by the given id and type (ERR01)");
    }
    return list.iterator().next();
  }

  @SuppressWarnings(UNCHECKED_VALUE)
  private List<T> findTypeById(final T type, final long id) {
    return createCriteriaByType(type).add(Restrictions.eq(ID_PROPERTY_NAME, id))
        .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).setCacheable(true).list();
  }

  @Override
  @OperationAccess(operation = Operation.NONE)
  public T merge(final T type) {
    return genericTypeDao.merge(type);
  }

  @Override
  @OperationAccess(operation = Operation.NONE)
  public void persist(final T type) {
    genericTypeDao.persist(type);
  }

  @Override
  @OperationAccess(operation = Operation.NONE)
  public T refresh(final T type, final long id) {
    return genericTypeDao.refresh(type, id, LockModeType.OPTIMISTIC);
  }

  @Override
  @OperationAccess(operation = Operation.NONE)
  public void remove(final T type) {
    genericTypeDao.remove(type);
  }

  @Override
  @OperationAccess(operation = Operation.NONE)
  public void flush() {
    genericTypeDao.flush();
  }

  @Override
  @OperationAccess(operation = Operation.NONE)
  public void disableFilter(final String filterName) {
    genericTypeDao.disableFilter(filterName);
  }

  @Override
  @OperationAccess(operation = Operation.NONE)
  public void enableFilter(final String filterName, final Parameter... param) {
    genericTypeDao.enableFilter(filterName, param);
  }
}
