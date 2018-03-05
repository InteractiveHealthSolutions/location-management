package com.ihs.location.web.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.InstanceAlreadyExistsException;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/rest")
public class LocationModalController {

	@RequestMapping(value = "/newlocation", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody String addlocation(@RequestParam("name")String name, @RequestParam("shortName")String shortName, @RequestParam("fullName")String fullName, @RequestParam("pName")String parentName, @RequestParam("description")String description, @RequestParam("otherIdentifier")String otherIdentifier, @RequestParam("typeName")String typeName, @RequestParam("latitude")String latitude, @RequestParam("longitude")String longitude) throws JSONException, InstanceAlreadyExistsException {
		LocationServiceContext sc = LocationContext.getServices();
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
		LocationServiceContext sc = LocationContext.getServices();
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
				locationType.setIsEditable(true);
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
		LocationServiceContext sc = LocationContext.getServices();
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
		LocationServiceContext sc = LocationContext.getServices();
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
				if(jsonArr.get("type").toString().equals("Text")) { }
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
		LocationServiceContext sc = LocationContext.getServices();
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
		LocationServiceContext sc = LocationContext.getServices();
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
		LocationServiceContext sc = LocationContext.getServices();
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
		String status = "";
		String message = "";
		String returnStr = "";

		JSONObject result = new JSONObject();
		try {
			// PARSE CSV DATA TO LOCATION OBJECTS			
			ArrayList<Location> csvLocations = new ArrayList<Location>();
			JSONArray head = new JSONArray();
			JSONArray arr = new JSONArray(csvData);
			if(arr.length() == 0){
				returnStr += "ERROR: No column header information available.";
			}
			else if((head=arr.getJSONArray(0)).toString().toLowerCase().contains("\"name\"") == false
					|| head.toString().toLowerCase().contains("\"full_name\"") == false
					|| head.toString().toLowerCase().contains("\"parent\"") == false
					|| head.toString().toLowerCase().contains("\"identifier\"") == false
					|| head.toString().toLowerCase().contains("\"type\"") == false){
				returnStr += "ERROR: Columns 'name, full_name, parent, identifier, type' MUST be specified.";				
			}
			else {
				for (int i = 1; i < arr.length(); i++) {
					LocationServiceContext sc = LocationContext.getServices();
					try{
					JSONArray csvLoc = arr.getJSONArray(i);	
		            Location l = new Location();
		            
					for (int j = 0; j < head.length(); j++) {
						String column = head.getString(j).trim();
						String value = csvLoc.getString(j).trim();
						
			        	if(column.equalsIgnoreCase("name")) {
			        		l.setName(value);
			        	}
			        	else if(column.equalsIgnoreCase("short_name")) { 
			        		l.setShortName(value); 
			        	}
			        	else if(column.equalsIgnoreCase("full_name")) { 
			        		l.setFullName(value); 
			        	}
			        	else if(column.equalsIgnoreCase("parent")
			        			&& !StringUtils.isEmptyOrWhitespaceOnly(value)) {
			        		Location parent = new Location(); 
		        			parent.setName(value); 
		        			l.setParentLocation(parent);
		        		}
			        	else if(column.equalsIgnoreCase("type")) { 
			        		LocationType lt = new LocationType();
			        		lt.setTypeName(value);
			        		l.setLocationType(lt);
		        		}
			        	else if (column.equalsIgnoreCase("identifier")) {
							l.setOtherIdentifier(value);
						} 
			        	else if (column.equalsIgnoreCase("description")) {
							l.setDescription(value);
						} 
			        	else if (column.equalsIgnoreCase("latitude")) {
							l.setLatitude(value);
						} 
			        	else if (column.equalsIgnoreCase("longitude")) {
							l.setLongitude(value);
						}
					}
					
					boolean validationPassed = true;
					if(StringUtils.isEmptyOrWhitespaceOnly(l.getName())
							|| StringUtils.isEmptyOrWhitespaceOnly(l.getLocationType().getTypeName())){
						returnStr += "ERROR: A mandatory column 'name, or type' is empty for row "+(i+1);	
						validationPassed = false;
					}
					
					if(StringUtils.isEmptyOrWhitespaceOnly(l.getFullName())){
						l.setFullName(l.getName());
					}
					
					if(sc.getLocationService().findLocationByName(l.getName(), true, null) != null){
						returnStr += "WARNING: Location already exists with given name at row "+i;
						validationPassed = false;
					}
					
					if(l.getParentLocation() != null){
						Location parentL = sc.getLocationService().findLocationByName(l.getParentLocation().getName(), true, null);
						if(parentL == null){
							returnStr += "ERROR: Parent location does not exist for entry at row "+i;
							validationPassed = false;
						}
						else{
							l.setParentLocation(parentL);
						}
					}
					
					LocationType locationType = sc.getLocationService().findLocationTypeByName(l.getLocationType().getTypeName(), true, null);
					if(locationType == null){
						returnStr += "ERROR: Location type does not exist for entry at row "+i;
						validationPassed = false;
					}
					else if(locationType.getIsEditable() == false){
						returnStr += "ERROR: Location with given type is not allowed to be edited from UI at row "+i;
						validationPassed = false;				
					}
					else{
						l.setLocationType(locationType);
					}
					
					if(validationPassed){
						csvLocations.add(l);
						
						sc.getLocationService().addLocation(l);
						sc.commitTransaction();
						
						returnStr += "LOCATION '" + l.getName() + "' CREATED<br/>";
					}
					else {
						returnStr += "; LOCATION '" + l.getName() + "' NOT CREATED<br/>";
					}
				}
				catch(Exception e){
					e.printStackTrace();
        			returnStr += "ERROR WHILE ADDING LOCATION AT ROW "+(i+1)+": "+e.getMessage()+"<br/>"; 
				}
				finally {
					sc.closeSession();
				}
				}
			}
		    
	        status = "info";
	        message = "INFO: (Reload page to refresh location tree)";
	        result.put("status", status);
	        result.put("message", message);
			result.put("returnStr", returnStr);
		} 
		catch(Exception e) { 
			e.printStackTrace(); 
		}
		
		return result.toString();
	}

	@RequestMapping(value = "/newCsvForLocAttrs", method = RequestMethod.POST)
	public @ResponseBody String updateCsvForLocAttrs(@RequestParam("csvData")String csvData) throws JSONException, InstanceAlreadyExistsException {
		LocationServiceContext src = LocationContext.getServices();
	    JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
	    try {
	    	JSONArray head = new JSONArray();
	    	Map<String, Integer> headerIdMap = new HashMap<String, Integer>();
	    	List<String> missingAttributes = new ArrayList<String>();
	    	
	    	// PARSE CSV DATA TO LOCATION ATTRIBUTE OBJECTS
			JSONArray arr = new JSONArray(csvData);
			if(arr.length() == 0){
				returnStr += "ERROR: No column header information available.";
			}
			else if((head=arr.getJSONArray(0)).toString().toLowerCase().contains("\"location\"") == false){
				returnStr += "ERROR: No column with name 'location' is available.";				
			}
			else {
				for (int i = 0; i < head.length(); i++) {
					if(StringUtils.isEmptyOrWhitespaceOnly(head.getString(i)) 
							|| head.getString(i).equalsIgnoreCase("location")){
						continue;
					}
					
					LocationAttributeType lat = src.getLocationService().findLocationAttributeTypeByName(head.getString(i), true, null);					
					if(lat == null || lat.getDataType().equalsIgnoreCase("text") == false){
						missingAttributes.add(head.getString(i));
					}
					else {
						headerIdMap.put(head.getString(i), lat.getLocationAttributeTypeId());
					}
				}
				
				if(missingAttributes.size() > 0){
					returnStr += "ERROR: No attribute type configured with names specified in columns "+missingAttributes+" with data type Text. Add attribute types with appropriate data type first";				
				}
				else{
					// skip header row
					for (int i = 1; i < arr.length(); i++) {
						try{
							JSONArray thisrow = arr.getJSONArray(i);
							Location loc = null;
							
							for (int j = 0; j < head.length(); j++) {
								if(StringUtils.isEmptyOrWhitespaceOnly(head.getString(j))){
									continue;
								}
								
								String columnName = head.getString(j);
								String columnValue = thisrow.getString(j).trim();
								
								// if column is location check if it exists otherwise save attribute
								if(columnName.equalsIgnoreCase("location")
										&& (StringUtils.isEmptyOrWhitespaceOnly(columnValue)
										|| (loc = src.getLocationService().findLocationByName(columnValue, true, null)) == null)){
									returnStr += "LOCATION at row "+i+" in column "+columnName+" with value "+columnValue+" DOES NOT EXIST.<br/>"; 
								}
								else if(columnName.equalsIgnoreCase("location")){
									continue;
								}
								else if(StringUtils.isEmptyOrWhitespaceOnly(columnValue) == false){
									
									LocationServiceContext sc = LocationContext.getServices();
									// find existing attribute with type text and update or create new one
									// means that if any existing attribute is now being collected with time bound
									// data add a new
									
									try{
										List<LocationAttribute> latl = sc.getLocationService()
											.findLocationAttributeByCriteria("TEXT", null, null, null, loc.getLocationId(), headerIdMap.get(columnName), 0, 1, false, null, null);
									
										LocationAttribute locationAttribute = null;
										if(latl.isEmpty()){
											locationAttribute = new LocationAttribute();
											locationAttribute.setCreatedDate(new Date());
											locationAttribute.setTypeName("TEXT");
										}
										else {
											locationAttribute = latl.get(0);
											locationAttribute.setLastEditedDate(new Date());
										}
										
										locationAttribute.setLocationId(loc.getLocationId());
										locationAttribute.setLocationAttributeTypeId(headerIdMap.get(columnName));
										locationAttribute.setValue(columnValue);
										
										sc.getLocationService().addLocationAttribute(locationAttribute);
										sc.commitTransaction();
									}
									catch(Exception e){
										e.printStackTrace();
										returnStr += "ERROR importing location "+loc.getName()+" at row "+i;
									}
									finally{
										sc.closeSession();
									}
								}
								returnStr += loc.getName()+" at row "+i+" processed successfully";
							}
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
		    
			// TODO allow importing timebound attributes. CSV should be imported by type i.e. annual/daily/monthly
		} 
	    catch(Exception e) { 
	    	e.printStackTrace(); 
	    	returnStr += "ERROR while importing locations "+e.getMessage();
		} 
	    finally { 
	    	src.closeSession(); 
		} 
	    status = "info";
        message = "INFO:";
	    result.put("status", status);
	    result.put("message", message);
		result.put("returnStr", returnStr);
	    
	    return result.toString();
	}
	
	@RequestMapping(value = "/updatelocation/type", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody String updatelocation_type(@RequestParam("id")String id, @RequestParam("voided")String voided, @RequestParam("reason")String reason) throws JSONException, InstanceAlreadyExistsException {
		LocationServiceContext sc = LocationContext.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
			Location location = sc.getLocationService().findLocationById(Integer.parseInt(id), false, null);
			location.setVoided(Boolean.parseBoolean(voided));
			if(voided.equals("true")) { location.setVoidReason(reason); location.setDateVoided(new Date()); } else { location.setVoidReason(null); location.setDateVoided(null); }
			sc.getLocationService().updateLocation(location);
			sc.commitTransaction(); sc = LocationContext.getServices();

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
		LocationServiceContext sc = LocationContext.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
			Location location = sc.getLocationService().findLocationById(Integer.parseInt(id), false, null);
			location.setName(name);
			sc.getLocationService().updateLocation(location);
			sc.commitTransaction(); sc = LocationContext.getServices();
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
		LocationServiceContext sc = LocationContext.getServices();
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
		LocationServiceContext sc = LocationContext.getServices();
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
	
	@RequestMapping("/downloadLocations")
	public void downloadLocations(HttpServletResponse response) throws IOException {
 
        String csvFileName = "locations.csv";
 
        response.setContentType("text/csv");
 
        String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
        response.setHeader("Content-Disposition", headerValue);
        
        ICsvListWriter csvWriter = new CsvListWriter(response.getWriter(), CsvPreference.EXCEL_PREFERENCE);

        LocationServiceContext sc = LocationContext.getServices();

        try{
        	List<Object> lalist = sc.getLocationService().getDataBySQL("SELECT DISTINCT attributeName FROM locationattribute la "
        			+ " JOIN locationattributetype lat ON la.locationAttributeTypeId = lat.locationAttributeTypeId "
        			+ " WHERE typeName = 'text' ORDER BY attributeName ");
        	
        	String[] locHeader= {
        			"location_id", 
        			"name", 
        			"full_name", 
        			"other_identifier",
                    "location_type", 
                    "parent_location", 
                    "voided",
                    "void_reason",
                    "date_voided",
                    "latitude",
                    "longitude",
                    "description",
                    "date_created"};
        	
        	List<Object> headerlist = new ArrayList<Object>(lalist);
        	
        	headerlist.addAll(0, Arrays.asList(locHeader));
            String[] fullHeader = headerlist.toArray(new String[]{});

            csvWriter.writeHeader(fullHeader);
        	
        	List<Location> llist = sc.getLocationService().getAllLocation(true, new String[]{"locationType"});
        	
        	for (Location l : llist) {
        		String[] locationData = {
            			l.getLocationId() +"",
            			l.getName(),
            			l.getFullName(),
            			l.getOtherIdentifier(),
            			l.getLocationType().getTypeName(),
            			l.getParentLocation()==null?"":l.getParentLocation().getName(),
            			l.getVoided() +"",
            			l.getVoidReason(),
            			l.getDateVoided() +"",
            			l.getLatitude(),
            			l.getLongitude(),
            			l.getDescription()==null?"":l.getDescription().trim(),
            			l.getCreatedDate() +"",
            	};
        		
        		List<String> attributeData = new ArrayList<String>();
        		
        		for (Object attribute : lalist) {
					attributeData.add((String) attributeValue(attribute.toString(), l.getLocationId(), sc));
				}
        		
        		attributeData.addAll(0, Arrays.asList(locationData));
        		
        		csvWriter.write(attributeData.toArray(new String[]{}));
			}
        }
        catch(Exception e){
        	e.printStackTrace();
            csvWriter.writeHeader("ERROR: "+e.getMessage());
        }
        finally {
			sc.closeSession();
		}
 
        csvWriter.close();
	}
	
	@RequestMapping("/downloadLocationAttributes")
	public void downloadLocationAttributes(HttpServletResponse response) throws IOException {
 
        String csvFileName = "location_attributes.csv";
 
        response.setContentType("text/csv");
 
        String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
        response.setHeader("Content-Disposition", headerValue);
        
        ICsvListWriter csvWriter = new CsvListWriter(response.getWriter(), CsvPreference.EXCEL_PREFERENCE);

        LocationServiceContext sc = LocationContext.getServices();

        try{
        	String[] locHeader= {
        			"location_id", 
        			"location_name", 
        			"name", 
                    "display_name", 
                    "category", 
                    "type",
                    "value1",
                    "value2",
                    "description",
                    "date_created"};
        	        	
            csvWriter.writeHeader(locHeader);
        	
        	List<LocationAttribute> llist = sc.getLocationService().getAllLocationAttribute(true, new String[]{"locationType"});
        	
        	for (LocationAttribute l : llist) {
        		String[] locationData = {
            			l.getLocationId() +"",
            			l.getLocation().getName(),
            			l.getLocationAttributeType().getAttributeName(),
            			l.getLocationAttributeType().getDisplayName(),
            			l.getLocationAttributeType().getCategory(),
            			l.getTypeName(),
            			l.getTypeValue1(),
            			l.getTypeValue2(),
            			l.getLocationAttributeType().getDescription()==null?"":l.getLocationAttributeType().getDescription().trim(),
            			l.getCreatedDate() +"",
            	};
        		
        		csvWriter.write(locationData);
			}
        }
        catch(Exception e){
        	e.printStackTrace();
            csvWriter.writeHeader("ERROR: "+e.getMessage());
        }
        finally {
			sc.closeSession();
		}
 
        csvWriter.close();
	}
	
	public Object attributeValue(String attribute, int locationId, LocationServiceContext sc) {
		List<Object> dl = sc.getLocationService().getDataBySQL("SELECT value FROM locationattribute la "
				+ " JOIN locationattributetype lat ON la.locationAttributeTypeId = lat.locationAttributeTypeId "
				+ " WHERE typeName = 'text' AND la.locationId="+locationId+" AND lat.attributeName = '"+attribute+"'");
		
		return dl.size()==0?null:dl.get(0);
	}

	private Integer strToInt(String str) {
	    if(str.equals("")) return null;
	    else return Integer.parseInt(str);
	}
}