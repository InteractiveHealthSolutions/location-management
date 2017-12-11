/*
 * 
 */
package com.ihs.locationmanagement.mode.dao.hibernatedimpl;

import java.io.Serializable;

import org.hibernate.Session;
import com.ihs.locationmanagement.api.model.dao.DAO;

// TODO: Auto-generated Javadoc
/**
 * The Class DAOHibernateImpl.
 */
public class DAOHibernateImpl implements DAO{
	
	/** The session. */
	private Session session;
	
	/**
	 * Instantiates a new dAO hibernate impl.
	 *
	 * @param session the session
	 */
	public DAOHibernateImpl(Session session) {
		this.session=session;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAO#save(java.lang.Object)
	 */
	
	public Serializable save(Object objectinstance) {
		return session.save(objectinstance);
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAO#delete(java.lang.Object)
	 */
	
	public void delete(Object objectinstance) {
		session.delete(objectinstance);
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAO#merge(java.lang.Object)
	 */

	public Object merge(Object objectinstance) {
		return session.merge(objectinstance);
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAO#update(java.lang.Object)
	 */

	public void update(Object objectinstance) {
		session.update(objectinstance);
	}
	


	public void saveOrUpdate(Object objectinstance) {
		session.saveOrUpdate(objectinstance);
	}
}
