package com.ihs.locationmanagement.api.service.impl;

import java.io.Serializable;
import java.util.List;
import com.ihs.locationmanagement.api.context.ServiceContext;
import com.ihs.locationmanagement.api.model.*;
import com.ihs.locationmanagement.api.model.dao.*;
import com.ihs.locationmanagement.api.service.LocationService;


public class LocationServiceImpl implements LocationService {
	
	private ServiceContext sc;

	private DAOLocation daoLocation;

	private DAOLocationType daoLocationType;

	private DAOLocationAttribute daoLocationAttribute;

	private DAOLocationAttributeType daoLocationAttributeType;
	
	public LocationServiceImpl(ServiceContext sc, DAOLocation daoLocation, DAOLocationType daoLocationType, DAOLocationAttribute daoLocationAttribute, DAOLocationAttributeType daoLocationAttributeType){
		this.setSc(sc);
		this.daoLocation = daoLocation;
		this.daoLocationType = daoLocationType;
		this.daoLocationAttribute = daoLocationAttribute;
		this.daoLocationAttributeType = daoLocationAttributeType;
	}

	public Location findLocationById(int cityId, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.findById(cityId, isreadonly, mappingsToJoin);
	}
	
	public Location findLocationByName(String name, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.findByName(name, isreadonly, mappingsToJoin);
	}
	
	public Location findLocationByShortName(String shortName, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.findByShortName(shortName, isreadonly, mappingsToJoin);
	}
	
	public Location findLocationByFullName(String fullName, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.findByFullName(fullName, isreadonly, mappingsToJoin);
	}
	
	public Location findLocationByDescription(String description, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.findByDescription(description, isreadonly, mappingsToJoin);
	}
	
	
	public Location findLocationByIdentifier(String identifier, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.findByIdentifier(identifier, isreadonly, mappingsToJoin);
	}
	
	
	public Location findLocationByLatitude(String latitude, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.findByLatitude(latitude, isreadonly, mappingsToJoin);
	}
	

	public Location findLocationByLongitude(String longitude, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.findByLongitude(longitude, isreadonly, mappingsToJoin);
	}

	
	public List<Location> findLocationByVoided(boolean voided, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.findLocationByVoided(voided, isreadonly, mappingsToJoin);
	}

	
	public List<Location> getAllLocation(boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.getAll(isreadonly, mappingsToJoin);
	}
	

	public LocationType findLocationTypeById(int id, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationType.findById(id, isreadonly, mappingsToJoin);
	}
	
	List<Location> getChildLocations(Location location, boolean isreadonly, String[] mappingsToJoin)
	{
		return daoLocation.getChildLocations(location, false, null);
	}


	public LocationType findLocationTypeByName(String name, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationType.findByName(name, isreadonly, mappingsToJoin);
	}


	public LocationType findLocationTypeByLevel(int level, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationType.findByLevel(level, isreadonly, mappingsToJoin);
	}

	
	public LocationType findLocationTypeByDescription(String description, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationType.findByDescription(description, isreadonly, mappingsToJoin);
	}

	
	public List<LocationType> getAllLocationType(boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationType.getAll(isreadonly, mappingsToJoin);
	}

	
	public LocationAttribute findLocationAttributeById(int id, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttribute.findById(id, isreadonly, mappingsToJoin);
	}
	
	public LocationAttribute findLocationAttributeByValue(String value, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttribute.findByValue(value, isreadonly, mappingsToJoin);
	}

	
	public LocationAttribute findLocationAttributeByTypeName(String typeName, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttribute.findByTypeName(typeName, isreadonly, mappingsToJoin);
	}
	

	public LocationAttribute findLocationAttributeByTypeValue1(String typeValue1, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttribute.findByTypeValue1(typeValue1, isreadonly, mappingsToJoin);
	}

	
	public LocationAttribute findLocationAttributeByTypeValue2(String typeValue2, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttribute.findByTypeValue2(typeValue2, isreadonly, mappingsToJoin);
	}


	public List<LocationAttribute> findLocationAttributeByCriteria(Integer locationAttributeId, String value, String typeName, String typeValue1, String typeValue2, Integer locationId, Integer locationAttributeTypeId, int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttribute.findByCriteria(locationAttributeId, value, typeName, typeValue1, typeValue2, locationId, locationAttributeTypeId, firstResult, fetchsize, isreadonly, mappingsToJoin);
	}
	
	
	public List<LocationAttribute> getAllLocationAttribute(boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttribute.getAll(isreadonly, mappingsToJoin);
	}
	
	
	public LocationAttributeType findLocationAttributeTypeById(int id, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttributeType.findById(id, isreadonly, mappingsToJoin);
	}

	
	public LocationAttributeType findLocationAttributeTypeByName(String Name, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttributeType.findByName(Name, isreadonly, mappingsToJoin);
	}

	
	public LocationAttributeType findLocationAttributeTypeByDescription(String description, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttributeType.findByDescription(description, isreadonly, mappingsToJoin);
	}

	
	public LocationAttributeType findLocationAttributeTypeByCategory(String category, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttributeType.findByCategory(category, isreadonly, mappingsToJoin);
	}


	public List<LocationAttributeType> getAllLocationAttributeType(boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttributeType.getAll(isreadonly, mappingsToJoin);
	}

	
	public Serializable addLocation(Location location) {
		return daoLocation.save(location);
	}

	
	public Serializable addLocationType(LocationType locationType) {
		return daoLocationType.save(locationType);
	}

	
	public Serializable addLocationAttribute(LocationAttribute locationAttribute) {
		return daoLocationAttribute.save(locationAttribute);
	}

	
	public Serializable addLocationAttributeType(LocationAttributeType locationAttributeType) {
		return daoLocationAttributeType.save(locationAttributeType);
	}

	
	public void updateLocation(Location location) {
		daoLocation.update(location);
	}

	
	public void updateLocationType(LocationType locationType) {
		daoLocationType.update(locationType);
	}

	
	public void updateLocationAttribute(LocationAttribute locationAttribute) {
		daoLocationAttribute.update(locationAttribute);
	}


	public void updateLocationAttributeType(LocationAttributeType locationAttributeType) {
		daoLocationAttributeType.update(locationAttributeType);
	}

	
	public void deleteLocation(Location location) {
		daoLocation.delete(location);
	}

	
	public void deleteLocationType(LocationType locationType) {
		daoLocationType.delete(locationType);
	}


	public void deleteLocationAttribute(LocationAttribute locationAttribute) {
		daoLocationAttribute.delete(locationAttribute);
	}


	public void deleteLocationAttributeType(LocationAttributeType locationAttributeType) {
		daoLocationAttributeType.delete(locationAttributeType);
	}

	
	public void addOrUpdateLocation(Location location) {
		daoLocation.saveOrUpdate(location);
	}

	
	public void addOrUpdateLocationType(LocationType locationType) {
		daoLocationType.saveOrUpdate(locationType);
	}

	
	public void addOrUpdateLocationAttribute(LocationAttribute locationAttribute) {
		daoLocationAttribute.saveOrUpdate(locationAttribute);
	}


	public void addOrUpdateLocationAttributeType(LocationAttributeType locationAttributeType) {
		daoLocationAttributeType.saveOrUpdate(locationAttributeType);
	}

	public ServiceContext getSc() {
		return sc;
	}

	public void setSc(ServiceContext sc) {
		this.sc = sc;
	}
}
