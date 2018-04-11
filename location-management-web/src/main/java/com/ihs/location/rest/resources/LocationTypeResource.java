package com.ihs.location.rest.resources;

import java.text.ParseException;

import javax.management.InstanceAlreadyExistsException;

import org.codehaus.jettison.json.JSONException;
import org.ird.unfepi.context.LocationContext;
import org.ird.unfepi.context.LocationServiceContext;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LocationType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ihs.location.beans.LocationBean;
import com.ihs.location.beans.LocationTypeBean;

@RestController
@RequestMapping("/locationType")
public class LocationTypeResource {

	@RequestMapping(value = "/addlocationtype", method = {RequestMethod.POST})
	public @ResponseBody String addlocation(@RequestBody LocationTypeBean location) throws JSONException, InstanceAlreadyExistsException {
		LocationServiceContext sc = LocationContext.getServices();
		try{
			LocationType locType = new LocationType();
			locType.setTypeName(location.getTypeName());
			locType.setDescription(location.getDescription());
			locType.setLevel(location.getLevel());
			sc.getLocationService().addLocationType(locType);
			sc.commitTransaction();
			return "Sucess";
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		return "Failure";
	}
	
	@RequestMapping(value="/update/{identifier}", method = RequestMethod.PUT , consumes = "application/json")
	public @ResponseBody String updateLocation(@PathVariable("identifier") String identifier , @RequestBody LocationTypeBean location) throws ParseException{
		LocationServiceContext sc = LocationContext.getServices();
		try{
			LocationType locType = sc.getLocationService().findLocationTypeById(Integer.parseInt(identifier), false, null);
			locType.setTypeName(location.getTypeName());
			locType.setDescription(location.getDescription());
			locType.setLevel(location.getLevel());
			sc.getLocationService().updateLocationType(locType);
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