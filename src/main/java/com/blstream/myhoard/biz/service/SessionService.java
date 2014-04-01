package com.blstream.myhoard.biz.service;

import java.util.List;
import com.blstream.myhoard.biz.model.*;
import com.blstream.myhoard.db.dao.*;
import com.blstream.myhoard.db.model.SessionDS;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SessionService implements ResourceService<SessionDTO> {

    private ResourceDAO<SessionDS> sessionDAO;

    public void setSessionDAO(ResourceDAO<SessionDS> sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    @Override
    public List<SessionDTO> getList(Map<String, Object> params) {
        List<SessionDTO> result = new ArrayList<>();
        for (SessionDS i : sessionDAO.getList(params))
            result.add(i.toSessionDTO());
        return result;
    }

    @Override
    public SessionDTO get(int id) {
        return sessionDAO.get(id).toSessionDTO();
    }

    @Override
    public void create(SessionDTO obj) {
        SessionDS session = obj.toSessionDS();
        sessionDAO.create(session);
    }

    @Override
    public void update(SessionDTO obj) {
        sessionDAO.update(obj.toSessionDS());
    }

    @Override
    public void remove(int id) {
        sessionDAO.remove(id);
    }
}
