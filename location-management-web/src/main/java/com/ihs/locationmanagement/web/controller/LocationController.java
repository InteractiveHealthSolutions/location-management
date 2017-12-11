package com.ihs.locationmanagement.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.management.InstanceAlreadyExistsException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.ihs.locationmanagement.api.context.*;
import com.ihs.locationmanagement.api.model.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/location")
public class LocationController {

	@RequestMapping(value = "/newlocation", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody String addlocation(@RequestParam("name")String name, @RequestParam("shortName")String shortName, @RequestParam("fullName")String fullName, @RequestParam("pName")String parentName, @RequestParam("description")String description, @RequestParam("otherIdentifier")String otherIdentifier, @RequestParam("typeName")String typeName, @RequestParam("latitude")String latitude, @RequestParam("longitude")String longitude) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
			Integer id = null;
			Location location = sc.getLocationService().findLocationByName(name, true, null);
			Location parentLocation = sc.getLocationService().findLocationByName(parentName, true, null);
			LocationType locationType = sc.getLocationService().findLocationTypeByName(typeName, true, null);
			if(location == null) {
				location = new Location();
				location.setName(name);
				location.setShortName(shortName);
				location.setFullName(fullName);
				location.setDescription(description);
				location.setOtherIdentifier(otherIdentifier);
	    		location.setLatitude(latitude);
	    		location.setLongitude(longitude);
	    		location.setVoided(false);
				if(parentLocation == null) { location.setParentLocation(null); }
				else { location.setParentLocation(parentLocation); }
	    		if(locationType == null) { locationType = new LocationType(); locationType.setTypeName(typeName); sc.getLocationService().addLocationType(locationType); location.setLocationType(locationType); }
	    		else { location.setLocationType(locationType); }
	    		Serializable locationIdd = sc.getLocationService().addLocation(location); 
	    		id = Integer.parseInt(locationIdd.toString());
	    		status = "success";
	    		message = "SUCCESS!";
	    		returnStr = "LOCATION '" + location.getName() + "' CREATED SUCCESSFULLY<br/>";
			}
			else {
				id = location.getLocationId(); 
	    		status = "info";
	    		message = "INFO:";
	    		returnStr = "LOCATION '" + location.getName() + "' ALREADY EXISTS<br/>";
			}		
			sc.commitTransaction();
			result.put("status", status);
			result.put("message", message);
			result.put("returnStr", returnStr);
			result.put("id", id);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}

	@RequestMapping(value = "/newlocationtype", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody String addlocationType(@RequestParam("name")String name, @RequestParam("level")String level, @RequestParam("description")String description) throws InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
			Integer id = null;
			LocationType locationType = sc.getLocationService().findLocationTypeByName(name, true, null);
			if(locationType == null) {
				locationType = new LocationType();
				locationType.setTypeName(name);
				if(level.equals("")) { locationType.setLevel(0); }
				else { locationType.setLevel(strToInt(level)); }
				locationType.setDescription(description);
				Serializable locationTypeId = sc.getLocationService().addLocationType(locationType); 
	    		id = Integer.parseInt(locationTypeId.toString());
	    		status = "success";
	    		message = "SUCCESS!";
	    		returnStr = "LOCATION TYPE '" + locationType.getTypeName() + "' CREATED SUCCESSFULLY<br/>";
			}
			else {
				id = locationType.getLocationTypeId();
	    		status = "info";
	    		message = "INFO:";
	    		returnStr = "LOCATION TYPE '" + locationType.getTypeName() + "' ALREADY EXISTS<br/>";
			}
			sc.commitTransaction();
			result.put("status", status);
			result.put("message", message);
			result.put("returnStr", returnStr);
			result.put("id", id);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}	

