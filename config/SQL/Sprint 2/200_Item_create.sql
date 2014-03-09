CREATE TABLE `Item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(150) COLLATE utf8_polish_ci NOT NULL,
  `description` varchar(500) COLLATE utf8_polish_ci DEFAULT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `quantity` int(11) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_date` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP,
--  `collection` int(11) NOT NULL,
  `owner` varchar(30) COLLATE utf8_polish_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`name`, `owner`),
  FOREIGN KEY (`collection`) REFERENCES `Collection` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci
