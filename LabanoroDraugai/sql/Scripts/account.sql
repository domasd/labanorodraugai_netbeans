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
  `PointsQuantity` DECIMAL(30,2) ZEROFILL NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `Email_UNIQUE` (`Email` ASC),
  INDEX `Status` (`Status` ASC));

-- test data for account seed
INSERT INTO `labanorodraugai_db`.`account`
(`Password`, `Name`, `Lastname`, `Description`, `Image`, `Status`, `Email`, `FB_URL`, `PointsQuantity`) VALUES 
('9005c954a30e7a71d5cf8744e7b', 'Labanoro1', 'Draugai1', 'Test Description', null, 0, 'vytautas.labanoro1@gmail.com', 'https://www.facebook.com/ddziaugys',100),
('77cf552a2fd4c58b4154a245779d19c', 'Labanoro2', 'Draugai2', 'Test Description', null, 0, 'vytautas.labanoro2@gmail.com', 'https://www.facebook.com/ddziaugys',100),
('202cb962ac5975b964b7152d234b70', 'TestName', 'TestLastname', 'Test Description', null, 1, 'test@test.test', 'https://www.facebook.com/ddziaugys',0);

INSERT INTO `labanorodraugai_db`.`account`
(`Password`, `Name`, `Lastname`, `Description`, `Image`, `Status`, `Email`, `FB_URL`, `PointsQuantity`) 
VALUES ('cc175b9c0f1b6a831c399e269772661', 'a', 'TestLastname', 'Test Description', null, 0, 'a@a.a', 'https://www.facebook.com/serg.filon',0);

INSERT INTO `labanorodraugai_db`.`account`
(`Password`, `Name`, `Lastname`, `Description`, `Image`, `Status`, `Email`, `PointsQuantity`) 
VALUES ('202cb962ac5975b964b7152d234b70', 'User', 'B', 'Test Description2', null, 0, 'userb@somemail.com',100);

INSERT INTO `labanorodraugai_db`.`account`
(`Password`, `Name`, `Lastname`, `Description`, `Image`, `Status`, `Email`, `PointsQuantity`) 
VALUES ('202cb962ac5975b964b7152d234b70', 'User', 'C', 'Test Description3', null, 0, 'userc@somemail.com',100);

INSERT INTO `labanorodraugai_db`.`account`
(`Password`, `Name`, `Lastname`, `Description`, `Image`, `Status`, `Email`, `PointsQuantity`) 
VALUES ('202cb962ac5975b964b7152d234b70', 'User', 'D', 'Test Description4', null, 0, 'userd@somemail.com',100);

INSERT INTO `labanorodraugai_db`.`account`
(`Password`, `Name`, `Lastname`, `Description`, `Image`, `Status`, `Email`, `PointsQuantity`) 
VALUES ('202cb962ac5975b964b7152d234b70', 'User', 'E', 'Test Description5', null, 0, 'usere@somemail.com',100);


