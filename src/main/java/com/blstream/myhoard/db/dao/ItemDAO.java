package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.db.model.ItemDS;
import com.blstream.myhoard.db.model.MediaDS;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ItemDAO implements ResourceDAO<ItemDS> {

    private SessionFactory sessionFactory;

    @Override
    public List<ItemDS> getList() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<ItemDS> result = session.createQuery("from ItemDS").list();
        session.getTransaction().commit();
        return result;
    }

    @Override
    public ItemDS get(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        ItemDS result = (ItemDS)session.createQuery("from ItemDS where id = " + id);
        session.getTransaction().commit();
        return result;
    }

    @Override
    public void create(ItemDS obj) {
        Session session = sessionFactory.getCurrentSession();
        List<Integer> ids = new ArrayList<>();
        for (MediaDS i : obj.getMedia())
            ids.add(i.getId());
        session.beginTransaction();
        // czy można to prościej zrealizować?
        if (((Long)session.createQuery("select count(*) from MediaDS as m where m.item is not null and m.id in (:ids)").setParameterList("ids", ids).iterate().next()).longValue() > 0)
            throw new MyHoardException(2, "Próba przepisania Media do innego elementu.");
        obj.setCreatedDate(Calendar.getInstance().getTime());
        obj.setModifiedDate((Date)obj.getCreatedDate().clone());
        session.save(obj);
        session.getTransaction().commit();
    }

    @Override
    public void update(ItemDS obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
