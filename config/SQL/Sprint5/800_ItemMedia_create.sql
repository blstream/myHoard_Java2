CREATE TABLE IF NOT EXISTS ItemMedia (
	item INT NOT NULL,
	media INT NOT NULL,

	PRIMARY KEY(item, media),
	FOREIGN KEY(item) REFERENCES Item(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(media) REFERENCES Media(id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

