CREATE DATABASE IF NOT EXISTS `pet_clinic` DEFAULT CHARACTER SET='utf8';
GRANT ALL ON `pet_clinic`.* TO `job4j`@`%` IDENTIFIED BY 'job4j';

USE `pet_clinic`;

DROP TABLE IF EXISTS `pet`;
DROP TABLE IF EXISTS `client`;
DROP TABLE IF EXISTS `drug`;

CREATE TABLE `client` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `position` int(10) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL DEFAULT '',
  `phone` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `position` (`position`),
  UNIQUE KEY `name` (`name`)
);

CREATE TABLE `pet` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `client_id` int(10) unsigned NOT NULL,
  `type` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `age` int(10) unsigned NOT NULL,
  `sex` enum('m','f') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `client_id` (`client_id`),
  CONSTRAINT `pet_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`)
);

CREATE TABLE `drug` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `danger` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
);
