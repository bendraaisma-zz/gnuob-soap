package com.netbrasoft.gnuob.generic;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.hibernate.Filter;
import org.hibernate.Session;

@Stateless(name = "GenericTypeDaoImpl")
public class GenericTypeDaoImpl<T> implements GenericTypeDao<T> {

	@PersistenceContext(unitName = "primary")
	private EntityManager entityManager;

	public GenericTypeDaoImpl() {

	}

	@Override
	public boolean contains(T type) {
		return entityManager.contains(type);
	}

	@Override
	public void disableFilter(String filterName) {
		Session session = getDelegate();
		session.disableFilter(filterName);
	}

	@Override
	public void enableFilter(String fiterName, Parameter... param) {
		Session session = getDelegate();
		Filter filter = session.enableFilter(fiterName);

		for (Parameter p : param) {
			filter.setParameter(p.getName(), p.getValue());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T find(T type, long id, LockModeType lockModeType) {
		return (T) entityManager.find(type.getClass(), id, lockModeType);
	}

	@Override
	public Session getDelegate() {
		return (Session) entityManager.getDelegate();
	}

	@Override
	public LockModeType getLockModeTypeOfType(T type) {
		return entityManager.getLockMode(type);
	}

	@Override
	public boolean isOpen() {
		return entityManager.isOpen();
	}

	@Override
	public void lock(T type, LockModeType lockModeType) {
		entityManager.lock(type, lockModeType);
	}

	@Override
	public void merge(T type) {
		entityManager.merge(type);
	}

	@Override
	public void persist(T type) {
		entityManager.persist(type);
	}

	@Override
	public T refresh(T type, long id, LockModeType lockModeType) {
		if (!entityManager.contains(type)) {
			return find(type, id, lockModeType);
		}

		entityManager.refresh(type);
		return type;
	}

	@Override
	public void remove(T type) {
		entityManager.remove(entityManager.contains(type) ? type : entityManager.merge(type));
	}
}
