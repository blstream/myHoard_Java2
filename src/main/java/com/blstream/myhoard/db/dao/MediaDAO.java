package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.db.model.MediaDS;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class MediaDAO implements ResourceDAO<MediaDS> {

    private SessionFactory sessionFactory;

    @Override
    public List<MediaDS> getList() {
        return sessionFactory.getCurrentSession().createCriteria(MediaDS.class).list();
    }

    @Override
    public List<MediaDS> getList(Map<String, Object> params) {
        Session session = sessionFactory.getCurrentSession();
        if (params.size() == 1)
            return session.createQuery("from MediaDS as m where m.item in (from ItemDS as i where i.owner = '" + params.get("owner") + "')")
                    .list();
        else if (params.size() == 2)    // id oraz owner
            return session.createQuery("from MediaDS as m where m.id = " + params.get("id")
                    + " and m.item in (from ItemDS as i where i.owner = '" + params.get("owner") + "')")
                    .list();
        else
            return null;
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
        sessionFactory.getCurrentSession().delete(sessionFactory.getCurrentSession()
                .createCriteria(MediaDS.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult());
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public MediaDS getByAccess_token(String access_token) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MediaDS getByEmail(String email) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MediaDS getByRefresh_token(String refresh_token) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
