package com.guardian.data;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "elements")
public class ElementEntity {

	@Id
	private String id;
	private String type;
	private String icon;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private ElementCreator createdBy;
	private ElementLocation location;
	private Map<String, Object> elementAttribute;

	public ElementEntity() {
		// TODO Auto-generated constructor stub
	}

	// There are two elements which got created using the database which are: id and
	// created date
	public ElementEntity(String type, String icon, String name, Boolean active, ElementCreator createdBy,
			ElementLocation location, Map<String, Object> elementAttribute) {
		super();
		this.type = type;
		this.icon = icon;
		this.name = name;
		this.active = active;
		this.createdBy = createdBy;
		this.location = location;
		this.elementAttribute = elementAttribute;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public ElementCreator getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(ElementCreator createdBy) {
		this.createdBy = createdBy;
	}

	public ElementLocation getLocation() {
		return location;
	}

	public void setLocation(ElementLocation location) {
		this.location = location;
	}

	public Map<String, Object> getElementAttribute() {
		return elementAttribute;
	}

	public void setElementAttribute(Map<String, Object> elementAttribute) {
		this.elementAttribute = elementAttribute;
	}

	@Override
	public String toString() {
		return "ElementEntity [id=" + id + ", type=" + type + ", icon=" + icon + ", name=" + name + ", active=" + active
				+ ", createdTimestamp=" + createdTimestamp + ", createdBy=" + createdBy + ", location=" + location
				+ ", elementAttribute=" + elementAttribute + "]";
	}
	
}
