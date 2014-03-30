package com.blstream.myhoard.biz.model;

import com.blstream.myhoard.db.model.SessionDS;
import java.util.Date;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author z0lfik
 */
public class SessionDTO {

    @JsonIgnore
    private String id;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Date expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonIgnore
    private String userId;
    
    public SessionDTO() {
        id = "0";
    }

    public SessionDTO(String id, String access_token, Date expires_in, String refresh_token, String user_id) {
        this.id = id;
        this.accessToken = access_token;
        this.expiresIn = expires_in;
        this.refreshToken = refresh_token;
        this.userId = user_id;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonSerialize(using = CustomExpiresInSerializer.class)
    public Date getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Date expiresIn) {
        this.expiresIn = expiresIn;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refresh_token) {
        this.refreshToken = refresh_token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public SessionDS toSessionDS() {
        return new SessionDS(Integer.parseInt(id),accessToken, expiresIn, refreshToken, Integer.parseInt(userId));
    }
}
