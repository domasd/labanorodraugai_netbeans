CREATE TABLE `labanorodraugai_db`.`reservation_groups` (
  `GroupNumber` INT(11) NOT NULL,
  `StartOfReservationDate` DATE NOT NULL,
  PRIMARY KEY (`GroupNumber`));

ALTER TABLE `labanorodraugai_db`.`account` 
ADD COLUMN `ReservationGroup` INT(11) NULL,
ADD INDEX `ReservationGroupFK_idx` (`ReservationGroup` ASC);

ALTER TABLE `labanorodraugai_db`.`account` 
ADD CONSTRAINT `ReservationGroupFK`
  FOREIGN KEY (`ReservationGroup`)
  REFERENCES `labanorodraugai_db`.`reservation_groups` (`GroupNumber`)
  ON DELETE SET NULL
  ON UPDATE SET NULL;
