CREATE DATABASE  IF NOT EXISTS `class_modeler` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `class_modeler`;
-- MySQL dump 10.13  Distrib 5.6.11, for Win64 (x86_64)
--
-- Host: localhost    Database: class_modeler
-- ------------------------------------------------------
-- Server version	5.6.11

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
  `diagram_created_by` int(11) NOT NULL,
  `diagram_created_date` datetime NOT NULL,
  `diagram_modified_by` int(11) NOT NULL,
  `diagram_modified_date` datetime NOT NULL,
  `diagram_xmi` longtext NOT NULL,
  PRIMARY KEY (`diagram_key`),
  KEY `FK_DIAGRAM_USER_OWNER` (`diagram_created_by`),
  KEY `FK_DIAGRAM_USER_MODIFIER` (`diagram_modified_by`),
  CONSTRAINT `FK_DIAGRAM_USER_MODIFIER` FOREIGN KEY (`diagram_modified_by`) REFERENCES `user` (`user_key`),
  CONSTRAINT `FK_DIAGRAM_USER_OWNER` FOREIGN KEY (`diagram_created_by`) REFERENCES `user` (`user_key`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagram`
--

LOCK TABLES `diagram` WRITE;
/*!40000 ALTER TABLE `diagram` DISABLE KEYS */;
INSERT INTO `diagram` VALUES (1,'Gabriel Test','Mi Primer Diagrama',1,'2013-06-20 20:17:10',1,'2013-06-20 20:17:10',''),(2,'Gabriel Test - Copy','Mi Primer Diagrama',1,'2013-06-20 20:17:21',1,'2013-06-20 20:17:21',''),(3,'Gabriel Test Number 1','Mi Primer Diagrama',1,'2013-06-20 20:23:18',1,'2013-06-20 20:23:18','');
/*!40000 ALTER TABLE `diagram` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preference`
--

DROP TABLE IF EXISTS `preference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preference` (
  `preference_key` int(11) NOT NULL AUTO_INCREMENT,
  `preference_name` varchar(255) NOT NULL,
  `preference_value` varchar(4000) NOT NULL,
  `user_key` int(11) DEFAULT NULL,
  PRIMARY KEY (`preference_key`),
  UNIQUE KEY `UK_PREFERENCE_NAME` (`preference_name`),
  KEY `FK_PREFERENCE_USER` (`user_key`),
  CONSTRAINT `FK_PREFERENCE_USER` FOREIGN KEY (`user_key`) REFERENCES `user` (`user_key`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preference`
--

LOCK TABLES `preference` WRITE;
/*!40000 ALTER TABLE `preference` DISABLE KEYS */;
/*!40000 ALTER TABLE `preference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shared`
--

DROP TABLE IF EXISTS `shared`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shared` (
  `shared_key` int(11) NOT NULL AUTO_INCREMENT,
  `shared_date` datetime NOT NULL,
  `shared_comment` text,
  `shared_privilege` varchar(10) NOT NULL,
  `shared_from_user` int(11) NOT NULL,
  `shared_to_user` int(11) NOT NULL,
  `shared_diagram_key` int(11) NOT NULL,
  PRIMARY KEY (`shared_key`),
  UNIQUE KEY `UK_SHARED_TOUSER` (`shared_to_user`,`shared_diagram_key`),
  KEY `FK_SHARED_DIAGRAM` (`shared_diagram_key`),
  KEY `FK_SHARED_FROMUSER` (`shared_from_user`),
  KEY `FK_SHARED_TOUSER` (`shared_to_user`),
  CONSTRAINT `FK_SHARED_DIAGRAM` FOREIGN KEY (`shared_diagram_key`) REFERENCES `diagram` (`diagram_key`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_SHARED_FROMUSER` FOREIGN KEY (`shared_from_user`) REFERENCES `user` (`user_key`),
  CONSTRAINT `FK_SHARED_TOUSER` FOREIGN KEY (`shared_to_user`) REFERENCES `user` (`user_key`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shared`
--

LOCK TABLES `shared` WRITE;
/*!40000 ALTER TABLE `shared` DISABLE KEYS */;
/*!40000 ALTER TABLE `shared` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_key` int(11) NOT NULL AUTO_INCREMENT,
  `user_first_name` varchar(50) NOT NULL,
  `user_last_name` varchar(50) NOT NULL,
  `user_email` varchar(255) NOT NULL,
  `user_password` varchar(20) NOT NULL,
  `user_gender` varchar(10) NOT NULL,
  `user_account_status` varchar(15) NOT NULL DEFAULT '0' COMMENT '0 = Inactived\n1 = Actived\n2 = Deactived\n',
  `user_birthdate` date DEFAULT NULL,
  `user_created_date` datetime NOT NULL,
  `user_avatar` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_key`),
  UNIQUE KEY `UK_USER_EMAIL` (`user_email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Gabriel Leonardo','Diaz Cardenas','gabriel.leonardo.diaz@gmail.com','1234','MALE','ACTIVATED','1988-11-24','2013-06-20 20:16:27','/resources/uploads/male_avatar.png');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verification`
--

DROP TABLE IF EXISTS `verification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `verification` (
  `verification_key` int(11) NOT NULL AUTO_INCREMENT,
  `verification_code` varchar(255) NOT NULL,
  `verification_expire_date` datetime NOT NULL,
  `verification_valid` bit(1) NOT NULL,
  `verification_type` varchar(20) NOT NULL,
  `user_key` int(11) NOT NULL,
  PRIMARY KEY (`verification_key`),
  KEY `FK_VERIFICATION_USER` (`user_key`),
  CONSTRAINT `FK_VERIFICATION_USER` FOREIGN KEY (`user_key`) REFERENCES `user` (`user_key`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verification`
--

LOCK TABLES `verification` WRITE;
/*!40000 ALTER TABLE `verification` DISABLE KEYS */;
INSERT INTO `verification` VALUES (1,'6430e48fb18a824f719690c49fef6467','2013-06-22 20:16:27','\0','ACTIVATE_ACCOUNT',1);
/*!40000 ALTER TABLE `verification` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-07-09 18:48:27
