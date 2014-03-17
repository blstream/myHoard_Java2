package com.blstream.myhoard.biz.model;

import com.blstream.myhoard.db.model.MediaDS;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import javax.sql.rowset.serial.SerialBlob;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author gohilukk
 */
public class MediaDTO {


    private String id;
    @JsonIgnore
    private byte[] file;
    @JsonIgnore
    private Date created_date;

    public MediaDTO() throws IOException {
        // by Integer.parseInt() nie rzucał wyjątku w metodia toMediaDS,
        // id zostanie zmienione jak obiekt trafi do bazy danych
        id = "0";
        created_date = java.util.Calendar.getInstance().getTime();
    }

    public MediaDTO(String id, byte[] file, Date createdDate) {
        this.id = id;
        this.file = file;
        this.created_date = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public MediaDS toMediaDS() throws SQLException {
        return new MediaDS(Integer.parseInt(id),
                file == null ? null : new SerialBlob(file),
                created_date);
    }


}
