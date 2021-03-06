package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.db.model.CollectionDS;
import com.blstream.myhoard.db.model.ItemDS;
import com.blstream.myhoard.db.model.MediaDS;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ItemDAO implements ResourceDAO<ItemDS> {

    private SessionFactory sessionFactory;

    @Override
    public int getTotalCount(String owner) {
        return ((Number)sessionFactory.getCurrentSession().createQuery("select count(*) from ItemDS where owner = '" + owner + "'").uniqueResult()).intValue();
    }

    @Override
    public List<ItemDS> getList(Map<String, Object> params) {
        try {
            Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ItemDS.class);
            switch ((String)params.get("option")) {
                case "list":
                    criteria.add(Restrictions.eq("owner", params.get("owner")));
                    break;
                case "listfrom":
                    criteria.add(Restrictions.eq("collection", params.get("collection")))
                            .add(Restrictions.eq("owner", params.get("owner")));
                    break;
                case "find":
                    criteria.add(Restrictions.conjunction(
                            Restrictions.eq("collection", params.get("collection")),
                            Restrictions.disjunction(
                                    Restrictions.ilike("name", (String)params.get("name"), MatchMode.ANYWHERE),
                                    Restrictions.ilike("description", (String)params.get("name"), MatchMode.ANYWHERE)
                            )
                    ));
                    break;
                default:
                    return Collections.EMPTY_LIST;
            }
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
            return criteria.list();
        } catch (HibernateException ex) {
            throw new MyHoardException(ex);
        }
    }

    @Override
    public ItemDS get(int id) {
        ItemDS object = (ItemDS)sessionFactory.getCurrentSession()
                .createCriteria(ItemDS.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
        if (object == null)
            throw new MyHoardException(ErrorCode.NOT_FOUND).add("id", "Odwołanie do nieistniejącego elementu");
        return object;
    }

    @Override
    public void create(ItemDS obj) {
        try {
            Session session = sessionFactory.getCurrentSession();
            if (session.createCriteria(CollectionDS.class)
                    .add(Restrictions.eq("owner", obj.getOwner()))
                    .add(Restrictions.eq("id", obj.getCollection()))
                    .uniqueResult() == null)
                throw new MyHoardException(ErrorCode.FORBIDDEN).add("owner", "Próba zapisania elementu do obcej kolekcji");

            if (!obj.getMedia().isEmpty()) {
                List<Integer> media_ids = new ArrayList<>();
                Set<MediaDS> media;
                for (MediaDS i : obj.getMedia())
                    media_ids.add(i.getId());
                if ((media = new HashSet<>(session.createCriteria(MediaDS.class)
                            .add(Restrictions.eq("owner", obj.getOwner().getId()))
                            .add(Restrictions.in("id", media_ids)).list()))
                        .size() != media_ids.size())    // czy wszystkie media są moje?
                    throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Próba przypisania obcego Media do elementu");
                obj.setMedia(media);
            }
            if (obj.getCreated_date_client() == null)
                obj.setCreated_date_client(obj.getCreated_date());
            if (obj.getModified_date_client() == null)
                obj.setModified_date_client(obj.getModified_date());
            session.save(obj);
        } catch (HibernateException ex) {
            throw new MyHoardException(ex);
        }
        
    }

    @Override
    public void update(ItemDS obj) {
        ItemDS object = get(obj.getId());
        Set<MediaDS> hanging = object.getMedia();
        hanging.removeAll(obj.getMedia());
        object.updateObject(obj);

        try {
            Session session = sessionFactory.getCurrentSession();
            if (session.createCriteria(CollectionDS.class)
                    .add(Restrictions.eq("owner", object.getOwner()))
                    .add(Restrictions.eq("id", object.getCollection()))
                    .uniqueResult() == null)
                throw new MyHoardException(ErrorCode.FORBIDDEN).add("owner", "Próba zapisania elementu do obcej kolekcji");

            List<Number> media_ids = new ArrayList<>();
            if (obj.isMediaAltered()) {
                for (MediaDS i : obj.getMedia())
                    media_ids.add(i.getId());
                if (!media_ids.isEmpty() && session.createCriteria(MediaDS.class)
                            .add(Restrictions.eq("owner", obj.getOwner().getId()))
                            .add(Restrictions.in("id", media_ids))
                        .list().size() != media_ids.size())
                    throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Próba przypisania obcego Media do elementu");
                media_ids.clear();
                if (obj.isMediaAltered() && !hanging.isEmpty()) {
                    for (MediaDS i : hanging)
                        media_ids.add(i.getId());
                    List<Number> to_delete = session.createSQLQuery("SELECT id FROM Media LEFT JOIN ItemMedia ON Media.id = ItemMedia.media WHERE owner = " + obj.getOwner().getId() + " AND item = " + obj.getId() + " GROUP BY id HAVING COUNT(media) = 1").list();
                    to_delete.retainAll(media_ids);
                    media_ids = to_delete;
                }
            }
            object.setModified_date(Calendar.getInstance().getTime());
            if (obj.getModified_date_client() == null)
                object.setModified_date_client(obj.getModified_date());
            session.update(object);

            if (!media_ids.isEmpty())
                for (MediaDS i : (List<MediaDS>)session.createCriteria(MediaDS.class).add(Restrictions.in("id", media_ids)).list())
                    session.delete(i);

        } catch (HibernateException ex) {
            throw new MyHoardException(ex);
        }

        obj.updateObject(object);
    }

    @Override
    public void remove(int id) {
        Session session = sessionFactory.getCurrentSession();
        ItemDS item = get(id);
        List<Number> media_ids = session.createSQLQuery("SELECT id FROM Media LEFT JOIN ItemMedia ON Media.id = ItemMedia.media WHERE owner = " + item.getOwner().getId() + " AND item = " + id + " GROUP BY id HAVING COUNT(media) <= 1").list();
        session.delete(item);
        if (!media_ids.isEmpty())
            for (MediaDS i : (List<MediaDS>)session.createCriteria(MediaDS.class).add(Restrictions.in("id", media_ids)).list())
                session.delete(i);
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
