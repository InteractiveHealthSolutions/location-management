package org.ird.unfepi.util;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LocationTree;

public class LocationUtils {

	public static LocationTree toTree(List<Location> locations) throws IllegalAccessException, InvocationTargetException {
		LocationTree locationTree = new LocationTree();
		
		// assumption: location list is ordered by level and 
		// first locations must be a root node
		
		boolean processingRootLocations = true;
		
		for (int i = 0; i < locations.size(); i++) {
			Location loc = locations.get(i);
			
			// if still processing root locations
			if(processingRootLocations) {
				// if non root locations started and is not first location set switch to start a non root
				if(loc.getParentLocation() != null && i != 0) {
					processingRootLocations = false;
				}
				// if still processing root locations and parent location is found non null
				else if(loc.getParentLocation() != null){
					throw new IllegalStateException("Location list is not ordered by level. "+i+" row has a non root location "+loc.getName());
				}
			}
			// if not processingRootLocations and no parent specified
			else if(loc.getParentLocation() == null) {
				throw new IllegalStateException("Location list is not ordered by level. "+i+" row has a root location "+loc.getName());
			}
			
			if(processingRootLocations) {
				locationTree.addRootLocation(loc);
			}
			else {
				locationTree.addChildLocation(loc);
			}
		}

		return locationTree;
	}
}
