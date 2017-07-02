-- phpMyAdmin SQL Dump
-- version 3.4.5deb1
-- http://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2013 年 01 月 15 日 01:57
-- 服务器版本: 5.1.66
-- PHP 版本: 5.3.6-13ubuntu3.9

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 数据库: `tagassist`
--

-- --------------------------------------------------------

--
-- 表的结构 `baggage`
--

CREATE TABLE IF NOT EXISTS `baggage` (
  `pkid` int(11) NOT NULL AUTO_INCREMENT,
  `number` varchar(255) NOT NULL,
  `epc` varchar(255) NOT NULL,
  `flightId` varchar(255) NOT NULL,
  `passenger` varchar(255) NOT NULL,
  `weight` int(11) DEFAULT NULL,
  `bclass` varchar(255) DEFAULT NULL,
  `damageCode` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `lastTracedDevice` varchar(255) DEFAULT NULL,
  `lastTracedTime` bigint(20) DEFAULT NULL,
  `lastOperator` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pkid`),
  UNIQUE KEY `number` (`number`),
  UNIQUE KEY `epc` (`epc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- 触发器 `baggage`
--
DROP TRIGGER IF EXISTS `trigger_on_baggage_insert`;
DELIMITER //
CREATE TRIGGER `trigger_on_baggage_insert` AFTER INSERT ON `baggage`
 FOR EACH ROW begin
    insert into `log_tracing`(`baggageNumber`,`status`,`device`,`time`,`operator`)
    values (NEW.`number`,NEW.`status`,NEW.`lastTracedDevice`,NEW.`lastTracedTime`,NEW.`lastOperator`);
  end
//
DELIMITER ;
DROP TRIGGER IF EXISTS `trigger_on_baggage_update`;
DELIMITER //
CREATE TRIGGER `trigger_on_baggage_update` AFTER UPDATE ON `baggage`
 FOR EACH ROW begin
    if NEW.status <> OLD.status then
      insert into `log_tracing`(`baggageNumber`,`status`,`device`,`time`,`operator`)
      values (NEW.`number`,NEW.`status`,NEW.`lastTracedDevice`,NEW.`lastTracedTime`,NEW.`lastOperator`);
    end if;
  end
//
DELIMITER ;

-- --------------------------------------------------------

--
-- 表的结构 `checktunnel`
--

CREATE TABLE IF NOT EXISTS `checktunnel` (
  `uuid` varchar(255) NOT NULL,
  `boundFlightId` varchar(255) DEFAULT NULL,
  `locationParam1` varchar(255) DEFAULT NULL,
  `locationParam2` int(11) DEFAULT NULL,
  `locationParam3` float DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `checktunnel`
--

INSERT INTO `checktunnel` (`uuid`, `boundFlightId`, `locationParam1`, `locationParam2`, `locationParam3`) VALUES
('1b5707e5-9f1b-4dbb-bf17-47f088541984', 'HU7182/07AUG2012', NULL, NULL, NULL),
('8442d8ed-35a0-4552-bee5-34d2a13432a8', 'HU7182/06AUG2012', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- 表的结构 `device`
--

CREATE TABLE IF NOT EXISTS `device` (
  `uuid` varchar(255) NOT NULL,
  `component` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  `registered` tinyint(1) NOT NULL,
  `registTime` bigint(20) DEFAULT NULL,
  `remark` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `device`
--

INSERT INTO `device` (`uuid`, `component`, `name`, `status`, `registered`, `registTime`, `remark`) VALUES
('00195B2FF6070108322B0510020B1800', 7, 'MobileReader', 1, 1, 1333964044882, NULL),
('0062b7e1-a1ba-8d8a-05ad5161f15e', 3, '10', 2, 1, 1343803315304, NULL),
('02961150-52d0-11e1-b86c-0800200c9a66', 4, 'Administrator', 1, 0, NULL, NULL),
('1062b7e1-a1ba-8d8a-05ad5161f15e', 3, '1', 2, 1, 1332771618557, NULL),
('1b5707e5-9f1b-4dbb-bf17-47f088541984', 6, '复核通道2', 2, 1, 1337078579689, '无'),
('2062b7e1-a1ba-8d8a-05ad5161f15e', 3, '2', 2, 1, 1343978998337, NULL),
('3062b7e1-a1ba-8d8a-05ad5161f15e', 3, '3', 2, 1, 1343978996561, NULL),
('4062b7e1-a1ba-8d8a-05ad5161f15e', 3, '4', 2, 1, 1343803306444, NULL),
('4DF0F5AD-8D71-2E9A-2CB1-1AE4BA3D5490', 8, '虚拟化显示', 1, 1, 1343012982521, '无'),
('5062b7e1-a1ba-8d8a-05ad5161f15e', 3, '5', 2, 1, 1343803311384, NULL),
('6062b7e1-a1ba-8d8a-05ad5161f15e', 3, '6', 2, 1, 1343803285363, NULL),
('6b9c6e4e-595a-4337-8875-26d351861f19', 5, '????2', 2, 1, 1337078683897, '?'),
('7062b7e1-a1ba-8d8a-05ad5161f15e', 3, '7', 1, 1, 1343022746471, NULL),
('71ae0ff0-5e21-11e1-b86c-0800200c9a66', 1, 'Flight Proxy', 1, 0, NULL, '无'),
('8062b7e1-a1ba-8d8a-05ad5161f15e', 3, '8', 2, 1, 1343803293830, NULL),
('8442d8ed-35a0-4552-bee5-34d2a13432a8', 6, '复核通道1', 2, 1, 1337078573095, '无'),
('8562b7e1-a1ba-4bf0-8d8a-05ad5161f15e', 9, '242', 1, 1, 1336907163175, NULL),
('8ef11e48-a635-4655-94a6-85c0c1295250', 5, '????1', 1, 1, 1337078676237, '?'),
('9062b7e1-a1ba-8d8a-05ad5161f15e', 3, '9', 2, 1, 1343803272705, NULL),
('91a82170-5c8b-11e1-b86c-0800200c9a66', 2, 'Data Center', 1, 0, NULL, NULL),
('dced8bbf-9d8b-4126-952b-cea864ab7d29', 7, '手持设备1', 1, 1, 1333000947330, '');

-- --------------------------------------------------------

--
-- 表的结构 `facility`
--

CREATE TABLE IF NOT EXISTS `facility` (
  `uuid` varchar(255) NOT NULL,
  `type` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  `registTime` bigint(20) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `locationParam1` varchar(255) DEFAULT NULL,
  `locationParam2` float DEFAULT NULL,
  `locationParam3` float DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `flight`
--

CREATE TABLE IF NOT EXISTS `flight` (
  `pkid` int(11) NOT NULL AUTO_INCREMENT,
  `flightId` varchar(255) NOT NULL,
  `departTime` bigint(20) NOT NULL,
  `arriveTime` bigint(20) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`pkid`),
  UNIQUE KEY `flightId` (`flightId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `log_checking`
--

CREATE TABLE IF NOT EXISTS `log_checking` (
  `pkid` int(11) NOT NULL AUTO_INCREMENT,
  `checkTunnelId` varchar(255) NOT NULL,
  `epc` varchar(255) NOT NULL,
  `rssi` float DEFAULT NULL,
  `time` date NOT NULL,
  `direction` int(11) NOT NULL,
  `carriageId` varchar(255) NOT NULL,
  PRIMARY KEY (`pkid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `log_identification`
--

CREATE TABLE IF NOT EXISTS `log_identification` (
  `pkid` int(11) NOT NULL AUTO_INCREMENT,
  `wristbandId` varchar(255) NOT NULL,
  `epc` varchar(255) NOT NULL,
  `rssi` float DEFAULT NULL,
  `time` date NOT NULL,
  PRIMARY KEY (`pkid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `log_tracing`
--

CREATE TABLE IF NOT EXISTS `log_tracing` (
  `pkid` int(11) NOT NULL AUTO_INCREMENT,
  `baggageNumber` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  `device` varchar(255) DEFAULT NULL,
  `operator` varchar(255) DEFAULT NULL,
  `time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`pkid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `log_tracking`
--

CREATE TABLE IF NOT EXISTS `log_tracking` (
  `pkid` int(11) NOT NULL AUTO_INCREMENT,
  `trackTunnelId` varchar(255) NOT NULL,
  `epc` varchar(255) NOT NULL,
  `rssi` float DEFAULT NULL,
  `time` date NOT NULL,
  `poolId` varchar(255) NOT NULL,
  `distance` float NOT NULL,
  PRIMARY KEY (`pkid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `mobilereader`
--

CREATE TABLE IF NOT EXISTS `mobilereader` (
  `uuid` varchar(255) NOT NULL,
  `boundFlightId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `mobilereader`
--

INSERT INTO `mobilereader` (`uuid`, `boundFlightId`) VALUES
('00195B2FF6070108322B0510020B1800', 'HU7182/07AUG2012'),
('dced8bbf-9d8b-4126-952b-cea864ab7d29', NULL);

-- --------------------------------------------------------

--
-- 表的结构 `notification`
--

CREATE TABLE IF NOT EXISTS `notification` (
  `uuid` varchar(255) NOT NULL,
  `content` varchar(255) NOT NULL,
  `time` bigint(20) NOT NULL,
  `expire` bigint(20) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `notification`
--

INSERT INTO `notification` (`uuid`, `content`, `time`, `expire`, `deleted`) VALUES
('06d8a61e-8238-49c5-80a8-941250fe8cdf', 'test2', 1342578894413, 1342582491000, 1),
('15c462db-ebfc-472a-a370-0eabf4f8d280', '热烈欢迎上级领导视察检验系统运行情况！', 1342774117300, 1343728084000, 0),
('2357ff33-bf16-4c14-b97a-773e730c4ab1', '测试中文的，中文的，中文的，中文的，中文的，中文的，中文的，中文的，中文的。', 1342764858897, 1342768428000, 1),
('2d94c81a-2ea6-4bdc-a5ee-ebd01d688d3b', '少年是个好小伙', 1343630876474, 1343634460000, 1),
('34b63f06-f064-4621-adec-bf36292f5302', 'test', 1342578889030, 1342582477000, 1),
('58d0982d-8e58-4d99-b0af-a26fd48e083a', '支援基於射頻識別技術的電子物流網絡互聯互通之可信解決方案', 1342580031701, 1342587226000, 0),
('5b21431b-0adb-4b7f-ae62-b29420ff0d9e', '热烈欢迎领导莅临视察系统运行情况!', 1343781891520, 1345027418000, 0),
('8e671519-559f-411d-86ee-3fa35dbb087b', '祝贺"签信通v1.0"成功上线运行一个月零两天！', 1344232253126, 1346395765000, 0),
('9bf1ad47-946b-4d0e-bfa1-4fd2bdb2f039', 'Trust Solution for RFID Enabled Interoperable E-logistics', 1342579984506, 1342853550000, 1),
('a6a6f1f8-0246-4953-9436-234bf7345895', 'asdfghjk', 1357868101352, 1357871696000, 0),
('b3fe673a-e06f-4caf-89fa-00aadee9033f', '支援基於射頻識別技術的電子物流網絡互聯互通之可信解決方案支援基於射頻識別技術的電子物流網絡互聯互通之可信解決方案', 1342580329143, 1342583925000, 0),
('e7f93e9c-c9f5-48b6-bf4c-7dc735ddc996', '基于射频识别技术的航空行李人工分拣辅助系统测试进行中...', 1342774011898, 1343727974000, 0),
('f4477555-d8b3-4989-811d-fe5f1be02451', '最新的消息出来啦', 1343362021479, 1343365611000, 0),
('ff6535be-5495-48e1-8458-f7349c7a1ec7', '测试Trust Solution for RFID Enabled Interoperable E-logistics 001!!!', 1342761345807, 1342764895000, 0);

-- --------------------------------------------------------

--
-- 表的结构 `tracktunnel`
--

CREATE TABLE IF NOT EXISTS `tracktunnel` (
  `uuid` varchar(255) NOT NULL,
  `locationParam1` varchar(255) DEFAULT NULL,
  `locationParam2` float DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `tracktunnel`
--

INSERT INTO `tracktunnel` (`uuid`, `locationParam1`, `locationParam2`) VALUES
('6b9c6e4e-595a-4337-8875-26d351861f19', '8cf1e5f0-c30a-434d-b97b-b19ea08c671f', 675),
('8ef11e48-a635-4655-94a6-85c0c1295250', '', 2647);

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `pkid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`pkid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`pkid`, `username`, `password`, `role`, `status`) VALUES
(1, 'admin', 'admin', 1, 1),
(2, 'staff', 'staff', 2, 1),
(3, '1', '1', 2, 1);

-- --------------------------------------------------------

--
-- 表的结构 `wristband`
--

CREATE TABLE IF NOT EXISTS `wristband` (
  `uuid` varchar(255) NOT NULL,
  `subStatus` tinyint(1) DEFAULT NULL,
  `battery` int(11) DEFAULT NULL,
  `rfidStatus` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `wristband`
--

INSERT INTO `wristband` (`uuid`, `subStatus`, `battery`, `rfidStatus`) VALUES
('0062b7e1-a1ba-8d8a-05ad5161f15e', 0, 100, 0),
('1062b7e1-a1ba-8d8a-05ad5161f15e', 0, 100, 0),
('2062b7e1-a1ba-8d8a-05ad5161f15e', 0, 100, 0),
('3062b7e1-a1ba-8d8a-05ad5161f15e', 0, 100, 0),
('4062b7e1-a1ba-8d8a-05ad5161f15e', 0, 100, 0),
('5062b7e1-a1ba-8d8a-05ad5161f15e', 0, 100, 0),
('6062b7e1-a1ba-8d8a-05ad5161f15e', 0, 100, 0),
('7062b7e1-a1ba-8d8a-05ad5161f15e', 1, 81, 0),
('8062b7e1-a1ba-8d8a-05ad5161f15e', 0, 100, 0),
('9062b7e1-a1ba-8d8a-05ad5161f15e', 0, 100, 0);

-- --------------------------------------------------------

--
-- 表的结构 `wristband_binding`
--

CREATE TABLE IF NOT EXISTS `wristband_binding` (
  `pkid` int(11) NOT NULL AUTO_INCREMENT,
  `wristbandId` varchar(255) NOT NULL,
  `flightId` varchar(255) NOT NULL,
  PRIMARY KEY (`pkid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=51 ;

--
-- 转存表中的数据 `wristband_binding`
--

INSERT INTO `wristband_binding` (`pkid`, `wristbandId`, `flightId`) VALUES
(24, '6062b7e1-a1ba-8d8a-05ad5161f15e', 'HU7251/02AUG2012'),
(31, '7062b7e1-a1ba-8d8a-05ad5161f15e', 'HU7111/19JUL2012'),
(44, '4062b7e1-a1ba-8d8a-05ad5161f15e', 'HU7251/03AUG2012'),
(45, '4062b7e1-a1ba-8d8a-05ad5161f15e', 'HU7187/03AUG2012'),
(49, '7062b7e1-a1ba-8d8a-05ad5161f15e', 'HU491/06AUG2012'),
(50, '7062b7e1-a1ba-8d8a-05ad5161f15e', 'HU7805/06AUG2012');

--
-- 限制导出的表
--

--
-- 限制表 `checktunnel`
--
ALTER TABLE `checktunnel`
  ADD CONSTRAINT `checktunnel_ibfk_1` FOREIGN KEY (`uuid`) REFERENCES `device` (`uuid`) ON UPDATE CASCADE;

--
-- 限制表 `mobilereader`
--
ALTER TABLE `mobilereader`
  ADD CONSTRAINT `mobilereader_ibfk_1` FOREIGN KEY (`uuid`) REFERENCES `device` (`uuid`) ON UPDATE CASCADE;

--
-- 限制表 `tracktunnel`
--
ALTER TABLE `tracktunnel`
  ADD CONSTRAINT `tracktunnel_ibfk_1` FOREIGN KEY (`uuid`) REFERENCES `device` (`uuid`) ON UPDATE CASCADE;

--
-- 限制表 `wristband`
--
ALTER TABLE `wristband`
  ADD CONSTRAINT `wristband_ibfk_1` FOREIGN KEY (`uuid`) REFERENCES `device` (`uuid`) ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
