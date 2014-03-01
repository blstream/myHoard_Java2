package com.blstream.myhoard.db.model;

import java.sql.Blob;
import java.util.Date;

public class MediaDS {

    private int id;
    private Blob file;
    private Blob thumbnail;
    private CollectionDS collection;
    private Date createdDate;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Blob getFile() {
        return file;
    }
    public void setFile(Blob file) {
        this.file = file;
    }
    public Blob getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(Blob thumbnail) {
        this.thumbnail = thumbnail;
    }
    public CollectionDS getCollection() {
        return collection;
    }
    public void setCollection(CollectionDS collection) {
        this.collection = collection;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
}
