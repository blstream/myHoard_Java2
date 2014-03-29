package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.MyHoardError;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.SessionDTO;
import com.blstream.myhoard.biz.model.UserDTO;
import com.blstream.myhoard.biz.service.ResourceService;
import com.blstream.myhoard.biz.service.UserService;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
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
        try {
            Map<String, Object> params = new HashMap<>();
            if (user.getEmail() != null)
                params.put("email", user.getEmail());
            else if (user.getUsername() != null)
                params.put("username", user.getUsername());
            UserDTO saved = userService.getList(params).get(0);
//            UserDTO saved = ((UserService) userService).getByEmail(user.getEmail());
            user.setPassword(encode(user.getPassword()));
            if (user.getGrantType() != null) {
                if ((saved.getEmail().equals(user.getEmail()) || saved.getUsername().equals(user.getUsername())) && saved.getPassword().equals(user.getPassword())) {
                    if("password".equals(user.getGrantType())) {
                        //TODO Generowanie tokenu, temporary broken access_token
                        SessionDTO created = new SessionDTO("0", encode((java.util.Calendar.getInstance().getTime().toString()+user.getUsername())), java.util.Calendar.getInstance().getTime(), encode("refresh_token" + java.util.Calendar.getInstance().getTime()+user.getUsername()), saved.getId());
                        sessionService.create(created);
                        return created;
                    } else {
                        if(user.getRefreshToken() != null) {
                            params.clear();
                            params.put("refreshToken", user.getRefreshToken());
                            if (sessionService.getList(params).isEmpty())
                                throw new MyHoardException(200, "Błąd odświeżenia tokenu");
                            SessionDTO created = new SessionDTO("0", encode((java.util.Calendar.getInstance().getTime().toString()+user.getUsername())), java.util.Calendar.getInstance().getTime(), encode("refresh_token" + java.util.Calendar.getInstance().getTime()+user.getUsername()), saved.getId());
                            sessionService.create(created);
                            return created;
                        } else
                            throw new MyHoardException(400,"no refresh token received");
                    }
                } else
                    throw new MyHoardException(101, "BadCredentials");
            } else
                throw new MyHoardException(400, "wrong grant_type");
        } catch (NullPointerException ex) {
            throw new MyHoardException(202,"Resource not found",404).add("error","Nie znaleziono refresh_tokenu, lub uzytkownika w bazie danych");
        } catch (MyHoardException ex) {
            throw ex;
        }catch (Exception ex) {
            throw new MyHoardException(400, "Nieznany błąd: " + ex.toString() + (ex.getCause() != null ? " > " + ex.getCause().toString() : ""));
        }
    }

    public String encode(String tmp) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(tmp.getBytes());
            String pass = new BigInteger(1, md.digest()).toString(16);
            return pass;
        } catch (NoSuchAlgorithmException e) {
            throw new MyHoardException(400);
        }
    }

    @ExceptionHandler(MyHoardException.class)
    @ResponseBody
    public MyHoardError returnCode(MyHoardException exception, HttpServletResponse response) {
        response.setStatus(exception.getResponseStatus());
        return exception.toError();
    }
}
