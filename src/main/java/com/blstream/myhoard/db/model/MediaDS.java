package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.MediaDTO;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

public class MediaDS {

    private int id;
    private Blob file;
    private Date createdDate;
    private Integer item;

    public MediaDS() {}

    public MediaDS(int id, Blob file, Date createdDate, Integer item) {
        this.id = id;
        this.file = file;
        this.createdDate = createdDate;
        this.item = item;
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

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public MediaDTO toMediaDTO() {
        try {
            return new MediaDTO(Integer.toString(id), 
                    file == null ? null : file.getBytes(1, (int) file.length()),
                    createdDate,
                    Integer.toString(item));
        } catch (SQLException ex) {
            throw new MyHoardException(400, ex.getSQLState());
        }
    }

    public int hashCode() {
        return id;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof MediaDS))
            return false;
        else {
            MediaDS m = (MediaDS)o;
            return id == m.id;
        }
    }
}
