CREATE TABLE IF NOT EXISTS Collection (
	id INT AUTO_INCREMENT,
	name VARCHAR(128) CHARSET utf8 COLLATE utf8_polish_ci NOT NULL,
	description TEXT CHARSET utf8 COLLATE utf8_polish_ci,
	created_date TIMESTAMP NOT NULL,
	modified_date TIMESTAMP NOT NULL,
	created_date_client TIMESTAMP NOT NULL,
	modified_date_client TIMESTAMP NOT NULL,
	visible BIT(1) NOT NULL,
	owner INT NOT NULL,

	UNIQUE(name, owner),
	PRIMARY KEY(id),
	FOREIGN KEY(owner) REFERENCES User(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

