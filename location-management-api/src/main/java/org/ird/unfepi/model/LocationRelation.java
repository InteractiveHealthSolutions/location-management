package org.ird.unfepi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * This entity represents a closure table for maintaining relationships between locations and their ancestors.
 * The entity is not fully normalized and makes the locationType, names for location and ancestor replicate for
 * performance reasons.
 * Each location has info of its all ancestors.
 */
@Entity
@Table(name = "location_relation")
public class LocationRelation {
	
	private Integer locationId;
	
	private String name;
	
	private Integer parentId;

	private Integer relativeId;
	
	private String relativeName;
	
	private Integer locationType;

	private Integer relativeLocationType;

	private Integer level;

	@Column (length = 2048)
	private String ancestory;
	
	public LocationRelation() { }

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getRelativeId() {
		return relativeId;
	}

	public void setRelativeId(Integer relativeId) {
		this.relativeId = relativeId;
	}

	public String getRelativeName() {
		return relativeName;
	}

	public void setRelativeName(String relativeName) {
		this.relativeName = relativeName;
	}

	public Integer getLocationType() {
		return locationType;
	}

	public void setLocationType(Integer locationType) {
		this.locationType = locationType;
	}

	public Integer getRelativeLocationType() {
		return relativeLocationType;
	}

	public void setRelativeLocationType(Integer relativeLocationType) {
		this.relativeLocationType = relativeLocationType;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getAncestory() {
		return ancestory;
	}

	public void setAncestory(String ancestory) {
		this.ancestory = ancestory;
	}
	
}

