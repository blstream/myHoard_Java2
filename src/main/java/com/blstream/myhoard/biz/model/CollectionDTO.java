package com.blstream.myhoard.biz.model;

import java.util.Date;
import com.blstream.myhoard.db.model.*;
import com.blstream.myhoard.validator.CheckString;
import com.blstream.myhoard.validator.ValidationOpt;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.validator.constraints.NotEmpty;

public class CollectionDTO {

    @JsonIgnore
    private String id = "0";

    @NotNull(message = "Nazwa kolekcji jest wymagana")
    @NotEmpty(message = "Nazwa kolekcji jest wymagana")
    @Size(min = 2, max = 64, message = "Nazwa musi zawierać od 2 do 64 znaków")
    @CheckString(message = "Problem z białymi znakami w nazwie", value = ValidationOpt.COLLECTION_NAME)
    private String name;

    @Size(max = 900, message = "Opis może się składać z co najwyżej 900 znaków")
    private String description;

    private Set<TagDTO> tags = new HashSet<>(0);
    @JsonIgnore
    private int itemsNumber;
//    @JsonIgnore
    private Date createdDate;
//    @JsonIgnore
    private Date modifiedDate;
    private UserDTO owner;
    private boolean visible;

    @JsonIgnore
    private boolean tagsAltered = false;

    public CollectionDTO() {
//        createdDate = java.util.Calendar.getInstance().getTime();
//        modifiedDate = (Date) createdDate.clone();
    }

    public CollectionDTO(String id, UserDTO owner, String name, String description, Set<TagDTO> tags, int itemsNumber, boolean visible, Date createdDate, Date modifiedDate) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.itemsNumber = itemsNumber;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.visible = visible;
    }

    @JsonProperty(value = "id")
    public String getId() {
        return id;
    }

    @JsonIgnore
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty(value = "public")
    public boolean isVisible() {
        return visible;
    }
    
    @JsonProperty(value = "public")
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    @JsonProperty(value = "owner")
    @JsonSerialize(using = CustomOwnerSerializer.class)
    public UserDTO getOwner() {
        return owner;
    }

    @JsonIgnore
    public void setOwner(UserDTO owner) {
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
        this.tagsAltered = true;
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

//    @JsonIgnore
    @JsonProperty(value = "created_date")
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonProperty(value = "modified_date")
    public Date getModifiedDate() {
        return modifiedDate;
    }

//    @JsonIgnore
    @JsonProperty(value = "modified_date")
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public boolean isTagsAltered() {
        return tagsAltered;
    }

    public void toDS(CollectionDS obj) {
        Set<TagDS> set = new HashSet<>();
        if (tags != null)
            for (TagDTO i : tags)
                set.add(i.toTagDS());
        obj.setId(Integer.parseInt(id));
        obj.setOwner(owner.toUserDS());
        obj.setName(name);
        obj.setDescription(description);
        obj.setTags(set);
        obj.setItemsNumber(itemsNumber);
        obj.setVisible(visible);
        obj.setCreated_date_client(createdDate);
        obj.setModified_date_client(modifiedDate);
    }

    public void fromDS(CollectionDS obj) {
        tags = new HashSet<>();
        if (obj.getTags() != null)
            for (TagDS i : obj.getTags())
                tags.add(i.toTagTO());
        id = Integer.toString(obj.getId());
        owner = obj.getOwner().toUserDTO();
        name = obj.getName();
        description = obj.getDescription();
        itemsNumber = obj.getItemsNumber();
        visible = obj.isVisible();
        createdDate = obj.getCreated_date_client();
        modifiedDate = obj.getModified_date_client();
        tagsAltered = obj.isTagsAltered();
    }

    @Override
    public String toString() {
        return String.format("id: %d, owner: %s, name: %s, description: %s, tags: %s, itemsNumber: %d, createdDate: %s, modifiedDate: %s\n",
                id, owner.getUsername(), name, description, tags, itemsNumber, createdDate, modifiedDate);
    }
}
