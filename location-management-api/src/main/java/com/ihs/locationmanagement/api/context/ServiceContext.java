package com.ihs.locationmanagement.api.context;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import com.ihs.locationmanagement.mode.dao.hibernatedimpl.*;
import com.ihs.locationmanagement.api.service.LocationService;
import com.ihs.locationmanagement.api.service.impl.LocationServiceImpl;

public class ServiceContext {

	private LocationService locationService;

	private Session session;

	private Transaction transaction;

	ServiceContext(SessionFactory sessionObj)
	{
		session = sessionObj.openSession();
		transaction = session.beginTransaction();
		
		DAOLocationImpl locdao = new DAOLocationImpl(session);
		DAOLocationTypeImpl ltdao = new DAOLocationTypeImpl(session);
		DAOLocationAttributeImpl locattrdao = new DAOLocationAttributeImpl(session);
		DAOLocationAttributeTypeImpl locattrtypedao = new DAOLocationAttributeTypeImpl(session);
		this.setLocationService(new LocationServiceImpl(this, locdao, ltdao, locattrdao, locattrtypedao));
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
		try
		{
			session.close();
		}
		catch (Exception e)
		{
		}
	}

	public void commitTransaction()
	{
		transaction.commit();
	}

	@Override
	protected void finalize() throws Throwable
	{
		closeSession();
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
}
