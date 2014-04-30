package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.model.UserDTO;
import com.blstream.myhoard.controller.TokenController;
import java.util.Set;

public class UserDS {

    private int id;
    private String email;
    private String username;
    private String password;
    private Set<CollectionDS> favourites;

    public UserDS() {}

    public UserDS(int id, String mail, String username, String password) {
        this.id = id;
        this.email = mail;
        this.username = username;
        if (password != null)
            this.password = TokenController.encode(password);
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
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
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<CollectionDS> getFavourites() {
        return favourites;
    }

    public void setFavourites(Set<CollectionDS> favourites) {
        this.favourites = favourites;
    }

    public void updateObject(UserDS object) {
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
        if (favourites == null || object.favourites != null && !favourites.equals(object.favourites))
            favourites = object.favourites;
    }
    
    public UserDTO toUserDTO() {
        return new UserDTO(Integer.toString(id), email, username, password);
    }

    @Override
    public String toString() {
        return ("id: " + this.id +
                "\nusername: " + this.username +
                "\nmail: " + this.email +
                "\npassword: " + this.password);
    }
    
}
