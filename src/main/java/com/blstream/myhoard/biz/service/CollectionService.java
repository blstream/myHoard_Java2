package com.blstream.myhoard.biz.service;

import java.util.ArrayList;
import java.util.List;
import com.blstream.myhoard.biz.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.blstream.myhoard.db.dao.*;

public class CollectionService implements ResourceService<CollectionDTO> {

	@Autowired
	CollectionDAO collectionDAO;

    public List<CollectionDTO> getList() {
        //return collectionDAO.getList();
	    return null;
	}

	public CollectionDTO get(int id) {
        return null;
	}

	public void create(CollectionDTO collection) {
	    //collectionDAO.create(collection.convertToCollectionDS());
	}

	public void update(CollectionDTO obj) {
		// TODO Auto-generated method stub
		
	}

	public void remove(int id) {
		// TODO Auto-generated method stub
		
	}

}
