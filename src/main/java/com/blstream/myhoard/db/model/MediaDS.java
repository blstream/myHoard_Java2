package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.MediaDTO;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import org.hibernate.JDBCException;

public class MediaDS {

    private int id;
    private Blob file;
    private Date createdDate;
    private Integer item;
    private String owner;

    public MediaDS() {}

    public MediaDS(int id, Blob file, Date createdDate, Integer item, String owner) {
        this.id = id;
        this.file = file;
        this.createdDate = createdDate;
        this.item = item;
        this.owner = owner;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public MediaDTO toMediaDTO() {
        try {
            return new MediaDTO(Integer.toString(id), 
                    file == null ? null : file.getBytes(1, (int) file.length()),
                    createdDate,
                    item == null ? null : item.toString(),
                    owner);
        } catch (SQLException ex) {
            throw new MyHoardException(new JDBCException("Nieznany błąd", ex));
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
