package com.blstream.myhoard.db.dao;

import java.util.List;

public interface ResourceDAO<T> {

	public List<T> getList();

	public T get(int id);

	public void create(T obj);

	public void update(T obj);

	public void remove(int id);

}
