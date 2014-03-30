package com.blstream.myhoard.biz.service;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.MediaDTO;
import com.blstream.myhoard.db.dao.ResourceDAO;
import com.blstream.myhoard.db.model.MediaDS;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class MediaService implements ResourceService<MediaDTO> {
 
    private ResourceDAO<MediaDS> mediaDAO;

    public void setMediaDAO(ResourceDAO<MediaDS> mediaDAO) {
        this.mediaDAO = mediaDAO;
    }

    @Override
    public List<MediaDTO> getList(Map<String, Object> params) {
        List<MediaDTO> media = new ArrayList<>();
        for (MediaDS i : mediaDAO.getList(params))
            media.add(i.toMediaDTO());
        return media;
    }

    @Override
    public MediaDTO get(int id) {
        return mediaDAO.get(id).toMediaDTO();
    }

    @Override
    public void create(MediaDTO obj) {
        MediaDS media = obj.toMediaDS();
        mediaDAO.create(media);
        obj.setId(Integer.toString(media.getId()));
    }

    @Override
    public void update(MediaDTO obj) {
        mediaDAO.update(obj.toMediaDS());
    }

    @Override
    public void remove(int id) {
        mediaDAO.remove(id);
    }
    
    public byte[] getThumbnail(Integer id, Integer size) {
        MediaDTO media = mediaDAO.get(id).toMediaDTO();
        if (media.getFile() != null) {
            if(size > 0) {
                try {
                    InputStream in = new ByteArrayInputStream(media.getFile());
                    BufferedImage originalImage = ImageIO.read(in);
                    int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
                    BufferedImage resizedImage = new BufferedImage(size, size, type);
                    Graphics2D g = resizedImage.createGraphics();
                    g.drawImage(originalImage, 0, 0, size, size, null);
                    g.dispose();

                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    ImageIO.write(resizedImage, "jpg", b);
                    return b.toByteArray();
                } catch (IOException ex) {
                    throw new MyHoardException(ErrorCode.BAD_REQUEST).add("file", "Niepoprawny plik");
                }
            } else {
                return media.getFile();
            }
        } 
        return null;
    }
}
