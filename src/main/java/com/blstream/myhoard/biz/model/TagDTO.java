package com.blstream.myhoard.biz.model;

import com.blstream.myhoard.db.model.MediaDS;
import com.blstream.myhoard.db.model.TagDS;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author z0lfik
 */
public class TagDTO {

    @JsonIgnore
    private String id;

    private String tag;

    public TagDTO() {
        // by Integer.parseInt() nie rzucał wyjątku w metodia toMediaDS,
        // id zostanie zmienione jak obiekt trafi do bazy danych
        id = "0";
    }

    public TagDTO(String id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public TagDS toTagDS() {
        return new TagDS(Integer.parseInt(id), tag);
    }
}
