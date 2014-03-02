package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.db.model.ItemDS;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ItemDAO implements ResourceDAO<ItemDS> {

	private SessionFactory sessionFactory;

	@Override
	public List<ItemDS> getList() {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<ItemDS> result = session.createQuery("from ItemDS").list();
//		result.setMedia(null);
		session.getTransaction().commit();
		return result;
	}

	@Override
	public ItemDS get(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		ItemDS result = (ItemDS)session.createQuery("from ItemDS where id = " + id);
//		result.setMedia(null);
		session.getTransaction().commit();
		return result;
	}

	@Override
	public void create(ItemDS obj) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void update(ItemDS obj) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void remove(int id) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
}
