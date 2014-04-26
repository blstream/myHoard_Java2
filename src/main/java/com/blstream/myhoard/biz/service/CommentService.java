/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blstream.myhoard.biz.service;

import com.blstream.myhoard.biz.model.CommentDTO;
import com.blstream.myhoard.db.dao.ResourceDAO;
import com.blstream.myhoard.db.model.CommentDS;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author gohilukk
 */
@Service
public class CommentService  implements ResourceService<CommentDTO>{

    private ResourceDAO<CommentDS> commentDAO;

    public void setCommentDAO(ResourceDAO<CommentDS> commentDAO) {
        this.commentDAO = commentDAO;
    }

    
    @Override
    public int getTotalCount(String owner) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommentDTO> getList(Map<String, Object> params) {
        List<CommentDTO> comments = new ArrayList<>();
        for (CommentDS c: commentDAO.getList(params)) {
            comments.add(c.toDTO());
        }
        return comments;
    }

    @Override
    public CommentDTO get(int id) {
        CommentDS obj = commentDAO.get(id);
        if (obj == null)
            return null;
        return obj.toDTO();
    }

    @Override
    public void create(CommentDTO obj) {
        CommentDS comment = new CommentDS();
        comment.fromDTO(obj);
        commentDAO.create(comment);
        obj.setId(Integer.toString(comment.getId()));
    }

    @Override
    public void update(CommentDTO obj) {
        CommentDS comment = new CommentDS();
        comment.fromDTO(obj);
        commentDAO.update(comment);
    }

    @Override
    public void remove(int id) {
        commentDAO.remove(id);
    }
    
}
