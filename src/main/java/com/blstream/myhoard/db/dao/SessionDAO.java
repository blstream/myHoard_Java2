package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.db.model.SessionDS;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class SessionDAO implements ResourceDAO<SessionDS> {

    private SessionFactory sessionFactory;

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<SessionDS> getList(Map<String, Object> params) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SessionDS.class);
        if (params.containsKey("accessToken"))
            criteria.add(Restrictions.eq("accessToken", params.get("accessToken")));
        if (params.containsKey("refreshToken"))
            criteria.add(Restrictions.eq("refreshToken", params.get("refreshToken")));
        return criteria.list();
    }

    @Override
    public SessionDS get(int id) {
        SessionDS session = (SessionDS)sessionFactory.getCurrentSession().createCriteria(SessionDS.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
        if (session == null)
            throw new MyHoardException(ErrorCode.NOT_FOUND).add("id", "Odwołanie do nieistniejącego zasobu");
        return session;
    }

    @Override
    public void create(SessionDS obj) {
        try {
            sessionFactory.getCurrentSession().save(obj);
        } catch (HibernateException ex) {
            throw new MyHoardException(ex);
        }
    }

    @Override
    public void update(SessionDS obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
