package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.model.MediaDTO;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MediaDS {

    private int id;
    private Blob file;
    private Blob thumbnail;
    private int collection;
    private Date createdDate;
    
    public MediaDS() {
        
    }
    
    public MediaDS(int id, Blob file, Blob thumbnail, int collection, Date createdDate) {
        this.id = id;
        this.file = file;
        this.thumbnail = thumbnail;
        this.collection = collection;
        this.createdDate = createdDate;
    }
    
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
    public int getCollection() {
        return collection;
    }
    public void setCollection(int collection) {
        this.collection = collection;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public MediaDTO toMediaDTO() throws SQLException {
            return new MediaDTO(Integer.toString(id),
                    file == null ? null : file.getBytes(1, (int) file.length()),
                    thumbnail == null ? null : thumbnail.getBytes(1, (int) thumbnail.length()),
                    Integer.toString(collection),
                    createdDate);
    }
    
}
