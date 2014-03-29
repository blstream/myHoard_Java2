package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.MyHoardError;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.UserDTO;
import com.blstream.myhoard.biz.service.ResourceService;
import java.util.List;
import javax.servlet.http.HttpServlet;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/users")
public class UserController {

    private ResourceService<UserDTO> userService;

    public void setUserService(ResourceService<UserDTO> userService) {
        this.userService = userService;
    }

//    @RequestMapping(method = RequestMethod.GET)
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public List<UserDTO> getUsers() {
//        return userService.getList();
//    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserDTO addUser(@RequestBody @Valid UserDTO user, BindingResult result) {
        if (result.hasErrors())
            throw new MyHoardException(400, result.getFieldError().getDefaultMessage());
        try {
            userService.create(user);
            return user;
        } catch (Exception ex) {
            throw new MyHoardException(400, "Nieznany błąd: " + ex.toString() + " > " + ex.getCause().toString());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO getUser(@PathVariable String id, HttpServletRequest request) {
        try {
            UserDTO user = (UserDTO)request.getAttribute("user");
            if (!user.getId().equals(id))
                throw new MyHoardException(104, "Forbidden", HttpServletResponse.SC_FORBIDDEN);
            return userService.get(Integer.parseInt(id));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(300, "Niepoprawne id: " + id);
        } catch (RuntimeException ex) {
            throw new MyHoardException(300, "Nieznany błąd: " + ex.toString() + " > " + ex.getCause().toString());
        }
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO updateUser(@PathVariable String id, @RequestBody UserDTO user, HttpServletRequest request) {
        try {
            UserDTO currentUser = (UserDTO)request.getAttribute("user");
            if (!currentUser.getId().equals(id))
                throw new MyHoardException(104, "Forbidden", HttpServletResponse.SC_FORBIDDEN);
            user.setId(id);
            userService.update(user);
            return user;
        } catch (Exception ex) {
            throw new MyHoardException(111, "Nieznany błąd: " + ex.toString() + " > " + ex.getCause().toString());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable String id, HttpServletRequest request) {
        try {
            UserDTO user = (UserDTO)request.getAttribute("user");
            if (!user.getId().equals(id))
                throw new MyHoardException(104, "Forbidden", HttpServletResponse.SC_FORBIDDEN);
            userService.remove(Integer.parseInt(id));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(400, "Niepoprawne id: " + id);
        } catch (RuntimeException ex) {
            throw new MyHoardException(400, "Nieznany błąd: " + ex.toString() + " > " + ex.getCause().toString());
        }
    }

    @ExceptionHandler(MyHoardException.class)
    @ResponseBody
    public MyHoardError returnCode(MyHoardException exception, HttpServletResponse response) {
        response.setStatus(exception.getResponseStatus());
        return exception.toError();
    }
}
