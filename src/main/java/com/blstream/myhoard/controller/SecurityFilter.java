/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.SessionDTO;
import com.blstream.myhoard.biz.service.ResourceService;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author gohilukk
 */
public class SecurityFilter implements Filter {

    @Autowired
    private HttpServletRequest request;

    private ResourceService<SessionDTO> sessionService;
    private boolean autorization = false;

    public void setSessionService(ResourceService<SessionDTO> sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void init(FilterConfig fc) throws ServletException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {

        request = (HttpServletRequest) sr;
        String accessToken = request.getHeader("Authorization");

        if (request.getMethod().equals("POST") && request.getRequestURI().equals(request.getContextPath() + "/users")) {
            autorization = true;
        }

        SessionDTO sessionDTO = sessionService.getByAccess_token(accessToken);
        if (sessionDTO != null) {
            sessionDTO.getId();
            //TODO wez uzytkownika po id, 
            //sr.setAttribute("user", user);
            //sr.getAttribute("user")
        }
        
        //if (autorization) {
        fc.doFilter(sr, sr1);

    }

    @Override
    public void destroy() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
