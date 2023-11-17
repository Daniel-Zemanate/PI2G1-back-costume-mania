-- MySQL Script generated by MySQL Workbench
-- Wed Nov 15 00:59:29 2023
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema costumemania
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `costumemania` ;

-- -----------------------------------------------------
-- Schema costumemania
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `costumemania` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `costumemania` ;

-- -----------------------------------------------------
-- Table `costumemania`.`statuscomponent`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `costumemania`.`statuscomponent` ;

CREATE TABLE IF NOT EXISTS `costumemania`.`statuscomponent` (
  `id_status` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id_status`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `costumemania`.`category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `costumemania`.`category` ;

CREATE TABLE IF NOT EXISTS `costumemania`.`category` (
  `id_category` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `status_category` INT NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_category`),
  INDEX `status_idx` (`status_category` ASC) VISIBLE,
  CONSTRAINT `status_category`
    FOREIGN KEY (`status_category`)
    REFERENCES `costumemania`.`statuscomponent` (`id_status`))
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `costumemania`.`model`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `costumemania`.`model` ;

CREATE TABLE IF NOT EXISTS `costumemania`.`model` (
  `id_model` INT NOT NULL AUTO_INCREMENT,
  `name_model` VARCHAR(45) NOT NULL,
  `category` INT NULL DEFAULT NULL,
  `url_image` VARCHAR(255) NULL DEFAULT NULL,
  `status_model` INT NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_model`),
  INDEX `id_category_idx` (`category` ASC) VISIBLE,
  INDEX `satus_model_idx` (`status_model` ASC) VISIBLE,
  CONSTRAINT `id_category`
    FOREIGN KEY (`category`)
    REFERENCES `costumemania`.`category` (`id_category`),
  CONSTRAINT `satus_model`
    FOREIGN KEY (`status_model`)
    REFERENCES `costumemania`.`statuscomponent` (`id_status`))
ENGINE = InnoDB
AUTO_INCREMENT = 30
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `costumemania`.`size`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `costumemania`.`size` ;

CREATE TABLE IF NOT EXISTS `costumemania`.`size` (
  `id_size` INT NOT NULL AUTO_INCREMENT,
  `adult` INT NULL DEFAULT NULL,
  `no_size` VARCHAR(3) NOT NULL,
  `size_description` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id_size`))
ENGINE = InnoDB
AUTO_INCREMENT = 13
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `costumemania`.`catalog`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `costumemania`.`catalog` ;

CREATE TABLE IF NOT EXISTS `costumemania`.`catalog` (
  `id_catalog` INT NOT NULL AUTO_INCREMENT,
  `model` INT NOT NULL,
  `size` INT NOT NULL,
  `quantity` INT NOT NULL,
  `price` FLOAT NULL DEFAULT NULL,
  `status_catalog` INT NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_catalog`),
  INDEX `id_size_idx` (`size` ASC) VISIBLE,
  INDEX `id_model_idx` (`model` ASC) VISIBLE,
  INDEX `status_idx` (`status_catalog` ASC) VISIBLE,
  CONSTRAINT `id_model`
    FOREIGN KEY (`model`)
    REFERENCES `costumemania`.`model` (`id_model`),
  CONSTRAINT `id_size`
    FOREIGN KEY (`size`)
    REFERENCES `costumemania`.`size` (`id_size`),
  CONSTRAINT `status_catalog`
    FOREIGN KEY (`status_catalog`)
    REFERENCES `costumemania`.`statuscomponent` (`id_status`))
ENGINE = InnoDB
AUTO_INCREMENT = 59
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `costumemania`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `costumemania`.`role` ;

