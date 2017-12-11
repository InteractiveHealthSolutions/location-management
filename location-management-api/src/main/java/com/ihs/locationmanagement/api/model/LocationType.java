package com.ihs.locationmanagement.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "locationtype")
public class LocationType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer locationTypeId;
	
	@Column(length = 45, unique = true)
	private String typeName;
	
	@Column(name="level")
	public Integer level;
	
	@Column(name="description")
	private String description;
	
	
	public LocationType() {
	}

	public Integer getLocationTypeId() {
		return locationTypeId;
	}

	public void setLocationTypeId(Integer locationTypeId) {
		this.locationTypeId = locationTypeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
