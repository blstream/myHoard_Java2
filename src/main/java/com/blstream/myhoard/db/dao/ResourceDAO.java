package com.blstream.myhoard.db.dao;

import java.util.List;
import java.util.Map;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public interface ResourceDAO<T> {

    public List<T> getList();

    public List<T> getList(Map<String, String> params);

    public T get(int id);

    public void create(T obj);

    public void update(T obj);

    public void remove(int id);

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory);

    public T getByAccess_token(String access_token);

    public T getByRefresh_token(String refresh_token);

    public T getByUsername(String username);
}
