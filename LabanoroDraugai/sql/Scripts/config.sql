CREATE TABLE `labanorodraugai_db`.`config` (
    `Id` INT(10) UNSIGNED PRIMARY KEY DEFAULT 1,
    `MinApprovalsRequired` INT(10) UNSIGNED NOT NULL DEFAULT 2,
    `CustomRegistrationFields` TEXT NOT NULL
);

-- Default configuration
INSERT INTO `labanorodraugai_db`.`config` VALUES(1,5, '["Education","Address"]');
	