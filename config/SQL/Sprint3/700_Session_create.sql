CREATE TABLE IF NOT EXISTS Session (
        id INT AUTO_INCREMENT, 
	access_token VARCHAR(50) NOT NULL,
	date_created timestamp NOT NULL,
        refresh_token VARCHAR(80) NOT NULL,
	
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

