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
  UNIQUE INDEX `Email_UNIQUE` (`Email` ASC),
  INDEX `Status` (`Status` ASC));

-- test data for account seed
INSERT INTO `labanorodraugai_db`.`account`
(`Password`, `Name`, `Lastname`, `Description`, `Image`, `Status`, `Email`, `FB_URL`, `PointsQuantity`) VALUES 
('90005c954a300e7a071d5c0f87440e7b', 'Labanoro1', 'Draugai1', 'Test Description', null, 0, 'vytautas.labanoro1@gmail.com', 'https://www.facebook.com/ddziaugys',0),
('77cf552a2fd4c58b4154a2045779d19c', 'Labanoro2', 'Draugai2', 'Test Description', null, 0, 'vytautas.labanoro2@gmail.com', 'https://www.facebook.com/ddziaugys',0),
('202cb962ac5975b964b7152d234b70', 'TestName', 'TestLastname', 'Test Description', null, 1, 'test@test.test', 'https://www.facebook.com/ddziaugys',0);