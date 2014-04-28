package com.blstream.myhoard.biz.service;

import com.blstream.myhoard.biz.exception.ErrorCode;
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
    public int getTotalCount(String owner) {
        return collectionDAO.getTotalCount(owner);
    }

    @Override
    public List<CollectionDTO> getList(Map<String, Object> params) {
        if (params.containsKey("sort_dir") && !"asc".equals(params.get("sort_dir")) && !"desc".equals(params.get("sort_dir")))
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("sort_dir", "Nieprawidłowy porządek: " + params.get("sort_dir"));

        try {
            if (params.containsKey("max_count") && ((Integer)params.get("max_count")) <= 0)
                throw new MyHoardException(ErrorCode.BAD_REQUEST).add("max_count", "Nieprawidłowa wartość: " + params.get("max_count"));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("max_count", "Nieprawidłowa wartość: " + params.get("max_count"));
        }

        try {
            if (params.containsKey("start_num") && ((Integer)params.get("start_num")) < 0)
                throw new MyHoardException(ErrorCode.BAD_REQUEST).add("start_num", "Nieprawidłowa wartość: " + params.get("start_num"));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("start_num", "Nieprawidłowa wartość: " + params.get("max_count"));
        }

        List<CollectionDTO> list = new ArrayList<>();
        for (CollectionDS i : collectionDAO.getList(params))
            list.add(i.toDTO());
        return list;
    }

    @Override
    public CollectionDTO get(int id) {
        return collectionDAO.get(id).toDTO();
    }

    @Override
    public void create(CollectionDTO obj) {
        CollectionDS collection = new CollectionDS();
        collection.fromDTO(obj);
        collectionDAO.create(collection);
        obj.setId(Integer.toString(collection.getId()));
        obj.setCreatedDate(collection.getCreated_date_client());
        obj.setModifiedDate(collection.getModified_date_client());
    }

    @Override
    public void update(CollectionDTO obj) {
        CollectionDS object = new CollectionDS();
        object.fromDTO(obj);
        collectionDAO.update(object);
        obj.fromDS(object);
    }

    @Override
    public void remove(int id) {
        collectionDAO.remove(id);
    }
}
