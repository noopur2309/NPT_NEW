
CREATE DATABASE IF NOT EXISTS DwdmNpt;
--DROP database IF EXISTS DwdmNpt;
--CREATE database DwdmNpt;
use DwdmNpt;


CREATE TABLE IF NOT EXISTS `DwdmNpt`.`Network` (
  `NetworkId` INT NOT NULL,
  `SubNetworkId`  VARCHAR(100) NULL,
  `NetworkName` VARCHAR(45) NOT NULL,
  `Topology` VARCHAR(100) NULL,
  `Area` VARCHAR(45) NULL,
  `ServiceProvider` VARCHAR(100) NULL,
  `SAPI` VARCHAR(45) NULL,
  `NetworkIdBrownField` INT NULL,
  `NetworkUpdateDate` DATETIME NULL,
  `NetworkBrownFieldUpdateDate` DATETIME NULL,
  `UserName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`NetworkId`,`UserName`));
  
  CREATE TABLE IF NOT EXISTS `DwdmNpt`.`MapData` (
  `NetworkId` INT NOT NULL,
  `Map` MEDIUMTEXT NOT NULL,
  PRIMARY KEY (`NetworkId`),
  UNIQUE INDEX MapIndex (Map (80),NetworkId),
  CONSTRAINT `fk_Map_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
    CREATE TABLE IF NOT EXISTS `DwdmNpt`.`UserList` (
  `UserName` VARCHAR(45) NOT NULL,
  `Password` VARCHAR(45) NULL,
  `Privilege` VARCHAR(45) NULL,
  PRIMARY KEY (`UserName`));
  
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`Node` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `StationName` VARCHAR(45) NULL,
  `SiteName` VARCHAR(45) NULL,
  `NodeType` INT NULL,
  `NodeSubType` INT NULL,
  `Degree` INT NULL,
  `Ip` VARCHAR(45) NULL,
  `IsGne` INT NULL,
  `VlanTag` INT NULL,
  `EmsSubnet` VARCHAR(45) NULL,
  `EmsGateway` VARCHAR(45) NULL,
  `IpV6Add` VARCHAR(100) NULL,
  `Capacity` VARCHAR(45) NULL,
  `Direction` INT NULL,
   `OpticalReach` VARCHAR(45) NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`),
  CONSTRAINT `fk_Node_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE IF NOT EXISTS `DwdmNpt`.`Link` (
  `NetworkId` INT NOT NULL,
  `LinkId` INT NOT NULL,
  `SrcNode` INT NULL,
  `DestNode` INT NULL,
  `Colour` INT NULL,
  `MetricCost` INT NULL,
  `Length` INT NULL,
  `SpanLoss` FLOAT NULL,
  `Capacity` INT NULL,
  `FibreType` INT NULL,
  `SrlgId` INT NULL,
  `NSplices` INT NULL,
  `LossPerSplice` FLOAT NULL,
  `NConnector` INT NULL,
  `LossPerConnector` FLOAT NULL,
  `CalculatedSpanLoss` FLOAT NULL,
  `SpanLossCoff` FLOAT NULL,
  `CdCoff` FLOAT NULL,
  `Cd` FLOAT NULL,
  `PmdCoff` FLOAT NULL,
  `Pmd` FLOAT NULL,
  `SrcNodeDirection` VARCHAR(45) NULL,
  `DestNodeDirection` VARCHAR(45) NULL,
  `OMSProtection` VARCHAR(45) NULL,
  `LinkType` VARCHAR(45) NOT NULL Default 'DEFAULT (PA/BA)',
  PRIMARY KEY (`NetworkId`, `LinkId`),
  CONSTRAINT `fk_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

    CREATE TABLE IF NOT EXISTS `DwdmNpt`.`Topology` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `Dir1` INT NULL,
  `Dir2` INT NULL,
  `Dir3` INT NULL,
  `Dir4` INT NULL,
  `Dir5` INT NULL,
  `Dir6` INT NULL,
  `Dir7` INT NULL,
  `Dir8` INT NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`),
  CONSTRAINT `fk_Topology_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`Demand` (
  `NetworkId` int(11) NOT NULL,
  `DemandId` int(11) NOT NULL,
  `SrcNodeId` int(11) DEFAULT NULL,
  `DestNodeId` int(11) DEFAULT NULL,
  `RequiredTraffic` float DEFAULT NULL,
  `ProtectionType` varchar(100) DEFAULT NULL,
  `ColourPreference` varchar(100) DEFAULT NULL,
  `CircuitSet` varchar(100) DEFAULT NULL,
  `PathType` varchar(45) DEFAULT NULL,
  `LineRate` varchar(45) DEFAULT NULL,
  `ClientProtectionType` varchar(100) DEFAULT NULL,
  `ChannelProtection` varchar(45) DEFAULT NULL,
  `ClientProtection` varchar(45) DEFAULT NULL,
  `LambdaBlocking` varchar(45) DEFAULT NULL,
  `NodeInclusion` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`DemandId`,`NetworkId`),
  KEY `fk_Demand_NetworkId_idx` (`NetworkId`),
  CONSTRAINT `fk_Demand_NetworkId` FOREIGN KEY (`NetworkId`) 
  REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
  ON DELETE CASCADE
  ON UPDATE CASCADE);
    
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`DemandInMemoryTable` (
  `NetworkId` INT NOT NULL,
  `DemandId` INT NOT NULL,
  `SrcNodeId` INT NULL,
  `DestNodeId` INT NULL,
  `RequiredTraffic` FLOAT NULL,
  `ProtectionType` VARCHAR(100) NULL,
  `ColourPreference` VARCHAR(100) NULL,
  `CircuitSet` VARCHAR(100) NULL,
  `PathType` VARCHAR(45) NULL,
  `LineRate` VARCHAR(45) NULL,
  `ClientProtectionType` VARCHAR(100) NULL,
  `ChannelProtection` VARCHAR(45) NULL,
  `ClientProtection` varchar(45) DEFAULT NULL,
  `LambdaBlocking` varchar(45) DEFAULT NULL,
  `NodeInclusion` varchar(45) DEFAULT NULL,
   PRIMARY KEY (`DemandId`, `NetworkId`),
   FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

    
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`NetworkRoute` (
  `NetworkId` INT NOT NULL,
  `DemandId` INT NOT NULL,
  `RoutePriority` INT NOT NULL,
  `Path` VARCHAR(100),
  `PathLinkId` VARCHAR(100) NULL,
  `PathType` VARCHAR(100) NULL,
  `Traffic` FLOAT NULL,
  `WavelengthNo` INT NULL,
  `LineRate` VARCHAR(45) NULL,
  `Osnr` FLOAT NULL,
  `RegeneratorLoc` VARCHAR(100) NULL,
  `RegeneratorLocOsnr` VARCHAR(100) NULL,
  `Error` VARCHAR(200) NULL,
  `3RLocationHeadToTail` VARCHAR(45) NULL,
  `3RLocationTailToHead` VARCHAR(45) NULL,
  PRIMARY KEY (`NetworkId`, `DemandId`, `RoutePriority`),
  INDEX `fk_DemandId_NetworkRoute_idx` (`DemandId` ASC),
  CONSTRAINT `fk_DemandId_NetworkRoute`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`Circuit` (
 `NetworkId` int(11) NOT NULL,
  `CircuitId` int(11) NOT NULL,
  `QoS` int(11) NOT NULL,
  `SrcNodeId` int(11) DEFAULT NULL,
  `DestNodeId` int(11) DEFAULT NULL,
  `RequiredTraffic` float DEFAULT NULL,
  `TrafficType` varchar(100) DEFAULT NULL,
  `ProtectionType` varchar(100) DEFAULT NULL,
  `ClientProtectionType` varchar(100) DEFAULT NULL,
  `ColourPreference` varchar(100) DEFAULT NULL,
  `PathType` varchar(45) DEFAULT NULL,
  `LineRate` varchar(45) DEFAULT NULL,
  `ChannelProtection` varchar(10) DEFAULT NULL,
  `ClientProtection` varchar(45) DEFAULT NULL,
  `LambdaBlocking` varchar(45) DEFAULT NULL,
  `VendorLabel` varchar(45) DEFAULT NULL,
  `NodeInclusion` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`NetworkId`,`CircuitId`),
  CONSTRAINT `fk_Circuit_NetworkId` 
  FOREIGN KEY (`NetworkId`) 
  REFERENCES `DwdmNpt`.`Network` (`NetworkId`) 
  ON DELETE CASCADE 
  ON UPDATE CASCADE);


CREATE TABLE IF NOT EXISTS `DwdmNpt`.`CircuitInMemoryTable` (
  `NetworkId` INT NOT NULL,
  `CircuitId` INT NOT NULL,
  `QoS` int(11) NOT NULL,
  `SrcNodeId` INT NULL,
  `DestNodeId` INT NULL,
  `RequiredTraffic` FLOAT NULL,
  `TrafficType` VARCHAR(100) NULL,
  `ProtectionType` VARCHAR(100) NULL,
  `ClientProtectionType` VARCHAR(100) NULL,
  `ColourPreference` VARCHAR(100) NULL,
  `PathType` VARCHAR(45) NULL,
  `LineRate` VARCHAR(45) NULL,
  `ChannelProtection` VARCHAR(10) NULL,
  `ClientProtection` varchar(45) DEFAULT NULL,
  `LambdaBlocking` varchar(45) DEFAULT NULL,
  `VendorLabel` varchar(45) DEFAULT NULL,
  `NodeInclusion` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`NetworkId`, `CircuitId`),
  FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE=MEMORY;
    
    
 CREATE TABLE IF NOT EXISTS `DwdmNpt`.`CircuitDemandMapping` (
  `NetworkId` INT NOT NULL,
  `CircuitId` INT NOT NULL,
  `DemandId` INT NULL,
  PRIMARY KEY (`NetworkId`, `CircuitId`),
  INDEX `fk_CircuitDemandMapping_NetworkId_idx` (`NetworkId` ASC),
  CONSTRAINT `fk_CircuitDemandMapping_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`IpSchemeNode` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `LctIp` BIGINT(12) NULL,
  `RouterIp` BIGINT(12) NULL,
  `ScpIp` BIGINT(12) NULL,
  `McpIp` BIGINT(12) NULL,
  `McpSubnet` BIGINT(12) NULL,
  `McpGateway` BIGINT(12) NULL,
  `RsrvdIp1` BIGINT(12) NULL,
  `RsrvdIp2` BIGINT(12) NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`),
  CONSTRAINT `fk_IpSchemeNode_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
 CREATE TABLE IF NOT EXISTS `DwdmNpt`.`IpSchemeLink` (
  `NetworkId` INT NOT NULL,
  `LinkId` BIGINT(12) NOT NULL,
  `LinkIp` BIGINT(12) NULL,
  `SrcIp` BIGINT(12) NULL,
  `DestIp` BIGINT(12) NULL,
  `SubnetMask` BIGINT(12) NULL,
  PRIMARY KEY (`NetworkId`, `LinkId`),
  CONSTRAINT `fk_IpSchemeLink_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`Equipment` (
  `EquipmentId` INT NOT NULL,
  `Name` VARCHAR(100) NULL,
  `PartNo` VARCHAR(100) NULL,
  `PowerConsumption` INT NULL,
  `TypicalPower` INT NULL,
  `SlotSize` INT NULL,
  `Details` VARCHAR(200) NULL,
  `Price` FLOAT NULL,
  `Category` INT NULL,
  `RevisionCode` VARCHAR(100) NULL,
  PRIMARY KEY (`EquipmentId`));
  
  CREATE TABLE IF NOT EXISTS `DwdmNpt`.`Inventory` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `EquipmentId` INT NOT NULL,
  `Quantity` INT NULL,
  PRIMARY KEY (`EquipmentId`, `NetworkId`, `NodeId`),
  INDEX `fk_Inventory_NetworkId_idx` (`NetworkId` ASC),
  CONSTRAINT `fk_Inventory_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Inventory_EquipmentId`
    FOREIGN KEY (`EquipmentId`)
    REFERENCES `DwdmNpt`.`Equipment` (`EquipmentId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`OpticalData` (
  `EquipmentId` INT NOT NULL,
  `PIn` FLOAT NULL,
  `PInMax` FLOAT NULL,
  `PInMin` FLOAT NULL,
  `POut` FLOAT NULL,
  `POutMax` FLOAT NULL,
  `POutMin` FLOAT NULL,
  `Loss` FLOAT NULL,
  `Gain` FLOAT NULL,
  PRIMARY KEY (`EquipmentId`),
  CONSTRAINT `fk_OpticalData_EquipmentId`
    FOREIGN KEY (`EquipmentId`)
    REFERENCES `DwdmNpt`.`Equipment` (`EquipmentId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`CardPreference` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `CardFeature` VARCHAR(45) NULL,
  `CardType` VARCHAR(45) NOT NULL,
  `EquipmentId` INT NOT NULL,
  `Preference` INT NOT NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`, `EquipmentId`, `CardType`, `Preference`),
  CONSTRAINT `fk_CardPreference_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Node` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`LinkWavelength` (
  `NetworkId` INT NOT NULL,
  `LinkId` INT NOT NULL,
  `Wavelengths` VARCHAR(700) NULL,
  PRIMARY KEY (`NetworkId`, `LinkId`),
  CONSTRAINT `fk_LinkWavelength_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`LinkWavelengthInfo` (
  `NetworkId` INT NOT NULL,
  `LinkId` INT NOT NULL,
  `Wavelength` INT NOT NULL,
  `DemandId` INT NULL,
  `LineRate` VARCHAR(45) NULL,
  `Traffic` FLOAT NULL,
  `Path` VARCHAR(150) NULL,
  `RoutePriority` INT NULL,
  `Status` VARCHAR(45) NULL,
  PRIMARY KEY (`NetworkId`, `LinkId`, `Wavelength`),
  CONSTRAINT `fk_LinkWavelengthInfo_NetworkId0`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`McsPortMapping` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `Rack` INT NOT NULL,
  `Sbrack` INT NOT NULL,
  `Card` INT NOT NULL,
  `CardType` VARCHAR(45) NULL,
  `CardSubType` INT NULL,
  `McsId` INT NOT NULL,
  `McsAddPortInfo` VARCHAR(45) NOT NULL,
  `McsDropPortInfo` VARCHAR(45) NULL,
  `AddTpnRackId` INT NOT NULL,
  `AddTpnSubRackId` INT NOT NULL,
  `AddTpnSlotId` INT NOT NULL,
  `AddTpnLinePortNum` INT NULL,
  `DropTpnRackId` INT NULL,
  `DropTpnSubRackId` INT NULL,
  `DropTpnSlotId` INT NULL,
  `DropTpnLinePortNum` INT NULL,
  `McsSwitchPort` INT NOT NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`, `Rack`, `Sbrack`, `Card`, `McsId`, `AddTpnRackId`, `AddTpnSubRackId`, `AddTpnSlotId`, `McsAddPortInfo`,`McsSwitchPort`),
  CONSTRAINT `fk_McsPortMapping_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
   CREATE TABLE IF NOT EXISTS `DwdmNpt`.`OcmConfig` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `Rack` INT NOT NULL,
  `Sbrack` INT NOT NULL,
  `Card` INT NOT NULL,
  `CardType` VARCHAR(45) NULL,
  `CardSubType` INT NULL,
  `OcmId` INT NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`, `Rack`, `Sbrack`, `Card`),
  CONSTRAINT `fk_OcmConfig_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`PtcClientProtInfo` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `ActMpnRackId` INT NOT NULL,
  `ActMpnSubrackId` INT NOT NULL,
  `ActMpnCardId` INT NOT NULL,
  `ActMpnCardType` VARCHAR(45) NULL,
  `ActMpnCardSubType` VARCHAR(45) NULL,
  `ActMpnPort` INT NOT NULL,
  `ProtMpnRackId` INT NULL,
  `ProtMpnSubrackId` INT NULL,
  `ProtMpnCardId` INT NULL,
  `ProtMpnCardType` VARCHAR(45) NULL,
  `ProtMpnCardSubType` VARCHAR(45) NULL,
  `ProtMpnPort` INT NULL,
  `ProtCardRackId` INT NULL,
  `ProtCardSubrackId` INT NULL,
  `ProtCardCardId` INT NULL,
  `ProtTopology` VARCHAR(50) NULL,
  `ProtMechanism` VARCHAR(50) NULL,
  `ProtStatus` INT NULL,
  `ProtType` INT NULL,
  `ActiveLine` INT NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`, `ActMpnRackId`, `ActMpnSubrackId`, `ActMpnCardId`, `ActMpnPort`),
  CONSTRAINT `fk_PtcClientProtInfo_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
   CREATE TABLE IF NOT EXISTS `DwdmNpt`.`PtcLineProtInfo` (
 `NetworkId` int(11) NOT NULL,
  `NodeId` int(11) NOT NULL,
  `ProtCardRackId` int(11) NOT NULL,
  `ProtCardSubrackId` int(11) NOT NULL,
  `ProtCardCardId` int(11) NOT NULL,
  `ProtCardCardType` varchar(45) NOT NULL,
  `ProtCardCardSubType` int(11) NOT NULL,
  `MpnRackId` varchar(45) DEFAULT NULL,
  `MpnSubrackId` varchar(45) DEFAULT NULL,
  `MpnCardId` int(11) DEFAULT NULL,
  `MpnCardType` varchar(45) DEFAULT NULL,
  `MpnCardSubType` int(11) DEFAULT NULL,
  `ProtTopology` varchar(50) DEFAULT NULL,
  `ProtMechanism` varchar(50) DEFAULT NULL,
  `ProtStatus` int(11) DEFAULT NULL,
  `ProtType` int(11) DEFAULT NULL,
  `ActiveLine` int(11) DEFAULT NULL,
  PRIMARY KEY (`NetworkId`,`NodeId`,`ProtCardRackId`,`ProtCardSubrackId`,`ProtCardCardId`,`ProtCardCardType`,`ProtCardCardSubType`),
  CONSTRAINT `fk_PtcLineProtInfo_NetworkId` 
  FOREIGN KEY (`NetworkId`) 
  REFERENCES `DwdmNpt`.`Network` (`NetworkId`) 
  ON DELETE CASCADE 
  ON UPDATE CASCADE);
    
 CREATE TABLE IF NOT EXISTS `DwdmNpt`.`AmplifierConfig` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `Rack` INT NOT NULL,
  `Sbrack` INT NOT NULL,
  `Card` INT NOT NULL,
  `Direction` VARCHAR(45) NULL,
  `AmpType` VARCHAR(45) NULL,
  `AmpStatus` INT NULL,
  `GainLimit` FLOAT NULL,
  `ConfigurationMode` INT NULL,
  `VoaAttenuation` FLOAT NULL,
  `EdfaDirId` VARCHAR(45) NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`, `Rack`, `Sbrack`, `Card`),
  CONSTRAINT `fk_AmplifierConfig_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`WssDirectionConfig` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `Rack` INT NOT NULL,
  `Sbrack` INT NOT NULL,
  `Card` INT NOT NULL,
  `CardType` VARCHAR(45) NULL,
  `CardSubType` INT NULL,
  `WssDirection` VARCHAR(45) NULL,
  `WssDirectionType` INT NULL,
  `LaserStatus` INT NULL,
  `AttenuationConfigMode` INT NULL,
  `FixedAttenuation` INT NULL,
  `PreEmphasisTriggerPowerDiff` INT NULL,
  `Attenuation` FLOAT NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`, `Rack`, `Sbrack`, `Card`),
  CONSTRAINT `fk_WssDirectionConfig_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`TpnPortInfo` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `Rack` INT NOT NULL,
  `Sbrack` INT NOT NULL,
  `Card` INT NOT NULL,
  `Stream` INT NOT NULL,
  `CardType` VARCHAR(45) NULL,
  `CardSubType` INT NULL,
  `PortId` INT NOT NULL,
  `ProtectionSubType` INT NULL,
  `IsRevertive` INT NULL,
  `FecType` INT NULL,
  `FecStatus` INT NULL,
  `TxSegment` INT NULL,
  `OperatorSpecific` VARCHAR(45) NULL,
  `TimDectMode` INT NULL,
  `TCMActStatus` INT NULL,
  `TCMActValue` INT NULL,
  `TxTCMMode` INT NULL,
  `TxTCMPriority` VARCHAR(45) NULL,
  `GccType` INT NULL,
  `GccStatus` INT NULL,
  `GccValue` INT NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`, `Rack`, `Sbrack`, `Card`, `Stream`, `PortId`),
  CONSTRAINT `fk_TpnPortInfo_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
    CREATE TABLE IF NOT EXISTS `DwdmNpt`.`CardPortMap` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NULL,
  `End1Rack` INT NOT NULL,
  `End1Sbrack` INT NOT NULL,
  `End1Card` INT NOT NULL,
  `End1CardType` VARCHAR(45) NULL,
  `End1Port` VARCHAR(45) NOT NULL,
  `End2Rack` INT NULL,
  `End2Sbrack` INT NULL,
  `End2Card` INT NULL,
  `End2CardType` VARCHAR(45) NULL,
  `End2Port` VARCHAR(45) NULL,
  PRIMARY KEY (`NetworkId`, `End1Rack`, `End1Sbrack`, `End1Card`, `End1Port`),
  CONSTRAINT `fk_CardPortMap_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`McsMap` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `Rack` INT NOT NULL,
  `Sbrack` INT NOT NULL,
  `Card` INT NOT NULL,
  `McsId` INT NOT NULL,
  `McsSwitchPort` INT NOT NULL,
  `McsCommonPort` VARCHAR(50) NULL,
  `TpnLoc` VARCHAR(50) NULL,
  `TpnLinePortNo` INT NOT NULL,
  `EdfaLoc` VARCHAR(45) NULL,
  `EdfaId` INT NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`, `Rack`, `Sbrack`, `Card`, `McsId`, `McsSwitchPort`,`TpnLinePortNo`),
  CONSTRAINT `fk_McsMap_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
 CREATE TABLE IF NOT EXISTS `DwdmNpt`.`MuxDemuxPortInfo` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `Rack` INT NOT NULL,
  `Sbrack` INT NOT NULL,
  `CardId` INT NOT NULL,
  `wavelength` INT NOT NULL,
  `portNum` INT NOT NULL,
   PRIMARY KEY (`NetworkId`, `NodeId`, `Rack`, `Sbrack`, `CardId`, `portNum`),
   CONSTRAINT `fk_MuxDemuxPortInfo_NetworkId`
   FOREIGN KEY (`NetworkId`)
   REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
   ON DELETE CASCADE
   ON UPDATE CASCADE);

