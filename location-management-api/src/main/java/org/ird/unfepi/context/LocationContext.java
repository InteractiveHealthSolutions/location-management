package org.ird.unfepi.context;

import java.util.Properties;

import javax.management.InstanceAlreadyExistsException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ird.unfepi.model.dao.hibernatedimpl.*;

public class LocationContext {

	private static LocationContext _instance;
//	private Properties properties;

	private static SessionFactory sessionFactory;

//	public static Properties properties() {
//		return _instance.properties;
//	}
//	public static String property(String key) {
//		return properties().getProperty(key);
//	}

//	private LocationContext(Properties properties) {
//		this.properties = properties;
//	}
	
	public static void instantiate(Properties properties) throws InstanceAlreadyExistsException{
		if(_instance != null){
			throw new InstanceAlreadyExistsException("An instance of LocationContext already exists in system. Make sure to maintain correct flow.");
		}
		
		// session factory must have been instantiated before we could use any method involving data
		sessionFactory = LocationHibernateUtil.getSessionFactory(properties, null);

//		_instance = new LocationContext(properties);
	}
	
	/*public static Statistics getStatistics(){
		Statistics stats = sessionFactory.getStatistics();
		stats.setStatisticsEnabled(true);
		return stats;
	}*/
	
	/** Before calling this method make sure that LocationContext has been instantiated ONCE and ONLY ONCE by calling {@linkplain LocationContext#instantiate} method
	 */
	public static Session getNewSession() {
		return sessionFactory.openSession();
	}
	
	/** Before calling this method make sure that LocationContext has been instantiated ONCE and ONLY ONCE by calling {@linkplain LocationContext#instantiate} method
	 *  
	 * NOTE: For assurance of prevention from synchronization and consistency be sure to get new LocationServiceContext Object
	 * for each bulk or batch of transactions. i.e Using single object for whole application may produce undesired results
	 *
	 * @return the services
	 */
	public static LocationServiceContext getServices(){
		return new LocationServiceContext(sessionFactory);
	}
}
