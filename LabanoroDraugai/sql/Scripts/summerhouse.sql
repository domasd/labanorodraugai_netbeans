CREATE TABLE `labanorodraugai_db`.`summerhouse` (
  `Id` INT(10) NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `Number` INT(10) NULL,
  `Capacity` INT(10) NULL,
  `Description` VARCHAR(256) NULL,
  `PointsPerDay` DECIMAL(30,2) ZEROFILL NOT NULL,
  `Image` BLOB NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `Name_UNIQUE` (`Name` ASC));
  
INSERT INTO `labanorodraugai_db`.`summerhouse` (`Number`, `Name`, `Description`, `Capacity`, `PointsPerDay`) VALUES ('1', 'Sausio', 'jaukus namelis', '5', '10');
INSERT INTO `labanorodraugai_db`.`summerhouse` (`Number`, `Name`, `Description`, `Capacity`, `PointsPerDay`) VALUES ('2', 'Kovo', 'yra kavos aparatas', '10', '15');
INSERT INTO `labanorodraugai_db`.`summerhouse` (`Number`, `Name`, `Description`, `Capacity`, `PointsPerDay`) VALUES ('3', 'Vasario', 'yra elektra', '23', '20');

