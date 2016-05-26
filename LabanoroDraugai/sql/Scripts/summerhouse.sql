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
  
INSERT INTO `labanorodraugai_db`.`summerhouse` (`Number`, `Name`, `Description`, `Capacity`, `PointsPerDay`) VALUES ('1', 'Pietinis', 'Iki  ežero - 200 m. ežeras puikiai tinka buriavimui, kaitavimui, plaukiojimui jachtomis, valtimis, vandens dviračiais. Didelis erdvus sandeliukas, idealus šeimai.', '10', '10');
INSERT INTO `labanorodraugai_db`.`summerhouse` (`Number`, `Name`, `Description`, `Capacity`, `PointsPerDay`) VALUES ('2', 'Pietryciu', 'Iki ežero 500 m. Puikus namelis porai arba individualiem poilsiautojiam.', '2', '15');
INSERT INTO `labanorodraugai_db`.`summerhouse` (`Number`, `Name`, `Description`, `Capacity`, `PointsPerDay`) VALUES ('3', 'Vakarinis', 'Erdvus, 8 miegamieji, su sauna. Iki ežero 100 m.', '23', '20');

