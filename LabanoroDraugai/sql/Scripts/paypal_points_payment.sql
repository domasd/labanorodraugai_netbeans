CREATE TABLE `labanorodraugai_db`.`paypal_points_payment` (
	`GeneratedId` VARCHAR(36) NOT NULL,
	`AccountId` INT UNSIGNED NOT NULL,
	`PointsAmount` INT NOT NULL,
	`TotalCost` DECIMAL(30,2) NOT NULL,
	`TransferDateTime` DATETIME,
	PRIMARY KEY (GeneratedId),
	CONSTRAINT `paypal_points_accountId`
		FOREIGN KEY (`AccountId`)
		REFERENCES `labanorodraugai_db`.`account` (`Id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE);