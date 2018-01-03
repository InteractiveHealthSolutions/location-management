package org.ird.unfepi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name="locationattribute")
public class LocationAttribute {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="locationAttributeId")
	public Integer locationAttributeId;

	@Column(name="value")
	public String value;
	
	@Column(name="locationId")
	public Integer locationId;
	
	@Column(name="locationAttributeTypeId")
	public Integer locationAttributeTypeId;
	
	@Column(name="typeName")
	public String typeName;
	
	@Column(name="typeValue1")
	public String typeValue1;
	
	@Column(name="typeValue2")
	public String typeValue2;
	
	/** The location. */
	@OneToOne(targetEntity = Location.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "locationId", insertable = false, updatable = false)
	@ForeignKey(name = "locationattribute_locationId_location_locationId_FK")
	private Location location;
	
	/** The locationAttributeType. */
	@OneToOne(targetEntity = LocationAttributeType.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "locationAttributeTypeId", insertable = false, updatable = false)
	@ForeignKey(name = "locattr_locAttrTypeId_locAttrType_locAttrTypeId_FK")
	private LocationAttributeType locationAttributeType;
	
	private String createdByUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	private String lastEditedByUserId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;

	public LocationAttribute() {}

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

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Integer getLocationAttributeTypeId() {
		return locationAttributeTypeId;
	}

	public void setLocationAttributeTypeId(Integer locationAttributeTypeId) {
		this.locationAttributeTypeId = locationAttributeTypeId;
	}

	public LocationAttributeType getLocationAttributeType() {
		return locationAttributeType;
	}

	public void setLocationAttributeType(LocationAttributeType locationAttributeType) {
		this.locationAttributeType = locationAttributeType;
	}
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeValue1() {
		return typeValue1;
	}

	public void setTypeValue1(String typeValue1) {
		this.typeValue1 = typeValue1;
	}

	public String getTypeValue2() {
		return typeValue2;
	}

	public void setTypeValue2(String typeValue2) {
		this.typeValue2 = typeValue2;
	}
	
	/**
	 * Gets the created by user id.
	 *
	 * @return the created by user id
	 */
	public String getCreatedByUserId() {
		return createdByUserId;
	}

	/**
	 * Sets the created by user id.
	 *
	 * @param createdByUserId the new created by user id
	 */
	public void setCreatedByUserId(String createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	/**
	 * Gets the created date.
	 *
	 * @return the created date
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets the created date.
	 *
	 * @param createdDate the new created date
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets the last edited by user id.
	 *
	 * @return the last edited by user id
	 */
	public String getLastEditedByUserId() {
		return lastEditedByUserId;
	}

	/**
	 * Sets the last edited by user id.
	 *
	 * @param lastEditedByUserId the new last edited by user id
	 */
	public void setLastEditedByUserId(String lastEditedByUserId) {
		this.lastEditedByUserId = lastEditedByUserId;
	}

	/**
	 * Gets the last edited date.
	 *
	 * @return the last edited date
	 */
	public Date getLastEditedDate() {
		return lastEditedDate;
	}

	/**
	 * Sets the last edited date.
	 *
	 * @param lastEditedDate the new last edited date
	 */
	public void setLastEditedDate(Date lastEditedDate) {
		this.lastEditedDate = lastEditedDate;
	}
}
