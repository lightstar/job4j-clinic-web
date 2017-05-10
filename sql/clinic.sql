CREATE DATABASE IF NOT EXISTS `pet_clinic` DEFAULT CHARACTER SET='utf8';
GRANT ALL ON `pet_clinic`.* TO `job4j`@`%` IDENTIFIED BY 'job4j';

USE `pet_clinic`;

DROP TABLE IF EXISTS `pet`;
DROP TABLE IF EXISTS `message`;
DROP TABLE IF EXISTS `client`;
DROP TABLE IF EXISTS `drug`;
DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
);

CREATE TABLE `client` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `position` int(10) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL DEFAULT '',
  `phone` varchar(255) NOT NULL DEFAULT '',
  `role_id` int(10) unsigned NOT NULL,
  `password` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `position` (`position`),
  UNIQUE KEY `name` (`name`),
  CONSTRAINT `client_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
);

CREATE TABLE `pet` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `client_id` int(10) unsigned NOT NULL,
  `type` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `age` int(10) unsigned NOT NULL DEFAULT '0',
  `sex` enum('m','f') NOT NULL DEFAULT 'm',
  PRIMARY KEY (`id`),
  UNIQUE KEY `client_id` (`client_id`),
  CONSTRAINT `pet_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`) ON DELETE CASCADE
);

CREATE TABLE `message` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `text` longtext NOT NULL,
  `client_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `message_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`)
);

CREATE TABLE `drug` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `danger` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
);
