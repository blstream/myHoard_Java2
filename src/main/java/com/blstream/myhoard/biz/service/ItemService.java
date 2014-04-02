package com.blstream.myhoard.biz.service;

import com.blstream.myhoard.biz.model.ItemDTO;
import com.blstream.myhoard.db.dao.ResourceDAO;
import com.blstream.myhoard.db.model.ItemDS;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ItemService implements ResourceService<ItemDTO> {

    private ResourceDAO<ItemDS> itemDAO;

    public void setItemDAO(ResourceDAO<ItemDS> itemDAO) {
        this.itemDAO = itemDAO;
    }

    @Override
    public int getTotalCount(String owner) {
        return itemDAO.getTotalCount(owner);
    }

    @Override
    public List<ItemDTO> getList(Map<String, Object> params) {
        List<ItemDTO> result = new ArrayList<>();
        for (ItemDS i : itemDAO.getList(params))
            result.add(i.toDTO());
        return result;
    }

    @Override
    public ItemDTO get(int id) {
        return itemDAO.get(id).toDTO();
    }

    @Override
    public void create(ItemDTO obj) {
        ItemDS item = obj.toItemDS();
        itemDAO.create(item);
        item.toDTO(obj);
    }

    @Override
    public void update(ItemDTO obj) {
        ItemDS item = obj.toItemDS();
        itemDAO.update(item);
        item.toDTO(obj);
    }

    @Override
    public void remove(int id) {
        itemDAO.remove(id);
    }
}
