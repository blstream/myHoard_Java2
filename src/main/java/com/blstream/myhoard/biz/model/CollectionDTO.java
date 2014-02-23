package com.blstream.myhoard.biz.model;

import java.util.Date;

import com.blstream.myhoard.db.model.*;

public class CollectionDTO {

	private String id;
	private String owner;
	private String name;
	private String description;
	private String tags;
	private int itemsNumber;
	private Date createdDate;
	private Date modifiedDate;

	public CollectionDTO() {
		id = "0";	// by Integer.parseInt() nie rzucał wyjątku
		itemsNumber = 0;
		createdDate = java.util.Calendar.getInstance().getTime();
		modifiedDate = (Date)createdDate.clone();
	}

	public CollectionDTO(String id, String owner, String name, String description, String tags, int itemsNumber, Date createdDate, Date modifiedDate) {
	    this.id = id;
	    this.owner = owner;
	    this.name = name;
	    this.description = description;
	    this.tags = tags;
	    this.itemsNumber = itemsNumber;
	    this.createdDate = createdDate;
	    this.modifiedDate = modifiedDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

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

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public int getItemsNumber() {
		return itemsNumber;
	}

	public void setItemsNumber(int itemsNumber) {
		this.itemsNumber = itemsNumber;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public CollectionDS toCollectionDS() {
	    return new CollectionDS(Integer.parseInt(id), owner, name, description, tags, itemsNumber, createdDate, modifiedDate);
	}

	public void updateObject(CollectionDTO object) {
		if (this == object)
			return;
		if (name == null || object.name != null && !name.equals(object.name))
			name = object.name;
		if (description == null || object.description != null && !description.equals(object.description))
			description = object.description;
		if (tags == null || object.tags != null && !tags.equals(object.tags))
			tags = object.tags;
	}
	
	@Override
	public String toString() {
		return String.format("id: %d, owner: %s, name: %s, description: %s, tags: %s, itemsNumber: %d, createdDate: %s, modifiedDate: %s\n",
			id, owner, name, description, tags, itemsNumber, createdDate, modifiedDate);
	}	
}
