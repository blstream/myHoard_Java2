CREATE TABLE `Collection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `owner` varchar(30) COLLATE utf8_polish_ci NOT NULL,
  `name` varchar(40) COLLATE utf8_polish_ci NOT NULL,
  `description` varchar(120) COLLATE utf8_polish_ci DEFAULT NULL,
  `items_number` int(11) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_date` timestamp NOT NULL DEFAULT ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE(`owner`, `name`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;
