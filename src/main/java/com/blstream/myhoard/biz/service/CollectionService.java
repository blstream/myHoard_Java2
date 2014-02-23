package com.blstream.myhoard.biz.service;

import java.util.ArrayList;
import java.util.List;
import com.blstream.myhoard.biz.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.blstream.myhoard.db.dao.*;
import com.blstream.myhoard.db.model.CollectionDS;

public class CollectionService implements ResourceService<CollectionDTO> {

	@Autowired
	CollectionDAO collectionDAO;

	@Override
    public List<CollectionDTO> getList() {
        List<CollectionDS> data = collectionDAO.getList();
		List<CollectionDTO> result = new ArrayList<>();
		for (CollectionDS i : data)
			result.add(i.toCollectionDTO());
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
		obj.setCreatedDate(object.getCreatedDate());
		obj.setModifiedDate(object.getModifiedDate());
	}

	@Override
	public void remove(int id) {
		collectionDAO.remove(id);
	}

}
