package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardError;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.ItemDTO;
import com.blstream.myhoard.biz.model.MediaDTO;
import com.blstream.myhoard.biz.model.UserDTO;
import com.blstream.myhoard.biz.service.ResourceService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
//@RequestMapping("/items")
public class ItemController {

    private ResourceService<ItemDTO> itemService;

    public void setItemService(ResourceService<ItemDTO> itemService) {
        this.itemService = itemService;
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ItemDTO> getItems(@RequestParam(value = "sort_by", defaultValue = "name") String[] fieldName,
            @RequestParam(value = "sort_direction", defaultValue = "asc") String sortDir, HttpServletRequest request) {
        UserDTO user = (UserDTO)request.getAttribute("user");
        Map<String, Object> params = new HashMap<>();
        params.put("sort_by", fieldName);
        params.put("sort_dir", sortDir);
        params.put("option", "list");
        params.put("owner", user.toUserDS());
        return itemService.getList(params);
    }

    @RequestMapping(value = "/collections/{id}/items", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ItemDTO> getItems(@PathVariable("id") String id, @RequestParam(value = "sort_by", defaultValue = "name") String[] fieldName,
            @RequestParam(value = "sort_direction", defaultValue = "asc") String sortDir, HttpServletRequest request) {
        UserDTO user = (UserDTO)request.getAttribute("user");
        Map<String, Object> params = new HashMap<>();
        params.put("option", "listfrom");
        params.put("collection", Integer.parseInt(id));
        params.put("sort_by", fieldName);
        params.put("sort_dir", sortDir);
        params.put("owner", user.toUserDS());
        return itemService.getList(params);
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET, params = {"name", "collection"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ItemDTO> findItems(@RequestParam(value = "name") String name,
            @RequestParam(value = "collection") String collection,
            HttpServletRequest request) {
        if (name.length() < 2 || name.length() > 20)
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("name", "Zbyt krótka/długa nazwa do wyszukiwania");
        try {
            UserDTO user = (UserDTO)request.getAttribute("user");
            Map<String, Object> params = new HashMap<>();
            params.put("option", "find");
            params.put("name", name);
            params.put("collection", Integer.parseInt(collection));
            params.put("owner", user.toUserDS());
            return itemService.getList(params);
        } catch (NumberFormatException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("collection", "Niepoprawny identyfikator");
        }
    }

    @RequestMapping(value = "/items", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ItemDTO createItem(@Valid @RequestBody ItemDTO obj, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors())
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add(result.getFieldError().getField(), result.getFieldError().getDefaultMessage());
        UserDTO user = (UserDTO)request.getAttribute("user");
        obj.setOwner(user);
        for (MediaDTO i : obj.getMedia())
            i.setOwner(user.getId());
        itemService.create(obj);
        return obj;
    }

    @RequestMapping(value = "/items/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ItemDTO getItem(@PathVariable String id, HttpServletRequest request) {
        try {
            UserDTO user = (UserDTO)request.getAttribute("user");
            ItemDTO item = itemService.get(Integer.parseInt(id));
            if (!user.equals(item.getOwner()))
                throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Brak uprawnień do pobrania elementu");
            return item;
        } catch (NumberFormatException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("id", "Niepoprawny identyfikator.");
        }
    }

    @RequestMapping(value = "/items/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ItemDTO updateItem(@PathVariable String id, @Valid @RequestBody ItemDTO obj, BindingResult result, HttpServletRequest request) {
        if (obj.getName() != null && result.hasFieldErrors("name") || obj.getCollection() != null && result.hasFieldErrors("collection"))
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add(
                    obj.getName() != null ? "name" : "collection",
                    result.getFieldError(obj.getName() != null ? "name" : "collection").getDefaultMessage());
        try {
            UserDTO user = (UserDTO)request.getAttribute("user");
            ItemDTO item = itemService.get(Integer.parseInt(id));
            if (!user.equals(item.getOwner()))
                throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Brak uprawnień do modyfikacji elementu");
            obj.setOwner(user);
            obj.setId(id);
            for (MediaDTO i : obj.getMedia())
                i.setOwner(user.getId());
            itemService.update(obj);
            return obj;
        } catch (NumberFormatException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("id", "Niepoprawny identyfikator.");
        }
    }

    @RequestMapping(value = "/items/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable String id, HttpServletRequest request) {
        try {
            UserDTO user = (UserDTO)request.getAttribute("user");
            ItemDTO item = itemService.get(Integer.parseInt(id));
            if (!user.equals(item.getOwner()))
                throw new MyHoardException(ErrorCode.FORBIDDEN).add("id", "Brak uprawnień do usunięcia elementu");
            itemService.remove(Integer.parseInt(id));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("id", "Niepoprawny identyfikator.");
        }
    }

    @ExceptionHandler(MyHoardException.class)
    @ResponseBody
    public MyHoardError returnCode(MyHoardException exception, HttpServletResponse response) {
        response.setStatus(exception.getResponseStatus());
        response.setContentType("application/json;charset=UTF-8");
        return exception.toError();
    }
}
