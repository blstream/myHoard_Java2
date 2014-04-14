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
            session.save(obj);
        } catch (HibernateException ex) {
            throw new MyHoardException(ex);
        }
        
    }

    @Override
    public void update(ItemDS obj) {
        ItemDS object = get(obj.getId());
        object.updateObject(obj);

        try {
            Session session = sessionFactory.getCurrentSession();
            if (session.createCriteria(CollectionDS.class)
                    .add(Restrictions.eq("owner", object.getOwner()))
                    .add(Restrictions.eq("id", object.getCollection()))
                    .uniqueResult() == null)
                throw new MyHoardException(ErrorCode.FORBIDDEN).add("owner", "Próba zapisania elementu do obcej kolekcji");

            List<Integer> media_ids = new ArrayList<>();
            Set<MediaDS> result;
            if (obj.isMediaAltered()) {
                for (MediaDS i : obj.getMedia())
                    media_ids.add(i.getId());
                if (!media_ids.isEmpty() && (result = new HashSet<>(session.createCriteria(MediaDS.class)
                            .add(Restrictions.eq("owner", obj.getOwner().getId()))
                            .add(Restrictions.in("id", media_ids)).list()))
                        .size() != media_ids.size())
                        throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Próba przypisania obcego Media do elementu");
            }
            object.setModified_date(Calendar.getInstance().getTime());
            session.update(object);
            // TODO usuwanie ewentualnych odpiętych mediów

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
