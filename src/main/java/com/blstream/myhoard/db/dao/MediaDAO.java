package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.db.model.MediaDS;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class MediaDAO implements ResourceDAO<MediaDS> {

    private SessionFactory sessionFactory;

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
            throw new MyHoardException(202, "Resource not found", HttpServletResponse.SC_NOT_FOUND);
        return media;
    }

    @Override
    public void create(MediaDS obj) {
        sessionFactory.getCurrentSession().save(obj);
    }

    @Override
    public void update(MediaDS obj) {
       sessionFactory.getCurrentSession().update(obj);
    }

    @Override
    public void remove(int id) {
        sessionFactory.getCurrentSession().delete(get(id));
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
