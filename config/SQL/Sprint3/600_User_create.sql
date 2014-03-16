CREATE TABLE IF NOT EXISTS User (
        id INT AUTO_INCREMENT, 
	username varchar(50) CHARACTER SET utf8 COLLATE utf8_polish_ci,
	mail VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_polish_ci NOT NULL,
        password VARCHAR(80) NOT NULL,
	
	UNIQUE(username),
        UNIQUE(mail),
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

