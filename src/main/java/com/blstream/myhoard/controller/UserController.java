package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardError;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.UserDTO;
import com.blstream.myhoard.biz.service.ResourceService;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserDTO> getUsers() {
        return userService.getList(Collections.EMPTY_MAP);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserDTO addUser(@RequestBody @Valid UserDTO user, BindingResult result) {
        if (user.getUsername() != null && result.hasFieldErrors("username") || result.hasFieldErrors("email") || result.hasFieldErrors("password")) {
            MyHoardException exception = new MyHoardException(ErrorCode.BAD_REQUEST);
            if (user.getUsername() != null && result.hasFieldErrors("username"))
                exception.add("username", result.getFieldError("username").getDefaultMessage());
            if (result.hasFieldErrors("email"))
                exception.add("email", "Niepoprawny adres e-mail: " + user.getEmail());
            if (result.hasFieldErrors("password"))
                exception.add("password", result.getFieldError("password").getDefaultMessage());
            throw exception;
        }
        userService.create(user);
        return user;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO getUser(@PathVariable String id, HttpServletRequest request) {
        try {
            UserDTO user = (UserDTO)request.getAttribute("user");
            if (!user.getId().equals(id))
                throw new MyHoardException(ErrorCode.FORBIDDEN);
            return userService.get(Integer.parseInt(id));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("id", "Niepoprawne id");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO updateUser(@PathVariable String id, @RequestBody @Valid UserDTO user, BindingResult result, HttpServletRequest request) {
        UserDTO currentUser = (UserDTO)request.getAttribute("user");
        if (!currentUser.getId().equals(id))
            throw new MyHoardException(ErrorCode.FORBIDDEN);
        if (user.getUsername() != null && result.hasFieldErrors("username")
                || user.getEmail() != null && result.hasFieldErrors("email")
                || user.getPassword()!= null && result.hasFieldErrors("password")) {
            MyHoardException exception = new MyHoardException(ErrorCode.BAD_REQUEST);
            if (user.getUsername() != null && result.hasFieldErrors("username"))
                exception.add("username", result.getFieldError("username").getDefaultMessage());
            if (result.hasFieldErrors("email"))
                exception.add("email", "Niepoprawny adres e-mail: " + user.getEmail());
            if (result.hasFieldErrors("password"))
                exception.add("password", result.getFieldError("password").getDefaultMessage());
            throw exception;
        }
        user.setId(id);
        currentUser.updateObject(user);
        userService.update(user);
        return user;
    }

    @RequestMapping(value = "/current", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO updateCurrentUser(@RequestBody @Valid UserDTO user, BindingResult result, HttpServletRequest request) {
        UserDTO currentUser = (UserDTO)request.getAttribute("user");
        if (user.getUsername() != null && result.hasFieldErrors("username")
                || user.getEmail() != null && result.hasFieldErrors("email")
                || user.getPassword()!= null && result.hasFieldErrors("password")) {
            MyHoardException exception = new MyHoardException(ErrorCode.BAD_REQUEST);
            if (user.getUsername() != null && result.hasFieldErrors("username"))
                exception.add("username", result.getFieldError("username").getDefaultMessage());
            if (result.hasFieldErrors("email"))
                exception.add("email", "Niepoprawny adres e-mail: " + user.getEmail());
            if (result.hasFieldErrors("password"))
                exception.add("password", result.getFieldError("password").getDefaultMessage());
            throw exception;
        }
        user.setId(currentUser.getId());
        currentUser.updateObject(user);
        userService.update(user);
        return user;
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable String id, HttpServletRequest request) {
        try {
            UserDTO user = (UserDTO)request.getAttribute("user");
            if (!user.getId().equals(id))
                throw new MyHoardException(ErrorCode.FORBIDDEN);
            userService.remove(Integer.parseInt(id));
        } catch (NumberFormatException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("id", "Niepoprawne id");
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
