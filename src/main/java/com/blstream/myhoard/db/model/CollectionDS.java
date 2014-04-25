package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.model.CollectionDTO;
import com.blstream.myhoard.biz.model.TagDTO;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CollectionDS {

    private int id;
    private String name;
    private String description;
    private Set<TagDS> tags = new HashSet<>(0);
    private int itemsNumber;
    private Date created_date;
    private Date modified_date;
    private Date created_date_client;
    private Date modified_date_client;
    private UserDS owner;
    private boolean isPublic;
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

    public boolean getIsPublic() {
        return isPublic;
    }
    
    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    
    public UserDS getOwner() {
        return owner;
    }

    public void setOwner(UserDS owner) {
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

    public Date getCreated_date_client() {
        return created_date_client;
    }

    public void setCreated_date_client(Date created_date_client) {
        this.created_date_client = created_date_client;
    }

    public Date getModified_date_client() {
        return modified_date_client;
    }

    public void setModified_date_client(Date modified_date_client) {
        this.modified_date_client = modified_date_client;
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
                owner.toUserDTO(),
                name,
                description,
                set,
                itemsNumber,
                isPublic,
                created_date_client,
                modified_date_client);
    }

    public void toDTO(CollectionDTO obj) {
        Set<TagDTO> set = new HashSet<>();
        if (tags != null)
            for (TagDS i : tags)
                set.add(i.toTagTO());
        obj.setId(Integer.toString(id));
        obj.setOwner(owner.toUserDTO());
        obj.setName(name);
        obj.setDescription(description);
        obj.setTags(set);
        obj.setIsPublic(isPublic);
        obj.setItemsNumber(itemsNumber);
        obj.setCreatedDate(created_date_client);
        obj.setModifiedDate(modified_date_client);
    }

    public void fromDTO(CollectionDTO obj) {
        tags = new HashSet<>();
        if (obj.getTags() != null)
            for (TagDTO i : obj.getTags())
                tags.add(i.toTagDS());
        id = Integer.parseInt(obj.getId());
        owner = obj.getOwner().toUserDS();
        name = obj.getName();
        description = obj.getDescription();
        itemsNumber = obj.getItemsNumber();
        isPublic = obj.getIsPublic();
        modified_date_client = (Date)obj.getModifiedDate().clone();
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
        if (isPublic != object.isPublic)
            isPublic = object.isPublic;
        if (itemsNumber == 0)
            itemsNumber = object.itemsNumber;
        if (object.created_date_client != null)
            created_date_client = object.created_date_client;
        if (object.modified_date_client != null)
            modified_date_client = object.modified_date_client;
    }

    @Override
    public String toString() {
        return "CollectionDS{" + "id=" + id + ", owner=" + owner.getUsername() + ", name=" + name + ", description=" + description + ", tags=" + tags + ", itemsNumber=" + itemsNumber + ", created_date=" + created_date + ", modified_date=" + modified_date + ", tagsAltered=" + tagsAltered + '}';
    }

}
