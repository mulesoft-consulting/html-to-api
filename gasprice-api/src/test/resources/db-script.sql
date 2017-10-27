use <db_name>;

drop TABLE `FleetFuelCost`;

CREATE TABLE `FleetFuelCost` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `from_date` datetime DEFAULT NULL,
  `to_date` datetime DEFAULT NULL,
  `fuel_cost_per_gallon` double(11,7) DEFAULT NULL,
  `location` varchar(70) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
