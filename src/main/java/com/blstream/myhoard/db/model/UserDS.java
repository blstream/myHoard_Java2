package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.UserDTO;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserDS {

    private int id;
    private String mail;
    private String username;
    private String password;

    public UserDS() {}

    public UserDS(int id, String mail, String username, String password) {
        this.id = id;
        this.mail = mail;
        this.username = username;
        try {
        MessageDigest md = MessageDigest.getInstance( "SHA1" );
        md.update( password.getBytes() );
        this.password = new BigInteger( 1, md.digest() ).toString(16);
        }
        catch (NoSuchAlgorithmException e) {
        throw new MyHoardException(400);
        }
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
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
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        /*try {
        MessageDigest md = MessageDigest.getInstance( "SHA1" );
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
        if (mail == null || object.mail != null && !mail.equals(object.mail)) {
            mail = object.mail;
        }
        if (password == null || object.password != null && !password.equals(object.password)) {
            password = object.password;
        }
    }
    
    public UserDTO toUserDTO() {
        return new UserDTO(Integer.toString(id),mail, username, password);
    }


    @Override
    public String toString() {
        return ("id: " + this.id +
                "username: " + this.username +
                "\nmail: " + this.mail +
                "\npassword: " + this.password);
    }
    
}
