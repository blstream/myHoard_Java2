package com.blstream.myhoard.biz.model;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.db.model.ItemDS;
import com.blstream.myhoard.db.model.MediaDS;
import com.blstream.myhoard.validator.CheckString;
import com.blstream.myhoard.validator.GeographicLocation;
import com.blstream.myhoard.validator.ValidationOpt;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.validator.constraints.NotEmpty;

public class ItemDTO {

    private String id = "0";

    @NotNull(message = "Nazwa elementu jest wymagana")
    @NotEmpty(message = "Nazwa elementu jest wymagana")
    @Size(max = 50, message = "Nazwa może zawierać co najwyżej 50 znaków")
    @CheckString(message = "Niepoprawna nazwa (zła długość lub zawiera białe znaki)", value = ValidationOpt.ITEM_NAME)
    private String name;

    @Size(max = 250, message = "Opis może się składać z co najwyżej 250 znaków")
    private String description;

    @GeographicLocation
    private Location location;

    private Set<MediaDTO> media = new HashSet<>(0);
//    @JsonIgnore
    private Date createdDate;
//    @JsonIgnore
    private Date modifiedDate;

    @NotNull(message = "Element musi być przypisany do kolekcji")
    @NotEmpty(message = "Element musi być przypisany do kolekcji")
    private String collection;
    @JsonIgnore
    private UserDTO owner;

    @JsonIgnore
    private boolean mediaAltered = false;

    public ItemDTO() {
//        createdDate = java.util.Calendar.getInstance().getTime();
//        modifiedDate = (Date)createdDate.clone();
    }

    public ItemDTO(String id, String name, String description, Location location, Set<MediaDTO> media, Date createdDate, Date modifiedDate, String collection, UserDTO owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.media = media;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.collection = collection;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @JsonSerialize(using = CustomMediaSerializer.class)
    public Set<MediaDTO> getMedia() {
        return media;
    }

    @JsonDeserialize(using = CustomMediaDeserializer.class)
    public void setMedia(Set<MediaDTO> media) {
        this.media = media;
        mediaAltered = true;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonProperty(value = "created_date")
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

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
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

    public boolean isMediaAltered() {
        return mediaAltered;
    }

    public ItemDS toItemDS() {
        Set<MediaDS> set = new HashSet<>(media.size());
        for (MediaDTO i : media)
            set.add(i.toMediaDS());
 
        try {
            return new ItemDS(Integer.parseInt(id),
                    name,
                    description,
                    location == null ? null : location.getLat(),
                    location == null ? null : location.getLng(),
                    set,
                    createdDate,
                    modifiedDate,
                    collection == null || collection.isEmpty() ? -1 : Integer.parseInt(collection),
                    owner.toUserDS());
        } catch (NumberFormatException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("collection", "Niepoprawny identyfikator");
        }
    }
}