CREATE TABLE IF NOT EXISTS `DwdmNpt`.`WssMap` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `Rack` INT NOT NULL,
  `Sbrack` INT NOT NULL,
  `Card` INT NOT NULL,
  `CardType` VARCHAR(50) NULL,
  `CardSubtype` INT NULL,
  `WssSetNo` INT NOT NULL,
  `WssLevel2SwitchPort` INT NOT NULL,
  `WssLevel2CommonPort` VARCHAR(50) NULL,
  `TpnLoc` VARCHAR(50) NULL,
  `TpnLinePortNo` INT NULL,
  `WssLevel1Loc` VARCHAR(45) NULL,
  `WssLevel1SwitchPort` INT NULL,
  `WssLevel1CommonPort` VARCHAR(50) NULL,
  `EdfaLoc` VARCHAR(45) NULL,
  `EdfaId` INT NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`, `Rack`, `Sbrack`, `Card`, `WssSetNo`, `WssLevel2SwitchPort`),
  CONSTRAINT `fk_WssMap_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE IF NOT EXISTS `DwdmNpt`.`OpticalPowerCdcRoadmDrop` (
  `NetworkId` INT NOT NULL,
  `NodeId` VARCHAR(45) NOT NULL,
  `Direction` VARCHAR(45) NOT NULL,
  `NoOfLambdas` INT NULL,
  `PaIn` FLOAT NULL,
  `PaOut` FLOAT NULL,
  `RxWssIn` FLOAT NULL,
  `RxWssOut` FLOAT NULL,
  `RxWssAttenuation` FLOAT NULL,
  `DropEdfaIn` FLOAT NULL,
  `DropEdfaOut` FLOAT NULL,
  `DropMcsIn` FLOAT NULL,
  `DropMcsOut` FLOAT NULL,
  `MpnIn` FLOAT NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`,`Direction`),
CONSTRAINT `fk_OpticalPowerCdcRoadmDrop_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);


