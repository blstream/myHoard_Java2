package com.blstream.myhoard.biz.model;

import com.blstream.myhoard.db.model.UserDS;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author z0lfik
 */
public class UserDTO {

    private String id;

    @NotNull(message = "Adres e-mail jest wymagany")
    @Pattern(message = "Niepoprawny e-mail",
            regexp = "^[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)*@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+(\\.[a-zA-Z0-9]+)*$")
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9_]+", message = "Nazwa użytkownika może zawierać jedynie litery, cyfry oraz znak _")
    private String username;

    @NotNull(message = "Hasło jest wymagane")
    @Size(min = 4, message = "Hasło musi zawierać co najmniej 4 znaki")
    private String password;

    private String grantType;

    private String refreshToken;
    
    private boolean visible;

    @JsonIgnore
    private List<CollectionDTO> favourites;

    public UserDTO() {
        id = "0";
    }

    public UserDTO(String id, String email, String username, String password, boolean visible) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.visible = visible;
    }
    
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public String getId() {
        return id;
    }
    @JsonIgnore
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

    public List<CollectionDTO> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<CollectionDTO> favourites) {
        this.favourites = favourites;
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
        return new UserDS(Integer.parseInt(id), email, username, password, visible);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof UserDTO))
            return false;
        UserDTO user = (UserDTO)obj;
        return id.equals(user.id);
    }

    /**
     * @return the visible
     */
    @JsonIgnore
    public boolean getVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    @JsonProperty(value = "public")
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
