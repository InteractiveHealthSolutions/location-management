package com.ihs.location.rest.resources;

import java.text.ParseException;

import org.ird.unfepi.context.LocationContext;
import org.ird.unfepi.context.LocationServiceContext;
import org.ird.unfepi.model.LocationAttributeType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ihs.location.beans.LocationAttributeTypeBean;

@RestController
@RequestMapping("/locationAttributeType")
public class LocationAttributeTypeResource {

	@RequestMapping(value = "/addlocationattributetype", method = {RequestMethod.POST})
	public @ResponseBody String addlocation(@RequestBody LocationAttributeTypeBean locationattr) {
		LocationServiceContext sc = LocationContext.getServices();
		try{
			LocationAttributeType att = new LocationAttributeType();
			att.setAttributeName(locationattr.getAttributeTypeName());
			att.setCategory(locationattr.getCategory());
			att.setDisplayName(locationattr.getDisplayName());
			att.setDescription(locationattr.getDesription());
			sc.getLocationService().addLocationAttributeType(att);
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
	
	@RequestMapping(value="/update/{identifier}", method = RequestMethod.PUT , consumes = "application/json")
	public @ResponseBody String updateLocation(@PathVariable("identifier") String identifier , @RequestBody LocationAttributeTypeBean locationattr) throws ParseException{
		LocationServiceContext sc = LocationContext.getServices();
		try{
			LocationAttributeType att = new LocationAttributeType();
			att.setAttributeName(locationattr.getAttributeTypeName());
			att.setCategory(locationattr.getCategory());
			att.setDisplayName(locationattr.getDisplayName());
			att.setDescription(locationattr.getDesription());
			sc.getLocationService().addLocationAttributeType(att);
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
