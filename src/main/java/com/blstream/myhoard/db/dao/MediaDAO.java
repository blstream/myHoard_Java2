package com.blstream.myhoard.db.dao;
import com.blstream.myhoard.db.model.MediaDS;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author gohilukk
 */
public class MediaDAO implements ResourceDAO<MediaDS>{

    private SessionFactory sessionFactory;
    
    @Override
    public List<MediaDS> getList() {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<MediaDS> result = session.createQuery("from MediaDS").list();
		session.getTransaction().commit();
		return result;
    }

    @Override
    public MediaDS get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(MediaDS obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(MediaDS obj) {
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
