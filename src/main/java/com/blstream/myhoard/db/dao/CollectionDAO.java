package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.biz.exception.MyHoardException;
import java.util.List;
import org.hibernate.Session;
import com.blstream.myhoard.db.model.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
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
        if(params.containsKey("owner"))
            criteria.add(Restrictions.eq("owner", params.get("owner")));
        List<CollectionDS> result = criteria.list();
        if(!result.isEmpty()) {
            Map<Integer, CollectionDS> map = new HashMap<>();
            for (CollectionDS i: result)
                map.put(i.getId(), i);
            StringBuilder builder = new StringBuilder("select Collection.id, count(Item.id) from Collection left join Item on Collection.id = Item.collection where Collection.id in (");
            for (CollectionDS i : result)
                builder.append(i.getId()).append(',');
            builder.deleteCharAt(builder.length() - 1).append(") group by Collection.id");
            List<Object[]> count = session.createSQLQuery(builder.toString()).list();
            for (Object[] i : count)
                map.get(((Number)i[0]).intValue()).setItemsNumber(((Number)i[1]).intValue());
        }
        return result;
    }

    @Override
    public CollectionDS get(int id) {
        Session session = sessionFactory.getCurrentSession();
        CollectionDS result = (CollectionDS)session.createCriteria(CollectionDS.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
        if (result == null)
            throw new MyHoardException(202, "Resource not found", HttpServletResponse.SC_NOT_FOUND).add("id", "Odwołanie do nieistniejącego zasobu");
        result.setItemsNumber(((Number)session.createQuery("select count(id) from ItemDS where collection = " + result.getId()).uniqueResult()).intValue());
        return result;
    }

    @Override
    public void create(CollectionDS obj) {
        try {
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
                obj.setTags(Collections.EMPTY_SET);
                session.save(obj);

                CollectionDS object = new CollectionDS();
                object.setId(obj.getId());
                object.setOwner(obj.getOwner());
                object.setTags(result);
                update(object);
            }
            else
                session.save(obj);
        } catch (HibernateException ex) {
            throw new MyHoardException(ex);
        }
    }

    @Override
    public void update(CollectionDS obj) {
        CollectionDS object = get(obj.getId());
        object.updateObject(obj);

        try {
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
        } catch (HibernateException ex) {
            throw new MyHoardException(ex);
        }

        // TODO mapper
        obj.updateObject(object);
        obj.setTags(object.getTags());
        obj.setCreated_date(object.getCreated_date());
        obj.setModified_date(object.getModified_date());
    }

    @Override
    public void remove(int id) {
        Session session = sessionFactory.getCurrentSession();
        CollectionDS collection = get(id);
        // pozbycie się ewentualnych tagów
//        session.createSQLQuery("delete from CollectionTag where collection = " + collection.getId()).executeUpdate();
        session.delete(collection);
    }
}