	@RequestMapping(value = "/newlocationattributetype", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody String addlocationattributetype(@RequestParam("name")String locationAttributeTypeName, @RequestParam("displayName")String locationAttributeTypeDisplayName, @RequestParam("description")String locationAttributeTypeDescription, @RequestParam("category")String locationAttributeTypeCategory) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
			Integer id = null;
			LocationAttributeType locationAttributeType = sc.getLocationService().findLocationAttributeTypeByName(locationAttributeTypeName, true, null);
			if(locationAttributeType == null) {
				locationAttributeType = new LocationAttributeType();
				locationAttributeType.setAttributeName(locationAttributeTypeName);
				locationAttributeType.setDisplayName(locationAttributeTypeDisplayName);
				locationAttributeType.setDescription(locationAttributeTypeDescription);
				locationAttributeType.setCategory(locationAttributeTypeCategory);
				Serializable locationAttributeTypeId = sc.getLocationService().addLocationAttributeType(locationAttributeType); 
	    		id = Integer.parseInt(locationAttributeTypeId.toString());
	    		status = "success";
	    		message = "SUCCESS!";
	    		returnStr = "LOCATION ATTRIBUTE TYPE '" + locationAttributeType.getAttributeName() + "' CREATED SUCCESSFULLY<br/>";
			}
			else {
				id = locationAttributeType.getLocationAttributeTypeId();
	    		status = "info";
	    		message = "INFO:";
	    		returnStr = "LOCATION ATTRIBUTE TYPE '" + locationAttributeType.getAttributeName() + "' ALREADY EXISTS<br/>";
			}
			sc.commitTransaction();
			result.put("status", status);
			result.put("message", message);
			result.put("returnStr", returnStr);
			result.put("id", id);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}	

