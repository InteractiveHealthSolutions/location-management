package org.ird.unfepi.model.dao.hibernatedimpl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.ird.unfepi.model.dao.DAO;

public abstract class DAOHibernateImpl <T> implements DAO{
	
	protected Session session;
	protected Number LAST_QUERY_TOTAL_ROW_COUNT;
	
	public DAOHibernateImpl(Session session) {
		this.session=session;
	}
	
	public Serializable save(Object objectinstance) {
		return session.save(objectinstance);
	}
	
	public void delete(Object objectinstance) {
		session.delete(objectinstance);
	}
	
	public Object merge(Object objectinstance) {
		return session.merge(objectinstance);
	}
	
	public void update(Object objectinstance) {
		session.update(objectinstance);
	}
	
	public void saveOrUpdate(Object objectinstance) {
		session.saveOrUpdate(objectinstance);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> executeSQL(String sql) {
		return session.createSQLQuery(sql).list();
	}

	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	protected void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	protected abstract Class<?> getEntity();

	public Criteria buildCriteria(boolean isreadonly, String[] mappingsToJoin) {
 		Criteria cri = session.createCriteria(getEntity()).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		return cri;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T buildResult(Criteria cri) {
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<?> list = cri.list();
		return (T) (list.size() > 0 ? list.get(0) : null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> buildResultList(Criteria cri, Order... orders) {
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if(orders != null && orders.length > 0) {
			for (Order order : orders) {
				cri.addOrder(order);
			}
		}
		return cri.list();
	}

}
