package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.CollectionDTO;
import com.blstream.myhoard.biz.service.CollectionService;
import com.blstream.myhoard.biz.service.ResourceService;
import com.blstream.myhoard.validation.*;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/collections")
public class CollectionController {

//    @Autowired
    private ResourceService<CollectionDTO> collectionService;

    @Autowired
    CollectionDTOValidator collectionDTOValidator;

    public void setCollectionService(ResourceService<CollectionDTO> collectionService) {
        this.collectionService = collectionService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<CollectionDTO> getCollections() {
        return collectionService.getList();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    CollectionDTO addCollection(@RequestBody CollectionDTO collection, BindingResult result) {
        collectionDTOValidator.validate(collection, result);
        if (result.hasErrors()) {
            throw new MyHoardException(400);
        }
        try {
            collectionService.create(collection);
            return collection;
        } catch (Exception ex) {
            throw new MyHoardException(400);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    CollectionDTO getCollection(@PathVariable String id) {
        try {
            return collectionService.get(Integer.parseInt(id));
        } catch (Exception ex) {
            throw new MyHoardException(300);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    CollectionDTO updateCollection(@PathVariable String id, @RequestBody CollectionDTO collection, BindingResult result) {
        collectionDTOValidator.validate(collection, result);
        if (result.hasErrors()) {
            throw new MyHoardException(400);
        }
        try {
            collection.setId(id);
            collectionService.update(collection);
            return collection;
        } catch (Exception ex) {
            throw new MyHoardException(111);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCollection(@PathVariable String id) {
        try {
            collectionService.remove(Integer.parseInt(id));
        } catch (Exception ex) {
            throw new MyHoardException(400);
        }
    }

    @ExceptionHandler(MyHoardException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ErrorCode returnCode(MyHoardException exception) {
        return new ErrorCode(exception.getErrorCode());
    }
}
