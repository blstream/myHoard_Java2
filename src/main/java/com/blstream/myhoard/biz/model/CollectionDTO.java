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
