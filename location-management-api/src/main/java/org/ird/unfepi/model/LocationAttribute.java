package org.ird.unfepi.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name="locationattribute")
public class LocationAttribute extends BaseLocationObject{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer locationAttributeId;

	private String value;
	
	private String rangeValue1;
	
	private String rangeValue2;
	
	@OneToOne(targetEntity = Location.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "locationId")
	@ForeignKey(name = "locationattribute_locationId_location_locationId_FK")
	private Location location;
	
	@OneToOne(targetEntity = LocationAttributeType.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "locationAttributeTypeId")
	@ForeignKey(name = "locattr_locAttrTypeId_locAttrType_locAttrTypeId_FK")
	private LocationAttributeType locationAttributeType;
	
	public LocationAttribute() {}

	@Override
	public Integer getId() {
		return locationAttributeId;
	}

	@Override
	public void setId(Integer id) {
		this.locationAttributeId = id;
	}

	public Integer getLocationAttributeId() {
		return locationAttributeId;
	}

	public void setLocationAttributeId(Integer locationAttributeId) {
		this.locationAttributeId = locationAttributeId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRangeValue1() {
		return rangeValue1;
	}

	public void setRangeValue1(String rangeValue1) {
		this.rangeValue1 = rangeValue1;
	}

	public String getRangeValue2() {
		return rangeValue2;
	}

	public void setRangeValue2(String rangeValue2) {
		this.rangeValue2 = rangeValue2;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public LocationAttributeType getLocationAttributeType() {
		return locationAttributeType;
	}

	public void setLocationAttributeType(LocationAttributeType locationAttributeType) {
		this.locationAttributeType = locationAttributeType;
	}

}
