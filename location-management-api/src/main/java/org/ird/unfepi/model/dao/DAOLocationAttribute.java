package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.LocationAttribute;

public interface DAOLocationAttribute extends DAO {

	LocationAttribute findById(int id, boolean isreadonly, String[] mappingsToJoin);

	List<LocationAttribute> findByLocation(int locationId, boolean isreadonly, String[] mappingsToJoin);

	List<LocationAttribute> findByCriteria(Integer locationId, Integer locationAttributeTypeId, 
			String rangeValue1, String rangeValue2, String value, boolean isreadonly, String[] mappingsToJoin);
}