CREATE TABLE IF NOT EXISTS `DwdmNpt`.`OpticalPowerCdcRoadmAdd` (
  `NetworkId` INT NOT NULL,
  `NodeId` VARCHAR(45) NOT NULL,
  `Direction` VARCHAR(45) NOT NULL,
  `NoOfLambdas` INT NULL,
  `BaIn` FLOAT NULL,
  `BaOut` FLOAT NULL,
  `TxWssIn` FLOAT NULL,
  `TxWssOut` FLOAT NULL,
  `TxWssAttenuation` FLOAT NULL,
  `AddEdfaIn` FLOAT NULL,
  `AddEdfaOut` FLOAT NULL,
  `AddMcsIn` FLOAT NULL,
  `AddMcsOut` FLOAT NULL,
  `MpnLaunchPower` FLOAT NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`,`Direction`),
 CONSTRAINT `fk_OpticalPowerCdcRoadmAdd_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

  
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`VoaConfigInfo` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `Rack` INT NOT NULL,
  `Sbrack` INT NOT NULL,
  `Card` INT NOT NULL,
  `Direction` INT NULL,
  `ChannelType` INT NOT NULL,
  `AttenuationConfigMode` INT NULL,
  `Attenuation` FLOAT NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`, `Rack`, `Sbrack`, `Card`, `ChannelType`),
  CONSTRAINT `fk_VoaConfigInfo`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

  
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`YCablePortInfo` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `YCableRack` INT NOT NULL,
  `YCableSbrack` INT NOT NULL,
  `YCableCard` INT NOT NULL,
  `YCablePort` INT NOT NULL,
  `MpnLocN` VARCHAR(50) NULL,
  `MpnPortN` INT NULL,
  `MpnLocP` VARCHAR(50) NULL,
  `MpnPortP` INT NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`, `YCableRack`, `YCableSbrack`, `YCableCard`, `YCablePort`),
  CONSTRAINT `fk_YCablePortInfo_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
   CREATE TABLE IF NOT EXISTS `DwdmNpt`.`EquipmentPreference` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `Category` VARCHAR(45) NOT NULL,
  `CardType` VARCHAR(45) NOT NULL,
  `Preference` INT NOT NULL,
  `Redundancy` VARCHAR(45) NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`, `Category`, `CardType`, `Preference`),
  CONSTRAINT `fk_EquipmentPreference_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
    CREATE TABLE IF NOT EXISTS `DwdmNpt`.`Stock` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `Category` VARCHAR(45) NOT NULL,
  `CardType` VARCHAR(45) NOT NULL,
  `Quantity` INT NULL,
  `UsedQuantity` INT NULL,
  `SerialNo` VARCHAR(45) NOT NULL,
  `Status` VARCHAR(45) NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`, `Category`, `CardType`, `SerialNo`),
  CONSTRAINT `fk_Stock_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`ParametricPreference` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `Category` VARCHAR(45) NOT NULL,
  `CardType` VARCHAR(45) NOT NULL,
  `Parameter` VARCHAR(45) NOT NULL,
  `Value` VARCHAR(45) NOT NULL,
  `SerialNo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`, `Category`, `CardType`, `Parameter`, `Value`, `SerialNo`),
  CONSTRAINT `fk_ParametricPreference_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`AllocationExceptions` (
  `NetworkId` INT NOT NULL,
  `NodeId` INT NOT NULL,
  `SerialNo` VARCHAR(45) NOT NULL,
  `CardType` VARCHAR(45) NOT NULL,
  `Location` VARCHAR(45) NOT NULL,
  `Port` INT NOT NULL,
  `Type` VARCHAR(45) NULL,
  PRIMARY KEY (`NetworkId`, `NodeId`, `SerialNo`, `CardType`, `Location`, `Port`),
  CONSTRAINT `fk_AllocationExceptions_NetworkId`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
    

CREATE TABLE IF NOT EXISTS `DwdmNpt`.`LambdaLspInformation` (
  `NetworkId` INT NOT NULL,
  `DemandId` INT NOT NULL,
  `RoutePriority` INT NOT NULL,
  `Path` VARCHAR(100) NOT NULL,
  `LambdaLspTunnelId` INT NULL,  
  `LspId` INT NULL,
  `ForwardingAdj` INT NULL,
  --`OtnLspTunnelId` INT DEFAULT NULL,  
  --`ServiceType` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`NetworkId`, `DemandId`, `RoutePriority`, `Path`),
  CONSTRAINT `fk_LambdaLsp`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);



