package com.ihs.locationmanagement.api.model.dao;

import java.util.List;

import com.ihs.locationmanagement.api.model.*;;

public interface DAOLocationAttribute extends DAO {

	LocationAttribute findById(int id, boolean isreadonly, String[] mappingsToJoin);
	
	Number LAST_QUERY_TOTAL_ROW_COUNT();

	LocationAttribute findByValue(String value, boolean isreadonly, String[] mappingsToJoin);

	LocationAttribute findByTypeName(String typeName, boolean isreadonly, String[] mappingsToJoin);

	LocationAttribute findByTypeValue1(String typeValue, boolean isreadonly, String[] mappingsToJoin);

	LocationAttribute findByTypeValue2(String typeValue, boolean isreadonly, String[] mappingsToJoin);

	List<LocationAttribute> findByCriteria(Integer locationAttributeId, String value, String typeName, String typeValue1, String typeValue2, Integer locationId, Integer locationAttributeTypeId, int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin);

	List<LocationAttribute> getAll(boolean isreadonly, String[] mappingsToJoin);
}
