package com.ihs.locationmanagement.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="locationattributetype")
public class LocationAttributeType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="locationAttributeTypeId")
	public Integer locationAttributeTypeId;
	
	@Column(name="attributeName")
	public String attributeName;

	@Column(name="displayName")
	public String displayName;

	@Column(name="description")
	public String description;

	@Column(name="category")
	public String category;
	
	public LocationAttributeType() {}

	public Integer getLocationAttributeTypeId() {
		return locationAttributeTypeId;
	}

	public void setLocationAttributeTypeId(Integer locationAttributeTypeId) {
		this.locationAttributeTypeId = locationAttributeTypeId;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
