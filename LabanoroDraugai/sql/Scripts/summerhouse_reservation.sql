CREATE TABLE `labanorodraugai_db`.`summerhouse_reservation` (
  `AccountID` INT(10) UNSIGNED NOT NULL,
  `SummerhouseID` INT(10) NOT NULL,
  `BeginDate` DATE NOT NULL,
  `EndDate` DATE NOT NULL,
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
	

INSERT INTO `labanorodraugai_db`.`summerhouse_reservation` (`AccountID`, `SummerhouseID`, `BeginDate`, `EndDate`) VALUES ('1', '1', '2015-05-13', '2015-05-16');
INSERT INTO `labanorodraugai_db`.`summerhouse_reservation` (`AccountID`, `SummerhouseID`, `BeginDate`, `EndDate`) VALUES ('1', '1', '2016-04-22', '2016-04-24');
INSERT INTO `labanorodraugai_db`.`summerhouse_reservation` (`AccountID`, `SummerhouseID`, `BeginDate`, `EndDate`) VALUES ('1', '1', '2016-04-28', '2016-05-03');

