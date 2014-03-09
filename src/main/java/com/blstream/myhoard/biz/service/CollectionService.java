package com.blstream.myhoard.biz.service;

import java.util.ArrayList;
import java.util.List;
import com.blstream.myhoard.biz.model.*;
import com.blstream.myhoard.db.dao.*;
import com.blstream.myhoard.db.model.CollectionDS;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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
    }

    @Override
    public void update(CollectionDTO obj) {
        CollectionDS object = obj.toCollectionDS();
        collectionDAO.update(object);
        obj.updateObject(object.toCollectionDTO());
        obj.setOwner(object.getOwner());
        obj.setCreated_date(object.getCreatedDate());
        obj.setModified_date(object.getModifiedDate());
    }

    @Override
    public void remove(int id) {
        collectionDAO.get(id);	// żeby "nie usuwało" nieistniejącego obiektu
        collectionDAO.remove(id);
    }

}
