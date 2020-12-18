INSERT INTO `ims`.`customers` (`first_name`, `surname`, `email`) VALUES ('Jordan', 'Harrison', 'jharrison@qa.com');
INSERT INTO `ims`.`categories` (`category_id`, `name`) VALUES (1, 'General');
INSERT INTO `ims`.`items` (`name`, `value`, `category_id`, `quantity`) VALUES ('Frying Pan', 15.99, 1, 46);
INSERT INTO `ims`.`items` (`name`, `value`, `category_id`, `quantity`) VALUES ('Kettle', 24.99, 1, 57);
INSERT INTO `ims`.`orders` (`customer_id`, `date`) VALUES (1, '2020-12-18');
INSERT INTO `ims`.`order_items` (`order_id`, `item_id`, `quantity`) VALUES (1, 1, 2);
INSERT INTO `ims`.`order_items` (`order_id`, `item_id`, `quantity`) VALUES (1, 2, 1);