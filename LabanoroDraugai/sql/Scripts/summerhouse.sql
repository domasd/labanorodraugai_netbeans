CREATE TABLE `labanorodraugai_db`.`summerhouse` (
  `Id` INT(10) NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `Number` INT(10) NULL,
  `Capacity` INT(10) NULL,
  `Description` VARCHAR(256) NULL,
  `PointsPerDay` INT NOT NULL,
  `Image` BLOB NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `Name_UNIQUE` (`Name` ASC));
  
INSERT INTO `labanorodraugai_db`.`summerhouse` (`Id`, `Number`, `Name`, `Description`, `Capacity`, `PointsPerDay`) VALUES ('1', '1', 'Sausio1', 'jaukus namelis', '5', '10');
INSERT INTO `labanorodraugai_db`.`summerhouse` (`Id`, `Number`, `Name`, `Description`, `Capacity`, `PointsPerDay`) VALUES ('2', '2', 'Sausio2', 'jaukus namelis', '10', '10');
INSERT INTO `labanorodraugai_db`.`summerhouse` (`Id`, `Number`, `Name`, `Description`, `Capacity`, `PointsPerDay`) VALUES ('3', '3', 'Sausio3', 'jaukus namelis', '230', '10');

