/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.LocationAttribute;

public interface DAOLocationAttribute extends DAO {

	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	LocationAttribute findById(int id, boolean isreadonly, String[] mappingsToJoin);

	List<LocationAttribute> findByValue(String value, boolean isreadonly, String[] mappingsToJoin);

	List<LocationAttribute> findByTypeName(String typeName, boolean isreadonly, String[] mappingsToJoin);

	List<LocationAttribute> findByCriteria(String typeName, String value, String typeValue1, String typeValue2,
			Integer locationId, Integer locationAttributeTypeId, boolean isreadonly, 
			String[] mappingsToJoin, String[] sqlFilter);

	List<LocationAttribute> getAll(boolean isreadonly, String[] mappingsToJoin);
}
