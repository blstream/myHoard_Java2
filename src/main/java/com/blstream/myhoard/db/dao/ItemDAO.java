package com.blstream.myhoard.db.dao;

import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.db.model.ItemDS;
import com.blstream.myhoard.db.model.MediaDS;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ItemDAO implements ResourceDAO<ItemDS> {

    private SessionFactory sessionFactory;

    @Override
    public List<ItemDS> getList() {
        Session session = sessionFactory.getCurrentSession();
//        List result = session.createQuery("from ItemDS").list();
        List<ItemDS> result = session.createQuery("from ItemDS").list();
        return result;
    }

    @Override
    public ItemDS get(int id) {
        Session session = sessionFactory.getCurrentSession();
        ItemDS result = (ItemDS)session.createQuery("from ItemDS where id = " + id).uniqueResult();
        return result;
    }

    @Override
    public void create(ItemDS obj) {
        Session session = sessionFactory.getCurrentSession();
        List<Integer> ids = new ArrayList<>();
        for (MediaDS i : obj.getMedia())
            ids.add(i.getId());
        // czy można to prościej zrealizować?
        if (!ids.isEmpty() && ((Long)session.createQuery("select count(*) from MediaDS as m where m.item is not null and m.id in (:ids)").setParameterList("ids", ids).iterate().next()).longValue() > 0)
            throw new MyHoardException(2, "Próba przepisania Media do innego elementu.");
        obj.setCreatedDate(Calendar.getInstance().getTime());
        obj.setModifiedDate((Date)obj.getCreatedDate().clone());
        session.save(obj);
    }

    @Override
    public void update(ItemDS obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("delete ItemDS where id = " + id).executeUpdate();
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
