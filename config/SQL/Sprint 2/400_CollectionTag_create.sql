CREATE TABLE IF NOT EXISTS CollectionTag (
	id INT AUTO_INCREMENT,
	collection INT NOT NULL,
	tag INT NOT NULL,
	
	PRIMARY KEY(id),
	FOREIGN KEY(collection) REFERENCES Collection(id),
	FOREIGN KEY(tag) REFERENCES Tag(id)
);
