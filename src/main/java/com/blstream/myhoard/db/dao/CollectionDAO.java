package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.biz.exception.MyHoardException;
import java.util.List;
import org.hibernate.Session;

import com.blstream.myhoard.db.model.*;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

public class CollectionDAO implements ResourceDAO<CollectionDS> {

	private final SessionFactory sessionFactory;

	public CollectionDAO() {
		try {
			sessionFactory = new org.hibernate.cfg.Configuration()
					.configure()
					.buildSessionFactory();
		} catch (HibernateException ex) {
			// Log the exception.
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	@Override
	public List<CollectionDS> getList() {
//		Session session = HibernateUtil.beginTransaction();
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		List<CollectionDS> result = session.createQuery("from CollectionDS").list();

//		HibernateUtil.commitTransaction();
		sessionFactory.getCurrentSession().getTransaction().commit();
		return result;
	}

	@Override
	public CollectionDS get(int id) throws IndexOutOfBoundsException {
//		Session session = HibernateUtil.beginTransaction();
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<CollectionDS> result = session.createQuery("from CollectionDS where id = " + id).list();
//		HibernateUtil.commitTransaction();
		sessionFactory.getCurrentSession().getTransaction().commit();
		return result.get(0);
	}

	@Override
	public void create(CollectionDS obj) {
//		Session session = HibernateUtil.beginTransaction();
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.save(obj);
//		HibernateUtil.commitTransaction();
		sessionFactory.getCurrentSession().getTransaction().commit();
	}

	@Override
	public void update(CollectionDS obj) {
		CollectionDS object = get(obj.getId());
		object.updateObject(obj);

//		Session session = HibernateUtil.beginTransaction();
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		object.setModifiedDate(java.util.Calendar.getInstance().getTime());
		session.update(object);
//		HibernateUtil.commitTransaction();
		sessionFactory.getCurrentSession().getTransaction().commit();

		obj.updateObject(object);
		obj.setOwner(object.getOwner());
		obj.setCreatedDate(object.getCreatedDate());
		obj.setModifiedDate(object.getModifiedDate());
	}

	@Override
	public void remove(int id) {
//		Session session = HibernateUtil.beginTransaction();
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.createQuery("delete CollectionDS where id = " + id).executeUpdate();
//		HibernateUtil.commitTransaction();
		sessionFactory.getCurrentSession().getTransaction().commit();
	}

}
