package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.model.SessionDTO;
import java.util.Date;

public class SessionDS {

    private int id;
    private String accessToken;
    private Date dateCreated;
    private String refreshToken;

    public SessionDS() {
        dateCreated = java.util.Calendar.getInstance().getTime();
    }

    public SessionDS(int id, String access_token, Date date_created, String refresh_token) {
        this.id = id;
        this.accessToken = access_token;
        this.dateCreated = date_created;
        this.refreshToken = refresh_token;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date date_created) {
        this.dateCreated = date_created;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refresh_token) {
        this.refreshToken = refresh_token;
    }

    
    public SessionDTO toSessionDTO() {
        return new SessionDTO(Integer.toString(id),accessToken, dateCreated, refreshToken);
    }


    @Override
    public String toString() {
        return ("id: " + this.id +
                "access_token: " + this.accessToken +
                "\nDate created: " + this.dateCreated.toString() +
                "\nrefresh_token: " + this.refreshToken);
    }
    
}
