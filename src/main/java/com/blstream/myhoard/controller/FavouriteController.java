package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardError;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.CollectionDTO;
import com.blstream.myhoard.biz.model.UserDTO;
import com.blstream.myhoard.biz.service.ResourceService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class FavouriteController {

    private ResourceService<UserDTO> userService;

    public void setUserService(ResourceService<UserDTO> userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/users/{id}/favourites", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<CollectionDTO> getFavourites(@PathVariable String id, HttpServletRequest request) {
        UserDTO user = (UserDTO)request.getAttribute("user");
        Map<String, Object> params = new HashMap<>();
        try {
            params.put("id", Integer.parseInt(id));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("id", "Niepoprawne id");
        }
        params.put("fetch_favourites", true);
        params.put("own_favourites", id.equals(user.getId()));
        return userService.getList(params).get(0).getFavourites();
    }

    @RequestMapping(value = "/favourites/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void addFavourite(@PathVariable String id, HttpServletRequest request) {
        UserDTO user = (UserDTO)request.getAttribute("user");
        Map<String, Object> params = new HashMap<>();
        params.put("id", Integer.parseInt(user.getId()));
        try {
            params.put("add_collection", Integer.parseInt(id));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("id", "Niepoprawne id");
        }
        userService.getList(params);
    }

    @RequestMapping(value = "/favourites/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFavourite(@PathVariable String id, HttpServletRequest request) {
        UserDTO user = (UserDTO)request.getAttribute("user");
        Map<String, Object> params = new HashMap<>();
        params.put("id", Integer.parseInt(user.getId()));
        try {
            params.put("remove_collection", Integer.parseInt(id));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("id", "Niepoprawne id");
        }
        userService.getList(params);
    }

    @ExceptionHandler(MyHoardException.class)
    @ResponseBody
    public MyHoardError returnCode(MyHoardException exception, HttpServletResponse response) {
        response.setStatus(exception.getResponseStatus());
        response.setContentType("application/json;charset=UTF-8");
        return exception.toError();
    }
}
