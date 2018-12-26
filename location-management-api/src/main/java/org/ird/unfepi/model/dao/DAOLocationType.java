package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.LocationType;

public interface DAOLocationType extends DAO{

	LocationType findById(int id, boolean isreadonly, String[] mappingsToJoin);

	LocationType findByName(String name, boolean isreadonly, String[] mappingsToJoin);

	List<LocationType> findByLevel(int level, boolean isreadonly, String[] mappingsToJoin);
	
	List<LocationType> getAll( boolean isreadonly, String[] mappingsToJoin);
}
