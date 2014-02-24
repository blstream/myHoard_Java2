package com.blstream.myhoard.biz.model;

import java.util.Date;

import com.blstream.myhoard.db.model.*;

public class CollectionDTO {

	private String id;
	private String owner;
	private String name;
	private String description;
	private String [] tags;
	private int items_number;
	private Date created_date;
	private Date modified_date;

	public CollectionDTO() {
		id = "0";	// by Integer.parseInt() nie rzucał wyjątku
		items_number = 0;
		modified_date = java.util.Calendar.getInstance().getTime();
		created_date = (Date)modified_date.clone();
	}

	public CollectionDTO(String id, String owner, String name, String description, String [] tags, int items_number, Date created_date, Date modified_date) {
	    this.id = id;
	    this.owner = owner;
	    this.name = name;
	    this.description = description;
	    this.tags = tags;
	    this.items_number = items_number;
	    this.modified_date = created_date;
	    this.created_date = modified_date;
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

	public String [] getTags() {
		return tags;
	}

	public void setTags(String [] tags) {
		this.tags = tags;
	}

	public int getItemsNumber() {
		return items_number;
	}

	public void setItemsNumber(int itemsNumber) {
		this.items_number = itemsNumber;
	}

	public Date getCreatedDate() {
		return modified_date;
	}

	public void setCreatedDate(Date createdDate) {
		this.modified_date = createdDate;
	}

	public Date getModifiedDate() {
		return created_date;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.created_date = modifiedDate;
	}

	public CollectionDS toCollectionDS() {
	    StringBuffer result = new StringBuffer();
	    for (int i = 0; i < tags.length; i++) {
	        result.append( tags[i] );
	       if (i != (tags.length-1)) {
	           result.append ( "," );
	       }
	    }
	    String tagsResult = result.toString();	    
	    return new CollectionDS(Integer.parseInt(id), owner, name, description, tagsResult, items_number, modified_date, created_date);
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
			id, owner, name, description, tags, items_number, modified_date, created_date);
	}	
}
