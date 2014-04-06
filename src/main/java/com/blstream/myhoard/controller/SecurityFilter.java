package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardError;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.SessionDTO;
import com.blstream.myhoard.biz.model.UserDTO;
import com.blstream.myhoard.biz.service.ResourceService;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;

public class SecurityFilter implements Filter {

    @Autowired
    private HttpServletRequest request;

    private ResourceService<SessionDTO> sessionService;
    private ResourceService<UserDTO> userService;
    private boolean authorization_needed;
    private boolean authorization_given;

    public void setSessionService(ResourceService<SessionDTO> sessionService) {
        this.sessionService = sessionService;
    }
    
    public void setUserService(ResourceService<UserDTO> userService) {
        this.userService = userService;
    }

    @Override
    public void init(FilterConfig fc) throws ServletException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        authorization_needed = true;
        authorization_given = true;
        request = (HttpServletRequest) sr;
        String accessToken = request.getHeader("Authorization");

        try {
            String path = request.getRequestURI();
            if (!path.isEmpty() && path.charAt(path.length() - 1) == '/')
                path = path.substring(0, path.length() - 1);
            if (request.getMethod().equals("POST") && ("/users".equals(path) ||
                    "/oauth/token".equals(path))) {
                authorization_needed = false;
            }
            if (accessToken == null && authorization_needed) {
                throw new MyHoardException(ErrorCode.AUTH_TOKEN_NOT_PROVIDED);
            } else if(authorization_needed) {
                Map<String, Object> params = new HashMap<>();
                params.put("accessToken", accessToken);
                List<SessionDTO> list = sessionService.getList(params);
                if (list.isEmpty() || list.size() > 1) {
                    authorization_given = false;
                    throw new MyHoardException(ErrorCode.AUTH_TOKEN_INVALID);
                } else {
                    SessionDTO sessionDTO = list.get(0);
                    long actual = java.util.Calendar.getInstance().getTimeInMillis();
                    if((actual - sessionDTO.getExpiresIn().getTime()) > 1800000) { //30 min ze wzgledu na uciazliwosc testow
                        authorization_given = false;
                        throw new MyHoardException(ErrorCode.AUTH_TOKEN_INVALID);
                    } else {
                        UserDTO user = (UserDTO)userService.get(Integer.parseInt(sessionDTO.getUserId()));
                        sr.setAttribute("user", user);
                    }
                }
            }
            if (authorization_given)
                fc.doFilter(sr, sr1);
        } catch (MyHoardException ex) {
            ((HttpServletResponse)sr1).setStatus(ex.getResponseStatus());
            OutputStream out = sr1.getOutputStream();
            out.write(ex.toError().toString().getBytes());
        } catch (Throwable ex) {
            ((HttpServletResponse)sr1).setStatus(400);
            OutputStream out = sr1.getOutputStream();
            Throwable cause = ex;
            while (cause.getCause() != null)
                cause = cause.getCause();
            if (cause instanceof UnrecognizedPropertyException) {
                UnrecognizedPropertyException exception = (UnrecognizedPropertyException)cause;
                out.write(new MyHoardException(ErrorCode.BAD_REQUEST).add(exception.getUnrecognizedPropertyName(), "Unrecognized property").toError().toString().getBytes());
            } else if (cause instanceof HttpMediaTypeNotSupportedException) {
                HttpMediaTypeNotSupportedException exception = (HttpMediaTypeNotSupportedException)cause;
                out.write(new MyHoardException(ErrorCode.BAD_REQUEST).add(exception.getContentType().toString(), "Content type not supported").toError().toString().getBytes());
            } else {
                String message = cause.toString();
                message = message.substring(message.indexOf(':') + 1);
                out.write(("{\"error_message\":\"" + message.replace("\"", "&quot;") + "\"}").getBytes());
            }
        }
    }

    @Override
    public void destroy() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
