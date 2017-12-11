package com.ihs.locationmanagement.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.management.InstanceAlreadyExistsException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.ihs.locationmanagement.api.context.*;
import com.ihs.locationmanagement.api.model.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RESTController {

	@RequestMapping(value = "/createlocation", method = {RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String addlocationGET() throws JSONException, InstanceAlreadyExistsException {
		return "ERROR:\n\n -> Method: POST.\n\n -> Parameters:\n -\tname: unique, cannot be empty, there should be any 3 to 30 alphanumeric and/or special characters like ‘., -, _’.\n -\tshortName: can be empty, there should be any 3 to 30 alphanumeric and/or special characters like ‘., -, _’.\n -\tfullName: can be empty, there should be any 3 to 50 alphanumeric and/or special characters like ‘., -, _’.\n -\tpName: can be empty, if not empty then location must exist before used as parent.\n -\tdescription: can be empty, there should be any 0 to 255 characters.\n -\totherIdentifier: can be empty, there should be any 0 to 255 positive decimal numbers.\n -\ttypeName: cannot be empty, location type must exist before used in location.\n -\tlatitude: can be empty, there should be any 0 to 255 characters.\n -\tlongitude: can be empty, there should be any 0 to 255 characters."; 
	}
	
	@RequestMapping(value = "/createlocation", method = {RequestMethod.POST}, produces = "application/json")
	public @ResponseBody String addlocationPOST(@RequestParam("name")String name, @RequestParam("shortName")String shortName, @RequestParam("fullName")String fullName, @RequestParam("pName")String parentName, @RequestParam("description")String description, @RequestParam("otherIdentifier")String otherIdentifier, @RequestParam("typeName")String typeName, @RequestParam("latitude")String latitude, @RequestParam("longitude")String longitude) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";

		String pattern = "^[a-zA-Z0-9.-_ ]*$";
		String pattern2 = "-[0-9]*.[0-9]*$";
		String errorStr = "";
		if(name != "" && shortName == "") { shortName = name; }
		if(name != "" && fullName == "") { fullName = name; }
		if(!(name.length() >= 3)) { errorStr += "Location Name must have atleast 3 characters.\n"; }
	    if(!(name.length() <= 30)) { errorStr += "Location Name must have atmost 30 characters.\n"; }
	    if(!(Pattern.matches(pattern, name))) { errorStr += "Location Name must have alphabets only.\n"; }
		if(!(shortName.length() >= 3)) { errorStr += "Location Short Name must have atleast 3 characters.\n"; }
	    if(!(shortName.length() <= 30)) { errorStr += "Location Short Name must have atmost 30 characters.\n"; }
	    if(!(Pattern.matches(pattern, shortName))) { errorStr += "Location Short Name must have alphabets only.\n"; }
		if(!(fullName.length() >= 3)) { errorStr += "Location Full Name must have atleast 3 characters.\n"; }
	    if(!(fullName.length() <= 50)) { errorStr += "Location Full Name must have atmost 30 characters.\n"; }
	    if(!(Pattern.matches(pattern, fullName))) { errorStr += "Location Full Name must have alphabets only.\n"; }
	    if(Pattern.matches(pattern2, otherIdentifier)) { errorStr += "Other Identifier must have alphabets or positive numbers only.\n"; }
		if(!(latitude.length() <= 255)) { errorStr += "Location Latitude must have atmost 255 characters.\n"; }
		if(!(longitude.length() <= 255)) { errorStr += "Location Longitude must have atmost 255 characters.\n"; }
		if(name == "") { errorStr += "Location Name is required.\n"; }
		if(shortName == "") { errorStr += "Location Short Name is required.\n"; }
		if(fullName == "") { errorStr += "Location Full Name is required.\n"; }
		if(typeName == "") { errorStr += "Location Type is required.\n"; }
		
	    if(errorStr == "") {}
		else { 
			result.put("status", "error");
			result.put("messageType", "ERROR:");
			result.put("message", errorStr);
			result.put("id", "");
			return result.toString(); 
		}
		
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
	    		returnStr = "LOCATION '" + location.getName() + "' CREATED SUCCESSFULLY\n";
			}
			else {
				id = location.getLocationId(); 
	    		status = "info";
	    		message = "INFO:";
	    		returnStr = "LOCATION '" + location.getName() + "' ALREADY EXISTS\n";
			}		
			sc.commitTransaction();
			result.put("status", status);
			result.put("messageType", message);
			result.put("message", returnStr);
			result.put("id", id);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}

	@RequestMapping(value = "/createlocationtype", method = {RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String addlocationTypeGET() throws InstanceAlreadyExistsException { 
		return "ERROR:\n\n -> Method: POST.\n\n -> Parameters:\n -\tname: unique, cannot be empty, there should be any 3 to 30 alphanumeric and/or special \n -\tcharacters like ‘., -, _’.\n -\tlevel: can be empty, there should be any 0 to 255 positive decimal numbers.description: can be empty, there should be any 0 to 255 characters.";
	}
	
	@RequestMapping(value = "/createlocationtype", method = {RequestMethod.POST}, produces = "application/json")
	public @ResponseBody String addlocationTypePOST(@RequestParam("name")String name, @RequestParam("level")String level, @RequestParam("description")String description) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		
		String pattern = "^[0-9]*.[0-9]*$";
		String pattern2 = "^[a-zA-Z0-9.-_ ]*$";
		String errorStr = "";
		if(!(name.length() >= 3)) { errorStr += "Location Type Name must have atleast 3 characters.\n"; }
	    if(!(name.length() <= 50)) { errorStr += "Location Type Name must have atmost 50 characters.\n"; }
	    if(!(Pattern.matches(pattern2, name ))) { errorStr += "Location Type Name must have alphanumeric or special characters (.-_) only.\n"; }
	    if(!(Pattern.matches(pattern, level))) { errorStr += "Location Type Level must have numbers only.\n"; }
	    if(!(description.length() <= 255)) { errorStr += "Location Type Description must have atmost 255 characters.\n"; }
	    if(name == "") { errorStr += "Location Type Name is required.\n"; }
	    
		if(errorStr == "") {}
		else { 
			result.put("status", "error");
			result.put("messageType", "ERROR:");
			result.put("message", errorStr);
			result.put("id", "");
			return result.toString(); 
		}
		
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
	    		returnStr = "LOCATION TYPE '" + locationType.getTypeName() + "' CREATED SUCCESSFULLY\n";
			}
			else {
				id = locationType.getLocationTypeId();
	    		status = "info";
	    		message = "INFO:";
	    		returnStr = "LOCATION TYPE '" + locationType.getTypeName() + "' ALREADY EXISTS\n";
			}
			sc.commitTransaction();
			result.put("status", status);
			result.put("messageType", message);
			result.put("message", returnStr);
			result.put("id", id);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}	

	@RequestMapping(value = "/createlocationattributetype", method = {RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String addlocationattributetypeGET(@RequestParam("name")String locationAttributeTypeName, @RequestParam("displayName")String locationAttributeTypeDisplayName, @RequestParam("description")String locationAttributeTypeDescription, @RequestParam("category")String locationAttributeTypeCategory) throws JSONException, InstanceAlreadyExistsException {
		return "ERROR:\n\n -> Method: POST.\n\n -> Parameters:\n -\tname: unique, cannot be empty, there should be any 3 to 100 alphanumeric and/or special characters like ‘., -, _’.\n -\tdisplayName: unique, can be empty, there should be any 3 to 30 alphanumeric and/or special characters like ‘., -, _’.\n -\tdescription: can be empty, there should be any 0 to 255 characters.\n -\tcategory: can be empty, there should be any 0 to 255 characters.";
	}
	
	@RequestMapping(value = "/createlocationattributetype", method = {RequestMethod.POST}, produces = "application/json")
	public @ResponseBody String addlocationattributetypePOST(@RequestParam("name")String locationAttributeTypeName, @RequestParam("displayName")String locationAttributeTypeDisplayName, @RequestParam("description")String locationAttributeTypeDescription, @RequestParam("category")String locationAttributeTypeCategory) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		
		String pattern = "^[a-zA-Z0-9.-_ ]*$";
		String errorStr = "";
		
		if(!(locationAttributeTypeName.length() >= 3)) { errorStr += "Location Attribute Type Name must have atleast 3 characters.\n"; }
	    if(!(locationAttributeTypeName.length() <= 100)) { errorStr += "Location Attribute Type Name must have atmost 100 characters.\n"; }
	    if(!(Pattern.matches(pattern, locationAttributeTypeName ))) { errorStr += "Location Attribute Type Name must have alphanumeric or special characters (.-_) only.\n"; }
        if(!(locationAttributeTypeDisplayName.length() >= 3)) { errorStr += "Location Attribute Type Name must have atleast 3 characters.\n"; }
	    if(!(locationAttributeTypeDisplayName.length() <= 100)) { errorStr += "Location Attribute Type Name must have atmost 100 characters.\n"; }
	    if(!(Pattern.matches(pattern, locationAttributeTypeDisplayName ))) { errorStr += "Location Attribute Type Display Name must have alphanumeric or special characters (.-_) only.\n"; }
	    if(locationAttributeTypeName == "" ) { errorStr += "Location Attribute Type Name is required.\n"; }
	    if(!(locationAttributeTypeDescription.length() <= 255)) { errorStr += "Location Attribute Type Description must have atmost 255 characters.\n"; }
		
	    if(errorStr == "") {}
	    else { 
			result.put("status", "error");
			result.put("messageType", "ERROR:");
			result.put("message", errorStr);
			result.put("id", "");
			return result.toString(); 
		}
		
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
	    		returnStr = "LOCATION ATTRIBUTE TYPE '" + locationAttributeType.getAttributeName() + "' CREATED SUCCESSFULLY\n";
			}
			else {
				id = locationAttributeType.getLocationAttributeTypeId();
	    		status = "info";
	    		message = "INFO:";
	    		returnStr = "LOCATION ATTRIBUTE TYPE '" + locationAttributeType.getAttributeName() + "' ALREADY EXISTS\n";
			}
			sc.commitTransaction();
			result.put("status", status);
			result.put("messageType", message);
			result.put("message", returnStr);
			result.put("id", id);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}	

	@RequestMapping(value = "/createlocationattribute", method = {RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String addlocationattributeGET() throws JSONException, InstanceAlreadyExistsException {
		return "ERROR:\n\n -> Method: POST.\n\n -> Parameters:\n -\tname: unique, cannot be empty, there should be any 3 to 30 alphanumeric and/or special characters like ‘., -, _’.\n -\tarray: can be empty if no attributes exist otherwise it must have attribute values.";
	}
	
	@RequestMapping(value = "/createlocationattribute", method = {RequestMethod.POST}, produces = "application/json")
	public @ResponseBody String addlocationattributePOST(@RequestParam("parent")String locationParent, @RequestParam("array")String attributes) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
			ArrayList<Integer> ids = new ArrayList<Integer>();
//			ArrayList<LocationAttributeType> locationAttributeTypes = (ArrayList<LocationAttributeType>) sc.getLocationService().getAllLocationAttributeType(true, null);
//			ArrayList<ArrayList<String>> list = new ArrayList<>();
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

//			boolean check = false;
			for (LocationAttribute locAttr_UI : locationAttributes) {
				Integer locationId = locAttr_UI.getLocationId();
				Integer locationAttributeTypeId = locAttr_UI.getLocationAttributeTypeId();
				ArrayList<LocationAttribute> locAttrS = (ArrayList<LocationAttribute>) sc.getLocationService().findLocationAttributeByCriteria(null, "", "", "", "", locationId, locationAttributeTypeId, 0, ((ArrayList<LocationAttribute>) sc.getLocationService().getAllLocationAttribute(false, null)).size(), false, null);
				for (LocationAttribute locAttr_DB : locAttrS) {
					if(locAttr_UI.getTypeName().equals(locAttr_DB.getTypeName())) {
						locAttr_DB.setValue(locAttr_UI.getValue());
						if (locAttr_UI.getTypeName().equals("None")) { }
						else { locAttr_DB.setTypeValue1(locAttr_UI.getTypeValue1()); locAttr_DB.setTypeValue2(locAttr_UI.getTypeValue2()); }
						sc.getLocationService().updateLocationAttribute(locAttr_DB);
					} else { 
						locAttr_DB.setValue(locAttr_UI.getValue());
					}
				}
			}
			sc.commitTransaction();
    		status = "success";
    		message = "SUCCESS!";
			returnStr =  "New Attribute Created";
			result.put("status", status);
			result.put("messageType", message);
			result.put("message", returnStr);
			result.put("id", ids);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}

	@RequestMapping(value = "/updatelocation", method = {RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String updatelocationGET() throws JSONException, InstanceAlreadyExistsException {
		return "ERROR:\n\n -> Method: POST.\n\n -> Parameters:\n -\tname: unique, cannot be empty and cannot be updated, there should be any 3 to 30 alphanumeric and/or special characters like ‘., -, _’.\n -\tshortName: can be empty or may have new data, there should be any 3 to 30 alphanumeric and/or special characters like ‘., -, _’.\n -\tfullName: can be empty or may have new data, there should be any 3 to 50 alphanumeric and/or special characters like ‘., -, _’.\n -\tpName: can be empty or may have new data, if not empty then location must exist before used as parent.\n -\tdescription: can be empty or may have new data, there should be any 0 to 255 characters.\n -\totherIdentifier: can be empty or may have new data, there should be any 0 to 255 positive decimal numbers.\n -\ttypeName: cannot be empty and may have a different type than before, location type must exist before used in location.\n -\tlatitude: can be empty or may have new data, there should be any 0 to 255 characters.\n -\tlongitude: can be empty or may have new data, there should be any 0 to 255 characters.";
	}
	
	@RequestMapping(value = "/updatelocation", method = {RequestMethod.POST}, produces = "application/json")
	public @ResponseBody String updatelocationPOST(@RequestParam("name")String name, @RequestParam("shortName")String shortName, @RequestParam("fullName")String fullName, @RequestParam("pName")String pName, @RequestParam("description")String description, @RequestParam("otherIdentifier")String otherIdentifier, @RequestParam("typeName")String typeName, @RequestParam("latitude")String latitude, @RequestParam("longitude")String longitude) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		
		String pattern = "^[a-zA-Z0-9.-_ ]*$";
		String pattern2 = "-[0-9]*.[0-9]*$";
		String errorStr = "";
		if(name == "") { errorStr += "Location Name is required.\n"; }
		if(shortName == "") { errorStr += "Location Short Name is required.\n"; }
		if(fullName == "") { errorStr += "Location Full Name is required.\n"; }
		if(typeName == "") { errorStr += "Location Type is required.\n"; }	
		if(!(name.length() >= 3)) { errorStr += "Location Name must have atleast 3 characters.\n"; }
		if(!(name.length() <= 30)) { errorStr += "Location Name must have atmost 30 characters.\n"; }
		if(!(Pattern.matches(pattern, name))) { errorStr += "Location Name must have alphabets only.\n"; }
		if(!(shortName.length() >= 3)) { errorStr += "Location Short Name must have atleast 3 characters.\n"; }
		if(!(shortName.length() <= 30)) { errorStr += "Location Short Name must have atmost 30 characters.\n"; }
		if(!(Pattern.matches(pattern, shortName))) { errorStr += "Location Short Name must have alphabets only.\n"; }
		if(!(fullName.length() >= 3)) { errorStr += "Location Full Name must have atleast 3 characters.\n"; }
		if(!(fullName.length() <= 50)) { errorStr += "Location Full Name must have atmost 30 characters.\n"; }
		if(!(Pattern.matches(pattern, fullName))) { errorStr += "Location Full Name must have alphabets only.\n"; }
		if(Pattern.matches(pattern2, otherIdentifier)) { errorStr += "Other Identifier must have alphabets or positive numbers only.\n"; }
		if(!(description.length() <= 255)) { errorStr += "Location Description must have atmost 255 characters.\n"; }
		if(!(otherIdentifier.length() <= 50)) { errorStr += "Location Other Identifier must have atmost 255 characters.\n"; }
		if(!(latitude.length() <= 255)) { errorStr += "Location Latitude must have atmost 255 characters.\n"; }
		if(!(longitude.length() <= 255)) { errorStr += "Location Longitude must have atmost 255 characters.\n"; }

		if(errorStr == "") {}
		else { 
			result.put("status", "error");
			result.put("messageType", "ERROR:");
			result.put("message", errorStr);
			result.put("id", "");
			return result.toString(); 
		}
		
		try {
			Integer id = null;
			Location location = sc.getLocationService().findLocationByName(name, false, null);
			Location parentLocation = sc.getLocationService().findLocationByName(pName, false, null);
			LocationType locationType = sc.getLocationService().findLocationTypeByName(typeName, false, null);

			id = location.getLocationId();
			location.setLocationId(id);
			location.setName(name);
			location.setShortName(shortName);
			location.setFullName(fullName);
	
			if(parentLocation == null) { returnStr = "LOCATION PARENT '" + pName + "' DOES NOT EXIST."; location.setParentLocation(null); }
			else { location.setParentLocation(parentLocation); }
			
			if(locationType == null) { returnStr = "LOCATION TYPE '" + typeName + "' DOES NOT EXIST."; }
			else { location.setLocationType(locationType); }
			
			location.setDescription(description);
			location.setOtherIdentifier(otherIdentifier);

			location.setLatitude(latitude);
			location.setLongitude(longitude);
			
			sc.getLocationService().updateLocation(location);
			sc.commitTransaction();
    		status = "success";
    		message = "SUCCESS!";
    		returnStr = "LOCATION '" + location.getName() + "' UPDATED SUCCESSFULLY.";

			result.put("status", status);
			result.put("messageType", message);
			result.put("message", returnStr);
			result.put("id", id);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}

	@RequestMapping(value = "/updatelocationtype", method = {RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String updatelocationTypeGET() throws InstanceAlreadyExistsException {
		return "ERROR:\n\n -> Method: POST.\n\n -> Parameters:\n -\tname: unique, cannot be empty and cannot be updated, there should be any 3 to 30 alphanumeric and/or special characters like ‘., -, _’.\n -\tlevel: can be empty or may have new data, there should be any 0 to 255 positive decimal numbers.\n -\tdescription: can be empty or may have new data, there should be any 0 to 255 characters.";
	}
	
	@RequestMapping(value = "/updatelocationtype", method = {RequestMethod.POST}, produces = "application/json")
	public @ResponseBody String updatelocationTypePOST(@RequestParam("name")String name, @RequestParam("level")String level, @RequestParam("description")String description) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		
		String pattern = "^[a-zA-Z0-9.-_ ]*$";
		String pattern2 = "^[0-9]*.[0-9]*$";
		String errorStr = "";
		if(name == "") { errorStr += "Location Type Name is required.\n"; }
		if(!(name.length() >= 3)) { errorStr += "Location Type Name must have atleast 3 characters.\n"; }
		if(!(name.length() <= 30)) { errorStr += "Location Type Name must have atmost 30 characters.\n"; }
		if(!(Pattern.matches(pattern, name))) { errorStr += "Location Type Name must have alphabets only.\n"; }
		if(!(Pattern.matches(pattern2, level))) { errorStr += "Location Type Level must have numbers only.\n"; }
        if(!(description.length() <= 255)) { errorStr += "Location Type Description must have atmost 255 characters.\n"; }
		
		if(errorStr == "") {}
		else { 
			result.put("status", "error");
			result.put("messageType", "ERROR:");
			result.put("message", errorStr);
			result.put("id", "");
			return result.toString(); 
		}
		
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
			result.put("messageType", message);
			result.put("message", returnStr);
			result.put("id", id);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}	

	@RequestMapping(value = "/updatelocationattributetype", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String updatelocationAttributeTypeGET() throws InstanceAlreadyExistsException {
		return "ERROR:\n\n -> Method: POST.\n\n -> Parameters:\n -\tname: unique, cannot be empty and cannot be updated, there should be any 3 to 30 alphanumeric and/or special characters like ‘., -, _’.\n -\tdisplayName: unique, can be empty or may have new data,  there should be any 3 to 30 alphanumeric and/or special characters like ‘., -, _’.\n -\tdescription: can be empty or may have new data,  there should be any 0 to 255 characters.\n -\tcategory: can be empty or may have new data, there should be any 0 to 255 characters.";
	}
	
	@RequestMapping(value = "/updatelocationattributetype", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String updatelocationAttributeTypePOST(@RequestParam("name")String name, @RequestParam("displayName")String displayName, @RequestParam("description")String description, @RequestParam("category")String category) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		
		String pattern = "^[a-zA-Z0-9.-_ ]*$";
		String errorStr = "";
		if(name == "") { errorStr += "Location Name is required.\n"; }
		if(!(name.length() >= 3)) { errorStr += "Location Attribute Type Name must have atleast 3 characters.\n"; }
		if(!(name.length() <= 50)) { errorStr += "Location Attribute Type Name must have atmost 50 characters.\n"; }
		if(!(Pattern.matches(pattern, name))) { errorStr += "Location Attribute Type Name must have alphabets only.\n"; }
		if(!(displayName.length() >= 3)) { errorStr += "Location Attribute Type Display Name must have atleast 3 characters.\n"; }
		if(!(displayName.length() <= 50)) { errorStr += "Location Attribute Type Display Name must have atmost 50 characters.\n"; }
		if(!(Pattern.matches(pattern, displayName))) { errorStr += "Location Attribute Type Display Name must have alphabets only.\n"; }
        if(!(description.length() <= 255)) { errorStr += "Location Attribute Type Description must have atmost 255 characters.\n"; }
        if(!(category.length() <= 255)) { errorStr += "Location Attribute Type Category must have atmost 255 characters.\n"; }
		
		if(errorStr == "") {}
		else { 
			result.put("status", "error");
			result.put("messageType", "ERROR:");
			result.put("message", errorStr);
			result.put("id", "");
			return result.toString(); 
		}
		
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
			result.put("messageType", message);
			result.put("message", returnStr);
			result.put("id", id);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}	
	
	@RequestMapping(value = "/viewlocation", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String viewAlllocations() throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONArray result = new JSONArray();
		try {
			
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
							if(locations.get(i).getParentLocation() == null) { locationNode.put("pId", 0); }
							else { locationNode.put("pId", locations.get(i).getParentLocation().getLocationId()); }
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
							result.put(locationNode);
						} 
					}
				}
			}
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}

	@RequestMapping(value = "/viewlocationtype", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String viewAlllocationtypes() throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONArray result = new JSONArray();
		try {
			ArrayList<LocationType> locationTypes = (ArrayList<LocationType>) sc.getLocationService().getAllLocationType(true, null);		
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
						result.put(locationTypeNode);
					}
				}
			}
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}
	
	@RequestMapping(value = "/viewlocationattributetype", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String viewAlllocationattributetypes() throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONArray result = new JSONArray();
		try {
			ArrayList<LocationAttributeType> locationAttributeTypes = (ArrayList<LocationAttributeType>) sc.getLocationService().getAllLocationAttributeType(true, null);
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
						result.put(locationAttributeTypeNode);
					} 
				}
			}
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}
	
	@RequestMapping(value = "/viewlocationattribute", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String viewAlllocationattributes() throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONArray result = new JSONArray();
		try {
			ArrayList<LocationAttribute> locationAttributes = (ArrayList<LocationAttribute>) sc.getLocationService().getAllLocationAttribute(true, null);		
			if(locationAttributes == null) {}
			else {
				for(int i=0; i<locationAttributes.size(); i++) {
					if(locationAttributes.get(i) == null) {}
					else {
						JSONObject locationAttributeNode = new JSONObject();
						locationAttributeNode.put("id", locationAttributes.get(i).getLocationAttributeId());
						locationAttributeNode.put("typeId", locationAttributes.get(i).getLocationAttributeTypeId());
						locationAttributeNode.put("locationId", locationAttributes.get(i).getLocationId());
						if(locationAttributes.get(i).getTypeName() == null) { locationAttributeNode.put("typeName", ""); } else { locationAttributeNode.put("typeName", locationAttributes.get(i).getTypeName().replaceAll("_", " ")); }
						if(locationAttributes.get(i).getValue() == null) { locationAttributeNode.put("value", ""); } else { locationAttributeNode.put("value", locationAttributes.get(i).getValue()); }
						if(locationAttributes.get(i).getTypeName().equals("None")) {}
						else if(locationAttributes.get(i).getTypeName().equals("Time_Bound")) { locationAttributeNode.put("typeValue", (locationAttributes.get(i).getTypeValue1() + " to " + locationAttributes.get(i).getTypeValue2())); }
						else { locationAttributeNode.put("typeValue", locationAttributes.get(i).getTypeValue1()); }
						result.put(locationAttributeNode);
					}
				}
			}
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}
	
	@RequestMapping(value = "/viewlocation/id/{id}", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String viewlocationById(@PathVariable String id) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		try {
			Location location = sc.getLocationService().findLocationById(strToInt(id), true, null);
			ArrayList<LocationType> locationTypes = (ArrayList<LocationType>) sc.getLocationService().getAllLocationType(true, null);		
			if(location == null) {}
			else {
				if(location.getVoided()) { }
				else {
					result.put("id", location.getLocationId());
					if(location.getName() == null) { result.put("name", ""); } else { result.put("name", location.getName()); }
					if(location.getShortName() == null) { result.put("shortname", ""); } else { result.put("shortname", location.getShortName()); }
					if(location.getFullName() == null) { result.put("fullname", ""); } else { result.put("fullname", location.getFullName()); }
					if(location.getParentLocation() == null) { result.put("pId", 0); } else { result.put("pId", location.getParentLocation().getLocationId()); }
					if(location.getLocationType() == null) { result.put("type", 0); result.put("typeName", ""); }
					else { for(int j=0; j<locationTypes.size(); j++) { if(locationTypes.get(j).getLocationTypeId() == location.getLocationType().getLocationTypeId()) { result.put("type", location.getLocationType().getLocationTypeId()); result.put("typeName", locationTypes.get(j).getTypeName()); } } }
					if(location.getDescription() == null) { result.put("description", ""); } else { result.put("description", location.getDescription()); }
					if(location.getOtherIdentifier() == null) { result.put("otherIdentifier", ""); } else { result.put("otherIdentifier", location.getOtherIdentifier()); }
					if(location.getLatitude() == null) { result.put("latitude", ""); } else { result.put("latitude", location.getLatitude()); }
					if(location.getLongitude() == null) { result.put("longitude", ""); } else { result.put("longitude", location.getLongitude()); }
					result.put("voided", location.getVoided());
					//result.put("voidedBy", location.getVoidedBy().toString());
					if(location.getDateVoided() == null) { result.put("dateVoided", ""); } else { result.put("dateVoided", location.getDateVoided()); }
					if(location.getVoidReason() == null) { result.put("voidReason", ""); } else { result.put("voidReason", location.getVoidReason()); }
					result.put("open", true);
				} 
			}
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}

	@RequestMapping(value = "/viewlocationtype/id/{id}", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String viewlocationtypeById(@PathVariable String id) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		try {
			LocationType locationType = sc.getLocationService().findLocationTypeById(strToInt(id), true, null);		
			if(locationType == null) {}
			else {
				result.put("id", locationType.getLocationTypeId());
				if(locationType.getTypeName() == null) { result.put("name", ""); } else { result.put("name", locationType.getTypeName()); }
				if(locationType.getLevel() == null) { result.put("level", 0); } else { result.put("level", locationType.getLevel()); }
				if(locationType.getDescription() == null) { result.put("description", ""); } else { result.put("description", locationType.getDescription()); }
			}
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}
	
	@RequestMapping(value = "/viewlocationattributetype/id/{id}", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String viewlocationattributetypeById(@PathVariable String id) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		try {
			LocationAttributeType locationAttributeType = sc.getLocationService().findLocationAttributeTypeById(strToInt(id), true, null);
			if(locationAttributeType == null) {}
			else {
				result.put("id", locationAttributeType.getLocationAttributeTypeId());
				if(locationAttributeType.getAttributeName() == null) { result.put("name", ""); } else { result.put("name", locationAttributeType.getAttributeName()); }
				if(locationAttributeType.getDisplayName() == null) { result.put("displayName", ""); } else { result.put("displayName", locationAttributeType.getDisplayName()); }
				if(locationAttributeType.getDescription() == null) { result.put("description", ""); } else { result.put("description", locationAttributeType.getDescription()); }
				if(locationAttributeType.getCategory() == null) { result.put("category", ""); } else { result.put("category", locationAttributeType.getCategory()); }
			} 
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}
	
	@RequestMapping(value = "/viewlocationattribute/id/{id}", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String viewlocationattributeById(@PathVariable String id) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		try {
			LocationAttribute locationAttribute = sc.getLocationService().findLocationAttributeById(strToInt(id), true, null);		
			if(locationAttribute == null) {}
			else {
				result.put("id", locationAttribute.getLocationAttributeId());
				result.put("typeId", locationAttribute.getLocationAttributeTypeId());
				result.put("locationId", locationAttribute.getLocationId());
				if(locationAttribute.getTypeName() == null) { result.put("type", ""); } else { result.put("type", locationAttribute.getTypeName().replaceAll("_", " ")); }
				if(locationAttribute.getValue() == null) { result.put("value", ""); } else { result.put("value", locationAttribute.getValue()); }
				if(locationAttribute.getTypeName().equals("None")) {}
				else if(locationAttribute.getTypeName().equals("Time_Bound")) { result.put("typeValue", (locationAttribute.getTypeValue1() + " to " + locationAttribute.getTypeValue2())); }
				else { result.put("typeValue", locationAttribute.getTypeValue1()); }
			}
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}
		
	@RequestMapping(value = "/viewlocation/name/{name}", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String viewlocationByName(@PathVariable String name) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		try {
			Location location = sc.getLocationService().findLocationByName(name, true, null);
			ArrayList<LocationType> locationTypes = (ArrayList<LocationType>) sc.getLocationService().getAllLocationType(true, null);		
			if(location == null) {}
			else {
				if(location.getVoided()) { }
				else {
					result.put("id", location.getLocationId());
					if(location.getName() == null) { result.put("name", ""); } else { result.put("name", location.getName()); }
					if(location.getShortName() == null) { result.put("shortname", ""); } else { result.put("shortname", location.getShortName()); }
					if(location.getFullName() == null) { result.put("fullname", ""); } else { result.put("fullname", location.getFullName()); }
					if(location.getParentLocation() == null) { result.put("pId", 0); }
					else { result.put("pId", location.getParentLocation().getLocationId()); }
					if(location.getLocationType() == null) { result.put("type", 0); result.put("typeName", ""); }
					else { for(int j=0; j<locationTypes.size(); j++) { if(locationTypes.get(j).getLocationTypeId() == location.getLocationType().getLocationTypeId()) { result.put("type", location.getLocationType().getLocationTypeId()); result.put("typeName", locationTypes.get(j).getTypeName()); } } }
					if(location.getDescription() == null) { result.put("description", ""); } else { result.put("description", location.getDescription()); }
					if(location.getOtherIdentifier() == null) { result.put("otherIdentifier", ""); } else { result.put("otherIdentifier", location.getOtherIdentifier()); }
					if(location.getLatitude() == null) { result.put("latitude", ""); } else { result.put("latitude", location.getLatitude()); }
					if(location.getLongitude() == null) { result.put("longitude", ""); } else { result.put("longitude", location.getLongitude()); }
					result.put("voided", location.getVoided());
					//result.put("voidedBy", location.getVoidedBy().toString());
					if(location.getDateVoided() == null) { result.put("dateVoided", ""); } else { result.put("dateVoided", location.getDateVoided()); }
					if(location.getVoidReason() == null) { result.put("voidReason", ""); } else { result.put("voidReason", location.getVoidReason()); }
					result.put("open", true);
				} 
			}
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}

	@RequestMapping(value = "/viewlocationtype/name/{name}", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String viewlocationtypeByName(@PathVariable String name) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		try {
			LocationType locationType = sc.getLocationService().findLocationTypeByName(name, true, null);		
			if(locationType == null) {}
			else {
				result.put("id", locationType.getLocationTypeId());
				if(locationType.getTypeName() == null) { result.put("name", ""); } else { result.put("name", locationType.getTypeName()); }
				if(locationType.getLevel() == null) { result.put("level", 0); } else { result.put("level", locationType.getLevel()); }
				if(locationType.getDescription() == null) { result.put("description", ""); } else { result.put("description", locationType.getDescription()); }
			}
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}
	
	@RequestMapping(value = "/viewlocationattributetype/name/{name}", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
	public @ResponseBody String viewlocationattributetypeByName(@PathVariable String name) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		try {
			LocationAttributeType locationAttributeType = sc.getLocationService().findLocationAttributeTypeByName(name, true, null);
			if(locationAttributeType == null) {}
			else {
				result.put("id", locationAttributeType.getLocationAttributeTypeId());
				if(locationAttributeType.getAttributeName() == null) { result.put("name", ""); } else { result.put("name", locationAttributeType.getAttributeName()); }
				if(locationAttributeType.getDisplayName() == null) { result.put("displayName", ""); } else { result.put("displayName", locationAttributeType.getDisplayName()); }
				if(locationAttributeType.getDescription() == null) { result.put("description", ""); } else { result.put("description", locationAttributeType.getDescription()); }
				if(locationAttributeType.getCategory() == null) { result.put("category", ""); } else { result.put("category", locationAttributeType.getCategory()); }
			} 
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}
	
	private Integer strToInt(String str) {
	    if(str.equals("")) return null;
	    else return Integer.parseInt(str);
	}

}
