package com.blstream.myhoard.biz.model;

import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.db.model.MediaDS;
import java.sql.SQLException;
import java.util.Date;
import javax.sql.rowset.serial.SerialBlob;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.JDBCException;

/**
 *
 * @author gohilukk
 */
public class MediaDTO {

    private String id;
    @JsonIgnore
    private byte[] file;
    @JsonIgnore
    private Date createdDate;
    @JsonIgnore
    private String owner;

    public MediaDTO() {
        // by Integer.parseInt() nie rzucał wyjątku w metodia toMediaDS,
        // id zostanie zmienione jak obiekt trafi do bazy danych
        id = "0";
        createdDate = java.util.Calendar.getInstance().getTime();
    }

    public MediaDTO(String id, byte[] file, Date createdDate, String owner) {
        this.id = id;
        this.file = file;
        this.createdDate = createdDate;
        this.owner = owner;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonIgnore
    public void setId(String id) {
        this.id = id;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public MediaDS toMediaDS() {
        try {
            return new MediaDS(Integer.parseInt(id),
                    file == null ? null : new SerialBlob(file),
                    createdDate,
                    Integer.parseInt(owner));
        } catch (SQLException ex) {
            throw new MyHoardException(new JDBCException("Nieznany błąd", ex));
        }
    }
}
