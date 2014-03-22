package com.blstream.myhoard.biz.service;

import com.blstream.myhoard.biz.exception.MyHoardException;
import java.util.ArrayList;
import java.util.List;
import com.blstream.myhoard.biz.model.*;
import com.blstream.myhoard.db.dao.*;
import com.blstream.myhoard.db.model.CollectionDS;
import com.blstream.myhoard.db.model.UserDS;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class UserService implements ResourceService<UserDTO> {

    private Resource2DAO<UserDS> userDAO;
    
    public void setUserDAO(Resource2DAO<UserDS> userDAO) {
        this.userDAO = userDAO;
    }
    
    @Override
    public List<UserDTO> getList() {
        List<UserDTO> result = new ArrayList<>();
        for (UserDS i : userDAO.getList()) {
            result.add(i.toUserDTO());
        }
        return result;
    }

    @Override
    public List<UserDTO> getList(Map<String, String> params) {
        return null;
    }


    @Override
    public void create(UserDTO obj) {
        UserDS user = obj.toUserDS();
        userDAO.create(user);
        obj.updateObject(user.toUserDTO());
    }

    @Override
    public void update(UserDTO obj) {
        UserDS object = obj.toUserDS();
        userDAO.update(object);
        obj.updateObject(object.toUserDTO());
    }

    @Override
    public void remove(int id) {
        try {
            userDAO.get(id);	// żeby "nie usuwało" nieistniejącego obiektu
            userDAO.remove(id);
        } catch (RuntimeException ex) {
            throw new MyHoardException(111, "Element o id(" + id + ") prawdopodobnie nie istnieje.");
        }
    }

    @Override
    public UserDTO get(int id) {
        return userDAO.get(id).toUserDTO();
    }
    
    public UserDTO getByUsername(String username) {
        return userDAO.getByUsername(username).toUserDTO();
    }


}
