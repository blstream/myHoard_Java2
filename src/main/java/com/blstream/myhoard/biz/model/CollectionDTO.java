package com.blstream.myhoard.biz.model;

import java.sql.Timestamp;

import com.blstream.myhoard.db.model.*;

public class CollectionDTO {

	private String id, owner, name, description, tags;
	private int itemsNumber;
	private Timestamp createdDate, modifiedDate;

	public CollectionDTO() {}

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

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public CollectionDS convertToCollectionDS() {
	    CollectionDS collectionD = new CollectionDS(Integer.parseInt(getId()),
	            getOwner(),
	            getName(),
	            getDescription(),
	            getTags(),
	            getItemsNumber(),
	            getCreatedDate(),
	            getModifiedDate());
	    return collectionD;
	}
	
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append("id            : " + id)
		.append("\nowner       : " + owner)
        .append("\nname        : " + name)
        .append("\ndescription : " + description)
		.append("\ntags        : " + tags)
		.append("\nitemsNumber : " + itemsNumber)
		.append("\ncreatedDate : " + createdDate)
		.append("\nmodifiedDate: " + modifiedDate);
		return  string.toString();
	}	
}
