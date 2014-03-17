package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.model.MediaDTO;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

public class MediaDS {

    private int id;
    private Blob file;
    private Date createdDate;
    private ItemDS item;

    public MediaDS() {}

    public MediaDS(int id, Blob file, Date createdDate) {
        this.id = id;
        this.file = file;
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
                createdDate);
    }
}
