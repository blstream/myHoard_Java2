package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.biz.exception.MyHoardException;
import java.util.List;
import org.hibernate.Session;
import com.blstream.myhoard.db.model.*;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserDAO implements ResourceDAO<UserDS> {

    private SessionFactory sessionFactory;

    @Override
    public List<UserDS> getList(Map<String, Object> params) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserDS.class);
        if (params.containsKey("email"))
            criteria.add(Restrictions.eq("email", params.get("email")));
        else if (params.containsKey("username"))
            criteria.add(Restrictions.eq("username", params.get("username")));
        else
            throw new UnsupportedOperationException("Not supported yet.");
        List<UserDS> result = criteria.list();
        if (result.isEmpty())
            // może lepiej pozamieniać wszystkie wystąpięnia HttpServletResponse.kod_błędu na właściwą liczbę
            throw new MyHoardException(202, "Resource not found", HttpServletResponse.SC_NOT_FOUND);
        return result;
    }

    @Override
    public UserDS get(int id) {
        UserDS user = (UserDS)sessionFactory.getCurrentSession()
                .createCriteria(UserDS.class)
                .add(Restrictions.eq("id", id)).uniqueResult();
        if (user == null)
            throw new MyHoardException(202, "Resource not found", HttpServletResponse.SC_NOT_FOUND);
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
        UserDS object = get(obj.getId());
        object.updateObject(obj);
        Session session = sessionFactory.getCurrentSession();
        session.update(object);
        obj.updateObject(object);
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
