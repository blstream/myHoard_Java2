package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.biz.exception.MyHoardException;
import java.util.List;
import org.hibernate.Session;
import com.blstream.myhoard.db.model.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CollectionDAO implements ResourceDAO<CollectionDS> {

    private SessionFactory sessionFactory;

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<CollectionDS> getList() {
        Session session = sessionFactory.getCurrentSession();
        List<CollectionDS> result = session.createCriteria(CollectionDS.class).list();
        // sorry, ale musiałem (do tak prymitywnej operacji natywne zapytanie pasuje, skoro nie można zastosować "derived property"
        List<Number> count = session.createSQLQuery("select count(Item.id) from Collection left join Item on Collection.id = Item.collection group by Collection.id").list();
        for (int i = 0; i < result.size(); i++)
            result.get(i).setItemsNumber(count.get(i).intValue());
        return result;
    }

    @Override
    public List<CollectionDS> getList(Map<String, Object> params) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(CollectionDS.class);
        if (params.containsKey("sort_dir")) {
            if ("asc".equals(params.get("sort_dir")))
                for (String i : (String[])params.get("sort_by"))
                    criteria.addOrder(Order.asc(i));
            else
                for (String i : (String[])params.get("sort_by"))
                    criteria.addOrder(Order.desc(i));
        }
        if (params.containsKey("max_count"))
            criteria.setMaxResults(Integer.parseInt((String)params.get("max_count")));
        if (params.containsKey("start_num"))
            criteria.setFirstResult(Integer.parseInt((String)params.get("start_num")));
        List<CollectionDS> result = criteria.list();
        StringBuilder builder = new StringBuilder("select count(Item.id) from Collection left join Item on Collection.id = Item.collection where Collection.id in (");
        for (int i = 0; i < result.size(); i++)
            builder.append('"').append(result.get(i).getId()).append("\",");
        builder.deleteCharAt(builder.length() - 1).append(") group by Collection.id");
        List<Number> count = session.createSQLQuery(builder.toString()).list();
        if ("asc".equals(params.get("sort_dir")))
            for (int i = 0; i < result.size(); i++)
                result.get(i).setItemsNumber(count.get(i).intValue());
        else
            for (int i = 0; i < result.size(); i++)
                result.get(i).setItemsNumber(count.get(result.size() - i - 1).intValue());
        return result;
    }

    @Override
    public CollectionDS get(int id) throws IndexOutOfBoundsException {
        Session session = sessionFactory.getCurrentSession();
        CollectionDS result = (CollectionDS)session.createCriteria(CollectionDS.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
        result.setItemsNumber(((Number)session.createQuery("select count(id) from ItemDS where collection = " + result.getId()).uniqueResult()).intValue());
        return result;
    }

    @Override
    public void create(CollectionDS obj) {
        Session session = sessionFactory.getCurrentSession();
        if (obj.isTagsAltered()) {
            List<String> tags = new ArrayList<>();
            if (obj.getTags() != null)
                for (TagDS i : obj.getTags())
                    tags.add(i.getTag());
            Set<TagDS> result = new HashSet<>(tags.isEmpty() ? Collections.EMPTY_SET : (List<TagDS>)session.createCriteria(TagDS.class)
                .add(Restrictions.in("tag", tags))
                .list());
            Set<TagDS> remaining = obj.getTags();
            remaining.removeAll(result);
            for (TagDS i : remaining)   // pozostałe tagi trzeba utworzyć
                session.save(i);
            result.addAll(remaining);
            obj.setTags(result);
        }
        if (obj.getOwner() == null)
            obj.setOwner("krol.julian");
        session.save(obj);
    }

    @Override
    public void update(CollectionDS obj) {
        CollectionDS object = get(obj.getId());
        object.updateObject(obj);
        
        Session session = sessionFactory.getCurrentSession();
        if (obj.isTagsAltered()) {
            List<String> tags = new ArrayList<>();
            for (TagDS i : obj.getTags())
                tags.add(i.getTag());
            Set<TagDS> result = new HashSet<>(tags.isEmpty() ? Collections.EMPTY_SET : (List<TagDS>)session.createCriteria(TagDS.class)
                .add(Restrictions.in("tag", tags))
                .list());
            Set<TagDS> remaining = object.getTags();
            remaining.removeAll(result);
            for (TagDS i : remaining)   // pozostałe tagi trzeba utworzyć
                session.save(i);
            result.addAll(remaining);
            object.setTags(result);
        }
        object.setModified_date(Calendar.getInstance().getTime());
        session.update(object);

        // TODO mapper
        obj.updateObject(object);
        obj.setTags(object.getTags());
        obj.setCreated_date(object.getCreated_date());
        obj.setModified_date(object.getModified_date());
    }

    @Override
    public void remove(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(session.createCriteria(CollectionDS.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult());
    }

//    @Override
//    public Criteria getCriteria() {
//        return sessionFactory.getCurrentSession().createCriteria(CollectionDS.class);
//    }

    @Override
    public CollectionDS getByAccess_token(String access_token) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CollectionDS getByUsername(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
