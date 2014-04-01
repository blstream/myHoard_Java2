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
import javax.imageio.ImageIO;
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
                    BufferedImage image = ImageIO.read(in);
                    int width = image.getWidth();
                    int height = image.getHeight();

                    if (size < width && size < height) {        
                        int x = width/2 - size/2;
                        int y = height/2 - size/2;
                        image = image.getSubimage(x-1, y-1, size, size);
                        ByteArrayOutputStream b = new ByteArrayOutputStream();
                        ImageIO.write(image, "jpg", b);
                        return b.toByteArray();
                    } else {
                        return media.getFile();
                    }
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
