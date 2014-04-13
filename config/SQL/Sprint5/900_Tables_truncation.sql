-- W celu szybkiego wyczyszczenia bazy
SET FOREIGN_KEY_CHECKS=0;
TRUNCATE ItemMedia;
TRUNCATE CollectionTag;
TRUNCATE Media;
TRUNCATE Item;
TRUNCATE Collection;
TRUNCATE Session;
TRUNCATE Tag;
TRUNCATE User;
SET FOREIGN_KEY_CHECKS=1;
