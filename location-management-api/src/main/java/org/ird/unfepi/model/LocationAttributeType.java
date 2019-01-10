package org.ird.unfepi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="locationattributetype")
public class LocationAttributeType extends BaseLocationObject{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer locationAttributeTypeId;

	private String displayName;
	
	private boolean repeatable;

	private boolean editable;

	private boolean fore;
	
	private String validator;
	
	private String validatorMessage;
	
	private String dataType;
	
	private String category;
	
	public LocationAttributeType() { }

	@Override
	public Integer getId() {
		return locationAttributeTypeId;
	}

	@Override
	public void setId(Integer id) {
		this.locationAttributeTypeId = id;
	}

	public Integer getLocationAttributeTypeId() {
		return locationAttributeTypeId;
	}

	public void setLocationAttributeTypeId(Integer locationAttributeTypeId) {
		this.locationAttributeTypeId = locationAttributeTypeId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isRepeatable() {
		return repeatable;
	}

	public void setRepeatable(boolean repeatable) {
		this.repeatable = repeatable;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isFore() {
		return fore;
	}

	public void setFore(boolean fore) {
		this.fore = fore;
	}

	public String getValidator() {
		return validator;
	}

	public void setValidator(String validator) {
		this.validator = validator;
	}

	public String getValidatorMessage() {
		return validatorMessage;
	}

	public void setValidatorMessage(String validatorMessage) {
		this.validatorMessage = validatorMessage;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	

}
