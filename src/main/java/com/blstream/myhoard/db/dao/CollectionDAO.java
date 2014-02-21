package com.blstream.myhoard.db.dao;

import java.util.List;
import org.hibernate.Session;

import com.blstream.myhoard.db.model.*;

public class CollectionDAO implements ResourceDAO<CollectionDS> {

	@Override
	public List<CollectionDS> getList() {
		Session session = HibernateUtil.beginTransaction();
		List<CollectionDS> result = session.createQuery("from CollectionDS").list();
		HibernateUtil.commitTransaction();
		return result;
	}

	@Override
	public CollectionDS get(int id) throws IndexOutOfBoundsException {
		Session session = HibernateUtil.beginTransaction();
		List<CollectionDS> result = session.createQuery("from CollectionDS where id = " + id).list();
		HibernateUtil.commitTransaction();
		return result.get(0);
	}

	@Override
	public void create(CollectionDS obj) {
		Session session = HibernateUtil.beginTransaction();
		session.save(obj);
		HibernateUtil.commitTransaction();
	}

	@Override
	public void update(CollectionDS obj) {
		Session session = HibernateUtil.beginTransaction();
		session.update(obj);
		HibernateUtil.commitTransaction();
	}

	@Override
	public void remove(int id) {
		Session session = HibernateUtil.beginTransaction();
		session.createQuery("delete CollectionDS where id = " + id).executeUpdate();
		HibernateUtil.commitTransaction();
	} 

}
