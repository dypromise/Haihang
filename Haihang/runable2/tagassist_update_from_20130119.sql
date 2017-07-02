--use `tagassist`;

ALTER TABLE `log_checking` MODIFY `time` DATETIME NOT NULL;
ALTER TABLE `log_identification` MODIFY `time` DATETIME NOT NULL;
ALTER TABLE `log_tracking` MODIFY `time` DATETIME NOT NULL;

ALTER TABLE `checktunnel` DROP FOREIGN KEY `checktunnel_ibfk_1`;
ALTER TABLE `checktunnel` ADD CONSTRAINT `checktunnel_ibfk_1` FOREIGN KEY (`uuid`) REFERENCES `device` (`uuid`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `mobilereader` DROP FOREIGN KEY `mobilereader_ibfk_1`;
ALTER TABLE `mobilereader` ADD CONSTRAINT `mobilereader_ibfk_1` FOREIGN KEY (`uuid`) REFERENCES `device` (`uuid`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `tracktunnel` DROP FOREIGN KEY `tracktunnel_ibfk_1`;
ALTER TABLE `tracktunnel` ADD CONSTRAINT `tracktunnel_ibfk_1` FOREIGN KEY (`uuid`) REFERENCES `device` (`uuid`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `wristband` DROP FOREIGN KEY `wristband_ibfk_1`;
ALTER TABLE `wristband` ADD CONSTRAINT `wristband_ibfk_1` FOREIGN KEY (`uuid`) REFERENCES `device` (`uuid`) ON DELETE CASCADE ON UPDATE CASCADE;

alter table `baggage` add column `destination` varchar(255) not null after `flightId`;
alter table `baggage` add column `lastUpdatedTime` bigint(20) default null after `status`;
alter table `baggage` add column `createdTime` bigint(20) default null after `status`;
alter table `baggage` add column `carriageId` varchar(255) default null after `status`;
alter table `baggage` add column `revision` int(11) not null default 0;

alter table `flight` add column `lastUpdatedTime` bigint(20) default null after `status`;
alter table `flight` add column `createdTime` bigint(20) default null after `status`;
alter table `flight` add column `adt` bigint(20) default null after `flightId`;
alter table `flight` add column `edt` bigint(20)  default null after `flightId`;
alter table `flight` add column `sdt` bigint(20)  default null after `flightId`;
alter table `flight` add column `via` varchar(255) default null after `flightId`;
alter table `flight` add column `destination` varchar(255) default null after `flightId`;
alter table `flight` add column `origin` varchar(255) default null after `flightId`;

CREATE TABLE IF NOT EXISTS `carriage` (
  `uuid` varchar(255) NOT NULL,
  `number` varchar(255) NOT NULL,
  `netWeight` int(11) DEFAULT NULL,
  `flightId` varchar(255) DEFAULT NULL,
  `destination` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `registTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table `checktunnel` add column `boundCarriageId` varchar(255) default null after `boundFlightId`;

alter table `device` add column `lastActiveTime` bigint(20) default null after `registTime`;
alter table `device` add column `lastActiveIp` varchar(255) default null after `registTime`;
alter table `device` add column `macAddress` varchar(255) default null after `registTime`;
alter table `device` add column `hostname` varchar(255) default null after `registTime`;
alter table `device` add column `softwareVersion` varchar(255) default null after `registTime`;

CREATE TABLE IF NOT EXISTS `properties` (
  `name` varchar(255) NOT NULL DEFAULT '',
  `value` text,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `role` (
  `pkid` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(225) DEFAULT NULL,
  `name` varchar(225) NOT NULL,
  `permissions` text,
  PRIMARY KEY (`pkid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
