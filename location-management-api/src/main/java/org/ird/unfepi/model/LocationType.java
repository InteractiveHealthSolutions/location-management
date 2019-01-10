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
	
	private String displayName;
	
	private Integer level;
	
	private Boolean editable;
	
	public LocationType() { }

	public LocationType(int locationTypeId) {
		setLocationTypeId(locationTypeId);
	}

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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}


}
