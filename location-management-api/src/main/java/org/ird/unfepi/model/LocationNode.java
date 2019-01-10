package org.ird.unfepi.model;

import java.util.ArrayList;
import java.util.List;

public class LocationNode {
	private String key;
	private String path = "";
	private Location data;
	private List<LocationNode> children;
	
	public LocationNode(Location location, String path) {
		this.key = location.getLocationId()+"";
		this.data = location;
		this.path = path;
	}
	
	public Location getData() {
		return data;
	}

	public void setData(Location location) {
		this.data = location;
	}

	public List<LocationNode> getChildren() {
		if(this.children == null) {
			this.children = new ArrayList<>();
		}
		return children;
	}
	
	public String getPath() {
		return path;
	}

	public String getKey() {
		return key;
	}

	public void removeChildren() {
		if(this.children == null) {
			return;
		}
		this.children.clear();
	}
	
	public void removeChild(LocationNode child) {
		this.children.remove(child);
	}
	
	public void removeChild(Integer childId) {
		for (LocationNode node : children) {
			if(node.data.getLocationId() == childId.intValue()) {
				children.remove(node);
				break;
			}
		}
	}

	public void addChildren(List<LocationNode> children) {
		if(this.children == null) {
			this.children = new ArrayList<>();
		}
		
		this.children.addAll(children);
	}
	
	public void addChild(LocationNode child) {
		if(this.children == null) {
			this.children = new ArrayList<>();
		}
		
		this.children.add(child);
	}
	
	@Override
	public String toString() {
		return data.getLocationId() + (children != null ? children.toString() : "");
	}
}