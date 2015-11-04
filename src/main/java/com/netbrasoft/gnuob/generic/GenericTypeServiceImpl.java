package com.netbrasoft.gnuob.generic;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.LockModeType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.netbrasoft.gnuob.generic.security.OperationAccess;
import com.netbrasoft.gnuob.generic.security.Rule.Operation;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@Stateless(name = GenericTypeServiceImpl.GENERIC_TYPE_SERVICE_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class})
public class GenericTypeServiceImpl<T> implements GenericTypeService<T> {

  public static final String GENERIC_TYPE_SERVICE_IMPL_NAME = "GenericTypeServiceImpl";

  @EJB(beanName = GenericTypeDaoImpl.GENERIC_TYPE_DAO_IMPL_NAME)
  private transient GenericTypeDao<T> genericTypeDao;

  @Override
  @OperationAccess(operation = Operation.NONE)
  public long count(final T type, final Parameter... param) {
    final Session session = genericTypeDao.getDelegate();

    final ProjectionList projectionList = Projections.projectionList();
    projectionList.add(Projections.property("id"));

    final Criteria criteria = session.createCriteria(type.getClass());
    criteria.add(Example.create(type).excludeProperty("creation").excludeProperty("modification"));

    for (final Parameter p : param) {
      criteria.createCriteria(p.getName()).add((Criterion) p.getValue());
    }

    criteria.setProjection(projectionList);
    criteria.setProjection(Projections.rowCount());
    criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

    return (long) criteria.uniqueResult();
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

  @Override
  @SuppressWarnings("unchecked")
  @OperationAccess(operation = Operation.NONE)
  public T find(final T type, final long id) {
    final Session session = genericTypeDao.getDelegate();
    final Criteria criteria = session.createCriteria(type.getClass());

    criteria.add(Restrictions.eq("id", id));

    criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

    final List<T> list = criteria.setCacheable(true).list();

    return list.isEmpty() ? null : list.iterator().next();
  }

  @Override
  @OperationAccess(operation = Operation.NONE)
  public T find(final T type, final long id, final LockModeType lockModeType) {
    return genericTypeDao.find(type, id, lockModeType);
  }

  @Override
  @SuppressWarnings("unchecked")
  @OperationAccess(operation = Operation.NONE)
  public List<T> find(final T type, final Paging paging, final OrderBy orderBy, final Parameter... param) {
    final List<Long> idlist = findIds(type, paging, orderBy, param);

    if (idlist.isEmpty()) {
      return new ArrayList<T>();
    } else {
      final Session session = genericTypeDao.getDelegate();
      final Criteria criteria = session.createCriteria(type.getClass());

      criteria.add(Restrictions.in("id", idlist));

      switch (orderBy.getOrderBy()) {
        case ASC:
          criteria.addOrder(Order.asc(orderBy.getColumn()));
          break;
        case DESC:
          criteria.addOrder(Order.desc(orderBy.getColumn()));
          break;
        case NONE:
        default:
          break;
      }

      criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

      return criteria.setCacheable(true).list();
    }
  }

  @SuppressWarnings("unchecked")
  @OperationAccess(operation = Operation.NONE)
  protected List<Long> findIds(final T type, final Paging paging, final OrderBy orderBy, final Parameter... param) {
    final Session session = genericTypeDao.getDelegate();

    final Criteria criteria = session.createCriteria(type.getClass());
    criteria.add(Example.create(type).excludeProperty("creation").excludeProperty("modification"));

    for (final Parameter p : param) {
      criteria.createCriteria(p.getName()).add((Criterion) p.getValue());
    }

    switch (orderBy.getOrderBy()) {
      case ASC:
        criteria.addOrder(Order.asc(orderBy.getColumn()));
        break;
      case DESC:
        criteria.addOrder(Order.desc(orderBy.getColumn()));
        break;
      case NONE:
      default:
        break;
    }

    if (paging.getFirst() > 0) {
      criteria.setFirstResult(paging.getFirst());
    }

    if (paging.getMax() > 0) {
      criteria.setMaxResults(paging.getMax());
    }

    final ProjectionList projectionList = Projections.projectionList();
    projectionList.add(Projections.property("id"));

    criteria.setProjection(projectionList);
    criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

    return criteria.setCacheable(true).list();
  }

  @Override
  @OperationAccess(operation = Operation.NONE)
  public void flush() {
    genericTypeDao.flush();
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

}
