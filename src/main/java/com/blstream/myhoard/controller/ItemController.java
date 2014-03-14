package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.ItemDTO;
import com.blstream.myhoard.biz.service.ResourceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/items")
public class ItemController {

    private ResourceService<ItemDTO> itemService;

    public void setItemService(ResourceService<ItemDTO> itemService) {
        this.itemService = itemService;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<ItemDTO> getItems() {
        return itemService.getList();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    ItemDTO createItem(@RequestBody ItemDTO obj) {
        itemService.create(obj);
        return obj;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ItemDTO getItem(@PathVariable String id) {
        return itemService.get(Integer.parseInt(id));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void removeItem(@PathVariable String id) {
        itemService.remove(Integer.parseInt(id));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorCode errorHandler(Exception e) {
        if (e instanceof MyHoardException)
            return new ErrorCode(((MyHoardException)e).getErrorCode(), ((MyHoardException)e).getErrorMsg());
        else
            return new ErrorCode(-1, e.getMessage());
    }
}
