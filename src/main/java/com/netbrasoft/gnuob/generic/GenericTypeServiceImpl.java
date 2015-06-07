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

@Stateless(name = "GenericTypeServiceImpl")
@Interceptors(value = { AppSimonInterceptor.class })
public class GenericTypeServiceImpl<T> implements GenericTypeService<T> {

   @EJB(beanName = "GenericTypeDaoImpl")
   private GenericTypeDao<T> genericTypeDao;

   @Override
   @OperationAccess(operation = Operation.NONE)
   public long count(T type, Parameter... param) {
      Session session = genericTypeDao.getDelegate();

      ProjectionList projectionList = Projections.projectionList();
      projectionList.add(Projections.property("id"));

      Criteria criteria = session.createCriteria(type.getClass());
      criteria.add(Example.create(type).excludeProperty("creation").excludeProperty("modification"));

      for (Parameter p : param) {
         criteria.createCriteria(p.getName()).add((Criterion) p.getValue());
      }

      criteria.setProjection(projectionList);
      criteria.setProjection(Projections.rowCount());
      criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

      return (long) criteria.uniqueResult();
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
      List<Long> idlist = findIds(type, paging, orderBy, param);

      if (idlist.isEmpty()) {
         return new ArrayList<T>();
      } else {
         Session session = genericTypeDao.getDelegate();
         Criteria criteria = session.createCriteria(type.getClass());

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

         return criteria.list();
      }
   }

   @SuppressWarnings("unchecked")
   @OperationAccess(operation = Operation.NONE)
   protected List<Long> findIds(T type, Paging paging, OrderBy orderBy, Parameter... param) {
      Session session = genericTypeDao.getDelegate();

      Criteria criteria = session.createCriteria(type.getClass());
      criteria.add(Example.create(type).excludeProperty("creation").excludeProperty("modification").excludeProperty("active"));

      for (Parameter p : param) {
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

      ProjectionList projectionList = Projections.projectionList();
      projectionList.add(Projections.property("id"));

      criteria.setProjection(projectionList);
      criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

      return criteria.list();
   }

   @Override
   @OperationAccess(operation = Operation.NONE)
   public void flush() {
      genericTypeDao.flush();
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
