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
    private Date created_date;
    private Date modified_date;
    private boolean tagsAltered = false;

    public CollectionDS() {
        created_date = java.util.Calendar.getInstance().getTime();
        modified_date = (Date)created_date.clone();
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
        this.tagsAltered = true;
    }

    public int getItemsNumber() {
        return itemsNumber;
    }

    public void setItemsNumber(int itemsNumber) {
        this.itemsNumber = itemsNumber;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public Date getModified_date() {
        return modified_date;
    }

    public void setModified_date(Date modified_date) {
        this.modified_date = modified_date;
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
                created_date,
                modified_date);
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
        obj.setCreatedDate(created_date);
        obj.setModifiedDate(modified_date);
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
        created_date = (Date)obj.getCreatedDate().clone();
        modified_date = (Date)obj.getModifiedDate().clone();
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
        if (tags == null || object.tagsAltered) {
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
                + "\nitemsNumber : " + itemsNumber
                + "\ncreatedDate : " + created_date
                + "\nmodifiedDate: " + modified_date;
    }
}