	@RequestMapping(value = "/newlocationattribute", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody String addlocationattribute(@RequestParam("parent")String locationParent, @RequestParam("array")String attributes) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
			ArrayList<Integer> ids = new ArrayList<Integer>();
			ArrayList<LocationAttribute> locationAttributes = new ArrayList<LocationAttribute>();
			JSONArray arr = new JSONArray(attributes);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject jsonArr = new JSONObject(arr.get(i).toString());
				LocationAttribute locationAttribute = new LocationAttribute();
				locationAttribute.setLocationId(strToInt(locationParent));
				locationAttribute.setLocationAttributeTypeId(strToInt(jsonArr.get("locationAttributeTypeId").toString()));
				locationAttribute.setValue(jsonArr.get("locationAttributeTypeValue").toString());
				locationAttribute.setTypeName(jsonArr.get("type").toString());
				if(jsonArr.get("type").toString().equals("None")) { }
				else if(jsonArr.get("type").toString().equals("Time_Bound")) {
					locationAttribute.setTypeValue1(jsonArr.get("value1").toString());
					locationAttribute.setTypeValue2(jsonArr.get("value2").toString());
				}
				else {
					locationAttribute.setTypeValue1(jsonArr.get("value1").toString());
					locationAttribute.setTypeValue2(jsonArr.get("value1").toString());
				}
				locationAttributes.add(locationAttribute);
			}
			for (int i = 0; i < locationAttributes.size(); i++) {
				ids.add((Integer) sc.getLocationService().addLocationAttribute(locationAttributes.get(i)));
			}
			sc.commitTransaction();
    		status = "success";
    		message = "SUCCESS!";
			returnStr =  "New Attribute Created";
			result.put("status", status);
			result.put("message", message);
			result.put("returnStr", returnStr);
			result.put("id", ids);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}
	
	@RequestMapping(value = "/updatelocation", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody String updatelocation(@RequestParam("name")String name, @RequestParam("shortName")String shortName, @RequestParam("fullName")String fullName, @RequestParam("pName")String pName, @RequestParam("description")String description, @RequestParam("otherIdentifier")String otherIdentifier, @RequestParam("typeName")String typeName, @RequestParam("latitude")String latitude, @RequestParam("longitude")String longitude) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		String lastPid = "";
		try {
			Integer id = null;
			Location location = sc.getLocationService().findLocationByName(name, false, null);
			Location parentLocation = sc.getLocationService().findLocationByName(pName, false, null);
			LocationType locationType = sc.getLocationService().findLocationTypeByName(typeName, false, null);

			if(location.getParentLocation() != null) { lastPid = location.getParentLocation().getLocationId().toString(); } else { lastPid = "0"; }
			id = location.getLocationId();
			location.setLocationId(id);
			location.setName(name);
			location.setShortName(shortName);
			location.setFullName(fullName);
	
			if(parentLocation == null) { returnStr = "LOCATION PARENT '" + pName + "' DOES NOT EXIST.<br/>"; location.setParentLocation(null); }
			else { location.setParentLocation(parentLocation); }
			
			if(locationType == null) { returnStr = "LOCATION TYPE '" + typeName + "' DOES NOT EXIST.<br/>"; }
			else { location.setLocationType(locationType); }
			
			location.setDescription(description);
			location.setOtherIdentifier(otherIdentifier);

			location.setLatitude(latitude);
			location.setLongitude(longitude);
			
			sc.getLocationService().updateLocation(location);
			sc.commitTransaction();
			
			JSONArray locationNodes = new JSONArray();
			ArrayList<Location> locations = (ArrayList<Location>) sc.getLocationService().getAllLocation(true, null);
			ArrayList<LocationType> locationTypes = (ArrayList<LocationType>) sc.getLocationService().getAllLocationType(true, null);		
			if(locations == null) {}
			else {
				for(int i=0; i<locations.size(); i++) {
					if(locations.get(i) == null) {}
					else {
						if(locations.get(i).getVoided()) { }
						else {
							JSONObject locationNode = new JSONObject();
							locationNode.put("id", locations.get(i).getLocationId());
							if(locations.get(i).getName() == null) { locationNode.put("name", ""); } else { locationNode.put("name", locations.get(i).getName()); }
							if(locations.get(i).getShortName() == null) { locationNode.put("shortName", ""); } else { locationNode.put("shortName", locations.get(i).getShortName()); }
							if(locations.get(i).getFullName() == null) { locationNode.put("fullName", ""); } else { locationNode.put("fullName", locations.get(i).getFullName()); }
							if(locations.get(i).getParentLocation() == null) { locationNode.put("pId", 0); locationNode.put("pName", ""); }
							else { locationNode.put("pId", locations.get(i).getParentLocation().getLocationId()); locationNode.put("pName", locations.get(i).getParentLocation().getName()); }
							if(locations.get(i).getLocationType() == null) { locationNode.put("type", 0); locationNode.put("typeName", ""); }
							else { for(int j=0; j<locationTypes.size(); j++) { if(locationTypes.get(j).getLocationTypeId() == locations.get(i).getLocationType().getLocationTypeId()) { locationNode.put("type", locations.get(i).getLocationType().getLocationTypeId()); locationNode.put("typeName", locationTypes.get(j).getTypeName()); } } }
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
			status = "success";
    		message = "SUCCESS!";
    		returnStr = "LOCATION '" + location.getName() + "' UPDATED SUCCESSFULLY.";
			result.put("id", id);
			result.put("lastPid", lastPid);
			result.put("status", status);
			result.put("message", message);
			result.put("returnStr", returnStr);
			result.put("locationNodes", locationNodes);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}

	@RequestMapping(value = "/updatelocationtype", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody String updatelocationType(@RequestParam("name")String name, @RequestParam("level")String level, @RequestParam("description")String description) throws InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
			Integer id = null;
			LocationType locationType = sc.getLocationService().findLocationTypeByName(name, false, null);
			id = locationType.getLocationTypeId();
			locationType.setLocationTypeId(id);
			locationType.setTypeName(name);
			if(level.equals("")) { locationType.setLevel(0); }
			else { locationType.setLevel(strToInt(level)); }
			locationType.setDescription(description);
			sc.getLocationService().updateLocationType(locationType);
			sc.commitTransaction();
			status = "success";
			message = "SUCCESS!";
			returnStr = "LOCATION TYPE '" + locationType.getTypeName() + "' UPDATED SUCCESSFULLY.";
			result.put("status", status);
			result.put("message", message);
			result.put("returnStr", returnStr);
			result.put("id", id);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}	

	@RequestMapping(value = "/updatelocationattributetype", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody String updatelocationAttributeType(@RequestParam("name")String name, @RequestParam("displayName")String displayName, @RequestParam("description")String description, @RequestParam("category")String category) throws InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
			Integer id = null;
			LocationAttributeType locationAttributeType = sc.getLocationService().findLocationAttributeTypeByName(name, false, null);
			id = locationAttributeType.getLocationAttributeTypeId();
			locationAttributeType.setLocationAttributeTypeId(id);
			locationAttributeType.setAttributeName(name);
			locationAttributeType.setDisplayName(displayName);
			locationAttributeType.setDescription(description);
			locationAttributeType.setCategory(category);
			sc.getLocationService().updateLocationAttributeType(locationAttributeType); 
			sc.commitTransaction();
			status = "success";
			message = "SUCCESS!";
			returnStr = "LOCATION ATTRIBUTE TYPE '" + locationAttributeType.getAttributeName() + "' UPDATED SUCCESSFULLY.";
			result.put("status", status);
			result.put("message", message);
			result.put("returnStr", returnStr);
			result.put("id", id);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}	
	
	@RequestMapping(value = "/newCsv", method = RequestMethod.POST)
	public @ResponseBody String updateCsv(@RequestParam("csvData")String csvData) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
        String s1 = ""; 
        String s2 = ""; 
		String status = "";
		String message = "";
		String returnStr = "";
	    ArrayList<String> locationParent = new ArrayList<String>();
	    ArrayList<String> locationParentFound = new ArrayList<String>();
	    ArrayList<String> locationParentNotFound = new ArrayList<String>();	
	    ArrayList<String> locationCreated = new ArrayList<String>();	
	    ArrayList<String> locationExists = new ArrayList<String>();	
	    ArrayList<String> locationTypeCreated = new ArrayList<String>();	
	    ArrayList<String> locationTypeExists = new ArrayList<String>();	
	    JSONObject result = new JSONObject();
		try {
			// PARSE CSV DATA TO LOCATION OBJECTS			
			ArrayList<Location> csvLocations = new ArrayList<Location>();
			ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
			JSONArray arr = new JSONArray(csvData);
		    for(int i = 0 ; i < arr.length(); i++){
		    	ArrayList<String> array = new ArrayList<String>();
		        JSONArray arrr = arr.getJSONArray(i);
		        for(int j = 0; j < arrr.length(); j++){ array.add(arrr.get(j).toString()); }
		        list.add(array);
		    }
		    ArrayList<String> head = list.get(0);
		    for (int i = 1; i < list.size(); i++) {
		    	ArrayList<String> val = list.get(i);
				if(head.size()==val.size()) {
		            Location l = new Location();
					for (int j = 0; j < head.size(); j++) {
			        	if(head.get(j).trim().toLowerCase().equals("name")) { l.setName(val.get(j)); }
			        	else if(head.get(j).trim().toLowerCase().equals("short_name")) { l.setShortName(val.get(j)); }
			        	else if(head.get(j).trim().toLowerCase().equals("full_name")) { l.setFullName(val.get(j)); }
			        	else if(head.get(j).trim().toLowerCase().equals("parent")) {
			        		if(val.get(j).equals("") || val.get(j).equals("NULL")) { } 
			        		else {
			        			Location parent = new Location(); 
			        			parent.setName(val.get(j)); 
			        			l.setParentLocation(parent);
			        		}
		        		}
			        	else if(head.get(j).trim().toLowerCase().equals("type")) { 
			        		LocationType lt = sc.getLocationService().findLocationTypeByName(val.get(j), true, null); 
			        		if(lt == null) { 
			        			lt = new LocationType();
			        			lt.setTypeName(val.get(j));
			        			sc.getLocationService().addLocationType(lt); 
			        			l.setLocationType(lt);
			        			s1 += "LOCATION TYPE: '" + l.getLocationType().getTypeName() + "' CREATED SUCCESSFULLY<br/>"; 
			        			locationTypeCreated.add(l.getLocationType().getTypeName());
			        		}
			        		else {
			        			l.setLocationType(lt);
			        			s1 += "LOCATION TYPE '" + lt.getTypeName() + "' ALREADY EXISTS<br/>"; 
			        			locationTypeExists.add(l.getLocationType().getTypeName());
		        			}
		        		}
			        	else if(head.get(j).trim().toLowerCase().equals("identifier")) { l.setOtherIdentifier(val.get(j)); }
			        	else if(head.get(j).trim().toLowerCase().equals("description")) { l.setDescription(val.get(j)); }
			        	else if(head.get(j).trim().toLowerCase().equals("latitude")) { l.setLatitude(val.get(j)); }
			        	else if(head.get(j).trim().toLowerCase().equals("longitude")) { l.setLongitude(val.get(j)); }
					}
					csvLocations.add(l);
				}
			}
			
			// CHECK PARENT IF NULL OR NOT
		    ArrayList<String> locationName = new ArrayList<String>();
			for (Location l : csvLocations) {
	        	locationName.add(l.getName());
	        	if(l.getParentLocation() != null) { locationParent.add(l.getParentLocation().getName()); }
	        	else { locationParent.add("NULL"); }
			}
	        for (String locationParentName : locationParent) {
		        if(locationName.contains(locationParentName) || locationParentName.equals("NULL")) { locationParentFound.add(locationParentName); }
		        else {
		        	Location l = sc.getLocationService().findLocationByName(locationParentName, true, null);
					if(l == null) { locationParentNotFound.add(locationParentName); }
		        	else { locationParentFound.add(locationParentName); }
	        	}
			}
	        
		    // SAVE LOCATION, LOCATIONTYPE TO DATABASE
	        if(locationParent.size() == locationParentFound.size()) {
	        	for (Location l : csvLocations) {
	        		Location ll = sc.getLocationService().findLocationByName(l.getName(), true, null); 
	        		if(ll == null) { 
	        			ll = l;
	        			if(ll.getParentLocation() == null) { }
	        			else { 
	        				Location lp = sc.getLocationService().findLocationByName(ll.getParentLocation().getName(), true, null);
	        				if(lp != null) { ll.setParentLocation(lp); }
	        				else { System.out.println("PARENT LOCATION NULL ERROR"); }
	        			}
	        			sc.getLocationService().addLocation(ll);  
	        			s2 += "LOCATION '" + ll.getName() + "' CREATED SUCCESSFULLY<br/>"; 
	        			locationCreated.add(ll.getName());
	        		}
	        		else { 
	        			s2 += "LOCATION '" + ll.getName() + "' ALREADY EXISTS<br/>"; 
	        			locationExists.add(ll.getName()); 
	        		}
				}
        		if(s2.equals("")) { 
        			returnStr += "LOCATIONS CREATED<br/>"; 
        		}
	        	else { 
	        		//returnStr += s2; 
	        		if(!(locationExists.isEmpty())) {
	        			returnStr += "LOCATIONS " + (locationExists.toString()).replace("[", "'").replace("]", "'") + " ALREADY EXIST<br/>";
	        		}
	        		if(!(locationCreated.isEmpty()))  {
	        			returnStr += "LOCATIONS " + (locationCreated.toString()).replace("[", "'").replace("]", "'") + " CREATED SUCCESSFULLY<br/>";
	        		}
        		}
	        	
        		if(s1.equals("")) {
        			returnStr += "LOCATION TYPES CREATED<br/>"; 
    			}
	        	else { 
	        		//returnStr += s1; 
	        		if(!(locationTypeExists.isEmpty())) {
	        			returnStr += "LOCATION TYPES " + (locationTypeExists.toString()).replace("[", "'").replace("]", "'") + " ALREADY EXIST<br/>";
	        		}
	        		if(!(locationTypeCreated.isEmpty()))  {
	        			returnStr += "LOCATION TYPES " + (locationTypeCreated.toString()).replace("[", "'").replace("]", "'") + " CREATED SUCCESSFULLY<br/>";
	        		}
        		}

	        	// GENERATE TREE FROM DATABASE
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
				result.put("voidedLocationNodes", voidedLocationNodes);
	        	result.put("locationNodes", locationNodes);
				result.put("locationTypeNodes", locationTypeNodes);
				result.put("locationAttributeNodes", locationAttributeNodes);
				result.put("locationAttributeTypeNodes", locationAttributeTypeNodes);
	        }
	        else {
	        	returnStr = "PARENT LOCATION DOESN'T EXIST FOR:-";
		        for (int i = 0; i < locationParentNotFound.size(); i++) {
			        returnStr +=  "<br/>" + locationParentNotFound.get(i);
				}
				result.put("voidedLocationNodes", new JSONArray());
				result.put("locationNodes", new JSONArray());
				result.put("locationTypeNodes", new JSONArray());
				result.put("locationAttributeTypeNodes", new JSONArray());
	        }
	        status = "info";
	        message = "INFO:";
	        result.put("status", status);
	        result.put("message", message);
			result.put("returnStr", returnStr);
			sc.commitTransaction();
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}

	@RequestMapping(value = "/newCsvForLocAttrs", method = RequestMethod.POST)
	public @ResponseBody String updateCsvForLocAttrs(@RequestParam("csvData")String csvData) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
	    JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
	    try {
	    	// PARSE CSV DATA TO LOCATION ATTRIBUTE OBJECTS
	    	ArrayList<LocationAttribute> csvLocationAttributes = new ArrayList<LocationAttribute>();
			ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
			JSONArray arr = new JSONArray(csvData);
		    for(int i = 0 ; i < arr.length(); i++){
		    	ArrayList<String> array = new ArrayList<String>();
		        JSONArray arrr = arr.getJSONArray(i);
		        for(int j = 0; j < arrr.length(); j++){ array.add(arrr.get(j).toString()); }
		        list.add(array);
		    }
		    ArrayList<String> head = list.get(0);
		    for (int i = 1; i < list.size(); i++) {
			    ArrayList<String> val = list.get(i);
				if(head.size()==val.size()) {
					LocationAttribute locationAttribute = new LocationAttribute();
					for (int j = 0; j < head.size(); j++) {
						if(head.get(j).trim().toLowerCase().equals("parent")) { 
							Location location = sc.getLocationService().findLocationByName(val.get(j), true, null);
							if(location == null) { returnStr += "LOCATION '"+val.get(j)+"' DOES NOT EXIST.<br/>"; } 
							else { locationAttribute.setLocationId(location.getLocationId()); }
						}
						else if(head.get(j).trim().toLowerCase().equals("attribute_name")) { 
							String locationAttributeTypeName = "";
							String typeName = "";
							if(val.get(j).toUpperCase().contains("_ANUALLY")) { locationAttributeTypeName = val.get(j).substring(0, val.get(j).toUpperCase().replace("_ANUALLY", "").length()).replaceAll("_", " "); typeName = "Anually"; }
							else if(val.get(j).toUpperCase().contains("_ANUAL")) { locationAttributeTypeName = val.get(j).substring(0, val.get(j).toUpperCase().replace("_ANUAL", "").length()).replaceAll("_", " "); typeName = "Anually"; }
							else if(val.get(j).toUpperCase().contains("_MONTHLY")) { locationAttributeTypeName = val.get(j).substring(0, val.get(j).toUpperCase().replace("_MONTHLY", "").length()).replaceAll("_", " "); typeName = "Monthly"; }
							else if(val.get(j).toUpperCase().contains("_DAILY")) { locationAttributeTypeName = val.get(j).substring(0, val.get(j).toUpperCase().replace("_DAILY", "").length()).replaceAll("_", " "); typeName = "Daily"; }
							else if(val.get(j).toUpperCase().contains("_ANUALLY")) { locationAttributeTypeName = val.get(j).substring(0, val.get(j).toUpperCase().replace("_TIME_BOUND", "").length()).replaceAll("_", " "); typeName = "Time_Bound"; }
							else { locationAttributeTypeName = val.get(j).replaceAll("_", " "); typeName = "None"; }
							LocationAttributeType locationAttributeType = sc.getLocationService().findLocationAttributeTypeByName(locationAttributeTypeName, true, null);
							if(locationAttributeType == null) { 
								LocationAttributeType locAttrTy = new LocationAttributeType();
								locAttrTy.setAttributeName(locationAttributeTypeName);
								locAttrTy.setDisplayName(locationAttributeTypeName);
								locAttrTy.setDescription("");
								locAttrTy.setCategory("");
								Integer locAttrId = (Integer) sc.getLocationService().addLocationAttributeType(locAttrTy);
								locationAttribute.setLocationAttributeTypeId(locAttrId); 
								locationAttribute.setTypeName(typeName);
//								returnStr += "ATTRIBUTE TYPE NAME '"+locationAttributeTypeName+"' DOES NOT EXIST.<br/>"; 
							} 
							else { locationAttribute.setLocationAttributeTypeId(locationAttributeType.getLocationAttributeTypeId()); locationAttribute.setTypeName(typeName); }
						}
						else if(head.get(j).trim().toLowerCase().equals("value")) { locationAttribute.setValue(val.get(j)); }
						else if(head.get(j).trim().toLowerCase().equals("type_value")) { 
							String typeValue1 = "";
							String typeValue2 = "";
							if(val.get(j).contains(":")) { String[] parts = val.get(j).split(":"); typeValue1 = parts[0]; typeValue2 = parts[1]; }
							else if(val.get(j).contains("_")) { String[] parts = val.get(j).split("_"); typeValue1 = parts[0]; typeValue2 = parts[1]; }
							else if(val.get(j).contains(" to ")) { String[] parts = val.get(j).split(" to "); typeValue1 = parts[0]; typeValue2 = parts[1]; }
							else { typeValue1 = val.get(j); typeValue2 = val.get(j); }
							locationAttribute.setTypeValue1(typeValue1); locationAttribute.setTypeValue2(typeValue2);
						}
					} csvLocationAttributes.add(locationAttribute);
				}
		    }
		    if(returnStr == "") {
			    for (int i = 0; i < csvLocationAttributes.size(); i++) {
			    	if(csvLocationAttributes.get(i) != null) { 
			    		sc.getLocationService().addLocationAttribute(csvLocationAttributes.get(i)); 
			    		returnStr += "LOCATION ATTRIBUTE '"+((sc.getLocationService().findLocationAttributeTypeById(csvLocationAttributes.get(i).getLocationAttributeTypeId(), true, null)).getAttributeName() + " FOR " + (sc.getLocationService().findLocationById(csvLocationAttributes.get(i).getLocationId(), true, null)).getName())+"' CREATED SUCCESSFULLY<br/>"; 
		    		}
			    	else { returnStr += "LOCATION ATTRIBUTE ALREADY EXISTS<br/>"; }
			    }
			    JSONArray locationAttributeNodes = new JSONArray();
			    JSONArray locationAttributeTypeNodes = new JSONArray();
			    ArrayList<LocationAttribute> locationAttributes = (ArrayList<LocationAttribute>) sc.getLocationService().getAllLocationAttribute(true, null);
			    ArrayList<LocationAttributeType> locationAttributeTypes = (ArrayList<LocationAttributeType>) sc.getLocationService().getAllLocationAttributeType(true, null);
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
			    result.put("locationAttributeNodes", locationAttributeNodes);
			    result.put("locationAttributeTypeNodes", locationAttributeTypeNodes);
		    }
		    else {
		    	returnStr += "\nERROR WHILE CREATING LOCATION ATTRIBUTES<br/>" ;
		    }
		    status = "info";
	        message = "INFO:";
		    result.put("status", status);
		    result.put("message", message);
			result.put("returnStr", returnStr);
			sc.commitTransaction();
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}
	
	@RequestMapping(value = "/updatelocation/type", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody String updatelocation_type(@RequestParam("id")String id, @RequestParam("voided")String voided, @RequestParam("reason")String reason) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
			Location location = sc.getLocationService().findLocationById(Integer.parseInt(id), false, null);
			location.setVoided(Boolean.parseBoolean(voided));
			if(voided.equals("true")) { location.setVoidReason(reason); location.setDateVoided(new Date()); } else { location.setVoidReason(null); location.setDateVoided(null); }
			sc.getLocationService().updateLocation(location);
			sc.commitTransaction(); sc = Context.getServices();

			JSONArray locationNodes = new JSONArray();
			JSONArray voidedLocationNodes = new JSONArray();
			ArrayList<Location> voidedLocations = (ArrayList<Location>) sc.getLocationService().findLocationByVoided(true, false, null);
			ArrayList<Location> locations = (ArrayList<Location>) sc.getLocationService().getAllLocation(false, null);
			ArrayList<LocationType> locationTypes = (ArrayList<LocationType>) sc.getLocationService().getAllLocationType(true, null);		
			
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
						if(locations.get(i).getLocationId() == location.getLocationId()) { locations.set(i, location); }
						if(locations.get(i).getVoided()) {}//DELETED LOCATION
						else {
							JSONObject locationNode = new JSONObject();
							locationNode.put("id", locations.get(i).getLocationId());
							if(locations.get(i).getName() == null) { locationNode.put("name", ""); } else { locationNode.put("name", locations.get(i).getName()); }
							if(locations.get(i).getShortName() == null) { locationNode.put("shortName", ""); } else { locationNode.put("shortName", locations.get(i).getShortName()); }
							if(locations.get(i).getFullName() == null) { locationNode.put("fullName", ""); } else { locationNode.put("fullName", locations.get(i).getFullName()); }
							if(locations.get(i).getParentLocation() == null) { locationNode.put("pId", 0); locationNode.put("pName", ""); }
							else { if(locations.get(i).getParentLocation().getVoided()) { locationNode.put("pId", 0); locationNode.put("pName", ""); } else { locationNode.put("pId", locations.get(i).getParentLocation().getLocationId()); locationNode.put("pName", locations.get(i).getParentLocation().getName()); } }
							if(locations.get(i).getLocationType() == null) { locationNode.put("type", 0); locationNode.put("typeName", ""); }
							else { for(int j=0; j<locationTypes.size(); j++) { if(locationTypes.get(j).getLocationTypeId() == locations.get(i).getLocationType().getLocationTypeId()) { locationNode.put("type", locations.get(i).getLocationType().getLocationTypeId()); locationNode.put("typeName", locationTypes.get(j).getTypeName()); } } }
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
			sc.commitTransaction();
    		status = "success";
    		message = "SUCCESS!";
    		returnStr = "LOCATION '" + location.getName() + "' VOIDED SUCCESSFULLY.";
			result.put("id", id);
			result.put("status", status);
			result.put("message", message);
			result.put("returnStr", returnStr);
        	result.put("locationNodes", locationNodes);
        	result.put("voidedLocationNodes", voidedLocationNodes);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}
	
	@RequestMapping(value = "/updatelocationname", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody String updatelocationName(@RequestParam("id")String id, @RequestParam("name")String name) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
			Location location = sc.getLocationService().findLocationById(Integer.parseInt(id), false, null);
			location.setName(name);
			sc.getLocationService().updateLocation(location);
			sc.commitTransaction(); sc = Context.getServices();
			JSONArray locationNodes = new JSONArray();
			ArrayList<Location> locations = (ArrayList<Location>) sc.getLocationService().getAllLocation(false, null);
			ArrayList<LocationType> locationTypes = (ArrayList<LocationType>) sc.getLocationService().getAllLocationType(true, null);		
			
			if(locations == null) {}
			else {
				for(int i=0; i<locations.size(); i++) {
					if(locations.get(i) == null) {}
					else {
						if(locations.get(i).getLocationId() == location.getLocationId()) { locations.set(i, location); }
						if(locations.get(i).getVoided()) {}//DELETED LOCATION
						else {
							JSONObject locationNode = new JSONObject();
							locationNode.put("id", locations.get(i).getLocationId());
							if(locations.get(i).getName() == null) { locationNode.put("name", ""); } else { locationNode.put("name", locations.get(i).getName()); }
							if(locations.get(i).getShortName() == null) { locationNode.put("shortName", ""); } else { locationNode.put("shortName", locations.get(i).getShortName()); }
							if(locations.get(i).getFullName() == null) { locationNode.put("fullName", ""); } else { locationNode.put("fullName", locations.get(i).getFullName()); }
							if(locations.get(i).getParentLocation() == null) { locationNode.put("pId", 0); locationNode.put("pName", ""); }
							else { if(locations.get(i).getParentLocation().getVoided()) { locationNode.put("pId", 0); locationNode.put("pName", ""); } else { locationNode.put("pId", locations.get(i).getParentLocation().getLocationId()); locationNode.put("pName", locations.get(i).getParentLocation().getName()); } }
							if(locations.get(i).getLocationType() == null) { locationNode.put("type", 0); locationNode.put("typeName", ""); }
							else { for(int j=0; j<locationTypes.size(); j++) { if(locationTypes.get(j).getLocationTypeId() == locations.get(i).getLocationType().getLocationTypeId()) { locationNode.put("type", locations.get(i).getLocationType().getLocationTypeId()); locationNode.put("typeName", locationTypes.get(j).getTypeName()); } } }
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
			sc.commitTransaction();
    		status = "success";
    		message = "SUCCESS!";
    		returnStr = "LOCATION '" + location.getName() + "' UPDATED SUCCESSFULLY.";
    		result.put("id", id);
			result.put("status", status);
			result.put("message", message);
			result.put("returnStr", returnStr);
        	result.put("locationNodes", locationNodes);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}

	@RequestMapping(value = "/updatelocationtypename", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody String updatelocationTypeName(@RequestParam("id")String id, @RequestParam("name")String name) throws InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
			System.out.println(id);
			System.out.println(name);
			LocationType locationType = sc.getLocationService().findLocationTypeById(Integer.parseInt(id), false, null);
			locationType.setLocationTypeId(Integer.parseInt(id));
			locationType.setTypeName(name);
			sc.getLocationService().updateLocationType(locationType);
			sc.commitTransaction();
			System.out.println(locationType);
			status = "success";
			message = "SUCCESS!";
			returnStr = "LOCATION TYPE '" + locationType.getTypeName() + "' UPDATED SUCCESSFULLY.";
			result.put("status", status);
			result.put("message", message);
			result.put("returnStr", returnStr);
			result.put("id", id);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}	

	@RequestMapping(value = "/updatelocationattributetypename", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody String updatelocationAttributeTypeName(@RequestParam("id")String id, @RequestParam("name")String name) throws InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
			
			LocationAttributeType locationAttributeType = sc.getLocationService().findLocationAttributeTypeById(Integer.parseInt(id), false, null);
			locationAttributeType.setLocationAttributeTypeId(Integer.parseInt(id));
			locationAttributeType.setAttributeName(name);
			sc.getLocationService().updateLocationAttributeType(locationAttributeType); 
			sc.commitTransaction();
			status = "success";
			message = "SUCCESS!";
			returnStr = "LOCATION ATTRIBUTE TYPE '" + locationAttributeType.getAttributeName() + "' UPDATED SUCCESSFULLY.";
			result.put("status", status);
			result.put("message", message);
			result.put("returnStr", returnStr);
			result.put("id", id);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}	
	
	private Integer strToInt(String str) {
	    if(str.equals("")) return null;
	    else return Integer.parseInt(str);
	}
}