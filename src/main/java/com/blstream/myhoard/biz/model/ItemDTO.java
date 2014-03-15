package com.blstream.myhoard.biz.model;

import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.db.model.CollectionDS;
import com.blstream.myhoard.db.model.ItemDS;
import com.blstream.myhoard.db.model.MediaDS;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class ItemDTO {

    private String id = "0";
    private String name;
    private String description;
    private Location location;
    private Set<MediaDTO> media = new HashSet<>(0);
    private Date createdDate;
    private Date modifiedDate;
    private String collection;
    private String owner;

    public ItemDTO() {}

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
        if (media == null)
            this.media = new HashSet<>(0);
        else
            this.media = media;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonProperty(value = "created_date")
    public Date getCreatedDate() {
        return createdDate;
    }

    @JsonProperty(value = "created_date")
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonProperty(value = "modified_date")
    public Date getModifiedDate() {
        return modifiedDate;
    }

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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ItemDS toItemDS() {
        Set<MediaDS> set = new HashSet<>(media.size());
//        CollectionDS c = new CollectionDS();
        for (MediaDTO i : media)
            try {
                set.add(i.toMediaDS());
            } catch (SQLException ex) {
                throw new MyHoardException(ex.getErrorCode(), ex.getSQLState());
            }
//        c.setId(Integer.parseInt(collection));
        return new ItemDS(Integer.parseInt(id),
                name,
                description,
                location.getLat(),
                location.getLng(),
                set,
                createdDate,
                modifiedDate,
                Integer.parseInt(collection),
//                c,
                owner);
    }
}
