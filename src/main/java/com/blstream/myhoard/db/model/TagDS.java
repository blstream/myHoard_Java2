package com.blstream.myhoard.db.model;

import com.blstream.myhoard.biz.model.TagDTO;

public class TagDS {

    private int id;
    private String tag = "";

    public TagDS() {}

    public TagDS(int id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public TagDTO toTagTO() {
        return new TagDTO(Integer.toString(id), tag);
    }

    public int hashCode() {
        return tag.hashCode();
    }

    public boolean equals(Object o) {
        return (o == this) || (o instanceof TagDS && tag != null && tag.equals(((TagDS)o).tag));
    }

    public String toString() {
        return tag;
    }
}
