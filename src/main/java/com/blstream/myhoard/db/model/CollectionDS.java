package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.model.CollectionDTO;
import com.blstream.myhoard.biz.model.TagDTO;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CollectionDS {

    private int id;
    private String owner;
    private String name;
    private String description;
    private Set<TagDS> tags = new HashSet<>(0);
    private int itemsNumber;
    private Date createdDate;
    private Date modifiedDate;
    private boolean tagsAltered = false;

    public CollectionDS() {
        createdDate = java.util.Calendar.getInstance().getTime();
        modifiedDate = (Date)createdDate.clone();
    }

//    public CollectionDS(int id, String owner, String name, String description, Set<TagDS> tags, long itemsNumber, Date createdDate, Date modifiedDate) {
//        this.id = id;
//        this.owner = owner;
//        this.name = name;
//        this.description = description;
//        this.tags = tags;
//        this.itemsNumber = (int)itemsNumber;
//        this.createdDate = createdDate;
//        this.modifiedDate = modifiedDate;
//    }

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
        this.tagsAltered = true;
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

    public boolean isTagsAltered() {
        return tagsAltered;
    }

    public CollectionDTO toDTO() {
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

    public void toDTO(CollectionDTO obj) {
        Set<TagDTO> set = new HashSet<>();
        if (tags != null)
            for (TagDS i : tags)
                set.add(i.toTagTO());
        obj.setId(Integer.toString(id));
        obj.setOwner(owner);
        obj.setName(name);
        obj.setDescription(description);
        obj.setTags(set);
        obj.setItemsNumber(itemsNumber);
        obj.setCreatedDate(createdDate);
        obj.setModifiedDate(modifiedDate);
    }

    public void fromDTO(CollectionDTO obj) {
        tags = new HashSet<>();
        if (obj.getTags() != null)
            for (TagDTO i : obj.getTags())
                tags.add(i.toTagDS());
        id = Integer.parseInt(obj.getId());
        owner = obj.getOwner();
        name = obj.getName();
        description = obj.getDescription();
        itemsNumber = obj.getItemsNumber();
        createdDate = (Date)obj.getCreatedDate().clone();
        modifiedDate = (Date)obj.getModifiedDate().clone();
        tagsAltered = obj.isTagsAltered();
    }

    public void updateObject(CollectionDS object) {
        if (this == object || object == null)
            return;
        if (owner == null || object.owner != null && !owner.equals(object.owner))
            owner = object.owner;
        if (name == null || object.name != null && !name.equals(object.name))
            name = object.name;
        if (description == null || object.description != null && !description.equals(object.description))
            description = object.description;
        if (tags == null || !object.tagsAltered) {
            tags = object.tags;
            tagsAltered = object.tagsAltered;
        }
        if (itemsNumber == 0)
            itemsNumber = object.itemsNumber;
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
