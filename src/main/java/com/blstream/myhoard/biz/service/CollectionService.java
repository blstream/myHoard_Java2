package com.blstream.myhoard.biz.service;

import com.blstream.myhoard.biz.exception.MyHoardException;
import java.util.ArrayList;
import java.util.List;
import com.blstream.myhoard.biz.model.*;
import com.blstream.myhoard.db.dao.*;
import com.blstream.myhoard.db.model.CollectionDS;
import org.springframework.stereotype.Service;

@Service
public class CollectionService implements ResourceService<CollectionDTO> {

    private ResourceDAO<CollectionDS> collectionDAO;

    public void setCollectionDAO(ResourceDAO<CollectionDS> collectionDAO) {
        this.collectionDAO = collectionDAO;
    }

    @Override
    public List<CollectionDTO> getList() {
        List<CollectionDTO> result = new ArrayList<>();
        for (CollectionDS i : collectionDAO.getList()) {
            result.add(i.toCollectionDTO());
        }
        return result;
    }

    @Override
    public CollectionDTO get(int id) {
        return collectionDAO.get(id).toCollectionDTO();
    }

    @Override
    public void create(CollectionDTO obj) {
        CollectionDS collection = obj.toCollectionDS();
        collectionDAO.create(collection);
        obj.setId(Integer.toString(collection.getId()));
        obj.setOwner(collection.getOwner());
    }

    @Override
    public void update(CollectionDTO obj) {
        CollectionDS object = obj.toCollectionDS();
        collectionDAO.update(object);
        obj.updateObject(object.toCollectionDTO());
        obj.setOwner(object.getOwner());
        obj.setCreatedDate(object.getCreatedDate());
        obj.setModifiedDate(object.getModifiedDate());
    }

    @Override
    public void remove(int id) {
        try {
            collectionDAO.get(id);	// żeby "nie usuwało" nieistniejącego obiektu
            collectionDAO.remove(id);
        } catch (RuntimeException ex) {
            throw new MyHoardException(111, "Element o id(" + id + ") prawdopodobnie nie istnieje.");
        }
    }

}
