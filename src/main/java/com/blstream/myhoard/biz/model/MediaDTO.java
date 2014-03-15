package com.blstream.myhoard.biz.model;

import com.blstream.myhoard.db.model.MediaDS;
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
 * @author gohilukk
 */
public class MediaDTO {

    private static final int IMG_WIDTH = 30;
    private static final int IMG_HEIGHT = 30;

    private String id;
    @JsonIgnore
    private byte[] file;
    @JsonIgnore
    private byte[] thumbnail;
    @JsonIgnore
    private Date created_date;

    public MediaDTO() throws IOException {
        // by Integer.parseInt() nie rzucał wyjątku w metodia toMediaDS,
        // id zostanie zmienione jak obiekt trafi do bazy danych
        id = "0";
        created_date = java.util.Calendar.getInstance().getTime();
    }

    public MediaDTO(String id, byte[] file, byte[] thumbnail, Date createdDate) {
        this.id = id;
        this.file = file;
        this.thumbnail = thumbnail;
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

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
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
                thumbnail == null ? null : new SerialBlob(thumbnail),
                created_date);
    }

    public void resizeFile() {
        if (file != null) {
            try {
                InputStream in = new ByteArrayInputStream(this.file);
                BufferedImage originalImage = ImageIO.read(in);
                int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
                BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
                Graphics2D g = resizedImage.createGraphics();
                g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
                g.dispose();

                ByteArrayOutputStream b = new ByteArrayOutputStream();
                ImageIO.write(resizedImage, "jpg", b);
                this.thumbnail = b.toByteArray();
            } catch (IOException ex) {
                Logger.getLogger(MediaDTO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
