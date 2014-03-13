package com.blstream.myhoard.biz.service;

import com.blstream.myhoard.biz.model.MediaDTO;
import com.blstream.myhoard.db.dao.ResourceDAO;
import com.blstream.myhoard.db.model.MediaDS;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class MediaService implements ResourceService<MediaDTO> {
 
    private ResourceDAO<MediaDS> mediaDAO;

    public void setMediaDAO(ResourceDAO<MediaDS> mediaDAO) {
        this.mediaDAO = mediaDAO;
    }

    @Override
    public List<MediaDTO> getList() {
        try {
            List<MediaDTO> result = new ArrayList<>();
            for (MediaDS i : mediaDAO.getList()) {
                result.add(i.toMediaDTO());
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(MediaService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public MediaDTO get(int id) {
        try {
            return mediaDAO.get(id).toMediaDTO();
        } catch (SQLException ex) {
            Logger.getLogger(MediaService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void create(MediaDTO obj) {
        obj.resizeFile();
        MediaDS media;
        try {
            media = obj.toMediaDS();
            mediaDAO.create(media);
            obj.setId(Integer.toString(media.getId()));
        } catch (SQLException ex) {
            Logger.getLogger(MediaService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(MediaDTO obj) {
        try {
            mediaDAO.update(obj.toMediaDS());
        } catch (Exception ex) {
            Logger.getLogger(MediaService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void remove(int id) {
        mediaDAO.get(id); // jezeli ne istnieje obiekt rzuci to wyjatkiem indexoutofbounds
        mediaDAO.remove(id);
    }

}
