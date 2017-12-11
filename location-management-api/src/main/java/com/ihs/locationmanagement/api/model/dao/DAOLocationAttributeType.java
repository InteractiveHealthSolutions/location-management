package com.ihs.locationmanagement.api.model.dao;

import java.util.List;

import com.ihs.locationmanagement.api.model.LocationAttributeType;

public interface DAOLocationAttributeType extends DAO {

	LocationAttributeType findById(int id, boolean isreadonly, String[] mappingsToJoin);

	LocationAttributeType findByName(String name, boolean isreadonly, String[] mappingsToJoin);
	
	LocationAttributeType findByDisplayName(String displayname, boolean isreadonly, String[] mappingsToJoin);
	
	LocationAttributeType findByDescription(String description, boolean isreadonly, String[] mappingsToJoin);
	
	LocationAttributeType findByCategory(String category, boolean isreadonly, String[] mappingsToJoin);

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	List<LocationAttributeType> getAll(boolean isreadonly, String[] mappingsToJoin);
}
