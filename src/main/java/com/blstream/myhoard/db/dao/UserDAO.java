package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import java.util.List;
import org.hibernate.Session;
import com.blstream.myhoard.db.model.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
            else if (params.containsKey("id"))
                criteria.add(Restrictions.eq("id", params.get("id")));
            else if (params.containsKey("get_observers")) {
                if (!((CollectionDS)sessionFactory.getCurrentSession().createCriteria(CollectionDS.class).add(Restrictions.eq("id", (Integer)params.get("collection"))).uniqueResult()).isVisible())
                    return Collections.EMPTY_LIST;
                List<String> emails = sessionFactory.getCurrentSession().createSQLQuery("select email from Favourite left join User on Favourite.user = User.id where Favourite.collection = " + (Integer)params.get("collection") + " and User.id <> " + (Integer)params.get("owner")).list();
                List<UserDS> users = new ArrayList<>();
                for (String email : emails)
                    users.add(new UserDS(-1, email, "", "",true));
                return users;
            } else if(params.containsKey("currentUsername")) {
                criteria.add(Restrictions.or(
                Restrictions.eq("username", params.get("currentUsername")),
                Restrictions.conjunction(
                    Restrictions.eq("visible", Boolean.TRUE),
                    Restrictions.ne("username", params.get("currentUsername"))
                )
            ));
            }
            else 
                throw new UnsupportedOperationException("Not supported yet.");
        }
        List<UserDS> result = criteria.list();
        if (result.isEmpty())
            throw new MyHoardException(ErrorCode.NOT_FOUND).add(params.containsKey("email") ? "email" : params.containsKey("username") ? "username" : "id", "Odwołanie do nieistniejącego użytkownika");
        if (params.containsKey("fetch_favourites")) {
            UserDS user = result.get(0);
            Set<CollectionDS> favourites = new HashSet<>();
            for (CollectionDS c : user.getFavourites())
                favourites.add(c);
            user.setFavourites(favourites);
        }
        if (params.containsKey("add_collection")) {
            UserDS user = result.get(0);
            CollectionDS collection = new CollectionDS();
            collection.setId((Integer)params.get("add_collection"));
            if (!user.getFavourites().contains(collection)) {
                collection = (CollectionDS)sessionFactory.getCurrentSession().createCriteria(CollectionDS.class).add(Restrictions.eq("id", collection.getId())).uniqueResult();
                if (collection == null)
                    throw new MyHoardException(ErrorCode.NOT_FOUND).add("id", "Brak kolekcji o podanym id");
                else if (collection.isVisible() == false && user.getId() != collection.getOwner().getId())
                    throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Brak dostępu do kolekcji o podanym id");
                else {
                    user.getFavourites().add(collection);
                    sessionFactory.getCurrentSession().update(user);
                }
            }
            return Collections.EMPTY_LIST;
        } else if (params.containsKey("remove_collection")) {
            UserDS user = result.get(0);
            CollectionDS collection = new CollectionDS();
            collection.setId((Integer)params.get("remove_collection"));
            if (user.getFavourites().contains(collection)) {
                user.getFavourites().remove(collection);
                sessionFactory.getCurrentSession().update(user);
            } else
                throw new MyHoardException(ErrorCode.NOT_FOUND).add("id", "Brak kolekcji o podanym id");
            return Collections.EMPTY_LIST;
        }
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
