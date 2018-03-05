package com.ihs.location.rest.resources;

import javax.management.InstanceAlreadyExistsException;

import org.codehaus.jettison.json.JSONException;
import org.ird.unfepi.context.LocationContext;
import org.ird.unfepi.context.LocationServiceContext;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LocationType;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ihs.location.beans.locationBean;

@RestController
@RequestMapping("/refactor")
public class LocationResource {
	
	@RequestMapping(value = "/newlocation", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody String addlocation(@RequestBody String location) throws JSONException, InstanceAlreadyExistsException {
		LocationServiceContext sc = LocationContext.getServices();
		try{
//			Location parentLocation = sc.getLocationService().findLocationById(location.getParentLocation(), true, null) ;
	//		LocationType type = sc.getLocationService().findLocationTypeById(Integer.parseInt(location.getLocationType()), true, null);
			Location loc = new Location();
////			loc.setFullName(location.getFullName());
//			loc.setName(location.getName());
//			loc.setDescription(location.getDescription());
//			loc.setLocationType(type);
//			loc.setLocationType(type);
//			loc.setLatitude(location.getLatitude());
//			loc.setLongitude(location.getLongitude());
			sc.getLocationService().addLocation(loc);
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
	
}


//add, update, delete, view locations