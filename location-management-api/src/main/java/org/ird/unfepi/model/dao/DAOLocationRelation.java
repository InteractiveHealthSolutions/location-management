package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.LocationRelation;

public interface DAOLocationRelation extends DAO {
	
	List<LocationRelation> findById(int locationId, boolean isreadonly, String[] mappingsToJoin);
	List<LocationRelation> findByName(String name, boolean isreadonly, String[] mappingsToJoin);

	List<LocationRelation> findByRelativeName (String relativename, boolean isreadonly, String[] mappingsToJoin);
	List<LocationRelation> findByRelativeId (int relativeid, boolean isreadonly, String[] mappingsToJoin);

	List<LocationRelation> findByLocationType (int locationtype, boolean isreadonly, String[] mappingsToJoin);
	List<LocationRelation> findByRelativeLocationType (int relativetype, boolean isreadonly, String[] mappingsToJoin);

	List<LocationRelation> getAll( boolean isreadonly, String[] mappingsToJoin);
	
	List<LocationRelation> findByCriteria(Integer locationId, String name, Integer locationtype,
								Integer relativeid, String relativename, Integer relativetype,  
								boolean isreadonly, String[] mappingsToJoin);
}
