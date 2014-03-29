package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.model.SessionDTO;
import java.util.Date;

public class SessionDS {

    private int id;
    private String accessToken;
    private Date createdDate;
    private String refreshToken;
    private int user;
    
    public SessionDS() {
        createdDate = java.util.Calendar.getInstance().getTime();
    }

    public SessionDS(int id, String access_token, Date date_created, String refresh_token, int user_id) {
        this.id = id;
        this.accessToken = access_token;
        this.createdDate = date_created;
        this.refreshToken = refresh_token;
        this.user = user_id;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String access_token) {
        this.accessToken = access_token;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date date_created) {
        this.createdDate = date_created;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refresh_token) {
        this.refreshToken = refresh_token;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
    
    
    public SessionDTO toSessionDTO() {
        return new SessionDTO(Integer.toString(id),accessToken, createdDate, refreshToken, Integer.toString(user));
    }


    @Override
    public String toString() {
        return ("id: " + this.id +
                "access_token: " + this.accessToken +
                "\nDate created: " + this.createdDate.toString() +
                "\nrefresh_token: " + this.refreshToken);
    }
    
}
