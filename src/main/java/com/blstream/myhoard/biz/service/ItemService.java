package com.blstream.myhoard.biz.service;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.ItemDTO;
import com.blstream.myhoard.db.dao.ResourceDAO;
import com.blstream.myhoard.db.model.ItemDS;
import com.blstream.myhoard.db.model.UserDS;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class ItemService implements ResourceService<ItemDTO> {

    private ResourceDAO<ItemDS> itemDAO;
    private ResourceDAO<UserDS> userDAO;
    private MailSender mailSender;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setItemDAO(ResourceDAO<ItemDS> itemDAO) {
        this.itemDAO = itemDAO;
    }

    public void setUserDAO(ResourceDAO<UserDS> userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public int getTotalCount(String owner) {
        return itemDAO.getTotalCount(owner);
    }

    @Override
    public List<ItemDTO> getList(Map<String, Object> params) {
        if (params.containsKey("sort_dir") && !"asc".equals(params.get("sort_dir")) && !"desc".equals(params.get("sort_dir")))
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("sort_dir", "Nieprawidłowy porządek: " + params.get("sort_dir"));
                
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

        Map<String, Object> params = new HashMap<>();
        params.put("get_observers", true);
        params.put("collection", item.getCollection());
        params.put("owner", item.getOwner().getId());
        final List<UserDS> observers = userDAO.getList(params);
        final String collectionId = obj.getCollection(), itemId = Integer.toString(item.getId());
        new Thread(new Runnable() {

            @Override
            public void run() {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setSubject("Powiadomienie o nowym elemencie w obserwowanej kolekcji");
                message.setText("W obserwowanej przez Ciebie kolekcji (" + collectionId + ") pojawił się nowy element (" + itemId + ").");
                for (UserDS user : observers) {
                    message.setTo(user.getEmail());
                    mailSender.send(message);
                }
            }
        }).start();

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
