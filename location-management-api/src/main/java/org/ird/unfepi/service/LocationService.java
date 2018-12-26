package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LocationAttribute;
import org.ird.unfepi.model.LocationAttributeType;
import org.ird.unfepi.model.LocationHierarchyAncester;
import org.ird.unfepi.model.LocationType;

public interface LocationService {
	List<Object> getDataBySQL(String sql);
	
	Location findLocationById(int locationId, boolean isreadonly, String[] mappingsToJoin);
	
	Location findLocationByName(String name, boolean isreadonly, String[] mappingsToJoin);
	
	Location findLocationByIdentifier(String identifier, boolean isreadonly, String[] mappingsToJoin);
	
	List<Location> getAllLocation(boolean includeVoided, boolean isreadonly, String[] mappingsToJoin);

	LocationType findLocationTypeById(int id, boolean isreadonly, String[] mappingsToJoin);

	LocationType findLocationTypeByName(String name, boolean isreadonly, String[] mappingsToJoin);
	
	List<LocationType> findLocationTypeByLevel(int level, boolean isreadonly, String[] mappingsToJoin);
	
	List<LocationType> getAllLocationType(boolean isreadonly, String[] mappingsToJoin);
	
	LocationAttributeType findLocationAttributeTypeById(int id, boolean isreadonly, String[] mappingsToJoin);

	LocationAttributeType findLocationAttributeTypeByName(String name, boolean isreadonly, String[] mappingsToJoin);
	
	List<LocationAttributeType> findLocationAttributeTypeByCategory(String category, boolean isreadonly, String[] mappingsToJoin);

	List<LocationAttributeType> getAllLocationAttributeType(boolean includeVoided, boolean isreadonly, String[] mappingsToJoin);
//TODO add LocationAttributeType by displayname 
	LocationAttribute findLocationAttributeById(int id, boolean isreadonly, String[] mappingsToJoin);
	
	List<LocationAttribute> findLocationAttributeByLocation(int locationId, boolean isreadonly, String[] mappingsToJoin);
	
	List<LocationAttribute> findLocationAttributeByCriteria(Integer locationId, Integer locationAttributeTypeId, 
			String rangeValue1, String rangeValue2, String value, boolean isreadonly, String[] mappingsToJoin);

	List<LocationHierarchyAncester> findLocationHierarchyAncesterById(int locationId, boolean isreadonly, String[] mappingsToJoin);
	
	List<LocationHierarchyAncester> findLocationHierarchyAncesterByName(String name, boolean isreadonly, String[] mappingsToJoin);

	List<LocationHierarchyAncester> findLocationHierarchyAncesterByRelativeName (String relativename, boolean isreadonly, String[] mappingsToJoin);
	
	List<LocationHierarchyAncester> findLocationHierarchyAncesterByRelativeId (int relativeid, boolean isreadonly, String[] mappingsToJoin);

	List<LocationHierarchyAncester> findLocationHierarchyAncesterByLocationType (int locationtype, boolean isreadonly, String[] mappingsToJoin);
	
	List<LocationHierarchyAncester> findLocationHierarchyAncesterByRelativeLocationType (int relativetype, boolean isreadonly, String[] mappingsToJoin);

	List<LocationHierarchyAncester> findLocationAncesterByCriteria (Integer locationId, String name, Integer locationtype, Integer relativeid,
			String relativename, Integer relativetype, boolean isreadonly, String[] mappingsToJoin);
	
	List<LocationHierarchyAncester> getAllLocationHierarchyAncester(boolean isreadonly, String[] mappingsToJoin);
	
	Serializable addLocation(Location location);

	Serializable addLocationType(LocationType locationType);

	Serializable addLocationAttribute(LocationAttribute locationAttribute);

	Serializable addLocationAttributeType(LocationAttributeType locationAttributeType);

	Serializable addLocationHierarchyAncester(LocationHierarchyAncester locationHierarchyAncester);
	
	void updateLocation(Location location);

	void updateLocationType(LocationType locationType);

	void updateLocationAttribute(LocationAttribute locationAttribute);

	void updateLocationAttributeType(LocationAttributeType locationAttributeType);

	void updateLocationHierarchyAncester(LocationHierarchyAncester locationHierarchyAncester);
	
	void deleteLocation(Location location);

	void deleteLocationType(LocationType locationType);

	void deleteLocationAttribute(LocationAttribute locationAttribute);

	void deleteLocationAttributeType(LocationAttributeType locationAttributeType);
	
	void deleteLocationHierarchyAncester(LocationHierarchyAncester locationHierarchyAncester);
}
