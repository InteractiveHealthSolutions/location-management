package com.ihs.locationmanagement.api.model.dao;

import java.util.List;

import com.ihs.locationmanagement.api.model.Location;;

public interface DAOLocation extends DAO {
	
	List findById(int cityId, boolean isreadonly, String[] mappingsToJoin);

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	List findByName(String name, boolean isreadonly, String[] mappingsToJoin);
	
	Location findByShortName(String shortName, boolean isreadonly, String[] mappingsToJoin);

	Location findByFullName(String fullName, boolean isreadonly, String[] mappingsToJoin);

	Location findByDescription(String description, boolean isreadonly, String[] mappingsToJoin);

	Location findByIdentifier(String identifier, boolean isreadonly, String[] mappingsToJoin);

	Location findByLatitude(String latitude, boolean isreadonly, String[] mappingsToJoin);

	Location findByLongitude(String longitude, boolean isreadonly, String[] mappingsToJoin);
	
	List<Location> findLocationByVoided(boolean voided, boolean isreadonly, String[] mappingsToJoin);

	List<Location> getAll(boolean isreadonly, String[] mappingsToJoin);
	List<Location> getChildLocations(Location location, boolean isreadonly, String[] mappingsToJoin);
}