CREATE TABLE IF NOT EXISTS `DwdmNpt`.`OtnLspInformation` (
  `NetworkId` INT NOT NULL,
  `DemandId` INT NOT NULL,
  `LineRate` varchar(45) DEFAULT NULL,
  `Path` VARCHAR(100) NOT NULL,
  `WavelengthNo` INT NOT NULL,
  `RoutePriority` INT NOT NULL,
  `CircuitId` varchar(100) NOT NULL,
  `TrafficType` varchar(100) NOT NULL,  
  `ProtectionType` varchar(100) DEFAULT NULL,
  `OtnLspTunnelId` INT NOT NULL,    
  `LspId` INT NOT NULL,
  `ForwardingAdj` INT NULL,
  `TypeOfOtnLsp` INT NOT NULL,           
  PRIMARY KEY (`NetworkId`, `DemandId`, `RoutePriority`, `Path`, `TrafficType`,`OtnLspTunnelId`,`LspId`,`TypeOfOtnLsp`),
  CONSTRAINT `fk_OtnLsp`
    FOREIGN KEY (`NetworkId`)
    REFERENCES `DwdmNpt`.`Network` (`NetworkId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`Cards` (
  `NetworkId` int(11) NOT NULL,
  `NodeId` int(11) NOT NULL,
  `Rack` int(11) NOT NULL,
  `Sbrack` int(11) NOT NULL,
  `Card` int(11) NOT NULL,
  `CardType` varchar(45) NOT NULL,
  `Direction` varchar(45) NOT NULL,
  `Wavelength` int(11) DEFAULT NULL,
  `DemandId` int(11) DEFAULT NULL,
  `EquipmentId` int(11) DEFAULT NULL,
  `Status` varchar(45) NOT NULL,
  `CircuitId` varchar(45) NOT NULL,
  `SerialNo` varchar(45) NOT NULL,
  PRIMARY KEY (`NetworkId`,`NodeId`,`Rack`,`Sbrack`,`Card`)
);

CREATE TABLE IF NOT EXISTS `DwdmNpt`.`Ports` (
  `NetworkId` int(11) NOT NULL,
  `NodeId` int(11) NOT NULL,
  `Rack` int(11) NOT NULL,
  `Sbrack` int(11) NOT NULL,
  `Card` int(11) NOT NULL,
  `CardType` varchar(45) NOT NULL,
  `Port` int(11) NOT NULL,
  `LinePort` int(11) NOT NULL,
  `EquipmentId` int(11) DEFAULT NULL,
  `CircuitId` int(11) DEFAULT NULL,
  `DemandId` int(11) DEFAULT NULL,
  `Direction` varchar(45) NOT NULL,
  `MpnPortNo` int(11) DEFAULT 0,
  PRIMARY KEY (`NetworkId`,`NodeId`,`Rack`,`Sbrack`,`Card`,`Port`,`LinePort`,`MpnPortNo`)
);

CREATE TABLE IF NOT EXISTS `PatchCordInfo` (
  `NetworkId` int(11) NOT NULL,
  `NodeId` int(11) NOT NULL,
  `EquipmentId` int(11) NOT NULL,
  `CordType` varchar(150) DEFAULT NULL,
  `End1CardType` varchar(45) DEFAULT NULL,
  `End1Location` varchar(45) NOT NULL,
  `End1Port` varchar(45) NOT NULL,
  `End2CardType` varchar(45) DEFAULT NULL,
  `End2Location` varchar(45) NOT NULL,
  `End2Port` varchar(45) NOT NULL,
  `Length` int(11) DEFAULT NULL,
  `DirectionEnd1` varchar(45) DEFAULT NULL,
  `DirectionEnd2` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`NetworkId`,`NodeId`,`EquipmentId`,`End1Location`,`End1Port`,`End2Location`,`End2Port`)
);

CREATE TABLE  IF NOT EXISTS `DwdmNpt`.`Circuit10gAgg` (
  `NetworkId` INT(11) NOT NULL,
  `CircuitId` INT(11) NOT NULL,
  `Circuit10gAggId` INT(11) NOT NULL,
  `RequiredTraffic` FLOAT NULL,
  `TrafficType` VARCHAR(100) NULL,
  `LineRate` VARCHAR(45) NULL,
  PRIMARY KEY ( `NetworkId`, `CircuitId`, `Circuit10gAggId`),
  CONSTRAINT `fk_Circuit10gAgg_1`
    FOREIGN KEY (`NetworkId` , `CircuitId`)
    REFERENCES `DwdmNpt`.`Circuit` (`NetworkId` , `CircuitId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
 
CREATE TABLE IF NOT EXISTS `DwdmNpt`.`Circuitpotpmapping` (
  `RowNo` INT(11) NOT NULL,  
  `NetworkId` INT(11) NOT NULL,
  `potpcircuitId`  INT(11) NOT NULL,
  `CircuitId` INT(11) NOT NULL,
  `CircuitpotpType` VARCHAR(55) NULL,
  `TributoryId` INT(11) NOT NULL,
   PRIMARY KEY (`RowNo`,`NetworkId`,`CircuitId`),
   CONSTRAINT `fk_CircuitId1` 
   FOREIGN KEY (`NetworkId`,`CircuitId`) 
   REFERENCES `DwdmNpt`.`Circuit` (`NetworkId` ,`CircuitId`)
   ON DELETE CASCADE 
   ON UPDATE CASCADE);
    
 

    --views    
    --CREATE OR REPLACE VIEW RouteMapping AS
   -- SELECT n.NetworkId, cd.CircuitId , c.SrcNodeId, c.DestNodeId, c.TrafficType, c.ProtectionType, n.Path, n.WavelengthNo , n.Osnr FROM NetworkRoute n
   --LEFT JOIN CircuitDemandMapping cd
   --ON n.DemandId = cd.DemandId     
   --LEFT JOIN Circuit c
   --ON c.CircuitId = cd.CircuitId ;

 CREATE OR REPLACE VIEW RouteMapping AS
 SELECT n.NetworkId, n.DemandId , d.SrcNodeId, d.DestNodeId, n.Traffic, d.ProtectionType,d.ChannelProtection, n.RoutePriority, n.Path,n.PathLinkId, n.PathType, n.WavelengthNo , n.Osnr , n.LineRate , 				n.RegeneratorLoc, n.Error FROM NetworkRoute n
  LEFT JOIN Demand d
  ON n.NetworkId=d.NetworkId and n.DemandId = d.DemandId;
    
CREATE OR REPLACE VIEW DemandMapping AS
    SELECT c.NetworkId, cd.DemandId, c.CircuitId , c.SrcNodeId, c.DestNodeId, c.RequiredTraffic, c.TrafficType, c.ProtectionType, c.ClientProtectionType, c.ColourPreference, c.PathType, c.LineRate, c.ChannelProtection FROM Circuit c
    LEFT JOIN CircuitDemandMapping cd
    ON cd.NetworkId=c.NetworkId and cd.CircuitId = c.CircuitId ;
    
CREATE OR REPLACE VIEW Bom AS
    SELECT i.NetworkId, i.NodeId, n.StationName, n.SiteName, e.Name, i.Quantity, e.Price, i.Quantity*e.Price as TotalPrice, e.PartNo , e.PowerConsumption, e.TypicalPower,i.Quantity*e.PowerConsumption as TotalPowerConsumption,i.Quantity*e.TypicalPower as TotalTypicalPower, e.RevisionCode, e.Category, e.EquipmentId FROM Inventory i
    LEFT JOIN Equipment e 	
    ON i.EquipmentId = e.EquipmentId 
    LEFT JOIN Node n 	
    ON i.NodeId = n.NodeId and i.NetworkId = n.NetworkId;
    
   CREATE OR REPLACE VIEW AggClientsMapping AS
 select distinct P.NetworkId,P.NodeId,P.Rack,P.SbRack,P.Card,P.CardType,P.Port,P.LinePort,P.EquipmentId,P.CircuitId,
P.DemandId,P.Direction,P.MpnPortNo,C.Rack as MpnRack,C.SbRack as MpnSbRack,C.Card as MpnCard,C.LinePort as MpnLinePort,C.Wavelength as MpnWavelength,C.DemandId as MpnDemandId from (select * from Ports where MpnPortNo!=0) P left join 
 ( select Z.LinePort,Z.Rack,Z.SbRack,Z.Card,Z.DemandId,Z.Direction,Q.Wavelength,Q.NetworkId,Q.NodeId from (select * from Ports where MpnPortNo=0) Z left join (select * from Cards) Q 
 on Z.NetworkId=Q.NetworkId and Z.NodeId=Q.NodeId and Z.Rack=Q.Rack and Z.SbRack=Q.SbRack and Z.Card=Q.Card ) C on P.NetworkId=C.NetworkId and P.NodeId=C.NodeId and P.DemandId=C.DemandId and P.Direction=C.Direction; 
    
    
    
