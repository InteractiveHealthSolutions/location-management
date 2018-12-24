package org.ird.unfepi.model.dao.hibernatedimpl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.ird.unfepi.model.dao.DAO;

public class DAOHibernateImpl implements DAO{
	
	private Session session;
	
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
}
