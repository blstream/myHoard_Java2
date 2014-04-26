/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blstream.myhoard.biz.model;

import java.util.Date;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author gohilukk
 */
public class CommentDTO {
    
    private String id = "0";
    
    @Length(max=160)
    @NotNull(message = "Treść nie może być pusta")
    @NotEmpty(message = "Treść nie może być pusta")
    private String content;
    
    @JsonIgnore
    private Date createdDate;
    private UserDTO owner;
    
    @NotNull(message = "Element musi być przypisany do kolekcji")
    @NotEmpty(message = "Element musi być przypisany do kolekcji")
    private String collection;

    public CommentDTO() {
        createdDate = java.util.Calendar.getInstance().getTime();
    } 
    
    @JsonProperty(value = "created_date")
    @JsonSerialize(using = CustomDateSerializer.class)
    public Date getCreatedDate() {
        return createdDate;
    }
    
    @JsonIgnore
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @JsonProperty(value = "id")    
    public String getId() {
        return id;
    }

    @JsonIgnore    
    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonProperty(value = "owner")
    @JsonSerialize(using = CustomOwnerSerializer.class)
    public UserDTO getOwner() {
        return owner;
    }

    @JsonIgnore
    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public CommentDTO(String id, String content, Date createdDate, UserDTO owner, String collection) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
        this.owner = owner;
        this.collection = collection;
    }
    
}
