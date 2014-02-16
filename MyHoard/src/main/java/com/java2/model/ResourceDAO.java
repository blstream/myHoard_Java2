package com.java2.model;

import java.util.List;

public interface ResourceDAO<T> {

	public List<T> getList();

	public T get(int id);

	public void create(T obj);

	public void update(T obj);

	public void remove(int id);

}