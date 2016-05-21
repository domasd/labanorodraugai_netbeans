CREATE TABLE `labanorodraugai_db`.`additional_services` (
  `ServiceID` INT(11) NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NULL,
  `Description` VARCHAR(256) NULL DEFAULT NULL,
  `PointsPerDay` DECIMAL(30,2) ZEROFILL NOT NULL,
  UNIQUE INDEX `Name_UNIQUE` (`Name` ASC),
  PRIMARY KEY (`ServiceID`));

INSERT INTO `labanorodraugai_db`.`additional_services` (`Name`, `Description`, `PointsPerDay`) VALUES ('Dviratis', 'vogtas, bet geras', '2');
INSERT INTO `labanorodraugai_db`.`additional_services` (`Name`, `Description`, `PointsPerDay`) VALUES ('Valtis', 'biski paseno, bet vis dar plaukia', '3');
