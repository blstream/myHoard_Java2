package com.blstream.myhoard.biz.service;

import com.blstream.myhoard.biz.model.ItemDTO;
import com.blstream.myhoard.db.dao.ResourceDAO;
import com.blstream.myhoard.db.model.ItemDS;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ItemService implements ResourceService<ItemDTO> {

    private ResourceDAO<ItemDS> itemDAO;

    public void setItemDAO(ResourceDAO<ItemDS> itemDAO) {
        this.itemDAO = itemDAO;
    }

    @Override
    public List<ItemDTO> getList() {
        List<ItemDTO> result = new ArrayList<>();
        for (ItemDS i : itemDAO.getList())
            result.add(i.toItemDTO());
        return result;
    }

    @Override
    public ItemDTO get(int id) {
        return itemDAO.get(id).toItemDTO();
    }

    @Override
    public void create(ItemDTO obj) {
        ItemDS item = obj.toItemDS();
        itemDAO.create(item);
        obj.setId(Integer.toString(item.getId()));
        obj.setCreatedDate(item.getCreatedDate());
        obj.setModifiedDate(item.getModifiedDate());
    }

    @Override
    public void update(ItemDTO obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(int id) {
        itemDAO.get(id);
        itemDAO.remove(id);
    }

}
