/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blstream.myhoard.biz.service;

import com.blstream.myhoard.biz.model.MediaDTO;
import com.blstream.myhoard.db.dao.MediaDAO;
import com.blstream.myhoard.db.model.MediaDS;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author gohilukk
 */
public class MediaService implements ResourceService<MediaDTO> {

    @Autowired
    private MediaDAO mediaDAO;    
    
    @Override
    public List<MediaDTO> getList() {
        try {
            List<MediaDTO> result = new ArrayList<>();
            for (MediaDS i : mediaDAO.getList())
                result.add(i.toMediaDTO());
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
