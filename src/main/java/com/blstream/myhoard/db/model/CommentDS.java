package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.exception.ErrorCode;
import com.blstream.myhoard.biz.exception.MyHoardException;
import com.blstream.myhoard.biz.model.CommentDTO;
import java.util.Date;

/**
 *
 * @author gohilukk
 */
public class CommentDS {

    private int id;
    private String content;
    private Date created_date;
    private int collection;
    private UserDS owner;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public UserDS getOwner() {
        return owner;
    }

    public void setOwner(UserDS owner) {
        this.owner = owner;
    }

    public void fromDTO(CommentDTO obj) {
        if (obj == null) {
            return;
        }

        id = Integer.parseInt(obj.getId());
        content = obj.getContent();

        created_date = obj.getCreatedDate();
        try {
            collection = Integer.parseInt(obj.getCollection());
        } catch (NumberFormatException ex) {
            throw new MyHoardException(ErrorCode.BAD_REQUEST).add("collection", "Niepoprawny identyfikator");
        }
        owner = obj.getOwner().toUserDS();
    }

    public CommentDTO toDTO() {
        return new CommentDTO(Integer.toString(id), content ,created_date, owner.toUserDTO(), Integer.toString(collection));
    }
}
