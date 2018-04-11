package org.ird.unfepi.context;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.ird.unfepi.model.dao.hibernatedimpl.*;
import org.ird.unfepi.service.LocationService;
import org.ird.unfepi.service.impl.LocationServiceImpl;

public class LocationServiceContext {

	private LocationService locationService;

	private Session session;

	private Transaction transaction;

	LocationServiceContext(SessionFactory sessionObj)
	{
		session = sessionObj.openSession();
		transaction = session.beginTransaction();
		
		DAOLocationImpl locdao = new DAOLocationImpl(session);
		DAOLocationTypeImpl ltdao = new DAOLocationTypeImpl(session);
		DAOLocationAttributeImpl locattrdao = new DAOLocationAttributeImpl(session);
		DAOLocationAttributeTypeImpl locattrtypedao = new DAOLocationAttributeTypeImpl(session);
		this.setLocationService(new LocationServiceImpl(locdao, ltdao, locattrdao, locattrtypedao));
	}
	
	public LocationServiceContext(Session session) {
		this.session = session;
		
		DAOLocationImpl locdao = new DAOLocationImpl(session);
		DAOLocationTypeImpl ltdao = new DAOLocationTypeImpl(session);
		DAOLocationAttributeImpl locattrdao = new DAOLocationAttributeImpl(session);
		DAOLocationAttributeTypeImpl locattrtypedao = new DAOLocationAttributeTypeImpl(session);
		this.setLocationService(new LocationServiceImpl(locdao, ltdao, locattrdao, locattrtypedao));
	}

	public LocationService getLocationService()
	{
		return locationService;
	}

	public void setLocationService(LocationService locationService)
	{
		this.locationService = locationService;
	}
	
	public void beginTransaction()
	{
		if (transaction == null || !transaction.isActive())
		{
			transaction = session.beginTransaction();
		}
	}

	public void closeSession()
	{
		try {
			session.close();
		} catch (Exception e) {
		}
	}

	public void commitTransaction()
	{
		transaction.commit();
	}

	public void rollbackTransaction()
	{
		if (transaction != null)
		{
			transaction.rollback();
		}
	}

	public void flushSession()
	{
		session.flush();
	}
	
	public Session getSession()
	{
		return session;
	}
}
