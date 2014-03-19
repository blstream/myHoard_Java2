package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.CollectionDTO;
import com.blstream.myhoard.biz.service.ResourceService;
import com.blstream.myhoard.db.model.SortResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(value = "/sort", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SortResult sortCollections(@RequestParam(value = "sort_by", defaultValue = "name") String fieldName,
            @RequestParam(value = "sort_direction", defaultValue = "asc") String sortDir,
            @RequestParam(value = "max_count", defaultValue = "1") String maxCount,
            @RequestParam(value = "start_num", defaultValue = "0") String startNum) {
        Map<String, String> params = new HashMap<>();
        SortResult result = new SortResult();
        params.put("sort_by", fieldName);
        params.put("sort_dir", sortDir);
        params.put("max_count", maxCount);
        params.put("start_num", startNum);
        result.setCollections(collectionService.getList(params));
        result.setTotalCount(result.getCollections().size());
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CollectionDTO addCollection(@RequestBody @Valid CollectionDTO collection, BindingResult result) {
        if (result.hasErrors())
            throw new MyHoardException(400, result.getFieldError().getDefaultMessage());
        collectionService.create(collection);
        return collection;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CollectionDTO getCollection(@PathVariable String id) {
        try {
            return collectionService.get(Integer.parseInt(id));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(300, "Niepoprawne id: " + id);
        }
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CollectionDTO updateCollection(@PathVariable String id, @Valid @RequestBody CollectionDTO collection, BindingResult result) {
        if (collection.getName() != null && result.hasErrors())
            throw new MyHoardException(320, result.getFieldError().getDefaultMessage());
        collection.setId(id);
        collectionService.update(collection);
        return collection;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCollection(@PathVariable String id) {
        try {
            collectionService.remove(Integer.parseInt(id));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(400, "Niepoprawne id: " + id);
        }
    }

    @ExceptionHandler(MyHoardException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorCode returnCode(MyHoardException exception) {
        return new ErrorCode(exception.getErrorCode(), exception.getErrorMsg());
    }
}
