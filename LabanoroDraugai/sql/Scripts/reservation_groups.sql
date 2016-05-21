CREATE TABLE `labanorodraugai_db`.`reservation_groups` (
  `GroupNumber` INT(11) NOT NULL,
  `StartOfReservationDate` DATE NOT NULL,
  PRIMARY KEY (`GroupNumber`));

INSERT INTO `labanorodraugai_db`.`reservation_groups` (`GroupNumber`, `StartOfReservationDate`) VALUES ('0', '2016-04-01');
INSERT INTO `labanorodraugai_db`.`reservation_groups` (`GroupNumber`, `StartOfReservationDate`) VALUES ('1', '2016-07-01');

