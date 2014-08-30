package com.netbrasoft.gnuob.generic;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.LockModeType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.netbrasoft.gnuob.generic.security.OperationAccess;
import com.netbrasoft.gnuob.generic.security.Rule.Operation;

@Stateless(name = "GenericTypeServiceImpl")
public class GenericTypeServiceImpl<T> implements GenericTypeService<T> {

	@EJB(beanName = "GenericTypeDaoImpl")
	private GenericTypeDao<T> genericTypeDao;

	@Override
	@OperationAccess(operation = Operation.NONE)
	public long count(T type, Parameter... param) {
		Session session = genericTypeDao.getDelegate();

		Criteria criteria = session.createCriteria(type.getClass());
		criteria.add(Example.create(type));

		for (Parameter p : param) {
			criteria.createCriteria(p.getName()).add((Criterion) p.getValue());
		}

		criteria.setProjection(Projections.rowCount());
		Long uniqueResult = (Long) criteria.uniqueResult();

		return uniqueResult.longValue();
	}

	@Override
	@OperationAccess(operation = Operation.NONE)
	public void disableFilter(String filterName) {
		genericTypeDao.disableFilter(filterName);
	}

	@Override
	@OperationAccess(operation = Operation.NONE)
	public void enableFilter(String filterName, Parameter... param) {
		genericTypeDao.enableFilter(filterName, param);
	}

	@Override
	@SuppressWarnings("unchecked")
	@OperationAccess(operation = Operation.NONE)
	public T find(T type, long id) {
		Session session = genericTypeDao.getDelegate();
		Criteria criteria = session.createCriteria(type.getClass());

		criteria.add(Restrictions.eq("id", id));

		criteria.setFirstResult(0);
		criteria.setMaxResults(1);

		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List<T> list = criteria.list();

		return list.isEmpty() ? null : list.iterator().next();
	}

	@Override
	@OperationAccess(operation = Operation.NONE)
	public T find(T type, long id, LockModeType lockModeType) {
		return genericTypeDao.find(type, id, lockModeType);
	}

	@Override
	@SuppressWarnings("unchecked")
	@OperationAccess(operation = Operation.NONE)
	public List<T> find(T type, Paging paging, OrderBy orderBy, Parameter... param) {

		Session session = genericTypeDao.getDelegate();
		Criteria criteria = session.createCriteria(type.getClass());
		criteria.add(Example.create(type));

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

		for (Parameter p : param) {
			criteria.createCriteria(p.getName()).add((Criterion) p.getValue());
		}

		if (paging.getFirst() >= 0) {
			criteria.setFirstResult(paging.getFirst());
		}

		if (paging.getMax() >= 0) {
			criteria.setMaxResults(paging.getMax());
		}

		// TODO, uitzoeken wat de beste transformatie is ivm met Has-A lijsten.
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	@OperationAccess(operation = Operation.NONE)
	public void merge(T type) {
		genericTypeDao.merge(type);
	}

	@Override
	@OperationAccess(operation = Operation.NONE)
	public void persist(T type) {
		genericTypeDao.persist(type);
	}

	@Override
	@OperationAccess(operation = Operation.NONE)
	public T refresh(T type, long id) {
		return genericTypeDao.refresh(type, id, LockModeType.OPTIMISTIC);
	}

	@Override
	@OperationAccess(operation = Operation.NONE)
	public void remove(T type) {
		genericTypeDao.remove(type);
	}

}
