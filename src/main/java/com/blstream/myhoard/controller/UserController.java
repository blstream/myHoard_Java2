package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.UserDTO;
import com.blstream.myhoard.biz.service.ResourceService;
import java.util.List;
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

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserDTO> getUsers() {
        return userService.getList();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserDTO addUser(@RequestBody @Valid UserDTO user, BindingResult result) {
        /*if (result.hasErrors())
            throw new MyHoardException(400, result.toString());
        */try {
            userService.create(user);
            return user;
        } catch (Exception ex) {
            throw new MyHoardException(400, "Nieznany błąd: " + ex.toString() + " > " + ex.getCause().toString());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO getUser(@PathVariable String id) {
        try {
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
    public UserDTO updateUser(@PathVariable String id, @RequestBody UserDTO user) {
        try {
            user.setId(id);
            userService.update(user);
            return user;
        } catch (Exception ex) {
            throw new MyHoardException(111, "Nieznany błąd: " + ex.toString() + " > " + ex.getCause().toString());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCollection(@PathVariable String id) {
        try {
            userService.remove(Integer.parseInt(id));
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
