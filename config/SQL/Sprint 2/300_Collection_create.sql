CREATE TABLE `Collection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) COLLATE utf8_polish_ci NOT NULL,
  `description` varchar(120) COLLATE utf8_polish_ci DEFAULT NULL,
  `created_date` timestamp NOT NULL,
  `modified_date` timestamp NOT NULL,
  `owner` varchar(30) COLLATE utf8_polish_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE(`owner`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

