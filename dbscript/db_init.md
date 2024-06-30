CREATE TABLE `linpub_4455`.`customer_info` (
`id` INT NOT NULL AUTO_INCREMENT,
`customer_id` VARCHAR(45) NULL,
`first_name` VARCHAR(45) NULL,
`last_name` VARCHAR(45) NULL,
PRIMARY KEY (`id`),
UNIQUE INDEX `customerId_UNIQUE` (`customer_id` ASC) VISIBLE);


CREATE TABLE `linpub_4455`.`offer` (
`id` INT NOT NULL AUTO_INCREMENT,
`offer_id` VARCHAR(45) NOT NULL,
`price` VARCHAR(45) NOT NULL,
`credit_factor` VARCHAR(45) NULL,
PRIMARY KEY (`id`),
UNIQUE INDEX `offer_id_UNIQUE` (`offer_id` ASC) VISIBLE,
UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);

CREATE TABLE `linpub_4455`.`purchase_offer` (
`id` INT GENERATED ALWAYS AS (),
`customer_id` VARCHAR(45) NOT NULL,
`offer_id` VARCHAR(45) NOT NULL,
PRIMARY KEY (`id`),
UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);


CREATE TABLE `linpub_4455`.`channel` (
`id` INT NOT NULL AUTO_INCREMENT,
`channel_name` VARCHAR(45) NOT NULL,
`price` DOUBLE NULL,
`channel_id` VARCHAR(45) NOT NULL,
PRIMARY KEY (`id`),
UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
UNIQUE INDEX `channel_name_UNIQUE` (`channel_name` ASC) VISIBLE,
UNIQUE INDEX `channel_id_UNIQUE` (`channel_id` ASC) VISIBLE);


CREATE TABLE `linpub_4455`.`credit_balance` (
`id` INT NOT NULL AUTO_INCREMENT,
`account_id` VARCHAR(45) NULL,
`credit_balance` DOUBLE NULL,
PRIMARY KEY (`id`),
UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
UNIQUE INDEX `account_id_UNIQUE` (`account_id` ASC) VISIBLE);
