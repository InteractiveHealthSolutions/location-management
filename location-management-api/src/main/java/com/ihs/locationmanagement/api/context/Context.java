package com.ihs.locationmanagement.api.context;

import java.util.Properties;

import javax.management.InstanceAlreadyExistsException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.ihs.locationmanagement.mode.dao.hibernatedimpl.*;

public class Context {

	private static Context _instance;
//	private Properties properties;

	private static SessionFactory sessionFactory;

//	public static Properties properties() {
//		return _instance.properties;
//	}
//	public static String property(String key) {
//		return properties().getProperty(key);
//	}

//	private Context(Properties properties) {
//		this.properties = properties;
//	}
	
	public static void instantiate(Properties properties) throws InstanceAlreadyExistsException{
		if(_instance != null){
			throw new InstanceAlreadyExistsException("An instance of Context already exists in system. Make sure to maintain correct flow.");
		}
		
		// session factory must have been instantiated before we could use any method involving data
		sessionFactory = HibernateUtil.getSessionFactory(properties, null);

//		_instance = new Context(properties);
	}
	
	/*public static Statistics getStatistics(){
		Statistics stats = sessionFactory.getStatistics();
		stats.setStatisticsEnabled(true);
		return stats;
	}*/
	
	/** Before calling this method make sure that Context has been instantiated ONCE and ONLY ONCE by calling {@linkplain Context#instantiate} method
	 */
	public static Session getNewSession() {
		return sessionFactory.openSession();
	}
	
	/** Before calling this method make sure that Context has been instantiated ONCE and ONLY ONCE by calling {@linkplain Context#instantiate} method
	 *  
	 * NOTE: For assurance of prevention from synchronization and consistency be sure to get new ServiceContext Object
	 * for each bulk or batch of transactions. i.e Using single object for whole application may produce undesired results
	 *
	 * @return the services
	 */
	public static ServiceContext getServices(){
		return new ServiceContext(sessionFactory);
	}
}
