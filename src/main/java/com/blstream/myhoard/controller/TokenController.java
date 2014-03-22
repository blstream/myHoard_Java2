package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.SessionDTO;
import com.blstream.myhoard.biz.model.UserDTO;
import com.blstream.myhoard.biz.service.ResourceService;
import com.blstream.myhoard.biz.service.UserService;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
@RequestMapping(value = "/oauth/token")
public class TokenController {

    private ResourceService<SessionDTO> sessionService;
    private ResourceService<UserDTO> userService;

    public void setSessionService(ResourceService<SessionDTO> sessionService) {
        this.sessionService = sessionService;
    }

    public void setUserService(ResourceService<UserDTO> userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public SessionDTO login(@RequestBody @Valid UserDTO user, BindingResult result) {
        /*if (result.hasErrors())
         throw new MyHoardException(400, result.toString());
         */
        try {
            UserDTO saved = ((UserService) userService).getByUsername(user.getUsername());
            //TODO SHA1 of user Password
            user.setPassword(encode(user.getPassword()));
            if (user.getGrant_type() != null) {
                if (saved.getEmail().equals(user.getEmail()) && saved.getPassword().equals(user.getPassword()) && user.getGrant_type().equals("password")) {
                    //TODO Generowanie tokenu, temporary broken access_token
                    SessionDTO created = new SessionDTO("0", encode(java.util.Calendar.getInstance().getTime().toString()), java.util.Calendar.getInstance().getTime(), "refresh_token", saved.getId());
                    sessionService.create(created);
                    return created;
                } else {
                    throw new MyHoardException(101, "BadCredentials");
                }
            }
            throw new MyHoardException(400, "Empty field: grant_type");
        } catch (Exception ex) {
            throw new MyHoardException(400, "Nieznany błąd: " + ex.toString() + " > " + ex.getCause().toString());
        }
    }

    public String encode(String tmp) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(tmp.getBytes());
            String pass = new BigInteger(1, md.digest()).toString(16);
            return pass;
        } catch (NoSuchAlgorithmException e) {
            throw new MyHoardException(400);
        }
    }

    @ExceptionHandler(MyHoardException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorCode returnCode(MyHoardException exception) {
        return new ErrorCode(exception.getErrorCode(), exception.getErrorMsg());
    }
}
