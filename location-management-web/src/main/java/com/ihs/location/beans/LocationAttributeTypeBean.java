package com.ihs.location.beans;

public class LocationAttributeTypeBean {

	private String attributeTypeName;
	private String displayName;
	private String category;
	private String description;

	public void setAttributeTypeName(String name){
		this.attributeTypeName = name;
	}
	public void setDisplayName(String name){
		this.displayName = name;
	}
	public void setCategory(String cat){
		this.category = cat;
	}
	public void setDescription(String desc){
		this.description = desc;
	}
	public String getAttributeTypeName(){
		return attributeTypeName;
	}
	public String getDisplayName(){
		return displayName;
	}
	public String getCategory(){
		return category;
	}
	public String getDesription(){
		return description;
	}
}
