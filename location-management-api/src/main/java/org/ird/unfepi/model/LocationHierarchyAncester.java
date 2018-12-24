package org.ird.unfepi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "location_hierarchy_ancester")
public class LocationHierarchyAncester {
	
	@Column 
	private Integer locationId;
	
	@Column (length = 45)
	private String name;
	
	@Column
	private Integer relative;
	
	@Column (length = 45)
	private String relativeName;
	
	@Column
	private Integer locationType;

	@Column 
	private Integer relativeLocationType;

	@Column (length = 1024)
	private String ancestory;
	
	@Column 
	private Integer isRelative;
	
	public void setLocationId(Integer location){
		this.locationId = location;
	}
	public Integer getLocationId(){
		return locationId;
	}
	
	public void setName (String locationName){
		this.name = locationName;
	}
	public String getName(){
		return name;
	}
	
	public void setRelative(Integer rel){
		this.relative = rel;
	}
	public Integer getRelative(){
		return relative;
	}
	
	public void setRelativeName(String relName){
		this.relativeName = relName;
	}
	public String getRelativeName(){
		return relativeName;
	}
	
	public void setLocationType(Integer locationtype){
		this.locationType = locationtype;
	}
	public Integer getLocationType(){
		return locationType;
	}
	
	public void setRelativeLocationType(Integer relLocationtype){
		this.relativeLocationType = relLocationtype;
	}
	public Integer getRelativeLocationType(){
		return relativeLocationType;
	}
	
	public void setIsRelative(Integer isrelative){
		this.isRelative = isrelative;
	}
	public Integer getIsRelative(){
		return isRelative;
	}
}

