package com.blstream.myhoard.db.dao;

import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public interface ResourceDAO<T> {

    public List<T> getList();

    public T get(int id);

    public void create(T obj);

    public void update(T obj);

    public void remove(int id);

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory);

}
