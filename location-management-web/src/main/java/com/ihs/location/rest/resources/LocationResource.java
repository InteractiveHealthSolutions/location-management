package com.ihs.location.rest.resources;

import java.text.ParseException;

import javax.management.InstanceAlreadyExistsException;

import org.codehaus.jettison.json.JSONException;
import org.ird.unfepi.context.LocationContext;
import org.ird.unfepi.context.LocationServiceContext;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LocationType;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ihs.location.beans.LocationBean;

@RestController
@RequestMapping("/refactor")
public class LocationResource {
	
	@RequestMapping(value = "/newlocation", method = {RequestMethod.POST})
	public @ResponseBody String addlocation(@RequestBody LocationBean location) throws JSONException, InstanceAlreadyExistsException {
		LocationServiceContext sc = LocationContext.getServices();
		try{
			Location parentLocation = sc.getLocationService().findLocationById(location.getParentLocation(), true, null) ;
			LocationType type = sc.getLocationService().findLocationTypeById(Integer.parseInt(location.getLocationType()), true, null);
			
			if(parentLocation.getLocationType().getLevel() > type.getLevel()){
				return "Failure";
			}
			Location loc = new Location();
			loc.setParentLocation(parentLocation);
			loc.setFullName(location.getFullName());
			loc.setName(location.getName());
			loc.setDescription(location.getDescription());
			loc.setLocationType(type);
			loc.setShortName(location.getShortName());
			loc.setLatitude(location.getLatitude());
			loc.setLongitude(location.getLongitude());
			sc.getLocationService().addLocation(loc);
			sc.commitTransaction();
			return "Success";
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		return "failure";
	}

	@RequestMapping(value="/update/{identifier}", method = RequestMethod.PUT , consumes = "application/json")
	public @ResponseBody String updateLocation(@PathVariable("identifier") String identifier , @RequestBody LocationBean location) throws ParseException{
		LocationServiceContext sc = LocationContext.getServices();
		try{
			Location loc = sc.getLocationService().findLocationById(Integer.parseInt(identifier), false, null);
			Location parentLocation = sc.getLocationService().findLocationById(location.getParentLocation(), true, null) ;
			LocationType type = sc.getLocationService().findLocationTypeById(Integer.parseInt(location.getLocationType()), true, null);
			if(parentLocation.getLocationType().getLevel() > type.getLevel()){
				return "Failure";
			}
			loc.setFullName(location.getFullName());
			loc.setName(location.getName());
			loc.setLongitude(location.getLongitude());
			loc.setLatitude(location.getLatitude());
			loc.setShortName(location.getShortName());
			loc.setDescription(location.getDescription());
			loc.setLocationType(type);
			loc.setParentLocation(parentLocation);
			sc.getLocationService().updateLocation(loc);
			sc.commitTransaction();
			return "Success";
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		return "Failure";
	}
	
	@RequestMapping(value = "/deletelocation/{identifier}", method = {RequestMethod.PUT})
	public @ResponseBody String deletelocation(@PathVariable("identifier") String identifier) throws JSONException, InstanceAlreadyExistsException {
		LocationServiceContext sc = LocationContext.getServices();
		try{
			Location loc = sc.getLocationService().findLocationById(Integer.parseInt(identifier), false, null);
			loc.setVoided(true);
			sc.getLocationService().updateLocation(loc);
			sc.commitTransaction();
			return "Success";
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		return "Failure";
	}

}
