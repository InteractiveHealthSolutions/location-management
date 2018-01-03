--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
CREATE TABLE `location` (
  `locationId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `shortName` varchar(45) DEFAULT NULL,
  `fullName` varchar(45) DEFAULT NULL,
  `parentLocation` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `otherIdentifier` varchar(45) DEFAULT NULL,
  `locationType` int(11) NOT NULL,
  `address1` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `address3` varchar(255) DEFAULT NULL,
  `address4` varchar(255) DEFAULT NULL,
  `address5` varchar(255) DEFAULT NULL,
  `address6` varchar(255) DEFAULT NULL,
  `cityVillage` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `countyDistrict` varchar(255) DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `lastEditedDate` datetime DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `voided` tinyint(4) NOT NULL DEFAULT '0',
  `voidReason` varchar(255) DEFAULT NULL,
  `dateVoided` datetime DEFAULT NULL,
  `voidedBy` int(11) DEFAULT NULL,
  `postalCode` varchar(255) DEFAULT NULL,
  `stateProvince` varchar(255) DEFAULT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `lastEditedByUserId` int(11) DEFAULT NULL,
  PRIMARY KEY (`locationId`),
  UNIQUE KEY `location_name_UNIQUE` (`name`),
  KEY `LocationTypeLocationType_idx` (`locationType`),
  KEY `location_lastEditedByUserId_user_mappedId_FK` (`lastEditedByUserId`),
  KEY `location_createdByUserId_user_mappedId_FK` (`createdByUserId`),
  KEY `location_parentLocation_location_locationId_FK` (`parentLocation`),
  CONSTRAINT `loaction_locationTypeId_locationType_locationTypeId_FK` FOREIGN KEY (`locationType`) REFERENCES `locationtype` (`locationTypeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `location_createdByUserId_user_mappedId_FK` FOREIGN KEY (`createdByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `location_lastEditedByUserId_user_mappedId_FK` FOREIGN KEY (`lastEditedByUserId`) REFERENCES `user` (`mappedId`),
  CONSTRAINT `location_parentLocation_location_locationId_FK` FOREIGN KEY (`parentLocation`) REFERENCES `location` (`locationId`) ON DELETE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=383845 DEFAULT CHARSET=latin1;

--
-- Table structure for table `locationattribute`
--

DROP TABLE IF EXISTS `locationattribute`;
CREATE TABLE `locationattribute` (
  `locationAttributeId` int(11) NOT NULL AUTO_INCREMENT,
  `locationAttributeTypeId` int(11) NOT NULL,
  `locationId` int(11) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  `typeName` varchar(50) DEFAULT NULL,
  `typeValue1` varchar(50) DEFAULT NULL,
  `typeValue2` varchar(50) DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `lastEditedByUserId` int(11) DEFAULT NULL,
  `lastEditedDate` datetime DEFAULT NULL,
  PRIMARY KEY (`locationAttributeId`),
  UNIQUE KEY `locationAttributeId_UNIQUE` (`locationAttributeId`),
  KEY `locationattribute_locationId_location_locationId_FK` (`locationId`),
  KEY `locattr_locAttrTypeId_locAttrType_locAttrTypeId_FK` (`locationAttributeTypeId`),
  CONSTRAINT `locationattribute_locationId_location_locationId_FK` FOREIGN KEY (`locationId`) REFERENCES `location` (`locationId`),
  CONSTRAINT `locattr_locAttrTypeId_locAttrType_locAttrTypeId_FK` FOREIGN KEY (`locationAttributeTypeId`) REFERENCES `locationattributetype` (`locationAttributeTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Table structure for table `locationattributetype`
--

DROP TABLE IF EXISTS `locationattributetype`;
CREATE TABLE `locationattributetype` (
  `locationAttributeTypeId` int(11) NOT NULL AUTO_INCREMENT,
  `attributeName` varchar(255) NOT NULL,
  `category` varchar(255) DEFAULT NULL,
  `dataType` varchar(45) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `displayName` varchar(255) DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `lastEditedByUserId` int(11) DEFAULT NULL,
  `lastEditedDate` datetime DEFAULT NULL,
  PRIMARY KEY (`locationAttributeTypeId`),
  UNIQUE KEY `locationAttributeTypeId_UNIQUE` (`locationAttributeTypeId`),
  UNIQUE KEY `attributeName_UNIQUE` (`attributeName`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;

--
-- Table structure for table `locationtype`
--

DROP TABLE IF EXISTS `locationtype`;
CREATE TABLE `locationtype` (
  `locationTypeId` int(11) NOT NULL AUTO_INCREMENT,
  `typeName` varchar(45) NOT NULL,
  `level` int(11) DEFAULT NULL,
  `isEditable` tinyint(1) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `createdByUserId` int(11) DEFAULT NULL,
  `lastEditedByUserId` int(11) DEFAULT NULL,
  `lastEditedDate` datetime DEFAULT NULL,
  PRIMARY KEY (`locationTypeId`),
  UNIQUE KEY `typeName_UNIQUE` (`typeName`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

