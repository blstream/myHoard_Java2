package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.UserDTO;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserDS {

    private int id;
    private String email;
    private String username;
    private String password;

    public UserDS() {}

    public UserDS(int id, String mail, String username, String password) {
        this.id = id;
        this.email = mail;
        this.username = username;
        if(password!=null) {
            try {
            MessageDigest md = MessageDigest.getInstance( "SHA-256" );
            md.update( password.getBytes() );
            this.password = new BigInteger( 1, md.digest() ).toString(16);
            }
            catch (NoSuchAlgorithmException e) {
            throw new MyHoardException(400);
            }
        }
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
        /*try {
        MessageDigest md = MessageDigest.getInstance( "SHA256" );
        md.update( password.getBytes() );
        this.password = new BigInteger( 1, md.digest() ).toString(16);
        }
        catch (NoSuchAlgorithmException e) {
        throw new MyHoardException(400,"Błąd SHA");
        }*/
        this.password = password;
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
