package com.ihs.locationmanagement.api.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.ForeignKey;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "location")
public class Location {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer locationId;
	
	@Column(length = 30, unique = true)
	private String name;
	
	@Column(length = 50)
	private String fullName;
	
	@Column(length = 30)
	private String shortName;
	
	private String otherIdentifier;
	
	private String latitude;
	
	private String longitude;
	
	@ManyToOne(targetEntity = Location.class, fetch = FetchType.LAZY)//, cascade=CascadeType.ALL

	@JoinColumn(name = "parentLocation")
	@ForeignKey(name = "location_parentLocation_location_locationId_FK")
	@JsonIgnore
	private Location parentLocation;
	
	@OneToMany(fetch = FetchType.EAGER, targetEntity = Location.class, mappedBy = "parentLocation")
	private Set<Location> childLocations;
	
	@ManyToOne(targetEntity = LocationType.class)//, cascade=CascadeType.ALL
	@ForeignKey(name = "loaction_locationTypeId_locationType_locationTypeId_FK")
	@JoinColumn(name = "locationType")
	private LocationType locationType;
	
	private String description;
	
	private boolean voided;
	
	private Integer voidedBy;
	
	private String voidReason;
	
	private Date dateVoided;
	
	public Location() {
	}

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

	public Location getParentLocation() {
		return parentLocation;
	}

	public void setParentLocation(Location parentLocation) {
		this.parentLocation = parentLocation;
	}

	public Set<Location> getChildLocations() {
		return childLocations;
	}

	public void setChildLocations(Set<Location> childLocations) {
		this.childLocations = childLocations;
	}

	public LocationType getLocationType() {
		return locationType;
	}

	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getVoided() {
		return voided;
	}

	public void setVoided(boolean voided) {
		this.voided = voided;
	}


	public Date getDateVoided() {
		return dateVoided;
	}

	public void setDateVoided(Date dateVoided) {
		this.dateVoided = dateVoided;
	}

	public String getVoidReason() {
		return voidReason;
	}

	public void setVoidReason(String voidReason) {
		this.voidReason = voidReason;
	}

	public Integer getVoidedBy() {
		return voidedBy;
	}

	public void setVoidedBy(Integer voidedBy) {
		this.voidedBy = voidedBy;
	}

	public void addChildLocation(Location child) throws Exception {
		if (child == null)
			return;
		
		if (getChildLocations() == null)
			childLocations = new HashSet<Location>();
		
		if (child.equals(this))
			throw new Exception("A location cannot be its own child!");
		
		// Traverse all the way up (down?) to the root, then check whether the child is already
		// anywhere in the tree
		Location root = this;
		while (root.getParentLocation() != null)
			root = root.getParentLocation();
		
		if (isInHierarchy(child, root))
			throw new Exception("Location hierarchy loop detected! You cannot add: '" + child + "' to the parent: '"
			        + this
			        + "' because it is in the parent hierarchy somewhere already and a location cannot be its own parent.");
		
		child.setParentLocation(this);
		childLocations.add(child);
	}
	
	/**
	 * Checks whether 'location' is a member of the tree starting at 'root'.
	 * 
	 * @param location The location to be tested.
	 * @param root Location node from which to start the testing (down in the hierarchy).
	 * @since 1.5
	 * @should return false given any null parameter
	 * @should return true given same object in both parameters
	 * @should return true given location that is already somewhere in hierarchy
	 * @should return false given location that is not in hierarchy
	 * @should should find location in hierarchy
	 */
	public static Boolean isInHierarchy(Location location, Location root) {
		if (root == null)
			return false;
		while (true) {
			if (location == null)
				return false;
			else if (root.equals(location))
				return true;
			location = location.getParentLocation();
		}
	}
	
}
