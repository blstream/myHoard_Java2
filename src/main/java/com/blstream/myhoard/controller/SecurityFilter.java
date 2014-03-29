/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.SessionDTO;
import com.blstream.myhoard.biz.model.UserDTO;
import com.blstream.myhoard.biz.service.ResourceService;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author gohilukk
 */
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
        
        if (request.getMethod().equals("POST") && request.getRequestURI().equals(request.getContextPath() + "/users") ||
            (request.getMethod().equals("POST") && request.getRequestURI().startsWith(request.getContextPath() + "/oauth/token")) ||
                request.getMethod().equals("POST") && request.getRequestURI().startsWith(request.getContextPath() + "/media")) {           
           
            authorization_needed = false;
        }
        if(accessToken == null && authorization_needed) {
            String response = "{\"error_message\": \"Token not provided\",\"error_code\": 102}";
            HttpServletResponse resp = (HttpServletResponse) sr1;
            OutputStream  out = resp.getOutputStream();
            out.write(response.getBytes());
            resp.setStatus(401);
            resp.setContentType("application/json");
            out.close();
        } else if(authorization_needed) {
            SessionDTO sessionDTO = null;
            try {
                Map<String, Object> params = new HashMap<>();
                params.put("accessToken", accessToken);
                sessionDTO = sessionService.getList(params).get(0);
            } catch (NullPointerException ex) {
                String response = "{\"error_message\": \"Invalid Token\",\"error_code\": 103}";
                    HttpServletResponse resp = (HttpServletResponse) sr1;
                    OutputStream  out = resp.getOutputStream();
                    out.write(response.getBytes());
                    resp.setStatus(404);
                    resp.setContentType("application/json");
                    out.close();
            }
            if (sessionDTO != null) {
                long actual = java.util.Calendar.getInstance().getTimeInMillis();
                if((actual - sessionDTO.getExpires_in().getTime()) > 1800000) { //30 min ze wzgledu na uciazliwosc testow
                    authorization_given = false;
                    String response = "{\"error_message\": \"Invalid token\",\"error_code\": 103}";
                    HttpServletResponse resp = (HttpServletResponse) sr1;
                    OutputStream  out = resp.getOutputStream();
                    out.write(response.getBytes());
                    resp.setStatus(401);
                    resp.setContentType("application/json");
                    out.close();
                } else {
                UserDTO user = (UserDTO)userService.get(Integer.parseInt(sessionDTO.getUser_id()));
                sr.setAttribute("user", user);
                }
            } else {
                authorization_given = false;
                String response = "{\"error_message\": \"Invalid token\",\"error_code\": 103}";
                HttpServletResponse resp = (HttpServletResponse) sr1;
                OutputStream  out = resp.getOutputStream();
                out.write(response.getBytes());
                resp.setStatus(401);
                resp.setContentType("application/json");
                out.close();
            }
        
        }
        if(authorization_given)
            fc.doFilter(sr, sr1);

    }

    @Override
    public void destroy() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
