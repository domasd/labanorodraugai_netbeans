CREATE TABLE `labanorodraugai_db`.`account` (
  `Id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Password` CHAR(32), 
  `Name` VARCHAR(50) NOT NULL,
  `Lastname` VARCHAR(50) NOT NULL,
  `Description` NVARCHAR(500) NULL,
  `Image` BLOB NULL,
  `Status` INT ZEROFILL NOT NULL,
  `Email` VARCHAR(50) NOT NULL,
  `FB_URL` VARCHAR(100) NULL,
  `ReservationGroup` INT(11) NULL,
  `PointsQuantity` DECIMAL(30,2) ZEROFILL NOT NULL,
  `MembershipValidUntil` DATE NULL,
  `OptLockVersion` INT NOT NULL DEFAULT 0,
  `CustomRegistrationFields` TEXT,
  CONSTRAINT `ReservationGroupFK`
    FOREIGN KEY (`ReservationGroup`)
    REFERENCES `labanorodraugai_db`.`reservation_groups` (`GroupNumber`)
    ON DELETE SET NULL
    ON UPDATE SET NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `Email_UNIQUE` (`Email` ASC),
  INDEX `Status` (`Status` ASC));

-- test data for account seed
INSERT INTO `labanorodraugai_db`.`account`
(`Password`, `Name`, `Lastname`, `Description`, `Image`, `Status`, `Email`, `FB_URL`, `MembershipValidUntil`,`ReservationGroup`, `PointsQuantity`) VALUES 
('202cb962ac5975b964b7152d234b70', 'Vytautas', 'Traškevičius', 'Duomiuosi politiką', null, 0, 'vytautas.labanoro1@gmail.com', 'https://www.facebook.com/ddziaugys','20160501',0,1000),
('202cb962ac5975b964b7152d234b70', 'Vieslav', 'Lapin', 'Važinėju dviračiu', null, 0, 'vytautas.labanoro2@gmail.com', 'https://www.facebook.com/ddziaugys','20160601',1,1000),
('202cb962ac5975b964b7152d234b70', 'Jonas', 'Jonaitis', 'Labai mėgstu žvejoti, medžioti.', null, 1, 'jonas@jonaitis.lt', 'https://www.facebook.com/serg.filon',null,0,0),
('202cb962ac5975b964b7152d234b70', 'Andrius', 'Andriukaitis', 'Adminsitruoju sistemą.', null, 2, 'andrius@andriukaitis.lt','20160601', null, 0, 1000);


