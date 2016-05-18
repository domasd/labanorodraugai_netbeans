CREATE TABLE `labanorodraugai_db`.`additional_services_reservation` (
  `ServiceID` INT(11) NOT NULL,
  `AccountID` INT(11) UNSIGNED NOT NULL,
  `BeginDate` DATE NOT NULL,
  `EndDate` DATE NOT NULL,
  INDEX `AccountFK_idx` (`AccountID` ASC),
  INDEX `ServiceFK_idx` (`ServiceID` ASC),
  CONSTRAINT `AccountFK`
    FOREIGN KEY (`AccountID`)
    REFERENCES `labanorodraugai_db`.`account` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ServiceFK`
    FOREIGN KEY (`ServiceID`)
    REFERENCES `labanorodraugai_db`.`additional_services` (`ServiceID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    PRIMARY KEY (`ServiceID`, `AccountID`, `BeginDate`, `EndDate`));
