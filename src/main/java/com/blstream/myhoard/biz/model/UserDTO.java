package com.blstream.myhoard.biz.model;

import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.db.model.UserDS;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author z0lfik
 */
public class UserDTO {

    @JsonIgnore
    private String id;

    @Email
    private String email;

    private String username;

    private String password;

    private String grant_type;

    private String refresh_token;

    public UserDTO() {
        id = "0";
    }

    public UserDTO(String id, String email, String username, String password) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public String getGrant_type() {
        return grant_type;
    }

    @JsonProperty("grant_type")
    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    @JsonIgnore
    public String getRefresh_token() {
        return refresh_token;
    }

    @JsonProperty("refresh_token")
    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public void updateObject(UserDTO object) {
        if (this == object || object == null) {
            return;
        }
        if (username == null || object.username != null && !username.equals(object.username)) {
            username = object.username;
        }
        if (email == null || object.email != null && !email.equals(object.email)) {
            email = object.email;
        }
        if (password == null || object.password != null && !password.equals(object.password)) {
            password = object.password;
        }
    }

    public UserDS toUserDS() {
        return new UserDS(Integer.parseInt(id), email, username, password);
    }
}
