package com.blstream.myhoard.biz.service;

import java.util.List;
import java.util.Map;

public interface ResourceService<T> {

    public List<T> getList(Map<String, Object> params);

    public T get(int id);

    public void create(T obj);

    public void update(T obj);

    public void remove(int id);

}
