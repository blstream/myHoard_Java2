package com.blstream.myhoard.biz.model;

import com.blstream.myhoard.db.model.UserDS;
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

    private String grantType;

    private String refreshToken;

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
    public String getGrantType() {
        return grantType;
    }

    @JsonProperty("grant_type")
    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    @JsonIgnore
    public String getRefreshToken() {
        return refreshToken;
    }

    @JsonProperty("refresh_token")
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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
