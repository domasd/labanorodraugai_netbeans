CREATE TABLE `labanorodraugai_db`.`config` (
    `Id` INT(10) UNSIGNED PRIMARY KEY DEFAULT 1,
    `MinApprovalsRequired` INT(10) UNSIGNED NOT NULL DEFAULT 2,
    `CustomRegistrationFields` TEXT NOT NULL,
    `ReservationProcessBeginDate` DATE NOT NULL,
    `MaxNumberOfAccountsInOneGroup` INT NOT NULL,
    `MaxReservationDaysLength` INT NOT NULL,
    `FirstDateAbleToReserve` DATE NOT NULL,
    `LastDateAbleToReserve` DATE NOT NULL,
    `SeasonDays` INT NOT NULL DEFAULT 365,
    `MembershipPrice` INT NOT NULL

);

-- Default configuration
INSERT INTO `labanorodraugai_db`.`config` VALUES(1,5, '["Telefonas","Adresas"]','2016-04-01',2,4,'2016-06-01','2016-08-31',365, 500);

