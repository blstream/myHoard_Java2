package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.db.model.MediaDS;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class MediaDAO implements ResourceDAO<MediaDS> {

    private SessionFactory sessionFactory;

    @Override
    public List<MediaDS> getList() {
        Session session = sessionFactory.getCurrentSession();
        List<MediaDS> result = session.createQuery("from MediaDS").list();
        return result;
    }

    @Override
    public List<MediaDS> getList(Map<String, String> args) {
        return null;
    }

    @Override
    public MediaDS get(int id) throws IndexOutOfBoundsException {
        Session session = sessionFactory.getCurrentSession();
        MediaDS result = (MediaDS) session.createQuery("from MediaDS where id = " + id).uniqueResult();
        return result;
    }

    @Override
    public void create(MediaDS obj) {
        Session session = sessionFactory.getCurrentSession();
        session.save(obj);
    }

    @Override
    public void update(MediaDS obj) {
        Session session = sessionFactory.getCurrentSession();
        session.update(obj);
    }

    @Override
    public void remove(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("delete MediaDS where id = " + id).executeUpdate();
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
