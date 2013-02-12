
CREATE TABLE IF NOT EXISTS `reports` (
  `weatherDate` datetime NOT NULL,
  `location` varchar(256) NOT NULL,
  `temp` float NOT NULL,
  `windSpeed` float NOT NULL,
  `windDir` int(11) NOT NULL,
  `cloudCover` float NOT NULL,
  UNIQUE KEY `weatherDate` (`weatherDate`,`location`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `forecasts` (
  `weatherDate` datetime NOT NULL,
  `origin` datetime NOT NULL,
  `location` varchar(256) NOT NULL,
  `temp` float NOT NULL,
  `windSpeed` float NOT NULL,
  `windDir` int(11) NOT NULL,
  `cloudCover` float NOT NULL,
  UNIQUE KEY `weatherDate` (`weatherDate`,`origin`,`location`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;