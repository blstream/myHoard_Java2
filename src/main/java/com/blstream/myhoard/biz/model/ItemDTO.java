package com.blstream.myhoard.biz.model;

import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.db.model.ItemDS;
import com.blstream.myhoard.db.model.MediaDS;
import com.blstream.myhoard.validator.CheckString;
import com.blstream.myhoard.validator.ValidationOpt;
import java.sql.SQLException;
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
import org.springframework.http.HttpStatus;

public class ItemDTO {

    private String id = "0";

    @NotNull(message = "Nazwa elementu jest wymagana")
    @NotEmpty(message = "Nazwa elementu jest wymagana")
    @CheckString(message = "Za dużo białych znaków", value = ValidationOpt.ITEM_NAME)
    private String name;

    private String description;
    private Location location;
    private Set<MediaDTO> media = new HashSet<>(0);
    @JsonIgnore
    private Date createdDate;
    @JsonIgnore
    private Date modifiedDate;

    @NotNull(message = "Element musi być przypisany do kolekcji")
    @NotEmpty(message = "Element musi być przypisany do kolekcji")
    private String collection;
    @JsonIgnore
    private String owner;

    @JsonIgnore
    private boolean mediaAltered = false;

    public ItemDTO() {
        createdDate = java.util.Calendar.getInstance().getTime();
        modifiedDate = (Date)createdDate.clone();
    }

    public ItemDTO(String id, String name, String description, Location location, Set<MediaDTO> media, Date createdDate, Date modifiedDate, String collection, String owner) {
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

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    @JsonProperty(value = "owner")
    public String getOwner() {
        return owner;
    }

    @JsonIgnore
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isMediaAltered() {
        return mediaAltered;
    }

    public ItemDS toItemDS() {
        Set<MediaDS> set = new HashSet<>(media.size());
        for (MediaDTO i : media)
            try {
                set.add(i.toMediaDS());
            } catch (SQLException ex) {
                throw new MyHoardException(ex.getErrorCode(), ex.getSQLState());
            }
        return new ItemDS(Integer.parseInt(id),
                name,
                description,
                location == null ? null : location.getLat(),
                location == null ? null : location.getLng(),
                set,
                createdDate,
                modifiedDate,
                collection == null || collection.isEmpty() ? -1 : Integer.parseInt(collection),
                owner);
    }
}
