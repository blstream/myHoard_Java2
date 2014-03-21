package com.blstream.myhoard.biz.model;

import com.blstream.myhoard.db.model.SessionDS;
import com.blstream.myhoard.db.model.UserDS;
import java.util.Date;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author z0lfik
 */
public class SessionDTO {

    @JsonIgnore
    private String id;
    
    private String access_token;

    private Date date_created;
    
    private String refresh_token;

    public SessionDTO() {
        id = "0";
    }

    public SessionDTO(String id, String access_token, Date date_created, String refresh_token) {
        this.id = id;
        this.access_token = access_token;
        this.date_created = date_created;
        this.refresh_token = refresh_token;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

    public Date getDateCreated() {
        return date_created;
    }

    public void setDateCreated(Date date_created) {
        this.date_created = date_created;
    }
    
    public String getRefreshToken() {
        return refresh_token;
    }
    
    public void setRefreshToken(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public SessionDS toSessionDS() {
        return new SessionDS(Integer.parseInt(id),access_token, date_created, refresh_token);
    }
}
