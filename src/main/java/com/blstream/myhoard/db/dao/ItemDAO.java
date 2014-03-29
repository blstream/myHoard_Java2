package com.blstream.myhoard.db.dao;

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
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
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
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ItemDS.class);
        if (params.size() == 1)
            criteria.add(Restrictions.eq("owner", params.get("owner")));
        else if (params.containsKey("collection") && params.containsKey("owner"))
            criteria.add(Restrictions.eq("collection", params.get("collection")))
                    .add(Restrictions.eq("owner", params.get("owner")))
                    .list();
        else
            criteria.add(Restrictions.ilike("name", (String)params.get("name"), MatchMode.ANYWHERE))
                    .add(Restrictions.ilike("description", (String)params.get("name"), MatchMode.ANYWHERE))
                    .add(Restrictions.eq("collection", params.get("collection")))
                    .list();
//        List<ItemDS> result = criteria.list();
//        if (result.isEmpty())
//            throw new MyHoardException(202, "Resource not found", 404);
//        return result;
        return criteria.list();
    }

    @Override
    public ItemDS get(int id) {
        ItemDS object = (ItemDS)sessionFactory.getCurrentSession()
                .createCriteria(ItemDS.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
        if (object == null)
            throw new MyHoardException(202, "Not found", HttpServletResponse.SC_NOT_FOUND).add("id", "Odwołanie do nieistniejącego zasobu");
        return object;
    }

    @Override
    public void create(ItemDS obj) {
        Session session = sessionFactory.getCurrentSession();
        if (session.createCriteria(CollectionDS.class)
                .add(Restrictions.eq("owner", obj.getOwner()))
                .add(Restrictions.eq("id", obj.getCollection()))
                .list().isEmpty())
            throw new MyHoardException(403, "Próba zapisania elementu do obcej kolekcji");
        List<Integer> ids = new ArrayList<>();
        for (MediaDS i : obj.getMedia())
            ids.add(i.getId());
        // czy można to prościej zrealizować?
        if (!ids.isEmpty() && ((Number)session.createQuery("select count(*) from MediaDS as m where m.item is not null and m.id in (:ids)").setParameterList("ids", ids).iterate().next()).longValue() > 0)
            throw new MyHoardException(2, "Próba przepisania Media do innego elementu.");
        session.save(obj);
        List<MediaDS> media = session.createCriteria(MediaDS.class).add(Restrictions.in("id", ids)).list();
        for (MediaDS i : media) {
            i.setItem(obj.getId());
            session.update(i);
        }
    }

    @Override
    public void update(ItemDS obj) {
        ItemDS object = get(obj.getId());
        if (object == null)
            throw new MyHoardException(202, "Not found", HttpServletResponse.SC_NOT_FOUND).add("id", "Odwołanie do nieistniejącego zasobu");
        object.updateObject(obj);

        Session session = sessionFactory.getCurrentSession();
        if (session.createCriteria(CollectionDS.class)
                .add(Restrictions.eq("owner", object.getOwner()))
                .add(Restrictions.eq("id", object.getCollection()))
                .list().isEmpty())
            throw new MyHoardException(403, "Próba zapisania elementu do obcej kolekcji");
        if (obj.isMediaAltered()) {
            List<Integer> media = new ArrayList<>();
            for (MediaDS i : obj.getMedia())
                media.add(i.getId());
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
