package com.blstream.myhoard.biz.model;

import com.blstream.myhoard.db.model.UserDS;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author z0lfik
 */
public class UserDTO {

    @JsonIgnore
    private String id;
    
    private String mail;

    private String username;
    
    private String password;

    public UserDTO() {
        id = "0";
    }

    public UserDTO(String id, String mail, String username, String password) {
        this.id = id;
        this.mail = mail;
        this.username = username;
        this.password = password;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

     public void updateObject(UserDTO object) {
        if (this == object || object == null) {
            return;
        }
        if (username == null || object.username != null && !username.equals(object.username)) {
            username = object.username;
        }
        if (mail == null || object.mail != null && !mail.equals(object.mail)) {
            mail = object.mail;
        }
        if (password == null || object.password != null && !password.equals(object.password)) {
            password = object.password;
        }
    }
    
    public UserDS toUserDS() {
        return new UserDS(Integer.parseInt(id),mail, username, password);
    }
}
