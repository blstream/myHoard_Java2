package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.biz.exception.ErrorCode;
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
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
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
    public int getTotalCount(String owner) {
        return ((Number)sessionFactory.getCurrentSession().createQuery("select count(*) from CollectionDS where owner = " + owner).uniqueResult()).intValue();
    }

    @Override
    public List<CollectionDS> getList(Map<String, Object> params) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(CollectionDS.class);
        if (params.containsKey("sort_dir")) {
            if ("asc".equals(params.get("sort_dir")))
                for (String i : (String[])params.get("sort_by"))
                    criteria.addOrder(Order.asc(i));
            else if ("desc".equals(params.get("sort_dir")))
                for (String i : (String[])params.get("sort_by"))
                    criteria.addOrder(Order.desc(i));
            else
                throw new MyHoardException(ErrorCode.BAD_REQUEST).add("sort_dir", "Niepoprawny kierunek sortowania");
        }
        if (params.containsKey("max_count"))
            criteria.setMaxResults((Integer)params.get("max_count"));
        if (params.containsKey("start_num"))
            criteria.setFirstResult((Integer)params.get("start_num"));
        if (params.containsKey("name"))
            criteria.add(
                Restrictions.ilike("name", (String)params.get("name"), MatchMode.ANYWHERE)
            );
        if (params.get("options").equals("all")) {
            criteria.add(Restrictions.or(
                Restrictions.eq("owner", params.get("owner")),
                Restrictions.conjunction(
                    Restrictions.eq("visible", Boolean.TRUE),
                    Restrictions.ne("owner", params.get("owner"))
                )
            ));
        } else if (params.get("options").equals("current")) {
            criteria.add(Restrictions.eq("owner",params.get("owner")));
        } else if (params.get("options").equals("user")) {
            UserDS tmp = new UserDS();
            tmp.setId(Integer.parseInt((String)params.get("userId")));
            criteria.add(Restrictions.conjunction(
                Restrictions.eq("owner", tmp),
                Restrictions.eq("visible", Boolean.TRUE)
            ));
        }
        
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
            throw new MyHoardException(ErrorCode.NOT_FOUND).add("id", "Odwołanie do nieistniejącej kolekcji");
        result.setItemsNumber(((Number)session.createQuery("select count(id) from ItemDS where collection = " + result.getId()).uniqueResult()).intValue());
        return result;
    }

    @Override
    public void create(CollectionDS obj) {
        try {
            if (obj.getCreated_date_client() == null)
                obj.setCreated_date_client(obj.getCreated_date());
            if (obj.getModified_date_client() == null)
                obj.setModified_date_client(obj.getModified_date());
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
                for (TagDS i : remaining) // pozostałe tagi trzeba utworzyć
                    session.save(i);
                result.addAll(remaining);
                obj.setTags(Collections.EMPTY_SET);
                session.save(obj);

                obj.setTags(result);
                update(obj);
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
                for (TagDS i : remaining) // pozostałe tagi trzeba utworzyć
                    session.save(i);
                result.addAll(remaining);
                object.setTags(result);
            }
            object.setModified_date(Calendar.getInstance().getTime());
            if (obj.getModified_date_client() == null)
                object.setModified_date_client(obj.getModified_date());
            session.update(object);
        } catch (HibernateException ex) {
            throw new MyHoardException(ex);
        }

        // TODO mapper
        obj.updateObject(object);
        obj.setTags(object.getTags());
        obj.setCreated_date(object.getCreated_date());
        obj.setModified_date(object.getModified_date());
        obj.setCreated_date_client(object.getCreated_date_client());
        obj.setModified_date_client(object.getModified_date_client());
    }

    @Override
    public void remove(int id) {
        Session session = sessionFactory.getCurrentSession();
        CollectionDS collection = get(id);
        session.delete(collection);
        List<Number> media_ids = session.createSQLQuery("SELECT id FROM Media LEFT JOIN ItemMedia ON Media.id = ItemMedia.media WHERE owner = " + collection.getOwner().getId() + " GROUP BY id HAVING COUNT(media) = 0").list();
        if (!media_ids.isEmpty())
            for (MediaDS i : (List<MediaDS>)session.createCriteria(MediaDS.class).add(Restrictions.in("id", media_ids)).list())
                session.delete(i);
    }
}
