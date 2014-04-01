package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardError;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.SessionDTO;
import com.blstream.myhoard.biz.model.UserDTO;
import com.blstream.myhoard.biz.service.ResourceService;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
        Map<String, Object> params = new HashMap<>();
        if (user.getEmail() != null)
            params.put("email", user.getEmail());
        else if (user.getUsername() != null)
            params.put("username", user.getUsername());
        List<UserDTO> list = userService.getList(params);
        if (list.isEmpty() || list.size() > 1)
            throw new MyHoardException(ErrorCode.NOT_FOUND);
        UserDTO saved = list.get(0);
        user.setPassword(encode(user.getPassword()));
        if (user.getGrantType() != null) {
            if ((saved.getEmail().equals(user.getEmail()) || saved.getUsername().equals(user.getUsername())) && saved.getPassword().equals(user.getPassword())) {
                SessionDTO created;
                switch (user.getGrantType()) {
                    case "password":
                        //TODO Generowanie tokenu, temporary broken access_token
                        created = new SessionDTO("0", encode((java.util.Calendar.getInstance().getTime().toString()+user.getUsername())), java.util.Calendar.getInstance().getTime(), encode("refresh_token" + java.util.Calendar.getInstance().getTime()+user.getUsername()), saved.getId());
                        sessionService.create(created);
                        return created;
                    case "refresh_token":
                        if(user.getRefreshToken() != null) {
                            params.clear();
                            params.put("refreshToken", user.getRefreshToken());
                            if (sessionService.getList(params).isEmpty())
                                throw new MyHoardException(ErrorCode.NOT_FOUND).add("refresh_token", "Not found in database");
                            created = new SessionDTO("0", encode((java.util.Calendar.getInstance().getTime().toString()+user.getUsername())), java.util.Calendar.getInstance().getTime(), encode("refresh_token" + java.util.Calendar.getInstance().getTime()+user.getUsername()), saved.getId());
                            sessionService.create(created);
                            return created;
                        } else
                            throw new MyHoardException(ErrorCode.AUTH_TOKEN_NOT_PROVIDED).add("refresh_token", "Missing field");
                    default:
                        throw new MyHoardException(ErrorCode.BAD_REQUEST).add("grant_type", "Wrong value: " + user.getGrantType());
                }
            } else
                throw new MyHoardException(ErrorCode.AUTH_BAD_CREDENTIALS);
        } else
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("grant_type", "Missing field");
    }

    public static String encode(String tmp) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(tmp.getBytes());
            String pass = new BigInteger(1, md.digest()).toString(16);
            return pass;
        } catch (NoSuchAlgorithmException e) {
            throw new MyHoardException(ErrorCode.INTERNAL_SERVER_ERROR).add("reason", "No such algorithm exception");
        }
    }

    @ExceptionHandler(MyHoardException.class)
    @ResponseBody
    public MyHoardError returnCode(MyHoardException exception, HttpServletResponse response) {
        response.setStatus(exception.getResponseStatus());
        return exception.toError();
    }
}
