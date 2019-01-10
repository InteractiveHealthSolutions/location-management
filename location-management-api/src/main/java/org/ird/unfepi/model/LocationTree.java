package org.ird.unfepi.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.StringUtils;

public class LocationTree extends ArrayList<LocationNode>{
	
	private static final long serialVersionUID = 1L;
	
	public void addRootLocation(Location location) throws IllegalAccessException, InvocationTargetException {
		if(location.getParentLocation() != null) {
			throw new IllegalStateException("A child location was added as root location");
		}
		
		add(new LocationNode(location, ""));
	}
	
	public void addChildLocation(Location location) throws IllegalAccessException, InvocationTargetException {
		if(location.getParentLocation() == null) {
			throw new IllegalStateException("A root location was added as child location");
		}
		
		// get node from given tree
		Integer parentId = location.getParentLocation().getLocationId();

		// if node is not in main / given tree find in children nodes of each node
		LocationNode parentage = findLocationTree(parentId, this);

		if(parentage == null) {
			throw new IllegalStateException("Messed up or unordered hierarchy. No parent was found for child location "+location.getLocationId());
		}
		
		parentage.getChildren().add(new LocationNode(location, makePath(parentage.getPath(), parentId)));
	}
	
	private String makePath(String parentPath, Integer parentLocation) {
		if(StringUtils.isEmptyOrWhitespaceOnly(parentPath)) {
			return parentLocation+"";
		}
		return parentPath + ","+parentLocation;
	}
	
	private static LocationNode findLocationTree(int locationId, List<LocationNode> nodes) {
		for (LocationNode node : nodes) {
			if(node.getData().getLocationId() == locationId) {
				return node;
			}
			
			if(node.getChildren().size() > 0) {
				// recursively find in children nodes in tree and return if any matches
				LocationNode result = findLocationTree(locationId, node.getChildren());
				if(result != null) {
					return result;
				}
			}
		}
		return null;
	}
	
}
