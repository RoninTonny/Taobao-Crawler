#Replace **website.xml file path in line 38-56 , UrlCrawlerThreadTest.java.
#Replace **getter.xml file path in line 76 , UrlReceive.java.

#Add jars at ./jar .

#Use CrawlerGUI.java and ServerGUI.java to start.

#Change mysql password of root to "toor".

#DateBase Create:
-------------------------

CREATE DATABASE `zr` /*!40100 DEFAULT CHARACTER SET utf8 */;

CREATE TABLE `attributes` (
  `ID` varchar(100) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Price` double DEFAULT NULL,
  `ShopName` varchar(100) DEFAULT NULL,
  `SaleVolume` float DEFAULT NULL,
  `PraiseNum` float DEFAULT NULL,
  `MediumNum` float DEFAULT NULL,
  `BadNum` float DEFAULT NULL,
  `Origin` varchar(100) DEFAULT NULL,
  `Category` varchar(10) DEFAULT NULL,
  `Smallcate` varchar(10) DEFAULT NULL,
  `Sore` float DEFAULT NULL,
  `Source` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-------------------------