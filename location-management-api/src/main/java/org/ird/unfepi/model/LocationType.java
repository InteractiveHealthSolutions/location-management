package org.ird.unfepi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "locationtype")
public class LocationType extends BaseLocationObject{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer locationTypeId;
	
	private Integer level;
	
	private Boolean isEditable;
	
	public LocationType() { }

	@Override
	public Integer getId() {
		return locationTypeId;
	}

	@Override
	public void setId(Integer id) {
		this.locationTypeId = id;
	}

	public Integer getLocationTypeId() {
		return locationTypeId;
	}

	public void setLocationTypeId(Integer locationTypeId) {
		this.locationTypeId = locationTypeId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

}
