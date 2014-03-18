package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.model.MediaDTO;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

public class MediaDS {

    private int id;
    private Blob file;
    private Date createdDate;
    private int item;

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

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public MediaDTO toMediaDTO() throws SQLException {
        return new MediaDTO(Integer.toString(id),
                file == null ? null : file.getBytes(1, (int) file.length()),
                createdDate);
    }

    public int hashCode() {
        return id;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof MediaDS))
            return false;
        else {
            MediaDS m = (MediaDS)o;
            return id == m.id && file != null && file.equals(m.file) && createdDate != null && createdDate.equals(m.createdDate);
        }
    }
}
