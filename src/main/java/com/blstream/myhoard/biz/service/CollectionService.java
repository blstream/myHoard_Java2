package com.blstream.myhoard.biz.service;

import com.blstream.myhoard.biz.exception.MyHoardException;
import java.util.ArrayList;
import java.util.List;
import com.blstream.myhoard.biz.model.*;
import com.blstream.myhoard.db.dao.*;
import com.blstream.myhoard.db.model.CollectionDS;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

@Service
public class CollectionService implements ResourceService<CollectionDTO> {

    private ResourceDAO<CollectionDS> collectionDAO;

    public void setCollectionDAO(ResourceDAO<CollectionDS> collectionDAO) {
        this.collectionDAO = collectionDAO;
    }

    @Override
    public List<CollectionDTO> getList(Map<String, Object> params) {
        if (params.containsKey("sort_dir") && !"asc".equals(params.get("sort_dir")) && !"desc".equals(params.get("sort_dir")))
            throw new MyHoardException(400, "Nieprawidłowy porządek : " + params.get("sort_dir"));

        try {
            if (params.containsKey("max_count") && Integer.parseInt((String)params.get("max_count")) <= 0)
                throw new MyHoardException(400, "Nieprawidłowa warotść max_count: " + params.get("max_count"));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(400, params.get("max_count") + " nie jest poprawną liczbą");
        }

        try {
            if (params.containsKey("start_num") && Integer.parseInt((String)params.get("start_num")) <= 0)
                throw new MyHoardException(400, "Nieprawidłowa warotść max_count: " + params.get("start_num"));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(400, params.get("start_num") + " nie jest poprawną liczbą");
        }

        List<CollectionDTO> list = new ArrayList<>();
        for (CollectionDS i : collectionDAO.getList(params))
            list.add(i.toDTO());
        return list;
    }

    @Override
    public CollectionDTO get(int id) {
        CollectionDS obj = collectionDAO.get(id);
        if (obj == null)
            return null;
        return obj.toDTO();
    }

    @Override
    public void create(CollectionDTO obj) {
        try {
            CollectionDS collection = new CollectionDS();
            collection.fromDTO(obj);
            collectionDAO.create(collection);
            obj.setId(Integer.toString(collection.getId()));
            obj.setOwner(collection.getOwner());
        } catch (RuntimeException ex) {
            throw new MyHoardException(400, "Nieznany błąd: " + ex.toString() + " > " + ex.getCause().toString());
        }
    }

    @Override
    public void update(CollectionDTO obj) {
        try {
            CollectionDS object = new CollectionDS();
            object.fromDTO(obj);
            collectionDAO.update(object);
            obj.fromDS(object);
        } catch (RuntimeException ex) {
            throw new MyHoardException(111, "Nieznany błąd: " + ex.toString() + " > " + ex.getCause().toString());
        }
    }

    @Override
    public void remove(int id) {
        try {
            collectionDAO.remove(id);
        } catch (RuntimeException ex) {
            throw new MyHoardException(400, "Nieznany błąd: " + ex.toString() + (ex.getCause() != null ? " > " + ex.getCause().toString() : ""));
        }
    }
}
