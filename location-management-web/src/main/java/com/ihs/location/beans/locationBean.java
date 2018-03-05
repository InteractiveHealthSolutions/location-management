package com.ihs.location.beans;

public class locationBean {
	
	private String name;
	private String fullName;
	private String shortName;
	private String description;
	private String latitude; 
	private String longitude;
	private String locationType;
	private Integer parentLocation;
	
	public void setName(String nameLoc){
		this.name = nameLoc;
	}
	public void setFullName(String fullname){
		this.fullName = fullname;
	}
	public void setShortName(String sname){
		this.shortName = sname;
	}
	public void setDescription(String desc){
		this.description = desc;
	}
	public void setLongitude(String longi){
		this.longitude = longi;
	}
	public void setLatitude(String lat){
		this.latitude = lat;
	}
	public void setParentLocation(Integer parentLoc){
		this.parentLocation = parentLoc;
	}
	public void setLocationType(String type){
		this.locationType= type;
	}
	
	public String getName(){
		return name;
	}
	public String getFullName(){
		return fullName;
	}
	public String getShortName(){
		return shortName;
	}
	public String getDescription(){
		return description;
	}
	public String getLatitude(){
		return latitude;
	}
	public String getLongitude(){
		return longitude;
	}
	public Integer getParentLocation(){
		return parentLocation;
	}
	public String getLocationType(){
		return locationType;
	}
}
