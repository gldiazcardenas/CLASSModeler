CREATE DATABASE  IF NOT EXISTS `class_modeler` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `class_modeler`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: localhost    Database: class_modeler
-- ------------------------------------------------------
-- Server version	5.6.14

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `diagram`
--

DROP TABLE IF EXISTS `diagram`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagram` (
  `diagram_key` int(11) NOT NULL AUTO_INCREMENT,
  `diagram_name` varchar(255) NOT NULL,
  `diagram_description` text,
  `diagram_xml` longtext NOT NULL,
  `diagram_created_by` int(11) NOT NULL,
  `diagram_created_date` datetime NOT NULL,
  `diagram_modified_by` int(11) NOT NULL,
  `diagram_modified_date` datetime NOT NULL,
  PRIMARY KEY (`diagram_key`),
  KEY `IDX_DIAGRAM_DIAGRAMMER_OWNER` (`diagram_created_by`),
  KEY `IDX_DIAGRAM_DIAGRAMMER_MODIFIER` (`diagram_modified_by`),
  CONSTRAINT `FK_DIAGRAM_DIAGRAMMER_MODIFIER` FOREIGN KEY (`diagram_modified_by`) REFERENCES `diagrammer` (`diagrammer_key`),
  CONSTRAINT `FK_DIAGRAM_DIAGRAMMER_OWNER` FOREIGN KEY (`diagram_created_by`) REFERENCES `diagrammer` (`diagrammer_key`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `diagrammer`
--

DROP TABLE IF EXISTS `diagrammer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagrammer` (
  `diagrammer_key` int(11) NOT NULL AUTO_INCREMENT,
  `diagrammer_first_name` varchar(50) NOT NULL,
  `diagrammer_last_name` varchar(50) NOT NULL,
  `diagrammer_email` varchar(255) NOT NULL,
  `diagrammer_password` varchar(20) NOT NULL,
  `diagrammer_gender` int(11) NOT NULL COMMENT '0 = Male\n1 = Female',
  `diagrammer_account_status` int(11) NOT NULL DEFAULT '0' COMMENT '0 = Inactived\n1 = Actived\n2 = Deactived\n',
  `diagrammer_registration_date` datetime NOT NULL,
  `diagrammer_avatar_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`diagrammer_key`),
  UNIQUE KEY `UK_DIAGRAMMER_EMAIL` (`diagrammer_email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `security_code`
--

DROP TABLE IF EXISTS `security_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_code` (
  `security_code_key` int(11) NOT NULL AUTO_INCREMENT,
  `security_code_hash` varchar(255) NOT NULL,
  `security_code_expiration_date` datetime NOT NULL,
  `security_code_type` int(11) NOT NULL COMMENT '0 = Activate Account\n1 = Reset Password',
  `security_code_valid` bit(1) NOT NULL,
  `diagrammer_key` int(11) NOT NULL,
  PRIMARY KEY (`security_code_key`),
  KEY `IDX_VERIFICATION_DIAGRAMMER` (`diagrammer_key`),
  CONSTRAINT `FK_VERIFICATION_DIAGRAMMER` FOREIGN KEY (`diagrammer_key`) REFERENCES `diagrammer` (`diagrammer_key`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shared_item`
--

DROP TABLE IF EXISTS `shared_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shared_item` (
  `shared_item_key` int(11) NOT NULL AUTO_INCREMENT,
  `shared_item_date` datetime NOT NULL,
  `shared_item_privilege` int(11) NOT NULL COMMENT '0 = Read\n1 = Write',
  `shared_item_diagrammer_key` int(11) NOT NULL,
  `shared_item_diagram_key` int(11) NOT NULL,
  PRIMARY KEY (`shared_item_key`),
  UNIQUE KEY `UK_SHARED_ITEM_DIAGRAMMER` (`shared_item_diagrammer_key`,`shared_item_diagram_key`),
  KEY `IDX_SHARED_ITEM_DIAGRAM` (`shared_item_diagram_key`),
  KEY `IDX_SHARED_ITEM_DIAGRAMMER` (`shared_item_diagrammer_key`),
  CONSTRAINT `FK_SHARED_ITEM_DIAGRAM` FOREIGN KEY (`shared_item_diagram_key`) REFERENCES `diagram` (`diagram_key`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_SHARED_ITEM_DIAGRAMMER` FOREIGN KEY (`shared_item_diagrammer_key`) REFERENCES `diagrammer` (`diagrammer_key`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-02-16 18:58:50
