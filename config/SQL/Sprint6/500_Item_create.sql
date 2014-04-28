CREATE TABLE IF NOT EXISTS Item (
	id INT AUTO_INCREMENT,
	name VARCHAR(150) CHARSET utf8 COLLATE utf8_polish_ci NOT NULL,
	description TEXT CHARSET utf8 COLLATE utf8_polish_ci,
	latitude FLOAT,
	longitude FLOAT,
	created_date TIMESTAMP NOT NULL,
	modified_date TIMESTAMP NOT NULL,
	created_date_client TIMESTAMP NOT NULL,
	modified_date_client TIMESTAMP NOT NULL,
	collection INT NOT NULL,
	owner INT NOT NULL,

	UNIQUE(name, collection),
	PRIMARY KEY(id),
	FOREIGN KEY(collection) REFERENCES Collection(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(owner) REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

