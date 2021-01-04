drop schema ims;
CREATE SCHEMA IF NOT EXISTS `ims`;
USE `ims` ;
CREATE TABLE IF NOT EXISTS `ims`.`customers` (
    `customer_id` INT(11) NOT NULL AUTO_INCREMENT,
    `first_name` VARCHAR(40) NULL DEFAULT NULL,
    `surname` VARCHAR(40) NULL DEFAULT NULL,
    `email` VARCHAR(100) NULL DEFAULT NULL,
    PRIMARY KEY (`customer_id`)
);
CREATE TABLE IF NOT EXISTS `ims`.`categories` (
    `category_id` INT(11) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(40) NULL DEFAULT NULL,
    PRIMARY KEY (`category_id`)
);
CREATE TABLE IF NOT EXISTS `ims`.`items` (
    `item_id` INT(11) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(40) NULL DEFAULT NULL,
    `value` FLOAT(20) NOT NULL DEFAULT 0,
    `category_id` INT(11) NULL DEFAULT NULL,
    `quantity` INT(11) NOT NULL DEFAULT 0,
    PRIMARY KEY (`item_id`),
    FOREIGN KEY (`category_id`) REFERENCES `ims`.`categories`(`category_id`)
);
CREATE TABLE IF NOT EXISTS `ims`.`orders` (
    `order_id` INT(11) NOT NULL AUTO_INCREMENT,
    `customer_id` INT(11) NULL DEFAULT NULL,
    `date` DATE NULL DEFAULT NULL,
    PRIMARY KEY (`order_id`),
    FOREIGN KEY (`customer_id`) REFERENCES `ims`.`customers`(`customer_id`)
);
CREATE TABLE IF NOT EXISTS `ims`.`order_items` (
	`order_items_id` INT(11) NOT NULL AUTO_INCREMENT,
	`order_id` INT(11) NULL DEFAULT NULL,
	`item_id` INT(11) NULL DEFAULT NULL,
	`quantity` INT(11) NULL DEFAULT NULL,
	PRIMARY KEY (`order_items_id`),
	FOREIGN KEY (`order_id`) REFERENCES `ims`.`orders`(`order_id`) ON DELETE CASCADE,
	FOREIGN KEY (`item_id`) REFERENCES `ims`.`items`(`item_id`) ON DELETE CASCADE
	
);