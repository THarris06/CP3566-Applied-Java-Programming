CREATE DATABASE `test2` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `test2`;
CREATE TABLE `users` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(55) NOT NULL,
  `PW_Hash` binary(60) NOT NULL,
  `token` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE TABLE `items` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `brand` varchar(45) DEFAULT NULL,
  `size` varchar(45) DEFAULT NULL,
  `unitCost` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `comp_UNIQUE` (`name`,`brand`,`size`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO `items`
  (`name`, `brand`, `size`, `unitCost`)
VALUES
  ("Cola", "Pepsi", "2l", 359),
  ("Cola", "Pepsi", "6x710ml", 699),
  ("Cola", "Pepsi", "12x355ml", 799),
  ("Cola", "Pepsi", "591ml", 249),
  ("Cola", "Coke", "2l", 359),
  ("Cola", "Coke", "6x710ml", 699),
  ("Cola", "Coke", "12x355ml", 799),
  ("Cola", "Coke", "591ml", 249),
  ("Cola", "Big 8", "2l", 150),
  ("Cola", "Big 8", "12x355ml", 499)
;
CREATE TABLE `carts` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `userId_UNIQUE` (`userId`),
  CONSTRAINT `fk_carts_1` FOREIGN KEY (`userId`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE TABLE `cart_entry` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `cartID` bigint(20) unsigned NOT NULL,
  `itemID` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `comp_unique` (`cartID`,`itemID`),
  KEY `fk_cart_entry_2_idx` (`itemID`),
  CONSTRAINT `fk_cart_entry_1` FOREIGN KEY (`cartID`) REFERENCES `carts` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_cart_entry_2` FOREIGN KEY (`itemID`) REFERENCES `items` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
