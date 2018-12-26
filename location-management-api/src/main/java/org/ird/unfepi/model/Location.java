package org.ird.unfepi.model;

import java.util.Date;

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
@Table(name = "location")
public class Location extends BaseLocationObject {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer locationId;
	
	private String fullName;
	
	private String shortName;
	
	private String otherIdentifier;
	
	private String geopoint;
	
	private String latitude;
	
	private String longitude;
	
	private String ancestry;
	
	private String ancestryDetail;
	
	private boolean active;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOpened;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateClosed;
	
	@ManyToOne(targetEntity = Location.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "parentLocation")
	@ForeignKey(name = "location_parentLocation_location_locationId_FK")
	private Location parentLocation;
	
	@OneToOne(targetEntity = LocationType.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "locationType")
	@ForeignKey(name = "loaction_locationTypeId_locationType_locationTypeId_FK")
	private LocationType locationType;
	
	//TODO add location tags
	
	public Location() { }

	@Override
	public Integer getId() {
		return locationId;
	}

	@Override
	public void setId(Integer id) {
		this.locationId = id;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getOtherIdentifier() {
		return otherIdentifier;
	}

	public void setOtherIdentifier(String otherIdentifier) {
		this.otherIdentifier = otherIdentifier;
	}

	public String getGeopoint() {
		return geopoint;
	}

	public void setGeopoint(String geopoint) {
		this.geopoint = geopoint;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAncestry() {
		return ancestry;
	}

	public void setAncestry(String ancestry) {
		this.ancestry = ancestry;
	}

	public String getAncestryDetail() {
		return ancestryDetail;
	}

	public void setAncestryDetail(String ancestryDetail) {
		this.ancestryDetail = ancestryDetail;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getDateOpened() {
		return dateOpened;
	}

	public void setDateOpened(Date dateOpened) {
		this.dateOpened = dateOpened;
	}

	public Date getDateClosed() {
		return dateClosed;
	}

	public void setDateClosed(Date dateClosed) {
		this.dateClosed = dateClosed;
	}

	public Location getParentLocation() {
		return parentLocation;
	}

	public void setParentLocation(Location parentLocation) {
		this.parentLocation = parentLocation;
	}

	public LocationType getLocationType() {
		return locationType;
	}

	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}
		
}
