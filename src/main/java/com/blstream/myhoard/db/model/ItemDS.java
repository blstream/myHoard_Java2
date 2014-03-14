package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.ItemDTO;
import com.blstream.myhoard.biz.model.Location;
import com.blstream.myhoard.biz.model.MediaDTO;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ItemDS {

    private int id;
    private String name;
    private String description;
    private float latitude;
    private float longitude;
    private int quantity;
    private Set<MediaDS> media = new HashSet<>(0);
    private Date createdDate;
    private Date modifiedDate;
    private int collection;
    private String owner;

    public ItemDS() {}

    public ItemDS(int id, String name, String description, float latitude, float longitude, int quantity, Set<MediaDS> media, Date createdDate, Date modifiedDate, int collection, String owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.quantity = quantity;
        this.media = media;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.collection = collection;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Set<MediaDS> getMedia() {
        return media;
    }

    public void setMedia(Set<MediaDS> media) {
        if (media == null)
            this.media = new HashSet<>(0);
        else
            this.media = media;
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

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ItemDTO toItemDTO() {
        Set<MediaDTO> set = new HashSet<>(media.size());
        for (MediaDS i : media)
            try {
                set.add(i.toMediaDTO());
            } catch (SQLException ex) {
                throw new MyHoardException(ex.getErrorCode(), ex.getSQLState());
            }
        return new ItemDTO(Integer.toString(id),
                name,
                description,
                new Location(latitude, longitude),
                quantity,
                set,
                createdDate,
                modifiedDate,
                Integer.toString(collection),
//                Integer.toString(collection.getId()),
                owner);
    }
}
