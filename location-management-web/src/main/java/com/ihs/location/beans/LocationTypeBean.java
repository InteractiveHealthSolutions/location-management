package com.ihs.location.beans;

public class LocationTypeBean {

	private String typeName; 
	private String description;
	private Integer level;
	
	public void setTypeName(String name){
		this.typeName = name;
	}
	public void setDescription(String desc){
		this.description = desc;
	}
	public void setLevel(Integer lev){
		this.level = lev;
	}
	public String getTypeName(){
		return typeName;
	}
	public String getDescription(){
		return description;
	}
	public Integer getLevel(){
		return level;
	}
}
