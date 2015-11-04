package com.netbrasoft.gnuob.generic;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.hibernate.Filter;
import org.hibernate.Session;

import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@Stateless(name = GenericTypeDaoImpl.GENERIC_TYPE_DAO_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class})
public class GenericTypeDaoImpl<T> implements GenericTypeDao<T> {

  protected static final String GENERIC_TYPE_DAO_IMPL_NAME = "GenericTypeDaoImpl";

  @PersistenceContext(unitName = "primary")
  private EntityManager entityManager;

  public GenericTypeDaoImpl() {
    // Empty constructor.
  }

  @Override
  public boolean contains(final T type) {
    return entityManager.contains(type);
  }

  @Override
  public void disableFilter(final String filterName) {
    final Session session = getDelegate();
    session.disableFilter(filterName);
  }

  @Override
  public void enableFilter(final String fiterName, final Parameter... param) {
    final Session session = getDelegate();
    final Filter filter = session.enableFilter(fiterName);

    for (final Parameter p : param) {
      filter.setParameter(p.getName(), p.getValue());
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public T find(final T type, final long id, final LockModeType lockModeType) {
    return (T) entityManager.find(type.getClass(), id, lockModeType);
  }

  @Override
  public void flush() {
    entityManager.flush();
  }

  @Override
  public Session getDelegate() {
    return (Session) entityManager.getDelegate();
  }

  @Override
  public LockModeType getLockModeTypeOfType(final T type) {
    return entityManager.getLockMode(type);
  }

  @Override
  public boolean isOpen() {
    return entityManager.isOpen();
  }

  @Override
  public void lock(final T type, final LockModeType lockModeType) {
    entityManager.lock(type, lockModeType);
  }

  @Override
  public T merge(final T type) {
    return entityManager.merge(type);
  }

  @Override
  public void persist(final T type) {
    entityManager.persist(type);
  }

  @Override
  public T refresh(final T type, final long id, final LockModeType lockModeType) {
    if (!entityManager.contains(type)) {
      return find(type, id, lockModeType);
    }
    entityManager.refresh(type);
    return type;
  }

  @Override
  public void remove(final T type) {
    entityManager.remove(entityManager.contains(type) ? type : entityManager.merge(type));
  }
}
