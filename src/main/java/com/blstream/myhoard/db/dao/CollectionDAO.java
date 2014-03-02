package com.blstream.myhoard.db.dao;

import java.util.List;
import org.hibernate.Session;
import com.blstream.myhoard.db.model.*;
import java.util.Calendar;
import org.hibernate.SessionFactory;

public class CollectionDAO implements ResourceDAO<CollectionDS> {

	private SessionFactory sessionFactory;

	@Override
	public List<CollectionDS> getList() {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<CollectionDS> result = session.createQuery("from CollectionDS").list();
		session.getTransaction().commit();
		return result;
	}

	@Override
	public CollectionDS get(int id) throws IndexOutOfBoundsException {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		CollectionDS result = (CollectionDS)session.createQuery("from CollectionDS where id = " + id).uniqueResult();
		session.getTransaction().commit();
		return result;
	}

	@Override
	public void create(CollectionDS obj) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.save(obj);
		session.getTransaction().commit();
	}

	@Override
	public void update(CollectionDS obj) {
		CollectionDS object = get(obj.getId());
		object.updateObject(obj);

		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		object.setModifiedDate(Calendar.getInstance().getTime());
		session.update(object);
		session.getTransaction().commit();

		obj.updateObject(object);
		obj.setOwner(object.getOwner());
		obj.setCreatedDate(object.getCreatedDate());
		obj.setModifiedDate(object.getModifiedDate());
	}

	@Override
	public void remove(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.createQuery("delete CollectionDS where id = " + id).executeUpdate();
		session.getTransaction().commit();
	}

	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
