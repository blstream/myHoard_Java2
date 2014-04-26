package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.db.model.CollectionDS;
import com.blstream.myhoard.db.model.CommentDS;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author gohilukk
 */
@Transactional
public class CommentDAO implements ResourceDAO<CommentDS> {

    private SessionFactory sessionFactory;

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public int getTotalCount(String owner) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommentDS> getList(Map<String, Object> params) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CommentDS.class);
        if (params.containsKey("owner"))
            criteria.add(Restrictions.eq("owner", params.get("owner")));
        if (params.containsKey("collection"))
            criteria.add(Restrictions.eq("collection", params.get("collection")));
        List<CommentDS> commentsList = criteria.list();
        return commentsList;
    }

    @Override
    public CommentDS get(int id) {
        CommentDS object = (CommentDS) sessionFactory.getCurrentSession()
                .createCriteria(CommentDS.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
        if (object == null) {
            throw new MyHoardException(ErrorCode.NOT_FOUND).add("id", "Odwołanie do nieistniejącego komentarza");
        }
        return object;
    }

    @Override
    public void create(CommentDS obj) {
        try {
            sessionFactory.getCurrentSession().save(obj);
        } catch (HibernateException ex) {
            throw new MyHoardException(ErrorCode.FORBIDDEN).add("collection", "Próba dodania komentarza do nieistniejącej kolekcji");
        }
    }

    @Override
    public void update(CommentDS obj) {
        try {
            sessionFactory.getCurrentSession().update(obj);
        } catch (HibernateException ex) {
            throw new MyHoardException(ErrorCode.FORBIDDEN).add("collection", "Próba aktualizacji komentarza do nieistniejącej kolekcji");
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
}
