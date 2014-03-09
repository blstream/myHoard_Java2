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
        // TODO zmienić na ItemDS
        List<CollectionDS> result = session.createQuery("select new CollectionDS(c.id, c.owner, c.name, c.description, count(item.id), c.createdDate, c.modifiedDate) from ItemDS as item right join item.collection as c group by c.id").list();
        session.getTransaction().commit();
        return result;
    }

    @Override
    public CollectionDS get(int id) throws IndexOutOfBoundsException {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        CollectionDS result = (CollectionDS) session.createQuery("select new CollectionDS(c.id, c.owner, c.name, c.description, count(item.id), c.createdDate, c.modifiedDate) from ItemDS as item right join item.collection as c where c.id = " + id + " group by c.id").uniqueResult();
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

        // TODO mapper
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
