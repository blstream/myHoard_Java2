package com.blstream.myhoard.db.model;

import java.util.Date;

public class CollectionDS {

	private int id;
	private String owner;
	private String name;
	private String description;
	private String tags;
	private int itemsNumber;
	private Date createdDate;
	private Date modifiedDate;

	public CollectionDS() {}

	public CollectionDS(int id, String owner, String name, String description, String tags, int itemsNumber, Date createdDate, Date modifiedDate) {
	    this.id = id;
	    this.owner = owner;
	    this.name = name;
	    this.description = description;
	    this.tags = tags;
	    this.itemsNumber = itemsNumber;
	    this.createdDate = createdDate;
	    this.modifiedDate = modifiedDate;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	@Override
	public String toString() {
		return  "id            : " + id +
				"\nowner       : " + owner +
				"\nname        : " + name +
				"\ndescription : " + description +
				"\ntags        : " + tags +
				"\nitemsNumber : " + itemsNumber +
				"\ncreatedDate : " + createdDate +
				"\nmodifiedDate: " + modifiedDate;
	}
}
