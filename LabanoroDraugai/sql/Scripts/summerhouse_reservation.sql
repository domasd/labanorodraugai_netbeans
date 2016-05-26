CREATE TABLE `labanorodraugai_db`.`summerhouse_reservation` (
  `AccountID` INT(10) UNSIGNED NOT NULL,
  `SummerhouseID` INT(10) NOT NULL,
  `BeginDate` DATE NOT NULL,
  `EndDate` DATE NOT NULL,
  `RecordCreated` DATE NOT NULL,
  `PointsAmount` DECIMAL(30,2) ZEROFILL NOT NULL,
  PRIMARY KEY (`AccountID`, `SummerhouseID`, `EndDate`, `BeginDate`),
  INDEX `SummerhouseFK_idx` (`SummerhouseID` ASC),
  CONSTRAINT `SummerhouseFK`
    FOREIGN KEY (`SummerhouseID`)
    REFERENCES `labanorodraugai_db`.`summerhouse` (`Id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `AccountID`
    FOREIGN KEY (`AccountID`)
    REFERENCES `labanorodraugai_db`.`account` (`Id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);


INSERT INTO `labanorodraugai_db`.`summerhouse_reservation` (`AccountID`, `SummerhouseID`, `BeginDate`, `EndDate`, `RecordCreated`, `PointsAmount`) 
VALUES ('1', '1', '2016-06-13', '2016-06-16', '2016-05-01', 17);

	