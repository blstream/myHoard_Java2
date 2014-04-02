package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.db.model.MediaDS;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class MediaDAO implements ResourceDAO<MediaDS> {

    private SessionFactory sessionFactory;

    @Override
    public int getTotalCount(String owner) {
        return ((Number)sessionFactory.getCurrentSession().createQuery("select count(*) from MediaDS where owner = '" + owner + "'").uniqueResult()).intValue();
    }

    @Override
    public List<MediaDS> getList(Map<String, Object> params) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MediaDS.class);
        if (params.containsKey("owner"))
            criteria.add(Restrictions.eq("owner", params.get("owner")));
        if (params.containsKey("id"))
            criteria.add(Restrictions.eq("id", params.get("id")));
        return criteria.list();
    }

    @Override
    public MediaDS get(int id) {
        MediaDS media = (MediaDS)sessionFactory.getCurrentSession().createCriteria(MediaDS.class).add(Restrictions.eq("id", id)).uniqueResult();
        if (media == null)
            throw new MyHoardException(ErrorCode.NOT_FOUND).add("id", "Odwołanie do nieistniejącego zasobu");
        return media;
    }

    @Override
    public void create(MediaDS obj) {
        try {
            sessionFactory.getCurrentSession().save(obj);
        } catch (HibernateException ex) {
            throw new MyHoardException(ex);
        }
    }

    @Override
    public void update(MediaDS obj) {
        try {
            sessionFactory.getCurrentSession().update(obj);
        } catch (HibernateException ex) {
            throw new MyHoardException(ex);
        }
    }

    @Override
    public void remove(int id) {
        try {
            sessionFactory.getCurrentSession().delete(get(id));
        } catch (HibernateException ex) {
            throw new MyHoardException(ex);
        }
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
