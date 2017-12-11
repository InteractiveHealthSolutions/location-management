package com.ihs.locationmanagement.web.controller;

import java.util.ArrayList;

import javax.management.InstanceAlreadyExistsException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import com.ihs.locationmanagement.api.context.*;
import com.ihs.locationmanagement.api.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyController {
	
	@RequestMapping("/locations")
	public ModelAndView tabletree() throws InstanceAlreadyExistsException {
		Context.instantiate(null);
		ServiceContext sc = Context.getServices();	
		ModelAndView model = new ModelAndView("tabletree");
		try {
			JSONArray voidedLocationNodes = new JSONArray();
			JSONArray locationNodes = new JSONArray();
			JSONArray locationTypeNodes = new JSONArray();
			JSONArray locationAttributeNodes = new JSONArray();
			JSONArray locationAttributeTypeNodes = new JSONArray();
			ArrayList<Location> voidedLocations = (ArrayList<Location>) sc.getLocationService().findLocationByVoided(true, true, null);
			ArrayList<Location> locations = (ArrayList<Location>) sc.getLocationService().getAllLocation(true, null);
			ArrayList<LocationType> locationTypes = (ArrayList<LocationType>) sc.getLocationService().getAllLocationType(true, null);		
			ArrayList<LocationAttribute> locationAttributes = (ArrayList<LocationAttribute>) sc.getLocationService().getAllLocationAttribute(true, null);
			ArrayList<LocationAttributeType> locationAttributeTypes = (ArrayList<LocationAttributeType>) sc.getLocationService().getAllLocationAttributeType(true, null);

			if(voidedLocations == null) {}
			else {
				JSONObject voidedLocationNode = new JSONObject(); voidedLocationNode.put("id", 0); voidedLocationNode.put("description", ""); voidedLocationNode.put("name", "VOIDED LOCATIONS"); voidedLocationNode.put("shortName", "VOIDED LOCATIONS"); voidedLocationNode.put("fullName", "VOIDED LOCATIONS"); voidedLocationNode.put("pId", 0); voidedLocationNode.put("pName", ""); voidedLocationNode.put("type", 5); voidedLocationNode.put("typeName", "REFUSED"); voidedLocationNode.put("otherIdentifier", "VOIDED LOCATIONS"); voidedLocationNode.put("latitude", ""); voidedLocationNode.put("longitude", ""); voidedLocationNode.put("voided", true); voidedLocationNode.put("dateVoided", ""); voidedLocationNode.put("voidReason", ""); voidedLocationNode.put("open", true); voidedLocationNodes.put(voidedLocationNode);
				for(int i=0; i<voidedLocations.size(); i++) {
					if(voidedLocations.get(i) == null) {}
					else {
						if(!(voidedLocations.get(i).getVoided())) { }
						else {
							JSONObject locationNode = new JSONObject();
							locationNode.put("id", voidedLocations.get(i).getLocationId());
							if(voidedLocations.get(i).getName() == null) { locationNode.put("name", ""); } else { locationNode.put("name", voidedLocations.get(i).getName()); }
							if(voidedLocations.get(i).getShortName() == null) { locationNode.put("shortName", ""); } else { locationNode.put("shortName", voidedLocations.get(i).getShortName()); }
							if(voidedLocations.get(i).getFullName() == null) { locationNode.put("fullName", ""); } else { locationNode.put("fullName", voidedLocations.get(i).getFullName()); }
							locationNode.put("pId", 0); 
							locationNode.put("pName", "VOIDED LOCATIONS");
							if(voidedLocations.get(i).getLocationType() == null) { locationNode.put("type", 0); locationNode.put("typeName", ""); }
							else { 
								for(int j=0; j<locationTypes.size(); j++) { 
									if(locationTypes.get(j).getLocationTypeId() == voidedLocations.get(i).getLocationType().getLocationTypeId()) { 
										locationNode.put("type", voidedLocations.get(i).getLocationType().getLocationTypeId()); 
										locationNode.put("typeName", locationTypes.get(j).getTypeName()); 
									}
								} 
							}
							if(voidedLocations.get(i).getDescription() == null) { locationNode.put("description", ""); } else { locationNode.put("description", voidedLocations.get(i).getDescription()); }
							if(voidedLocations.get(i).getOtherIdentifier() == null) { locationNode.put("otherIdentifier", ""); } else { locationNode.put("otherIdentifier", voidedLocations.get(i).getOtherIdentifier()); }
							if(voidedLocations.get(i).getLatitude() == null) { locationNode.put("latitude", ""); } else { locationNode.put("latitude", voidedLocations.get(i).getLatitude()); }
							if(voidedLocations.get(i).getLongitude() == null) { locationNode.put("longitude", ""); } else { locationNode.put("longitude", voidedLocations.get(i).getLongitude()); }
							locationNode.put("voided", voidedLocations.get(i).getVoided());
							//locationNode.put("voidedBy", voidedLocations.get(i).getVoidedBy().toString());
							if(voidedLocations.get(i).getDateVoided() == null) { locationNode.put("dateVoided", ""); } else { locationNode.put("dateVoided", voidedLocations.get(i).getDateVoided()); }
							if(voidedLocations.get(i).getVoidReason() == null) { locationNode.put("voidReason", ""); } else { locationNode.put("voidReason", voidedLocations.get(i).getVoidReason()); }
							locationNode.put("open", true);
							voidedLocationNodes.put(locationNode);
						} 
					}
				}
			}
			
			if(locations == null) {}
			else {
				for(int i=0; i<locations.size(); i++) {
					if(locations.get(i) == null) {}
					else {
						if(locations.get(i).getVoided()) {}//DELETED LOCATION
						else {
							JSONObject locationNode = new JSONObject();
							locationNode.put("id", locations.get(i).getLocationId());
							if(locations.get(i).getName() == null) { locationNode.put("name", ""); } else { locationNode.put("name", locations.get(i).getName()); }
							if(locations.get(i).getShortName() == null) { locationNode.put("shortName", ""); } else { locationNode.put("shortName", locations.get(i).getShortName()); }
							if(locations.get(i).getFullName() == null) { locationNode.put("fullName", ""); } else { locationNode.put("fullName", locations.get(i).getFullName()); }
							if(locations.get(i).getParentLocation() == null) { locationNode.put("pId", 0); locationNode.put("pName", ""); }
							else { locationNode.put("pId", locations.get(i).getParentLocation().getLocationId()); locationNode.put("pName", locations.get(i).getParentLocation().getName()); }
							if(locations.get(i).getLocationType() == null) { locationNode.put("type", 0); locationNode.put("typeName", ""); }
							else { 
								for(int j=0; j<locationTypes.size(); j++) { 
									if(locationTypes.get(j).getLocationTypeId() == locations.get(i).getLocationType().getLocationTypeId()) { 
										locationNode.put("type", locations.get(i).getLocationType().getLocationTypeId()); 
										locationNode.put("typeName", locationTypes.get(j).getTypeName()); 
									}
								} 
							}
							if(locations.get(i).getDescription() == null) { locationNode.put("description", ""); } else { locationNode.put("description", locations.get(i).getDescription()); }
							if(locations.get(i).getOtherIdentifier() == null) { locationNode.put("otherIdentifier", ""); } else { locationNode.put("otherIdentifier", locations.get(i).getOtherIdentifier()); }
							if(locations.get(i).getLatitude() == null) { locationNode.put("latitude", ""); } else { locationNode.put("latitude", locations.get(i).getLatitude()); }
							if(locations.get(i).getLongitude() == null) { locationNode.put("longitude", ""); } else { locationNode.put("longitude", locations.get(i).getLongitude()); }
							locationNode.put("voided", locations.get(i).getVoided());
							//locationNode.put("voidedBy", locations.get(i).getVoidedBy().toString());
							if(locations.get(i).getDateVoided() == null) { locationNode.put("dateVoided", ""); } else { locationNode.put("dateVoided", locations.get(i).getDateVoided()); }
							if(locations.get(i).getVoidReason() == null) { locationNode.put("voidReason", ""); } else { locationNode.put("voidReason", locations.get(i).getVoidReason()); }
							locationNode.put("open", true);
							locationNodes.put(locationNode);
						} 
					}
				}
			}

			if(locationTypes == null) {}
			else {
				for(int i=0; i<locationTypes.size(); i++) {
					if(locationTypes.get(i) == null) {}
					else {
						JSONObject locationTypeNode = new JSONObject();
						locationTypeNode.put("id", locationTypes.get(i).getLocationTypeId());
						if(locationTypes.get(i).getTypeName() == null) { locationTypeNode.put("name", 0); } else { locationTypeNode.put("name", locationTypes.get(i).getTypeName()); }
						if(locationTypes.get(i).getLevel() == null) { locationTypeNode.put("level", 0); } else { locationTypeNode.put("level", locationTypes.get(i).getLevel()); }
						if(locationTypes.get(i).getDescription() == null) { locationTypeNode.put("description", ""); } else { locationTypeNode.put("description", locationTypes.get(i).getDescription()); }
						locationTypeNodes.put(locationTypeNode);
					}
				}
			}
			
			if(locationAttributes == null) {}
			else {
				for(int i=0; i<locationAttributes.size(); i++) {
					if(locationAttributes.get(i) == null) {}
					else {
						JSONObject locationAttributeNode = new JSONObject();
						locationAttributeNode.put("id", locationAttributes.get(i).getLocationAttributeId());
						locationAttributeNode.put("typeId", locationAttributes.get(i).getLocationAttributeTypeId());
						locationAttributeNode.put("locationId", locationAttributes.get(i).getLocationId());
						if(locationAttributes.get(i).getValue() == null) { locationAttributeNode.put("value", 0); } else { locationAttributeNode.put("value", locationAttributes.get(i).getValue()); }
						if(locationAttributes.get(i).getTypeName() == null) { locationAttributeNode.put("typeName", 0); } else { locationAttributeNode.put("typeName", locationAttributes.get(i).getTypeName()); }
						if(locationAttributes.get(i).getTypeValue1() == null) { locationAttributeNode.put("typeValue1", 0); } else { locationAttributeNode.put("typeValue1", locationAttributes.get(i).getTypeValue1()); }
						if(locationAttributes.get(i).getTypeValue2() == null) { locationAttributeNode.put("typeValue2", 0); } else { locationAttributeNode.put("typeValue2", locationAttributes.get(i).getTypeValue2()); }
						locationAttributeNodes.put(locationAttributeNode);
					}
				}
			}

			if(locationAttributeTypes == null) {}
			else {
				for(int i=0; i<locationAttributeTypes.size(); i++) {
					if(locationAttributeTypes.get(i) == null) {}
					else {
						JSONObject locationAttributeTypeNode = new JSONObject();
						locationAttributeTypeNode.put("id", locationAttributeTypes.get(i).getLocationAttributeTypeId());
						if(locationAttributeTypes.get(i).getAttributeName() == null) { locationAttributeTypeNode.put("name", 0); } else { locationAttributeTypeNode.put("name", locationAttributeTypes.get(i).getAttributeName()); }
						if(locationAttributeTypes.get(i).getDisplayName() == null) { locationAttributeTypeNode.put("displayName", 0); } else { locationAttributeTypeNode.put("displayName", locationAttributeTypes.get(i).getDisplayName()); }
						if(locationAttributeTypes.get(i).getDescription() == null) { locationAttributeTypeNode.put("description", 0); } else { locationAttributeTypeNode.put("description", locationAttributeTypes.get(i).getDescription()); }
						if(locationAttributeTypes.get(i).getCategory() == null) { locationAttributeTypeNode.put("category", 0); } else { locationAttributeTypeNode.put("category", locationAttributeTypes.get(i).getCategory()); }
						locationAttributeTypeNodes.put(locationAttributeTypeNode);
					} 
				}
			}
			
			model.addObject("voidedLocationNodes", voidedLocationNodes);
			model.addObject("locationNodes", locationNodes);
			model.addObject("locationTypeNodes", locationTypeNodes);
			model.addObject("locationAttributeNodes", locationAttributeNodes);
			model.addObject("locationAttributeTypeNodes", locationAttributeTypeNodes);

			sc.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			sc.rollbackTransaction();
		} finally {
			sc.closeSession();
		}
		return model;
	}
}
