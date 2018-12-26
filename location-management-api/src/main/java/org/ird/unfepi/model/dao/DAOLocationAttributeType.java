package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.LocationAttributeType;

public interface DAOLocationAttributeType extends DAO{

	LocationAttributeType findById(int id, boolean isreadonly, String[] mappingsToJoin);

	LocationAttributeType findByName(String name, boolean isreadonly, String[] mappingsToJoin);
	
	List<LocationAttributeType> findByDisplayName(String displayname, boolean isreadonly, String[] mappingsToJoin);
		
	List<LocationAttributeType> findByCategory(String category, boolean isreadonly, String[] mappingsToJoin);

	List<LocationAttributeType> getAll(boolean includeVoided, boolean isreadonly, String[] mappingsToJoin);
}
