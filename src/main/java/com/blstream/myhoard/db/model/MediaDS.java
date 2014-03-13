package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.model.MediaDTO;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

public class MediaDS {

    private int id;
    private Blob file;
    private Blob thumbnail;
    private Date createdDate;
    private ItemDS item;

    public MediaDS() {}

    public MediaDS(int id, Blob file, Blob thumbnail, Date createdDate) {
        this.id = id;
        this.file = file;
        this.thumbnail = thumbnail;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public ItemDS getItem() {
        return item;
    }

    public void setItem(ItemDS item) {
        this.item = item;
    }

    public MediaDTO toMediaDTO() throws SQLException {
        return new MediaDTO(Integer.toString(id),
                file == null ? null : file.getBytes(1, (int) file.length()),
                thumbnail == null ? null : thumbnail.getBytes(1, (int) thumbnail.length()),
                "TODO: remove",
                createdDate);
    }
}
