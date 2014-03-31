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
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ItemDAO implements ResourceDAO<ItemDS> {

    private SessionFactory sessionFactory;

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
            throw new MyHoardException(ErrorCode.NOT_FOUND).add("id", "Odwołanie do nieistniejącego zasobu");
        return object;
    }

    @Override
    public void create(ItemDS obj) {
        try {
            Session session = sessionFactory.getCurrentSession();
            if (session.createCriteria(CollectionDS.class)
                    .add(Restrictions.eq("owner", obj.getOwner()))
                    .add(Restrictions.eq("id", obj.getCollection()))
                    .list().isEmpty())
                throw new MyHoardException(ErrorCode.FORBIDDEN).add("owner", "Próba zapisania elementu do obcej kolekcji");

            List<Integer> ids = new ArrayList<>();
            if(!obj.getMedia().isEmpty()) {
                for (MediaDS i : obj.getMedia())
                    ids.add(i.getId());
                // czy można to prościej zrealizować?
                if (!ids.isEmpty()) {
                    if (((Number)session.createQuery("select count(*) from MediaDS as m where m.item is not null and m.id in (:ids)").setParameterList("ids", ids).iterate().next()).longValue() > 0)
                        throw new MyHoardException(ErrorCode.FORBIDDEN).add("owner", "Próba przepisania Media do innego elementu");
                    if (session.createCriteria(MediaDS.class)
                            .add(Restrictions.eq("owner", obj.getOwner()))
                            .add(Restrictions.in("id", ids)).list().size() != ids.size())
                        throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Próba przypisania obcego Media do elementu");
                }
            }
            session.save(obj);
            if(!ids.isEmpty()) {
                List<MediaDS> media = session.createCriteria(MediaDS.class).add(Restrictions.in("id", ids)).list();
                if(media.isEmpty())
                    throw new MyHoardException(ErrorCode.NOT_FOUND).add("id", "Odwołanie do nieistniejącego zasobu");
                else
                    for (MediaDS i : media) {
                        i.setItem(obj.getId());
                        session.update(i);
                    }
            }
        } catch (HibernateException ex) {
            throw new MyHoardException(ex);
        }
        
    }

    @Override
    public void update(ItemDS obj) {
        ItemDS object = get(obj.getId());
        if (object == null)
            throw new MyHoardException(ErrorCode.NOT_FOUND).add("id", "Odwołanie do nieistniejącego zasobu");
        object.updateObject(obj);

        try {
            Session session = sessionFactory.getCurrentSession();
            if (session.createCriteria(CollectionDS.class)
                    .add(Restrictions.eq("owner", object.getOwner()))
                    .add(Restrictions.eq("id", object.getCollection()))
                    .list().isEmpty())
                throw new MyHoardException(ErrorCode.FORBIDDEN).add("owner", "Próba zapisania elementu do obcej kolekcji");
            if (obj.isMediaAltered()) {
                List<Integer> media = new ArrayList<>();
                for (MediaDS i : obj.getMedia())
                    media.add(i.getId());
                if (session.createCriteria(MediaDS.class)
                            .add(Restrictions.eq("owner", obj.getOwner()))
                            .add(Restrictions.in("id", media)).list().size() != media.size())
                        throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Próba przypisania obcego Media do elementu");
                Set<MediaDS> result = new HashSet<>(media.isEmpty() ? Collections.EMPTY_SET : (List<MediaDS>)session.createCriteria(MediaDS.class)
                    .add(Restrictions.in("id", media))
                    .list());
                Set<MediaDS> remaining = object.getMedia();
                remaining.removeAll(result);
                for (MediaDS i : remaining)   // pozostałe media trzeba usunąć
                    session.delete(i);
                for (MediaDS i : result)
                    i.setItem(obj.getId());
                object.setMedia(result);
            }
            object.setModifiedDate(Calendar.getInstance().getTime());
            session.update(object);
        } catch (HibernateException ex) {
            throw new MyHoardException(ex);
        }

        obj.updateObject(object);
    }

    @Override
    public void remove(int id) {
        Session session = sessionFactory.getCurrentSession();
        ItemDS item = get(id);
//        session.createSQLQuery("delete from Media where item = " + item.getId());
        session.delete(item);
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
