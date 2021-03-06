CREATE TABLE IF NOT EXISTS Media (
	id INT AUTO_INCREMENT,
	file MEDIUMBLOB NOT NULL,
	created_date TIMESTAMP NOT NULL,
	item INT,
	owner INT NOT NULL,

	PRIMARY KEY(id),
	FOREIGN KEY(item) REFERENCES Item(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(owner) REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

