package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.db.model.SessionDS;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
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
    public List<SessionDS> getList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SessionDS> getList(Map<String, Object> params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SessionDS get(int id) {
        Session session = sessionFactory.getCurrentSession();
        return (SessionDS)session.createCriteria(SessionDS.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }
    
    @Override
    public SessionDS getByAccess_token(String access_token) {
        Session session = sessionFactory.getCurrentSession();
        return (SessionDS)session.createCriteria(SessionDS.class)
                .add(Restrictions.eq("accessToken", access_token))
                .uniqueResult();
    }

    @Override
    public void create(SessionDS obj) {
        Session session = sessionFactory.getCurrentSession();
        session.save(obj);
    }

    @Override
    public void update(SessionDS obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SessionDS getByUsername(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
