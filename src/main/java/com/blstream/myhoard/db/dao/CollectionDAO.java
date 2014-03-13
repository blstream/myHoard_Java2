package com.blstream.myhoard.db.dao;

import java.util.List;
import org.hibernate.Session;
import com.blstream.myhoard.db.model.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Transactional
public class CollectionDAO implements ResourceDAO<CollectionDS> {


    private SessionFactory sessionFactory;



    public List<CollectionDS> getList() {
        Session session = sessionFactory.getCurrentSession();
//        List<CollectionDS> result = session.createQuery("select new CollectionDS(c.id, c.owner, c.name, c.description, count(item.id), c.createdDate, c.modifiedDate) from ItemDS as item right join item.collection as c group by c.id").list();
        List<CollectionDS> result = session.createQuery("from CollectionDS").list();
        List<Long> count = session.createQuery("select count(item.id) from ItemDS as item right join item.collection as c group by c.id").list();
        for (int i = 0; i < result.size(); i++)
            result.get(i).setItemsNumber(count.get(i).intValue());
        return result;
    }

    @Override
    public CollectionDS get(int id) throws IndexOutOfBoundsException {
        Session session = sessionFactory.getCurrentSession();
//        CollectionDS result = (CollectionDS)session.createQuery("select new CollectionDS(c.id, c.owner, c.name, c.description, count(item.id), c.createdDate, c.modifiedDate) from ItemDS as item right join item.collection as c where c.id = " + id + " group by c.id").uniqueResult();
        CollectionDS result = (CollectionDS)session.createQuery("from CollectionDS where id = " + id).uniqueResult();
        result.setItemsNumber(((Long)session.createQuery("select count(item.id) from ItemDS as item right join item.collection as c where c.id = " + id + " group by c.id").uniqueResult()).intValue());
        return result;
    }

    @Override
    public void create(CollectionDS obj) {
        Session session = sessionFactory.getCurrentSession();
        List<String> tags = new ArrayList<>();
        for (TagDS i : obj.getTags())
            tags.add(i.getTag());

        Set<TagDS> result = new HashSet<>((List<TagDS>)session.createQuery("from TagDS where tag in (:tags)").setParameterList("tags", tags).list());
        obj.getTags().removeAll(result);
        result.addAll(obj.getTags());
        obj.setTags(result);
        session.save(obj);
    }

    @Override
    public void update(CollectionDS obj) {
        CollectionDS object = get(obj.getId());
        object.updateObject(obj);
        List<String> tags = new ArrayList<>();
        for (TagDS i : obj.getTags())
            tags.add(i.getTag());
        
        Session session = sessionFactory.getCurrentSession();
        Set<TagDS> result = new HashSet<>((List<TagDS>)session.createQuery("from TagDS where tag in (:tags)").setParameterList("tags", tags).list());
        object.getTags().removeAll(result);
        result.addAll(object.getTags());
        object.setTags(result);
        object.setModifiedDate(Calendar.getInstance().getTime());
        session.update(object);

        // TODO mapper
        obj.updateObject(object);
        obj.setOwner(object.getOwner());
        obj.setCreatedDate(object.getCreatedDate());
        obj.setModifiedDate(object.getModifiedDate());
    }

    @Override
    public void remove(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("delete CollectionDS where id = " + id).executeUpdate();
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
