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
INSERT INTO `labanorodraugai_db`.`summerhouse_reservation` (`AccountID`, `SummerhouseID`, `BeginDate`, `EndDate`) VALUES ('1', '1', '2015-02-27', '2015-03-02');

INSERT INTO `labanorodraugai_db`.`summerhouse_reservation` (`AccountID`, `SummerhouseID`, `BeginDate`, `EndDate`) VALUES ('2', '1', '2015-01-13', '2015-01-16');
INSERT INTO `labanorodraugai_db`.`summerhouse_reservation` (`AccountID`, `SummerhouseID`, `BeginDate`, `EndDate`) VALUES ('2', '1', '2015-01-28', '2015-02-04');

INSERT INTO `labanorodraugai_db`.`summerhouse_reservation` (`AccountID`, `SummerhouseID`, `BeginDate`, `EndDate`) VALUES ('3', '1', '2014-04-22', '2014-04-24');
INSERT INTO `labanorodraugai_db`.`summerhouse_reservation` (`AccountID`, `SummerhouseID`, `BeginDate`, `EndDate`) VALUES ('3', '1', '2015-06-13', '2015-06-16');

INSERT INTO `labanorodraugai_db`.`summerhouse_reservation` (`AccountID`, `SummerhouseID`, `BeginDate`, `EndDate`) VALUES ('4', '1', '2015-09-08', '2015-09-11');
INSERT INTO `labanorodraugai_db`.`summerhouse_reservation` (`AccountID`, `SummerhouseID`, `BeginDate`, `EndDate`) VALUES ('4', '1', '2015-10-11', '2015-10-12');

INSERT INTO `labanorodraugai_db`.`summerhouse_reservation` (`AccountID`, `SummerhouseID`, `BeginDate`, `EndDate`) VALUES ('5', '1', '2015-04-13', '2015-04-16');
INSERT INTO `labanorodraugai_db`.`summerhouse_reservation` (`AccountID`, `SummerhouseID`, `BeginDate`, `EndDate`) VALUES ('5', '1', '2015-07-15', '2015-07-22');
