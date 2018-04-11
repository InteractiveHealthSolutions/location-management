/**
 * 
 */
package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LocationAttribute;
import org.ird.unfepi.model.LocationAttributeType;
import org.ird.unfepi.model.LocationHierarchyAncester;
import org.ird.unfepi.model.LocationType;
import org.ird.unfepi.model.dao.DAOLocation;
import org.ird.unfepi.model.dao.DAOLocationAttribute;
import org.ird.unfepi.model.dao.DAOLocationAttributeType;
import org.ird.unfepi.model.dao.DAOLocationHierarchyAncester;
import org.ird.unfepi.model.dao.DAOLocationType;
import org.ird.unfepi.service.LocationService;

/**
 * @author Safwan
 *
 */
public class LocationServiceImpl implements LocationService {
	
	private DAOLocation daoLocation;

	private DAOLocationType daoLocationType;

	private DAOLocationAttribute daoLocationAttribute;

	private DAOLocationAttributeType daoLocationAttributeType;

	private DAOLocationHierarchyAncester daoLocationHierarchyAncester;
	
	public LocationServiceImpl(DAOLocation daoLocation, DAOLocationType daoLocationType, DAOLocationAttribute daoLocationAttribute, DAOLocationAttributeType daoLocationAttributeType){
		this.daoLocation = daoLocation;
		this.daoLocationType = daoLocationType;
		this.daoLocationAttribute = daoLocationAttribute;
		this.daoLocationAttributeType = daoLocationAttributeType;
		this.daoLocationHierarchyAncester = daoLocationHierarchyAncester;
	}

	@Override
	public List<Object> getDataBySQL(String sql) {
		return daoLocation.executeSQL(sql);
	}

	@Override
	public Location findLocationById(int locationId, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.findById(locationId, isreadonly, mappingsToJoin);
	}
	
	@Override
	public Location findLocationByName(String name, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.findByName(name, isreadonly, mappingsToJoin);
	}

	@Override
	public List<Location> getAllLocation(boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.getAll(isreadonly, mappingsToJoin);
	}
	
	@Override
	public LocationType findLocationTypeById(int id, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationType.findById(id, isreadonly, mappingsToJoin);
	}

