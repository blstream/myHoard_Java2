package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.db.model.MediaDS;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author gohilukk
 */
public class MediaDAO implements ResourceDAO<MediaDS> {

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
    public MediaDS get(int id) throws IndexOutOfBoundsException {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        MediaDS result = (MediaDS) session.createQuery("from MediaDS where id = " + id).uniqueResult();
        session.getTransaction().commit();
        return result;
    }

    @Override
    public void create(MediaDS obj) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(obj);
        session.getTransaction().commit();
    }

    @Override
    public void update(MediaDS obj) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.update(obj);
        session.getTransaction().commit();
    }

    @Override
    public void remove(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createQuery("delete MediaDS where id = " + id).executeUpdate();
        session.getTransaction().commit();
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
