package com.blstream.myhoard.biz.service;

import com.blstream.myhoard.biz.exception.MyHoardException;
import java.util.ArrayList;
import java.util.List;
import com.blstream.myhoard.biz.model.*;
import com.blstream.myhoard.db.dao.*;
import com.blstream.myhoard.db.model.CollectionDS;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CollectionService implements ResourceService<CollectionDTO> {

    private ResourceDAO<CollectionDS> collectionDAO;

    public void setCollectionDAO(ResourceDAO<CollectionDS> collectionDAO) {
        this.collectionDAO = collectionDAO;
    }

    @Override
    public List<CollectionDTO> getList() {
        try {
            List<CollectionDTO> result = new ArrayList<>();
            for (CollectionDS i : collectionDAO.getList())
                result.add(i.toDTO());
            return result;
        } catch (RuntimeException ex) {
            throw new MyHoardException(300, "Nieznany błąd: " + ex.toString() + " > " + ex.getCause().toString());
        }
    }

    @Override
    public List<CollectionDTO> getList(Map<String, String> params) {
        if (!"asc".equals(params.get("sort_dir")) && !"desc".equals(params.get("sort_dir")))
            throw new MyHoardException(400, "Nieprawidłowy porządek : " + params.get("sort_dir"));
        if (Integer.parseInt(params.get("max_count")) <= 0)
            throw new MyHoardException(400, "Nieprawidłowa warotść max_count: " + params.get("max_count"));
        List<CollectionDTO> list = new ArrayList<>();
        for (CollectionDS i : collectionDAO.getList(params))
            list.add(i.toDTO());
        return list;
    }

    @Override
    public CollectionDTO get(int id) {
        try {
            return collectionDAO.get(id).toDTO();
        } catch (RuntimeException ex) {
            throw new MyHoardException(300, "Element o id(" + id + ") prawdopodobnie nie istnieje.");
        }
    }

    @Override
    public void create(CollectionDTO obj) {
        try {
            CollectionDS collection = new CollectionDS();
            collection.toDTO(obj);
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
            throw new MyHoardException(400, "Element o id(" + id + ") prawdopodobnie nie istnieje.");
        }
    }
}
