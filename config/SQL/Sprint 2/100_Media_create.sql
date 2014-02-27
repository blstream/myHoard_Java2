CREATE TABLE IF NOT EXISTS Media (
	id INT AUTO_INCREMENT,
	file BLOB,
	thumbnail BLOB,
	collection INT NOT NULL,
	created_date TIMESTAMP NOT NULL,
	
	PRIMARY KEY(id),
	FOREIGN KEY(collection) REFERENCES Collection(id)
);