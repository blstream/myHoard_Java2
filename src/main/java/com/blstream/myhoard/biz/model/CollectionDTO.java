package com.blstream.myhoard.biz.model;

import java.util.Date;
import com.blstream.myhoard.db.model.*;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.validator.constraints.NotEmpty;

public class CollectionDTO {

    private String id;

    @NotNull
    @NotEmpty
    private String name;

    private String description;
    private Set<TagDTO> tags;
    private int itemsNumber;
    private Date createdDate;
    private Date modifiedDate;
    private String owner;

    public CollectionDTO() {
        id = "0";	// by Integer.parseInt() nie rzucał wyjątku
        createdDate = java.util.Calendar.getInstance().getTime();
        modifiedDate = (Date) createdDate.clone();
//        tags = new HashSet<>();
    }

    public CollectionDTO(String id, String owner, String name, String description, Set<TagDTO> tags, int itemsNumber, Date createdDate, Date modifiedDate) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.itemsNumber = itemsNumber;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    @JsonProperty(value = "id")
    public String getId() {
        return id;
    }

    @JsonIgnore
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty(value = "owner")
    public String getOwner() {
        return owner;
    }

    @JsonIgnore
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

    @JsonSerialize(using = CustomTagSerializer.class)
    public Set<TagDTO> getTags() {
        return tags;
    }

    @JsonDeserialize(using = CustomTagDeserializer.class)
    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    @JsonProperty(value = "items_number")
    public int getItemsNumber() {
        return itemsNumber;
    }

    @JsonIgnore
    public void setItemsNumber(int itemsNumber) {
        this.itemsNumber = itemsNumber;
    }

    @JsonProperty(value = "created_date")
    @JsonSerialize(using = CustomDateSerializer.class)
    public Date getCreatedDate() {
        return createdDate;
    }

    @JsonIgnore
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonProperty(value = "modified_date")
    public Date getModifiedDate() {
        return modifiedDate;
    }

    @JsonIgnore
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public CollectionDS toCollectionDS() {
        Set<TagDS> set = new HashSet<>();
        if (tags != null)
            for (TagDTO i : tags)
                set.add(i.toTagDS());
        return new CollectionDS(Integer.parseInt(id), owner, name, description, set, itemsNumber, createdDate, modifiedDate);
    }

    public void updateObject(CollectionDTO object) {
        if (this == object || object == null) {
            return;
        }
        if (name == null || !name.equals(object.name)) {
            name = object.name;
        }
        if (description == null || !description.equals(object.description)) {
            description = object.description;
        }
        if (tags == null || !tags.equals(object.tags)) {
            tags = object.tags;
        }
    }

    @Override
    public String toString() {
        return String.format("id: %d, owner: %s, name: %s, description: %s, tags: %s, itemsNumber: %d, createdDate: %s, modifiedDate: %s\n",
                id, owner, name, description, tags, itemsNumber, createdDate, modifiedDate);
    }
}
