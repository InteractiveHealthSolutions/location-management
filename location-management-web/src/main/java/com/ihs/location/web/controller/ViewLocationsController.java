package com.ihs.location.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.ird.unfepi.context.LocationContext;
import org.ird.unfepi.context.LocationServiceContext;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LocationAttribute;
import org.ird.unfepi.model.LocationAttributeType;
import org.ird.unfepi.model.LocationType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/tree")
public class ViewLocationsController {

	@RequestMapping(method={RequestMethod.GET})
	public ModelAndView handleRequest1(HttpServletRequest req,	HttpServletResponse resp) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		LocationServiceContext sc = LocationContext.getServices();

		try{
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
										locationNode.put("isEditable", locationTypes.get(j).getIsEditable()); 
										locationNode.put("level", locationTypes.get(j).getLevel()); 
									}
								} 
							}
							if(voidedLocations.get(i).getDescription() == null) { locationNode.put("description", ""); } else { locationNode.put("description", voidedLocations.get(i).getDescription()); }
							if(voidedLocations.get(i).getOtherIdentifier() == null) { locationNode.put("otherIdentifier", ""); } else { locationNode.put("otherIdentifier", voidedLocations.get(i).getOtherIdentifier()); }
							if(voidedLocations.get(i).getLatitude() == null) { locationNode.put("latitude", ""); } else { locationNode.put("latitude", voidedLocations.get(i).getLatitude()); }
							if(voidedLocations.get(i).getLongitude() == null) { locationNode.put("longitude", ""); } else { locationNode.put("longitude", voidedLocations.get(i).getLongitude()); }
							locationNode.put("voided", voidedLocations.get(i).getVoided());
							if(voidedLocations.get(i).getDateVoided() == null) { locationNode.put("dateVoided", ""); } else { locationNode.put("dateVoided", voidedLocations.get(i).getDateVoided()); }
							if(voidedLocations.get(i).getVoidReason() == null) { locationNode.put("voidReason", ""); } else { locationNode.put("voidReason", voidedLocations.get(i).getVoidReason()); }
							locationNode.put("open", true);
							voidedLocationNodes.put(locationNode);
						} 
					}
				}
			}
			
			for(int i=0; i<locations.size(); i++) {
				if(locations.get(i).getVoided()) {}//DELETED LOCATION
				else if(locations.get(i).getLocationType().getLevel() != null 
						&& locations.get(i).getLocationType().getLevel() < 0){
					// skip
				}
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
								locationNode.put("isEditable", locationTypes.get(j).getIsEditable()); 
								locationNode.put("level", locationTypes.get(j).getLevel()); 
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
					
					locationNode.put("attributes", new JSONArray());
					
					List<LocationAttribute> locationAttibL = sc.getLocationService().findLocationAttributeByCriteria("text", null, locations.get(i).getLocationId(), null, 0, 1000, true, null, null);
					for (LocationAttribute locationAttribute : locationAttibL) {
						JSONObject locationAttributeNode = new JSONObject();
						locationAttributeNode.put("id", locationAttribute.getLocationAttributeId());
						locationAttributeNode.put("typeId", locationAttribute.getLocationAttributeTypeId());
						locationAttributeNode.put("attributeType", locationAttribute.getLocationAttributeType().getDisplayName());
						locationAttributeNode.put("locationId", locationAttribute.getLocationId());
						locationAttributeNode.put("value", locationAttribute.getValue()); 
						locationAttributeNode.put("typeName", locationAttribute.getTypeName());
						
						locationNode.getJSONArray("attributes").put(locationAttributeNode);
					}
					
					locationNodes.put(locationNode);
				} 
			}

			if(locationTypes == null) {}
			else {
				for(int i=0; i<locationTypes.size(); i++) {
					if(locationTypes.get(i) == null) {}
					else {
						JSONObject locationTypeNode = new JSONObject();
						locationTypeNode.put("id", locationTypes.get(i).getLocationTypeId());
						locationTypeNode.put("isEditable", locationTypes.get(i).getIsEditable());
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
						
						locationAttributeTypeNode.put("name", locationAttributeTypes.get(i).getAttributeName());
						locationAttributeTypeNode.put("displayName", locationAttributeTypes.get(i).getDisplayName());
						locationAttributeTypeNode.put("description", locationAttributeTypes.get(i).getDescription());
						locationAttributeTypeNode.put("category", locationAttributeTypes.get(i).getCategory());
						locationAttributeTypeNode.put("dataType", locationAttributeTypes.get(i).getDataType());
						
						locationAttributeTypeNodes.put(locationAttributeTypeNode);
					} 
				}
			}
			
			model.put("locationTypes", locationTypes);
			model.put("voidedLocationNodes", voidedLocationNodes);
			model.put("locationNodes", locationNodes);
			model.put("locationTypeNodes", locationTypeNodes);
			model.put("locationAttributeNodes", locationAttributeNodes);
			model.put("locationAttributeTypeNodes", locationAttributeTypeNodes);
			return new ModelAndView("locationtree", "model", model);
		}
		catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView("exception");
		}
		finally{
			sc.closeSession();
		}
	}
}

