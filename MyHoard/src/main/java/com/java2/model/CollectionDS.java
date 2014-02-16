package com.java2.model;

import java.sql.Timestamp;

public class CollectionDS {

	private int id;
	private String owner, name, description, tags;
	private int itemsNumber;
	private Timestamp createdDate, modifiedDate;

	public CollectionDS() {}

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
