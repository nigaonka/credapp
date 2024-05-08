CREATE TABLE `linpub_1122`.`customer_info` (
`id` INT NOT NULL AUTO_INCREMENT,
`customer_id` VARCHAR(45) NULL,
`first_name` VARCHAR(45) NULL,
`last_name` VARCHAR(45) NULL,
PRIMARY KEY (`id`),
UNIQUE INDEX `customerId_UNIQUE` (`customerId` ASC) VISIBLE);