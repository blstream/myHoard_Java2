/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.db.model.UserDS;
import java.util.List;
import java.util.Map;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author gohilukk
 */
public interface Resource2DAO<T> {
    
    public List<T> getList();

    public List<T> getList(Map<String, String> params);

    public T get(int id);

    public void create(T obj);

    public void update(T obj);

    public void remove(int id);

    public UserDS getByUsername(String username);
    
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory);

}
