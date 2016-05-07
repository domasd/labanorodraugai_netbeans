CREATE TABLE `labanorodraugai_db`.`summerhouse` (
  `Id` INT(10) NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `Number` INT(10) NULL,
  `Capacity` INT(10) NULL,
  `Description` VARCHAR(256) NULL,
  `Image` BLOB NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `Name_UNIQUE` (`Name` ASC));
  
INSERT INTO `labanorodraugai_db`.`summerhouse` (`Id`, `Name`, `Number`, `Capacity`, `Description`) VALUES ('1', 'Sausio g. 12', '2', '5', 'Labai šaunus namelis!');
INSERT INTO `labanorodraugai_db`.`summerhouse` (`Id`, `Name`, `Number`, `Capacity`, `Description`) VALUES ('2', 'Vasario g.11', '4', '10', 'kitas namelis');
