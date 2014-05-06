package com.blstream.myhoard.biz.service;

public interface MediaService<T> extends ResourceService<T> {

    public byte[] getThumbnail(int id, int size);

}
