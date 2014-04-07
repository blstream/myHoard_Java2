package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import java.util.List;
import org.hibernate.Session;
import com.blstream.myhoard.db.model.*;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserDAO implements ResourceDAO<UserDS> {

    private SessionFactory sessionFactory;

    @Override
    public List<UserDS> getList(Map<String, Object> params) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserDS.class);
        if (!params.isEmpty()) {
            if (params.containsKey("email"))
                criteria.add(Restrictions.eq("email", params.get("email")));
            else if (params.containsKey("username"))
                criteria.add(Restrictions.eq("username", params.get("username")));
            else 
                throw new UnsupportedOperationException("Not supported yet.");
    }
        List<UserDS> result = criteria.list();
        if (result.isEmpty())
            throw new MyHoardException(ErrorCode.NOT_FOUND).add(params.containsKey("email") ? "email" : "username", "Odwołanie do nieistniejącego użytkownika");
        return result;
    }

    @Override
    public UserDS get(int id) {
        UserDS user = (UserDS)sessionFactory.getCurrentSession()
                .createCriteria(UserDS.class)
                .add(Restrictions.eq("id", id)).uniqueResult();
        if (user == null)
            throw new MyHoardException(ErrorCode.NOT_FOUND).add("id", "Odwołanie do nieistniejącego użytkownika");
        return user;
    }    

    @Override
    public void create(UserDS obj) {
        if (obj.getUsername() == null)
            obj.setUsername(obj.getEmail());
        try {
            sessionFactory.getCurrentSession().save(obj);
        } catch (HibernateException ex) {
            throw new MyHoardException(ex);
        }
    }

    @Override
    public void update(UserDS obj) {
        try {
            UserDS object = get(obj.getId());
            object.updateObject(obj);
            Session session = sessionFactory.getCurrentSession();
            session.update(object);
            obj.updateObject(object);
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

    @Override
    public int getTotalCount(String owner) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
