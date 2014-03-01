package com.blstream.myhoard.db.dao;

import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.blstream.myhoard.db.model.MediaDS;

public class MediaDAO implements ResourceDAO<MediaDS>{

    private static SessionFactory sessionFactory;

    @Autowired
    private DriverManagerDataSource ds;

    @PostConstruct
    private void initialize() {
        if (sessionFactory == null)
            try {
                sessionFactory = new org.hibernate.cfg.Configuration()
                        .configure()
                        .setProperty("hibernate.connection.url", ds.getUrl())
                        .setProperty("hibernate.connection.username", ds.getUsername())
                        .setProperty("hibernate.connection.password", ds.getPassword())
                        .buildSessionFactory();
            } catch (HibernateException ex) {
                // Log the exception.
                System.err.println("Initial SessionFactory creation failed." + ex);
                throw new ExceptionInInitializerError(ex);
            }
    }    
    
    @Override
    public List<MediaDS> getList() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        List<MediaDS> result = session.createQuery("from MediaDS").list();

        sessionFactory.getCurrentSession().getTransaction().commit();
        return result;
    }

    @Override
    public MediaDS get(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void create(MediaDS obj) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(MediaDS obj) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void remove(int id) {
        // TODO Auto-generated method stub
        
    }

}
