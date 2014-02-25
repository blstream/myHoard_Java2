package com.blstream.myhoard.biz.model;

import java.util.Date;

import com.blstream.myhoard.db.model.*;
import java.util.HashSet;
import java.util.Set;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class CollectionDTO {

	private String id;
	private String owner;
	private String name;
	private String description;
	private Set<String> tags;
	private int items_number;
	private Date created_date;
	private Date modified_date;

	public CollectionDTO() {
		id = "0";	// by Integer.parseInt() nie rzucał wyjątku
		items_number = 0;
		created_date = java.util.Calendar.getInstance().getTime();
		modified_date = (Date)created_date.clone();
		tags = new HashSet<>();
	}

	public CollectionDTO(String id, String owner, String name, String description, Set<String> tags, int itemsNumber, Date createdDate, Date modifiedDate) {
		this.id = id;
		this.owner = owner;
		this.name = name;
		this.description = description;
		this.tags = tags;
		this.items_number = itemsNumber;
		this.created_date = createdDate;
		this.modified_date = modifiedDate;
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

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public int getItems_number() {
		return items_number;
	}

	public void setItems_number(int items_number) {
		this.items_number = items_number;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getModified_date() {
		return modified_date;
	}

	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}

	public CollectionDS toCollectionDS() {
		return new CollectionDS(Integer.parseInt(id), owner, name, description, tags, items_number, created_date, modified_date);
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
			id, owner, name, description, tags, getItems_number(), getCreated_date(), getModified_date());
	}
}
