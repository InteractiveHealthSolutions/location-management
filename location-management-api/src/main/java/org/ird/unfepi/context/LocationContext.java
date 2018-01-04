package org.ird.unfepi.context;

import java.util.Properties;
import java.util.logging.Logger;

import javax.management.InstanceAlreadyExistsException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ird.unfepi.model.dao.hibernatedimpl.*;

public class LocationContext {
	
	private static Logger log = Logger.getLogger(LocationContext.class.getName());

	private static LocationContext _instance;
	private static SessionFactory sessionFactory;

	private LocationContext() {
	}
	
	public static void instantiate(Properties properties) throws InstanceAlreadyExistsException{
		if(_instance != null){
			throw new InstanceAlreadyExistsException("An instance of LocationContext already exists in system. Make sure to maintain correct flow.");
		}
		
		// session factory must have been instantiated before we could use any method involving data
		sessionFactory = LocationHibernateUtil.getSessionFactory(properties, null);

		_instance = new LocationContext();
	}
	
	public static void instantiateWithSessionFactory(SessionFactory sessionFactory) throws InstanceAlreadyExistsException{
		if(_instance != null){
			throw new InstanceAlreadyExistsException("An instance of LocationContext already exists in system. Make sure to maintain correct flow.");
		}
		
		LocationContext.sessionFactory = sessionFactory;
		_instance = new LocationContext();
	}
	
	/*public static Statistics getStatistics(){
		Statistics stats = sessionFactory.getStatistics();
		stats.setStatisticsEnabled(true);
		return stats;
	}*/
	
	public static SessionFactory getSessionFactory() {
		if(sessionFactory == null) {
			log.info("Location sessionFactory is null initing it now with defaults...");
			try {
				instantiate(null);
			} catch (InstanceAlreadyExistsException e) {
				throw new RuntimeException(e);
			}
		}
		return sessionFactory;
	}

	/** Before calling this method make sure that LocationContext has been instantiated ONCE and ONLY ONCE by calling {@linkplain LocationContext#instantiate} method
	 * @throws InstanceAlreadyExistsException 
	 */
	public static Session getNewSession() {
		return getSessionFactory().openSession();
	}
	
	/** Before calling this method make sure that LocationContext has been instantiated ONCE and ONLY ONCE by calling {@linkplain LocationContext#instantiate} method
	 *  
	 * NOTE: For assurance of prevention from synchronization and consistency be sure to get new LocationServiceContext Object
	 * for each bulk or batch of transactions. i.e Using single object for whole application may produce undesired results
	 *
	 * @return the services
	 */
	public static LocationServiceContext getServices(){
		return new LocationServiceContext(getSessionFactory());
	}
}
