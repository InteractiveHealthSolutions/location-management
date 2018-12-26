package org.ird.unfepi.model;

import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class BaseLocationObject {
	
	private String name;
	
	private String description;

	private String creator;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;
	
	private String editor;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEdited;
	
	private boolean voided;
	
	private String voider;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateVoided;
	
	public BaseLocationObject() { }

	public abstract Integer getId(); 

	public abstract void setId(Integer id);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public Date getDateEdited() {
		return dateEdited;
	}

	public void setDateEdited(Date dateEdited) {
		this.dateEdited = dateEdited;
	}

	public boolean isVoided() {
		return voided;
	}

	public void setVoided(boolean voided) {
		this.voided = voided;
	}

	public String getVoider() {
		return voider;
	}

	public void setVoider(String voider) {
		this.voider = voider;
	}

	public Date getDateVoided() {
		return dateVoided;
	}

	public void setDateVoided(Date dateVoided) {
		this.dateVoided = dateVoided;
	}
	
}
