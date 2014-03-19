package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.db.model.ItemDS;
import com.blstream.myhoard.db.model.MediaDS;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ItemDAO implements ResourceDAO<ItemDS> {

    private SessionFactory sessionFactory;

    @Override
    public List<ItemDS> getList() {
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(ItemDS.class).list();
    }

    @Override
    public List<ItemDS> getList(Map<String, String> args) {
        return null;
    }

    @Override
    public ItemDS get(int id) {
        Session session = sessionFactory.getCurrentSession();
        return (ItemDS)session.createCriteria(ItemDS.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public void create(ItemDS obj) {
        Session session = sessionFactory.getCurrentSession();
        List<Integer> ids = new ArrayList<>();
        for (MediaDS i : obj.getMedia())
            ids.add(i.getId());
        // czy można to prościej zrealizować?
        if (!ids.isEmpty() && ((Long)session.createQuery("select count(*) from MediaDS as m where m.item is not null and m.id in (:ids)").setParameterList("ids", ids).iterate().next()).longValue() > 0)
            throw new MyHoardException(2, "Próba przepisania Media do innego elementu.");
        session.save(obj);
    }

    @Override
    public void update(ItemDS obj) {
        ItemDS object = get(obj.getId());
        object.updateObject(obj);

        Session session = sessionFactory.getCurrentSession();
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
            object.setMedia(result);
        }
        object.setModifiedDate(Calendar.getInstance().getTime());
        session.update(object);

        obj.updateObject(object);
    }

    @Override
    public void remove(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(session.createCriteria(ItemDS.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult());
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
