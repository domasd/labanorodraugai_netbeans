CREATE TABLE `labanorodraugai_db`.`account` (
  `Id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Password` CHAR(32) NOT NULL, 
  `Name` VARCHAR(50) NOT NULL,
  `Lastname` VARCHAR(50) NOT NULL,
  `Description` NVARCHAR(500) NULL,
  `Image` BLOB NULL,
  `Status` INT ZEROFILL NOT NULL,
  `Email` VARCHAR(50) NOT NULL,
  `FB_URL` VARCHAR(100) NULL,
  `PointsQuantity` DECIMAL(30,2) ZEROFILL NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `Name_UNIQUE` (`Email` ASC),
  UNIQUE INDEX `FBAccountUrl_UNIQUE` (`FB_URL` ASC),
  INDEX `Status` (`Status` ASC));

-- test data for account seed
INSERT INTO `labanorodraugai_db`.`account`
(`Password`, `Name`, `Lastname`, `Description`, `Image`, `Status`, `Email`, `FB_URL`, `PointsQuantity`) 
VALUES ('202cb962ac5975b964b7152d234b70', 'TestName', 'TestLastname', 'Test Description', null, 0, 'test@test.test', 'https://www.facebook.com/ddziaugys',0);