CREATE TABLE IF NOT EXISTS `costumemania`.`role` (
  `id_role` INT NOT NULL AUTO_INCREMENT,
  `role` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_role`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `costumemania`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `costumemania`.`users` ;

CREATE TABLE IF NOT EXISTS `costumemania`.`users` (
  `id_user` INT NOT NULL AUTO_INCREMENT,
  `role` INT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `last_name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `pass` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id_user`),
  INDEX `id_role_idx` (`role` ASC) VISIBLE,
  CONSTRAINT `id_role`
    FOREIGN KEY (`role`)
    REFERENCES `costumemania`.`role` (`id_role`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `costumemania`.`fav`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `costumemania`.`fav` ;

CREATE TABLE IF NOT EXISTS `costumemania`.`fav` (
  `id_fav` INT NOT NULL AUTO_INCREMENT,
  `user` INT NOT NULL,
  `model` INT NOT NULL,
  PRIMARY KEY (`id_fav`),
  INDEX `id_user_idx` (`user` ASC) VISIBLE,
  INDEX `id_model_idx` (`model` ASC) VISIBLE,
  CONSTRAINT `model`
    FOREIGN KEY (`model`)
    REFERENCES `costumemania`.`model` (`id_model`),
  CONSTRAINT `user`
    FOREIGN KEY (`user`)
    REFERENCES `costumemania`.`user` (`id_user`))
ENGINE = InnoDB
AUTO_INCREMENT = 14
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `costumemania`.`shipping`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `costumemania`.`shipping` ;

CREATE TABLE IF NOT EXISTS `costumemania`.`shipping` (
  `id_shipping` INT NOT NULL AUTO_INCREMENT,
  `destination` VARCHAR(255) NOT NULL,
  `cost` DECIMAL(4,2) NOT NULL,
  PRIMARY KEY (`id_shipping`))
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `costumemania`.`status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `costumemania`.`status` ;

CREATE TABLE IF NOT EXISTS `costumemania`.`status` (
  `id_status` INT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_status`))
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `costumemania`.`sale`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `costumemania`.`sale` ;

CREATE TABLE IF NOT EXISTS `costumemania`.`sale` (
  `id_sale` INT NOT NULL,
  `no_invoice` INT NULL DEFAULT NULL,
  `user` INT NOT NULL,
  `model` INT NOT NULL,
  `quantity` INT NOT NULL,
  `shipping_address` VARCHAR(45) NOT NULL,
  `shipping_city` INT NOT NULL,
  `status` INT NULL DEFAULT '1',
  `sale_date` DATE NULL DEFAULT NULL,
  `shipping_date` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`id_sale`),
  INDEX `id_user_idx` (`user` ASC) VISIBLE,
  INDEX `id_envio_idx` (`shipping_city` ASC) VISIBLE,
  INDEX `id_status_idx` (`status` ASC) VISIBLE,
  INDEX `id_catalog_idx` (`model` ASC) VISIBLE,
  CONSTRAINT `id_catalog`
    FOREIGN KEY (`model`)
    REFERENCES `costumemania`.`catalog` (`id_catalog`),
  CONSTRAINT `id_shipping`
    FOREIGN KEY (`shipping_city`)
    REFERENCES `costumemania`.`shipping` (`id_shipping`),
  CONSTRAINT `id_status`
    FOREIGN KEY (`status`)
    REFERENCES `costumemania`.`status` (`id_status`),
  CONSTRAINT `id_user`
    FOREIGN KEY (`user`)
    REFERENCES `costumemania`.`user` (`id_user`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;



INSERT INTO `costumemania`.`statuscomponent` (`id_status`, `description`) VALUES ('1', 'active');
INSERT INTO `costumemania`.`statuscomponent` (`id_status`, `description`) VALUES ('2', 'inactive');


INSERT INTO `costumemania`.`size` (`id_size`, `adult`, `no_size`, `size_description`) VALUES ('1', '0', '6', 'Kids from 5 to 7 years old');
INSERT INTO `costumemania`.`size` (`id_size`, `adult`, `no_size`, `size_description`) VALUES ('2', '0', '8', 'Kids from 7 to 9 years old');
INSERT INTO `costumemania`.`size` (`id_size`, `adult`, `no_size`, `size_description`) VALUES ('3', '0', '10', 'Kids from 9 to 11 years old');
INSERT INTO `costumemania`.`size` (`id_size`, `adult`, `no_size`, `size_description`) VALUES ('4', '0', '12', 'Kids from 11 to 13 years old');
INSERT INTO `costumemania`.`size` (`id_size`, `adult`, `no_size`, `size_description`) VALUES ('5', '0', '14', 'Kids from 13 to 15 years old');
INSERT INTO `costumemania`.`size` (`id_size`, `adult`, `no_size`, `size_description`) VALUES ('6', '0', '16', 'Kids from 15 to 17 years old');
INSERT INTO `costumemania`.`size` (`id_size`, `adult`, `no_size`, `size_description`) VALUES ('7', '1', 'XS', 'Adult height 1.50m');
INSERT INTO `costumemania`.`size` (`id_size`, `adult`, `no_size`, `size_description`) VALUES ('8', '1', 'S', 'Adult height 1.60m');
INSERT INTO `costumemania`.`size` (`id_size`, `adult`, `no_size`, `size_description`) VALUES ('9', '1', 'M', 'Adult height 1.70m');
INSERT INTO `costumemania`.`size` (`id_size`, `adult`, `no_size`, `size_description`) VALUES ('10', '1', 'L', 'Adult height 1.70m weight +70k');
INSERT INTO `costumemania`.`size` (`id_size`, `adult`, `no_size`, `size_description`) VALUES ('11', '1', 'XL', 'Adult height +1.70m weight +85k');
INSERT INTO `costumemania`.`size` (`id_size`, `adult`, `no_size`, `size_description`) VALUES ('12', '1', 'XXL', 'Adult height +1.70m weight +100k');


INSERT INTO `costumemania`.`category` (`id_category`, `name`, `status_category`) VALUES ('1', 'Halloween', '1');
INSERT INTO `costumemania`.`category` (`id_category`, `name`, `status_category`) VALUES ('2', 'Disney', '1');
INSERT INTO `costumemania`.`category` (`id_category`, `name`, `status_category`) VALUES ('3', 'Professions', '1');
INSERT INTO `costumemania`.`category` (`id_category`, `name`, `status_category`) VALUES ('4', 'Fantasy', '1');
INSERT INTO `costumemania`.`category` (`id_category`, `name`, `status_category`) VALUES ('5', 'Sexy', '1');
INSERT INTO `costumemania`.`category` (`id_category`, `name`, `status_category`) VALUES ('6', 'Old times', '1');


INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('1', 'Witch', '1', 'https://costumemania.s3.amazonaws.com/witch.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('2', 'Devil', '1', 'https://costumemania.s3.amazonaws.com/devil.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('3', 'Jack Skeleton', '1', 'https://costumemania.s3.amazonaws.com/jack.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('4', 'Elsa from Frozen', '2', 'https://costumemania.s3.amazonaws.com/elsa.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('5', 'Jack Sparrow', '2', 'https://costumemania.s3.amazonaws.com/jackSparrow.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('6', 'Isabella from Encanto', '2', 'https://costumemania.s3.amazonaws.com/isabella.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('7', 'Police Agent', '3', 'https://costumemania.s3.amazonaws.com/police.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('8', 'Firefighter', '3', 'https://costumemania.s3.amazonaws.com/fireman.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('9', 'Batman', '4', 'https://costumemania.s3.amazonaws.com/batman.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('10', 'Wonder woman', '4', 'https://costumemania.s3.amazonaws.com/wonderWoman.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('11', 'Spiderman', '4', 'https://costumemania.s3.amazonaws.com/spiderman.png', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('12', 'Dark Queen', '5', 'https://costumemania.s3.amazonaws.com/darkQueen.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('13', 'Playboy Bunny', '5', 'https://costumemania.s3.amazonaws.com/bunny.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('14', 'FBI', '5', 'https://costumemania.s3.amazonaws.com/fbi.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('15', 'Victorian', '6', 'https://costumemania.s3.amazonaws.com/victorian.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('16', 'Gentleman', '6', 'https://costumemania.s3.amazonaws.com/gentleman.jpg', '1');

INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('17', 'Witch child', '1', 'https://costumemania.s3.amazonaws.com/witch_small.png', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('18', 'Devil child', '1', 'https://costumemania.s3.amazonaws.com/devil_small.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('19', 'Jack Skeleton child', '1', 'https://costumemania.s3.amazonaws.com/jack.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('20', 'Elsa from Frozen child', '2', 'https://costumemania.s3.amazonaws.com/elsa_small.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('21', 'Jack Sparrow child', '2', 'https://costumemania.s3.amazonaws.com/jackSparrow_small.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('22', 'Isabella from Encanto child', '2', 'https://costumemania.s3.amazonaws.com/isabella_small.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('23', 'Police Agent child', '3', 'https://costumemania.s3.amazonaws.com/police_small.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('24', 'Firefighter child', '3', 'https://costumemania.s3.amazonaws.com/fireman_small.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('25', 'Batman child', '4', 'https://costumemania.s3.amazonaws.com/batman_small.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('26', 'Wonder woman child', '4', 'https://costumemania.s3.amazonaws.com/wonderWoman_small.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('27', 'Spiderman child', '4', 'https://costumemania.s3.amazonaws.com/spiderman_small.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('28', 'Southern Lady child', '6', 'https://costumemania.s3.amazonaws.com/southernLady.jpg', '1');
INSERT INTO `costumemania`.`model` (`id_model`, `name_model`, `category`, `url_image`, `status_model`) VALUES ('29', 'Granadero child', '6', 'https://costumemania.s3.amazonaws.com/tinSoldier_small.jpg', '1');


INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('1', '1', '9', '2', '50.50', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('2', '1', '10', '1', '50.50', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('3', '17', '3', '3', '46.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('4', '17', '5', '1', '46.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('5', '2', '7', '1', '46.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('6', '2', '12', '4', '46.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('7', '18', '1', '2', '42.20', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('8', '18', '2', '0', '42.20', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('9', '3', '8', '2', '55.10', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('10', '3', '11', '6', '55.10', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('11', '19', '3', '2', '51.90', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('12', '19', '4', '4', '51.90', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('13', '4', '9', '1', '60.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('14', '4', '10', '0', '60.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('15', '20', '5', '4', '58.20', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('16', '20', '6', '3', '58.20', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('17', '5', '7', '4', '54.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('18', '5', '12', '6', '54.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('19', '21', '1', '0', '50.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('20', '21', '2', '2', '50.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('21', '6', '8', '2', '45.50', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('22', '6', '11', '3', '45.50', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('23', '22', '3', '3', '43.20', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('24', '22', '4', '3', '43.20', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('25', '7', '9', '2', '40.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('26', '7', '10', '2', '40.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('27', '23', '5', '2', '36.50', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('28', '23', '6', '2', '36.50', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('29', '8', '7', '3', '40.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('30', '8', '12', '3', '40.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('31', '24', '1', '3', '36.20', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('32', '24', '2', '3', '36.20', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('33', '9', '8', '3', '39.50', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('34', '9', '11', '1', '39.50', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('35', '25', '3', '1', '32.10', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('36', '25', '4', '1', '32.10', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('37', '10', '9', '1', '39.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('38', '10', '10', '4', '39.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('39', '26', '5', '6', '32.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('40', '26', '6', '0', '32.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('41', '11', '7', '3', '60.10', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('42', '11', '12', '2', '60.10', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('43', '27', '1', '2', '50.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('44', '27', '2', '2', '50.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('45', '12', '7', '3', '50.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('46', '12', '8', '2', '50.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('47', '13', '9', '2', '50.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('48', '13', '10', '2', '50.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('49', '14', '11', '2', '50.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('50', '14', '12', '3', '50.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('51', '15', '7', '4', '65.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('52', '15', '8', '0', '65.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('53', '16', '9', '1', '65.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('54', '16', '10', '6', '65.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('55', '28', '1', '2', '35.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('56', '28', '2', '1', '35.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('57', '29', '3', '3', '35.00', '1');
INSERT INTO `costumemania`.`catalog` (`id_catalog`, `model`, `size`, `quantity`, `price`, `status_catalog`) VALUES ('58', '29', '4', '2', '35.00', '1');


INSERT INTO `costumemania`.`role` (`id_role`, `role`) VALUES ('1', 'Administrador');
INSERT INTO `costumemania`.`role` (`id_role`, `role`) VALUES ('2', 'Usuario');


INSERT INTO `costumemania`.`users` (`id_user`, `role`, `name`, `last_name`, `email`, `pass`) VALUES ('1', '1', 'Juan', 'Perez', 'j.perez@email.com', '1234');
INSERT INTO `costumemania`.`users` (`id_user`, `role`, `name`, `last_name`, `email`, `pass`) VALUES ('2', '2', 'Daniel', 'Gonzalez', 'd.gonzalez@email.com', '1234');
INSERT INTO `costumemania`.`users` (`id_user`, `role`, `name`, `last_name`, `email`, `pass`) VALUES ('3', '2', 'Eduardo', 'Calviño', 'eduardo@email.com', '1234');
INSERT INTO `costumemania`.`users` (`id_user`, `role`, `name`, `last_name`, `email`, `pass`) VALUES ('4', '2', 'Laura', 'Nuñez', 'laura@email.com', '1234');


INSERT INTO `costumemania`.`shipping` (`id_shipping`, `destination`, `cost`) VALUES ('1', 'CABA, Arg', '0.00');
INSERT INTO `costumemania`.`shipping` (`id_shipping`, `destination`, `cost`) VALUES ('2', 'Buenos Aires, Arg', '15.00');
INSERT INTO `costumemania`.`shipping` (`id_shipping`, `destination`, `cost`) VALUES ('3', 'Cordoba, Arg', '8.50');
INSERT INTO `costumemania`.`shipping` (`id_shipping`, `destination`, `cost`) VALUES ('4', 'Norte de Argentina', '35.00');
INSERT INTO `costumemania`.`shipping` (`id_shipping`, `destination`, `cost`) VALUES ('5', 'Sur de Argentina', '32.00');
INSERT INTO `costumemania`.`shipping` (`id_shipping`, `destination`, `cost`) VALUES ('6', 'Bogotá, Colombia', '0.00');
INSERT INTO `costumemania`.`shipping` (`id_shipping`, `destination`, `cost`) VALUES ('7', 'Medellín, Colombia', '22.00');
INSERT INTO `costumemania`.`shipping` (`id_shipping`, `destination`, `cost`) VALUES ('8', 'Cali, Colombia', '20.50');


INSERT INTO `costumemania`.`status` (`id_status`, `status`) VALUES ('1', 'In progress');
INSERT INTO `costumemania`.`status` (`id_status`, `status`) VALUES ('2', 'On the way');
INSERT INTO `costumemania`.`status` (`id_status`, `status`) VALUES ('3', 'Delivered');
INSERT INTO `costumemania`.`status` (`id_status`, `status`) VALUES ('4', 'Cancelled by the customer');
INSERT INTO `costumemania`.`status` (`id_status`, `status`) VALUES ('5', 'Canceled - Wrong addess');
INSERT INTO `costumemania`.`status` (`id_status`, `status`) VALUES ('6', 'Cancelled by admin');


INSERT INTO `costumemania`.`sale` (`id_sale`, `no_invoice`, `users`, `model`, `quantity`, `shipping_address`, `shipping_city`, `status`, `sale_date`,`shipping_date`) VALUES ('1', '00023', '2', '2', '1', 'calle 123', '1', '3', '2023-02-12', '2023-02-16');
INSERT INTO `costumemania`.`sale` (`id_sale`, `no_invoice`, `users`, `model`, `quantity`, `shipping_address`, `shipping_city`, `status`, `sale_date`,`shipping_date`) VALUES ('2', '00023', '2', '15', '1', 'calle 123', '1', '3', '2023-02-12', '2023-02-16');
INSERT INTO `costumemania`.`sale` (`id_sale`, `no_invoice`, `users`, `model`, `quantity`, `shipping_address`, `shipping_city`, `status`, `sale_date`,`shipping_date`) VALUES ('3', '00024', '2', '25', '1', 'calle 123', '8', '3', '2023-03-12', '2023-03-13');
INSERT INTO `costumemania`.`sale` (`id_sale`, `no_invoice`, `users`, `model`, `quantity`, `shipping_address`, `shipping_city`, `status`, `sale_date`) VALUES ('4', '00026', '3', '25', '1', 'calle 123', '3', '2', '2023-06-12');
INSERT INTO `costumemania`.`sale` (`id_sale`, `no_invoice`, `users`, `model`, `quantity`, `shipping_address`, `shipping_city`, `status`, `sale_date`) VALUES ('5', '00027', '4', '48', '2', 'calle 123', '4', '1', '2023-06-12');
INSERT INTO `costumemania`.`sale` (`id_sale`, `no_invoice`, `users`, `model`, `quantity`, `shipping_address`, `shipping_city`, `status`, `sale_date`,`shipping_date`) VALUES ('6', '00028', '2', '51', '1', 'calle 123', '5', '5', '2023-02-12', '2023-02-16');
INSERT INTO `costumemania`.`sale` (`id_sale`, `no_invoice`, `users`, `model`, `quantity`, `shipping_address`, `shipping_city`, `status`, `sale_date`) VALUES ('7', '00028', '3', '9', '1', 'calle 123', '6', '4', '2023-05-12');
INSERT INTO `costumemania`.`sale` (`id_sale`, `no_invoice`, `users`, `model`, `quantity`, `shipping_address`, `shipping_city`, `status`, `sale_date`) VALUES ('8', '00029', '3', '25', '1', 'calle 123', '7', '1', '2023-06-12');


INSERT INTO `costumemania`.`fav` (`id_fav`, `users`, `model`) VALUES ('1', '2', '11');
INSERT INTO `costumemania`.`fav` (`id_fav`, `users`, `model`) VALUES ('2', '2', '12');
INSERT INTO `costumemania`.`fav` (`id_fav`, `users`, `model`) VALUES ('3', '2', '13');
INSERT INTO `costumemania`.`fav` (`id_fav`, `users`, `model`) VALUES ('4', '4', '15');
INSERT INTO `costumemania`.`fav` (`id_fav`, `users`, `model`) VALUES ('5', '3', '18');
INSERT INTO `costumemania`.`fav` (`id_fav`, `users`, `model`) VALUES ('6', '4', '4');
INSERT INTO `costumemania`.`fav` (`id_fav`, `users`, `model`) VALUES ('7', '4', '10');
INSERT INTO `costumemania`.`fav` (`id_fav`, `users`, `model`) VALUES ('8', '2', '15');
INSERT INTO `costumemania`.`fav` (`id_fav`, `users`, `model`) VALUES ('9', '3', '15');
INSERT INTO `costumemania`.`fav` (`id_fav`, `users`, `model`) VALUES ('10', '3', '4');
INSERT INTO `costumemania`.`fav` (`id_fav`, `users`, `model`) VALUES ('11', '2', '1');
INSERT INTO `costumemania`.`fav` (`id_fav`, `users`, `model`) VALUES ('12', '2', '2');
INSERT INTO `costumemania`.`fav` (`id_fav`, `users`, `model`) VALUES ('13', '2', '3');
