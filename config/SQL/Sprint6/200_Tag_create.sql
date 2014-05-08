CREATE TABLE IF NOT EXISTS Tag (
	id INT AUTO_INCREMENT,
	tag VARCHAR(48) CHARACTER SET utf8 COLLATE utf8_polish_ci NOT NULL,

	UNIQUE(tag),
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;
