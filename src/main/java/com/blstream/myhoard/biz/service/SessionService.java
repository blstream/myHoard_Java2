package com.blstream.myhoard.biz.service;

import java.util.List;
import com.blstream.myhoard.biz.model.*;
import com.blstream.myhoard.db.dao.*;
import com.blstream.myhoard.db.model.SessionDS;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SessionService implements ResourceService<SessionDTO> {

    private ResourceDAO<SessionDS> sessionDAO;

    public void setSessionDAO(ResourceDAO<SessionDS> sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    @Override
    public List<SessionDTO> getList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SessionDTO> getList(Map<String, String> params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SessionDTO get(int id) {
        return sessionDAO.get(id).toSessionDTO();
    }
    
    public SessionDTO get(String access_token) {
        return ((SessionDAO)sessionDAO).getByToken(access_token).toSessionDTO();
    }

    @Override
    public void create(SessionDTO obj) {
        SessionDS session = obj.toSessionDS();
        sessionDAO.create(session);
    }

    @Override
    public void update(SessionDTO obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    


}