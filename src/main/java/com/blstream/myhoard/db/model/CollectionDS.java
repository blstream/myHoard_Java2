package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.model.CollectionDTO;
import com.blstream.myhoard.biz.model.ItemDTO;
import com.blstream.myhoard.biz.model.TagDTO;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CollectionDS {

    private int id;
    private String owner;
    private String name;
    private String description;
    private Set<TagDS> tags;
    private int itemsNumber;
    private Date createdDate;
    private Date modifiedDate;

    public CollectionDS() {
        createdDate = java.util.Calendar.getInstance().getTime();
        modifiedDate = (Date)createdDate.clone();
    }

    public CollectionDS(int id, String owner, String name, String description, Set<TagDS> tags, long itemsNumber, Date createdDate, Date modifiedDate) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.itemsNumber = (int)itemsNumber;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
    
    public CollectionDS(int id, String owner, String name, String description, long itemsNumber, Date createdDate, Date modifiedDate) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.itemsNumber = (int)itemsNumber;
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

    public Set<TagDS> getTags() {
        return tags;
    }

    public void setTags(Set<TagDS> tags) {
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

    public CollectionDTO toCollectionDTO() {
        Set<TagDTO> set = new HashSet<>();
        if (tags != null)
            for (TagDS i : tags)
                set.add(i.toTagTO());
            
        return new CollectionDTO(Integer.toString(id),
                owner,
                name,
                description,
                set,
                itemsNumber,
                createdDate,
                modifiedDate);
    }

    public void updateObject(CollectionDS object) {
        if (this == object || object == null) {
            return;
        }
        if (name == null || object.name != null && !name.equals(object.name)) {
            name = object.name;
        }
        if (description == null || object.description != null && !description.equals(object.description)) {
            description = object.description;
        }
        if (tags == null || object.tags != null && !tags.equals(object.tags)) {
            tags = object.tags;
        }
    }

    @Override
    public String toString() {
        return "id          : " + id
                + "\nowner       : " + owner
                + "\nname        : " + name
                + "\ndescription : " + description
                + "\ntags        : " + tags
                + "\nitemsNumber : " + getItemsNumber()
                + "\ncreatedDate : " + createdDate
                + "\nmodifiedDate: " + modifiedDate;
    }
}
