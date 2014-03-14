package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.CollectionDTO;
import com.blstream.myhoard.biz.service.ResourceService;
import com.blstream.myhoard.validation.*;
import java.util.List;
import javax.validation.Valid;
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

    private ResourceService<CollectionDTO> collectionService;

    public void setCollectionService(ResourceService<CollectionDTO> collectionService) {
        this.collectionService = collectionService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<CollectionDTO> getCollections() {
        return collectionService.getList();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CollectionDTO addCollection(@RequestBody @Valid CollectionDTO collection, BindingResult result) {
        if (result.hasErrors())
            throw new MyHoardException(400, result.toString());
        try {
            collectionService.create(collection);
            return collection;
        } catch (Exception ex) {
            throw new MyHoardException(400, "Nieznany błąd: " + ex.toString() + " > " + ex.getCause().toString());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CollectionDTO getCollection(@PathVariable String id) {
        try {
            return collectionService.get(Integer.parseInt(id));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(300, "Niepoprawne id: " + id);
        } catch (RuntimeException ex) {
            throw new MyHoardException(300, "Nieznany błąd: " + ex.toString() + " > " + ex.getCause().toString());
        }
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CollectionDTO updateCollection(@PathVariable String id, @RequestBody CollectionDTO collection) {
        try {
            collection.setId(id);
            collectionService.update(collection);
            return collection;
        } catch (Exception ex) {
            throw new MyHoardException(111, "Nieznany błąd: " + ex.toString() + " > " + ex.getCause().toString());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCollection(@PathVariable String id) {
        try {
            collectionService.remove(Integer.parseInt(id));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(400, "Niepoprawne id: " + id);
        } catch (RuntimeException ex) {
            throw new MyHoardException(400, "Nieznany błąd: " + ex.toString() + " > " + ex.getCause().toString());
        }
    }

    @ExceptionHandler(MyHoardException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorCode returnCode(MyHoardException exception) {
        return new ErrorCode(exception.getErrorCode(), exception.getErrorMsg());
    }
}
