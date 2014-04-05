package com.blstream.myhoard.biz.service;

import java.util.ArrayList;
import java.util.List;
import com.blstream.myhoard.biz.model.*;
import com.blstream.myhoard.db.dao.*;
import com.blstream.myhoard.db.model.UserDS;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class UserService implements ResourceService<UserDTO> {

    private ResourceDAO<UserDS> userDAO;

    public void setUserDAO(ResourceDAO<UserDS> userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public int getTotalCount(String owner) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<UserDTO> getList(Map<String, Object> params) {
        List<UserDTO> result = new ArrayList<>();
        for (UserDS i : userDAO.getList(params))
            result.add(i.toUserDTO());
        return result;
    }

    @Override
    public UserDTO get(int id) {
        return userDAO.get(id).toUserDTO();
    }

    @Override
    public void create(UserDTO obj) {
        UserDS user = obj.toUserDS();
        userDAO.create(user);
        obj.updateObject(user.toUserDTO());
        obj.setId(null);
    }

    @Override
    public void update(UserDTO obj) {
        UserDS object = obj.toUserDS();
        userDAO.update(object);
        obj.updateObject(object.toUserDTO());
        obj.setId(null);
    }

    @Override
    public void remove(int id) {
        userDAO.remove(id);
    }
}