	@Override
	public LocationType findLocationTypeByName(String Name, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationType.findByName(Name, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationType> getAllLocationType(boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationType.getAll(isreadonly, mappingsToJoin);
	}

	@Override
	public LocationAttribute findLocationAttributeById(int id, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttribute.findById(id, isreadonly, mappingsToJoin);
	}
	
	@Override
	public List<LocationAttribute> findLocationAttributeByCriteria(String typeName, String value, String typeValue1, String typeValue2, Integer locationId, Integer locationAttributeTypeId, int startRecord, int fetchSize, boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter) {
		return daoLocationAttribute.findByCriteria(typeName, value, typeValue1, typeValue2, locationId, locationAttributeTypeId,isreadonly, mappingsToJoin, sqlFilter);
	}
	
	@Override
	public List<LocationAttribute> getAllLocationAttribute(boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttribute.getAll(isreadonly, mappingsToJoin);
	}
	
	@Override
	public LocationAttributeType findLocationAttributeTypeById(int id, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttributeType.findById(id, isreadonly, mappingsToJoin);
	}

	@Override
	public LocationAttributeType findLocationAttributeTypeByName(String Name, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttributeType.findByName(Name, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationAttributeType> findLocationAttributeTypeByCategory(String category, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttributeType.findByCategory(category, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationAttributeType> getAllLocationAttributeType(boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttributeType.getAll(isreadonly, mappingsToJoin);
	}

	@Override
	public Serializable addLocation(Location location) {
		return daoLocation.save(location);
	}

	@Override
	public Serializable addLocationType(LocationType locationType) {
		return daoLocationType.save(locationType);
	}

	@Override
	public Serializable addLocationAttribute(LocationAttribute locationAttribute) {
		return daoLocationAttribute.save(locationAttribute);
	}

	@Override
	public Serializable addLocationAttributeType(LocationAttributeType locationAttributeType) {
		return daoLocationAttributeType.save(locationAttributeType);
	}

	@Override
	public void updateLocation(Location location) {
		daoLocation.update(location);
	}

	@Override
	public void updateLocationType(LocationType locationType) {
		daoLocationType.saveOrUpdate(locationType);
	}

	@Override
	public void updateLocationAttribute(LocationAttribute locationAttribute) {
		daoLocationAttribute.update(locationAttribute);
	}

	@Override
	public void updateLocationAttributeType(LocationAttributeType locationAttributeType) {
		daoLocationAttributeType.update(locationAttributeType);
	}

	@Override
	public void deleteLocation(Location location) {
		daoLocation.delete(location);
	}

	@Override
	public void deleteLocationType(LocationType locationType) {
		daoLocationType.delete(locationType);
	}

	@Override
	public void deleteLocationAttribute(LocationAttribute locationAttribute) {
		daoLocationAttribute.delete(locationAttribute);
	}

	@Override
	public void deleteLocationAttributeType(LocationAttributeType locationAttributeType) {
		daoLocationAttributeType.delete(locationAttributeType);
	}
	
	@Override
	public List<Location> findByHQL(int locId, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.findByHQL(locId, isreadonly, mappingsToJoin);
	}

	@Override
	public Location findLocationByIdentifier(String identifier, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.findByIdentifier(identifier, isreadonly, mappingsToJoin);
	}

	@Override
	public List<Location> findLocationByVoided(boolean voided, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.findLocationByVoided(voided, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationType> findLocationTypeByLevel(int level, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationType.findByLevel(level, isreadonly, mappingsToJoin);
	}


	@Override
	public List<LocationAttribute> findLocationAttributeByValue(String value, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttribute.findByValue(value, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationAttribute> findLocationAttributeByTypeName(String typeName,  boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttribute.findByTypeName(typeName, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationHierarchyAncester> findLocationHierarchyAncesterById(int locationId,  boolean isreadonly,
			String[] mappingsToJoin) {
		return daoLocationHierarchyAncester.findById(locationId, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationHierarchyAncester> findLocationHierarchyAncesterByName(String name, boolean isreadonly,
			String[] mappingsToJoin) {
		return daoLocationHierarchyAncester.findByName(name, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationHierarchyAncester> findLocationHierarchyAncesterByRelativeName(String relativename,
			boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationHierarchyAncester.findByRelativeName(relativename, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationHierarchyAncester> findLocationHierarchyAncesterByRelativeId(int relativeid, boolean isreadonly,
			String[] mappingsToJoin) {
		return daoLocationHierarchyAncester.findByRelativeId(relativeid, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationHierarchyAncester> findLocationHierarchyAncesterByLocationType(int locationtype,
			boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationHierarchyAncester.findByLocationType(locationtype, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationHierarchyAncester> findLocationHierarchyAncesterByRelativeLocationType(int relativetype,
			boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationHierarchyAncester.findByRelativeLocationType(relativetype,  isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationHierarchyAncester> getAllLocationHierarchyAncester(
			boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationHierarchyAncester.getAll(isreadonly, mappingsToJoin);
	}

	@Override
	public Serializable addLocationHierarchyAncester(LocationHierarchyAncester locationHierarchyAncester) {
		return daoLocationHierarchyAncester.save(locationHierarchyAncester);
	}

	@Override
	public void updateLocationHierarchyAncester(LocationHierarchyAncester locationHierarchyAncester) {
		 daoLocationHierarchyAncester.update(locationHierarchyAncester);
	}

	@Override
	public void deleteLocationHierarchyAncester(LocationHierarchyAncester locationHierarchyAncester) {
		 daoLocationHierarchyAncester.delete(locationHierarchyAncester);
	}

	@Override
	public List<LocationHierarchyAncester> findLocationAncesterByCriteria(Integer locationId, String name,
			Integer locationtype, Integer relativeid, String relativename, Integer relativetype, 
			boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationHierarchyAncester.findByCriteria(locationId, name, locationtype, relativeid, relativename, relativetype, isreadonly, mappingsToJoin);
		
	}

}
