package com.blstream.myhoard.db.model;

import java.util.Date;

public class ItemDS {

	private int id;
	private String name;
	private String description;
	private float latitude;
	private float longitude;
	private int quantity;
	// media
	private Date createdDate;
	private Date modifiedDate;
	private int collectionId;
	private String owner;
}
