package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LocationAttribute;
import org.ird.unfepi.model.LocationAttributeType;
import org.ird.unfepi.model.LocationRelation;
import org.ird.unfepi.model.LocationType;
import org.ird.unfepi.model.dao.DAOLocation;
import org.ird.unfepi.model.dao.DAOLocationAttribute;
import org.ird.unfepi.model.dao.DAOLocationAttributeType;
import org.ird.unfepi.model.dao.DAOLocationRelation;
import org.ird.unfepi.model.dao.DAOLocationType;
import org.ird.unfepi.service.LocationService;

public class LocationServiceImpl implements LocationService {
	
	private DAOLocation daoLocation;

	private DAOLocationType daoLocationType;

	private DAOLocationAttribute daoLocationAttribute;

	private DAOLocationAttributeType daoLocationAttributeType;

	private DAOLocationRelation daoLocationRelation;
	
	public LocationServiceImpl(DAOLocation daoLocation, DAOLocationType daoLocationType, DAOLocationAttribute daoLocationAttribute, DAOLocationAttributeType daoLocationAttributeType){
		this.daoLocation = daoLocation;
		this.daoLocationType = daoLocationType;
		this.daoLocationAttribute = daoLocationAttribute;
		this.daoLocationAttributeType = daoLocationAttributeType;
		//TODO revamp the approach
		this.daoLocationRelation = null;
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
	public Location findLocationByIdentifier(String identifier, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.findByIdentifier(identifier, isreadonly, mappingsToJoin);
	}
	
	@Override
	public Location findLocationByName(String name, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.findByName(name, isreadonly, mappingsToJoin);
	}

	@Override
	public List<Location> getAllLocation(boolean includeVoided, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.getAll(includeVoided, isreadonly, mappingsToJoin, null);
	}
	
	@Override
	public List<Location> getAllLocationOrderedByLevel(boolean includeVoided, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.getAll(includeVoided, isreadonly, mappingsToJoin, new String[] {"level", "locationId"});
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
	public List<LocationAttribute> findLocationAttributeByCriteria(Integer locationId, Integer locationAttributeTypeId, 
			String rangeValue1, String rangeValue2, String value, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttribute.findByCriteria(locationId, locationAttributeTypeId, rangeValue1, rangeValue2, value, isreadonly, mappingsToJoin);
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
	public List<LocationAttributeType> getAllLocationAttributeType(boolean includeVoided, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttributeType.getAll(includeVoided, isreadonly, mappingsToJoin);
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
	public List<LocationType> findLocationTypeByLevel(int level, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationType.findByLevel(level, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationAttribute> findLocationAttributeByLocation(int locationId, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttribute.findByLocation(locationId, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationRelation> findLocationRelationById(int locationId,  boolean isreadonly,
			String[] mappingsToJoin) {
		return daoLocationRelation.findById(locationId, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationRelation> findLocationRelationByName(String name, boolean isreadonly,
			String[] mappingsToJoin) {
		return daoLocationRelation.findByName(name, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationRelation> findLocationRelationByRelativeName(String relativename,
			boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationRelation.findByRelativeName(relativename, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationRelation> findLocationRelationByRelativeId(int relativeid, boolean isreadonly,
			String[] mappingsToJoin) {
		return daoLocationRelation.findByRelativeId(relativeid, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationRelation> findLocationRelationByLocationType(int locationtype,
			boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationRelation.findByLocationType(locationtype, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationRelation> findLocationRelationByRelativeLocationType(int relativetype,
			boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationRelation.findByRelativeLocationType(relativetype,  isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationRelation> getAllLocationRelation(
			boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationRelation.getAll(isreadonly, mappingsToJoin);
	}

	@Override
	public Serializable addLocationRelation(LocationRelation locationRelation) {
		return daoLocationRelation.save(locationRelation);
	}

	@Override
	public void updateLocationRelation(LocationRelation locationRelation) {
		 daoLocationRelation.update(locationRelation);
	}

	@Override
	public void deleteLocationRelation(LocationRelation locationRelation) {
		 daoLocationRelation.delete(locationRelation);
	}

	@Override
	public List<LocationRelation> findLocationAncesterByCriteria(Integer locationId, String name,
			Integer locationtype, Integer relativeid, String relativename, Integer relativetype, 
			boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationRelation.findByCriteria(locationId, name, locationtype, relativeid, relativename, relativetype, isreadonly, mappingsToJoin);
		
	}

}
