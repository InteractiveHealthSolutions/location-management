Setup instructions for developers
=================================

- To make use of API and data structure, copy location-management-api-1.0.0a.jar from location-api\target
- Run init scripts to create tables
- Init DAOs and LocationService
		DAOLocationImpl locdao = new DAOLocationImpl(session);
		DAOLocationTypeImpl ltdao = new DAOLocationTypeImpl(session);
		DAOLocationAttributeImpl locattrdao = new DAOLocationAttributeImpl(session);
		DAOLocationAttributeTypeImpl locattrtypedao = new DAOLocationAttributeTypeImpl(session);
		this.setLocationService(new LocationServiceImpl(locdao, ltdao, locattrdao, locattrtypedao));
		
- OR you can use LocationServiceContext as well

To use the web components
-------------------------
- Export web as jar
- Copy lmv in webapp/war/WebContent
- Copy locationmanagement-servlet.xml in WEB-INF
- Add entry in web.xml