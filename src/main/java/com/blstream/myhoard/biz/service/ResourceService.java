package com.blstream.myhoard.biz.service;

import java.util.List;

public interface ResourceService<T> {

	public List<T> getList();

	public T get(int id);

	public void create(T obj);

	public void update(T obj);

	public void remove(int id);
}
