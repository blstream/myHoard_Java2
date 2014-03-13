package com.blstream.myhoard.db.model;


public class CollectionTagDS {

	private int id;
	private int collectionId;
	private int tagId;
	public CollectionTagDS() {}

	public CollectionTagDS(int collectionId, int tagId) {
		this.collectionId = collectionId;
		this.tagId = tagId;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(int collectionId) {
		this.collectionId = collectionId;
	}

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}
}
