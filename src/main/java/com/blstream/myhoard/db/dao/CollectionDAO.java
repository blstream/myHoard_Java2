package com.blstream.myhoard.db.dao;

import java.util.List;
import org.hibernate.Session;

import com.blstream.myhoard.db.model.*;

public class CollectionDAO implements ResourceDAO<CollectionDS> {

	public List<CollectionDS> getList() {
		Session session = HibernateUtil.beginTransaction();
		List<CollectionDS> result = session.createQuery("from CollectionDS").list();
		HibernateUtil.commitTransaction();
		return result;
	}

	public CollectionDS get(int id) {
		Session session = HibernateUtil.beginTransaction();
		List<CollectionDS> result = session.createQuery("from CollectionDS where id = " + id).list();
		HibernateUtil.commitTransaction();
		return result.size() != 1 ? null : result.get(0);
	}

	public void create(CollectionDS obj) {
		Session session = HibernateUtil.beginTransaction();
		session.save(obj);
		HibernateUtil.commitTransaction();
	}

	public void update(CollectionDS obj) {
		Session session = HibernateUtil.beginTransaction();
		session.update(obj);
		HibernateUtil.commitTransaction();
	}

	public void remove(int id) {
		Session session = HibernateUtil.beginTransaction();
		session.createQuery("delete CollectionDS where id = " + id).executeUpdate();
		HibernateUtil.commitTransaction();
	} 

}
