package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.ItemDTO;
import com.blstream.myhoard.biz.model.Location;
import com.blstream.myhoard.biz.model.MediaDTO;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ItemDS {

    private int id;
    private String name;
    private String description;
    private Float latitude;
    private Float longitude;
    private Set<MediaDS> media = new HashSet<>(0);
    private Date created_date;
    private Date modified_date;
    private int collection;
    private UserDS owner;
    private boolean mediaAltered = false;

    public ItemDS() {
        created_date = java.util.Calendar.getInstance().getTime();
        modified_date = (Date)created_date.clone();
    }

    public ItemDS(int id, String name, String description, Float latitude, Float longitude, Set<MediaDS> media, Date createdDate, Date modifiedDate, int collection, UserDS owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.media = media;
        this.created_date = createdDate;
        this.modified_date = modifiedDate;
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

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Set<MediaDS> getMedia() {
        return media;
    }

    public void setMedia(Set<MediaDS> media) {
        this.media = media;
        mediaAltered = true;
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

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public UserDS getOwner() {
        return owner;
    }

    public void setOwner(UserDS owner) {
        this.owner = owner;
    }

    public boolean isMediaAltered() {
        return mediaAltered;
    }

    public void updateObject(ItemDS object) {
        if (this == object || object == null)
            return;
        if (name == null || object.name != null && !name.equals(object.name))
            name = object.name;
        if (description == null || object.description != null && !description.equals(object.description))
            description = object.description;
        if (latitude == null)
            latitude = object.latitude;
        if (longitude == null)
            longitude = object.longitude;
        if (media == null || object.mediaAltered) {
            media = object.media;
            mediaAltered = object.mediaAltered;
        }
        if (object.collection != -1)
            collection = object.collection;
    }

    public ItemDTO toDTO() {
        Set<MediaDTO> set = new HashSet<>(media.size());
        for (MediaDS i : media)
            set.add(i.toMediaDTO());

        return new ItemDTO(Integer.toString(id),
                name,
                description,
                latitude == null || longitude == null ? null : new Location(latitude, longitude),
                set,
                created_date,
                modified_date,
                Integer.toString(collection),
                owner.toUserDTO());
    }

    public void toDTO(ItemDTO obj) {
        if (obj == null)
            return;
        Set<MediaDTO> set = new HashSet<>(media.size());
        for (MediaDS i : media)
            set.add(i.toMediaDTO());

        obj.setId(Integer.toString(id));
        obj.setName(name);
        obj.setDescription(description);
        obj.setLocation(latitude == null || longitude == null ? null : new Location(latitude, longitude));
        obj.setMedia(set);
        obj.setCreatedDate(created_date);
        obj.setModifiedDate(modified_date);
        obj.setCollection(Integer.toString(collection));
        obj.setOwner(owner.toUserDTO());
    }

    public void fromDTO(ItemDTO obj) {
        if (obj == null)
            return;
        media = new HashSet<>();
        if (obj.getMedia() != null)
            for (MediaDTO i : obj.getMedia())
                media.add(i.toMediaDS());

        id = Integer.parseInt(obj.getId());
        name = obj.getName();
        description = obj.getDescription();
        if (obj.getLocation() == null)
            latitude = longitude = null;
        else {
            latitude = obj.getLocation().getLat();
            longitude = obj.getLocation().getLng();
        }
        created_date = obj.getCreatedDate();
        modified_date = obj.getModifiedDate();
        try {
            collection = Integer.parseInt(obj.getCollection());
        } catch (NumberFormatException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("collection", "Niepoprawny identyfikator");
        }
        owner = obj.getOwner().toUserDS();
    }
}
