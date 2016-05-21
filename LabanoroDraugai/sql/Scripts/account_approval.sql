CREATE TABLE `labanorodraugai_db`.`account_approval` (
	`CandidateId` INT UNSIGNED NOT NULL,
	`ApproverId` INT UNSIGNED NOT NULL,
	`ApprovalDate` DATE,
	`GeneratedId` VARCHAR(36) NOT NULL,
	PRIMARY KEY (`CandidateId`, `ApproverId`),
	UNIQUE INDEX `GeneratedId_UNIQUE` (`GeneratedId` ASC),
	CONSTRAINT `CandidateId`
		FOREIGN KEY (`CandidateId`)
		REFERENCES `labanorodraugai_db`.`account` (`Id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	CONSTRAINT `ApproverId`
		FOREIGN KEY (`ApproverId`)
		REFERENCES `labanorodraugai_db`.`account` (`Id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE);	