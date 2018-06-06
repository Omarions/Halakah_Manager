-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: halaka
-- ------------------------------------------------------
-- Server version	5.7.9

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
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `comments` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='To save all subjects info';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
INSERT INTO `activity` VALUES (1,'القرءان الكريم','  تعليم وتحفيظ  '),(2,'اللغة العربية','تعليم قواعد اللغة العربية لغير المتكلمين بها'),(3,'الخط العربي','تعليم قواعد كتابة الخط العربي للمهتمين به من جميع الاعمار والجنسيات'),(4,'الزخرفة','تعليم قواعد الزخرفة للمتهمين وعشاق فن الزخرفة'),(5,'المقامات الصوتية','تعليم فن المقامات الصوتية للمهتمين '),(6,'الايقاع','تعليم فنون الايقاع ( الدف فقط )'),(7,'الاسكان','توفير سكن للطلبة الوافدين');
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_teacher`
--

DROP TABLE IF EXISTS `activity_teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_teacher` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_id` int(11) NOT NULL,
  `teacher_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_activityteacher-activity_idx` (`activity_id`),
  KEY `FK_ACTIVITYTEACHER_TEACHER_idx` (`teacher_id`),
  CONSTRAINT `FK_ACTIVITYTEACHER_ACTIVITY` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ACTIVITYTEACHER_TEACHER` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8 COMMENT='saves the many-to-many relationship between activity and teacher tables. As may be some teachers study more than one activity.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_teacher`
--

LOCK TABLES `activity_teacher` WRITE;
/*!40000 ALTER TABLE `activity_teacher` DISABLE KEYS */;
INSERT INTO `activity_teacher` VALUES (3,2,3),(5,3,4),(6,3,5),(7,3,6),(8,3,7),(9,3,8),(10,3,9),(11,3,10),(12,3,11),(13,3,12),(14,3,13),(15,4,6),(16,4,7),(20,6,17),(26,5,14),(27,5,15),(28,5,16),(33,1,1),(34,1,2);
/*!40000 ALTER TABLE `activity_teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `certificate`
--

DROP TABLE IF EXISTS `certificate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `certificate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `activity_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `teacher_id` int(11) NOT NULL,
  `release_date` date NOT NULL,
  `image` varchar(45) NOT NULL,
  `comments` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `certificate`
--

LOCK TABLES `certificate` WRITE;
/*!40000 ALTER TABLE `certificate` DISABLE KEYS */;
/*!40000 ALTER TABLE `certificate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `arabic_name` varchar(50) DEFAULT NULL,
  `english_name` varchar(50) DEFAULT NULL,
  `code` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=222 DEFAULT CHARSET=utf8 COMMENT='saves the country name in English and Arabic and its code.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `country`
--

LOCK TABLES `country` WRITE;
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
INSERT INTO `country` VALUES (1,'أفغانستان','Afghanistan','AF'),(2,'ألبانيا','Albania','AL'),(3,'الجزائر','Algeria','DZ'),(4,'ساموا-الأمريكي','American Samoa','AS'),(5,'أندورا','Andorra','AD'),(6,'أنغولا','Angola','AO'),(7,'أنغويلا','Anguilla','AI'),(8,'أنتاركتيكا','Antarctica','AQ'),(9,'أنتيغوا وبربودا','Antigua and Barbuda','AG'),(10,'الأرجنتين','Argentina','AR'),(11,'أرمينيا','Armenia','AM'),(12,'أروبا','Aruba','AW'),(13,'أستراليا','Australia','AU'),(14,'النمسا','Austria','AT'),(15,'أذربيجان','Azerbaijan','AZ'),(16,'الباهاماس','Bahamas','BS'),(17,'البحرين','Bahrain','BH'),(18,'بنغلاديش','Bangladesh','BD'),(19,'بربادوس','Barbados','BB'),(20,'روسيا البيضاء','Belarus','BY'),(21,'بلجيكا','Belgium','BE'),(22,'بيليز','Belize','BZ'),(23,'بنين','Benin','BJ'),(24,'جزر برمودا','Bermuda','BM'),(25,'بوتان','Bhutan','BT'),(26,'بوليفيا','Bolivia, Plurinational State of','BO'),(27,'البوسنة و الهرسك','Bosnia and Herzegovina','BA'),(28,'بوتسوانا','Botswana','BW'),(29,'جزر بوفيت','Bouvet Island','BV'),(30,'البرازيل','Brazil','BR'),(31,'بروناي دار السلام','Brunei Darussalam','BN'),(32,'بلغاريا','Bulgaria','BG'),(33,'بوركينا فاسو','Burkina Faso','BF'),(34,'بوروندي','Burundi','BI'),(35,'كمبوديا','Cambodia','KH'),(36,'كاميرون','Cameroon','CM'),(37,'كندا','Canada','CA'),(38,'الرأس الأخضر','Cape Verde','CV'),(39,'جمهورية أفريقيا الوسطى\n','Central African Republic','CF'),(40,'تشاد','Chad','TD'),(41,'تشيلي','Chile','CL'),(42,'جمهورية الصين الشعبية','China','CN'),(43,'كولومبيا','Colombia','CO'),(44,'جزر القمر','Comoros','KM'),(45,'جمهورية الكونغو','Congo','CG'),(46,'جمهورية الكونغو الديمقراطية','Congo, the Democratic Republic of the','CD'),(47,'جزر كوك','Cook Islands','CK'),(48,'كوستاريكا','Costa Rica','CR'),(49,'ساحل العاج','Cote d\'Ivoire','CI'),(50,'كرواتيا','Croatia','HR'),(51,'كوبا','Cuba','CU'),(52,'قبرص','Cyprus','CY'),(53,'الجمهورية التشيكية','Czech Republic','CZ'),(54,'الدانمارك','Denmark','DK'),(55,'جيبوتي','Djibouti','DJ'),(56,'دومينيكا','Dominica','DM'),(57,'الجمهورية الدومينيكية','Dominican Republic','DO'),(58,'إكوادور','Ecuador','EC'),(59,'مصر','Egypt','EG'),(60,'إلسلفادور','El Salvador','SV'),(61,'غينيا الاستوائية','Equatorial Guinea','GQ'),(62,'إريتريا','Eritrea','ER'),(63,'استونيا','Estonia','EE'),(64,'أثيوبيا','Ethiopia','ET'),(65,'جزر فوكلاند','Falkland Islands (Malvinas)','FK'),(66,'جزر فارو','Faroe Islands','FO'),(67,'فيجي','Fiji','FJ'),(68,'فنلندا','Finland','FI'),(69,'فرنسا','France','FR'),(70,'غويانا الفرنسية','French Guiana','GF'),(71,'بولينيزيا الفرنسية','French Polynesia','PF'),(72,'الغابون','Gabon','GA'),(73,'غامبيا','Gambia','GM'),(74,'جيورجيا','Georgia','GE'),(75,'ألمانيا','Germany','DE'),(76,'غانا','Ghana','GH'),(77,'اليونان','Greece','GR'),(78,'جرينلاند','Greenland','GL'),(79,'غرينادا','Grenada','GD'),(80,'جزر جوادلوب','Guadeloupe','GP'),(81,'جوام','Guam','GU'),(82,'غواتيمال','Guatemala','GT'),(83,'غينيا','Guinea','GN'),(84,'غينيا-بيساو','Guinea-Bissau','GW'),(85,'غيانا','Guyana','GY'),(86,'هايتي','Haiti','HT'),(87,'الفاتيكان','Holy See (Vatican City State)','VA'),(88,'هندوراس','Honduras','HN'),(89,'هونغ كونغ','Hong Kong','HK'),(90,'المجر','Hungary','HU'),(91,'آيسلندا','Iceland','IS'),(92,'الهند','India','IN'),(93,'أندونيسيا','Indonesia','ID'),(94,'إيران','Iran, Islamic Republic of','IR'),(95,'العراق','Iraq','IQ'),(96,'جمهورية أيرلندا','Ireland','IE'),(97,'إيطاليا','Italy','IT'),(98,'جامايكا','Jamaica','JM'),(99,'اليابان','Japan','JP'),(100,'الأردن','Jordan','JO'),(101,'كازاخستان','Kazakhstan','KZ'),(102,'كينيا','Kenya','KE'),(103,'كيريباتي','Kiribati','KI'),(104,'كوريا الشمالية','Korea, Democratic People\'s Republic of','KP'),(105,'كوريا الجنوبية','Korea, Republic of','KR'),(106,'الكويت','Kuwait','KW'),(107,'قيرغيزستان','Kyrgyzstan','KG'),(108,'لاوس','Lao People\'s Democratic Republic','LA'),(109,'لاتفيا','Latvia','LV'),(110,'لبنان','Lebanon','LB'),(111,'ليسوتو','Lesotho','LS'),(112,'ليبيريا','Liberia','LR'),(113,'ليبيا','Libya','LY'),(114,'ليختنشتين','Liechtenstein','LI'),(115,'لتوانيا','Lithuania','LT'),(116,'لوكسمبورغ','Luxembourg','LU'),(117,'ماكاو','Macao','MO'),(118,'جمهورية مقدونيا','Macedonia, the Former Yugoslav Republic of','MK'),(119,'مدغشقر','Madagascar','MG'),(120,'مالاوي','Malawi','MW'),(121,'ماليزيا','Malaysia','MY'),(122,'المالديف','Maldives','MV'),(123,'مالي','Mali','ML'),(124,'مالطا','Malta','MT'),(125,'جزر مارشال','Marshall Islands','MH'),(126,'مارتينيك','Martinique','MQ'),(127,'موريتانيا','Mauritania','MR'),(128,'موريشيوس','Mauritius','MU'),(129,'المكسيك','Mexico','MX'),(130,'ولايات ميكرونيسيا المتحدة','Micronesia, Federated States of','FM'),(131,'مولدوفا','Moldova, Republic of','MD'),(132,'موناكو','Monaco','MC'),(133,'منغوليا','Mongolia','MN'),(134,'الجبل الأسود','Montenegro','ME'),(135,'مونتسيرات','Montserrat','MS'),(136,'المغرب','Morocco','MA'),(137,'موزمبيق','Mozambique','MZ'),(138,'ميانمار','Myanmar','MM'),(139,'ناميبيا','Namibia','NA'),(140,'ناورو','Nauru','NR'),(141,'نيبال','Nepal','NP'),(142,'هولندا','Netherlands','NL'),(143,'كاليدونيا الجديدة','New Caledonia','NC'),(144,'نيوزيلندا','New Zealand','NZ'),(145,'نيكاراجوا','Nicaragua','NI'),(146,'النيجر','Niger','NE'),(147,'نيجيريا','Nigeria','NG'),(148,'نييوي','Niue','NU'),(149,'جزر نورفولك','Norfolk Island','NF'),(150,'جزر ماريانا الشمالية','Northern Mariana Islands','MP'),(151,'النرويج','Norway','NO'),(152,'عُمان','Oman','OM'),(153,'باكستان','Pakistan','PK'),(154,'بالاو','Palau','PW'),(155,'الأراضي الفلسطينية','Palestine, State of','PS'),(156,'بنما','Panama','PA'),(157,'بابوا غينيا الجديدة','Papua New Guinea','PG'),(158,'باراغواي','Paraguay','PY'),(159,'بيرو','Peru','PE'),(160,'الفليبين','Philippines','PH'),(161,'بولندا','Poland','PL'),(162,'البرتغال','Portugal','PT'),(163,'بورتوريكو','Puerto Rico','PR'),(164,'قطر','Qatar','QA'),(165,'رومانيا','Romania','RO'),(166,'روسيا','Russian Federation','RU'),(167,'رواندا','Rwanda','RW'),(168,'سانت لوسيا','Saint Helena, Ascension and Tristan da Cunha','SH'),(169,'سانت فنسنت وجزر سانت فنسنت وجزر غرينادين','Saint Kitts and Nevis','KN'),(170,'ساموا','Samoa','WS'),(171,'سان مارينو','San Marino','SM'),(172,'ساو تومي وبرينسيبي','Sao Tome and Principe','ST'),(173,'المملكة العربية السعودية','Saudi Arabia','SA'),(174,'السنغال','Senegal','SN'),(175,'جمهورية صربيا','Serbia','RS'),(176,'سيشيل','Seychelles','SC'),(177,'سيراليون','Sierra Leone','SL'),(178,'سنغافورة','Singapore','SG'),(179,'سلوفاكيا','Slovakia','SK'),(180,'سلوفينيا','Slovenia','SI'),(181,'جزر سليمان','Solomon Islands','SB'),(182,'الصومال','Somalia','SO'),(183,'جنوب أفريقيا','South Africa','ZA'),(184,'جنوب السودان','South Sudan','SS'),(185,'إسبانيا','Spain','ES'),(186,'سريلانكا','Sri Lanka','LK'),(187,'السودان','Sudan','SD'),(188,'سورينام','Suriname','SR'),(189,'سوازيلند','Swaziland','SZ'),(190,'السويد','Sweden','SE'),(191,'سويسرا','Switzerland','CH'),(192,'سوريا','Syrian Arab Republic','SY'),(193,'تايوان','Taiwan, Province of China','TW'),(194,'طاجيكستان','Tajikistan','TJ'),(195,'تنزانيا','Tanzania, United Republic of','TZ'),(196,'تايلاند','Thailand','TH'),(197,'تيمور الشرقية','Timor-Leste','TL'),(198,'توغو','Togo','TG'),(199,'تونغا','Tonga','TO'),(200,'ترينيداد وتوباغو','Trinidad and Tobago','TT'),(201,'تونس','Tunisia','TN'),(202,'تركيا','Turkey','TR'),(203,'تركمانستان','Turkmenistan','TM'),(204,'توفالو','Tuvalu','TV'),(205,'أوغندا','Uganda','UG'),(206,'أوكرانيا','Ukraine','UA'),(207,'الإمارات العربية المتحدة','United Arab Emirates','AE'),(208,'المملكة المتحدة','United Kingdom','GB'),(209,'الولايات المتحدة','United States','US'),(210,'أورغواي','Uruguay','UY'),(211,'أوزباكستان','Uzbekistan','UZ'),(212,'فانواتو','Vanuatu','VU'),(213,'فنزويلا','Venezuela, Bolivarian Republic of','VE'),(214,'فيتنام','Viet Nam','VN'),(215,'الجزر العذراء البريطانية','Virgin Islands, British','VG'),(216,'الجزر العذراء الأمريكي','Virgin Islands, U.S.','VI'),(217,'والس وفوتونا','Wallis and Futuna','WF'),(218,'الصحراء الغربية','Western Sahara','EH'),(219,'اليمن','Yemen','YE'),(220,'زامبيا','Zambia','ZM'),(221,'زمبابوي','Zimbabwe','ZW');
/*!40000 ALTER TABLE `country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `courses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_id` int(11) NOT NULL,
  `teacher_id` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `days` varchar(50) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `capacity` int(11) DEFAULT '10',
  `comments` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_COURSE_TEACHER_idx` (`teacher_id`),
  KEY `FK_COURSE_ACTIVITY_idx` (`activity_id`),
  CONSTRAINT `FK_COURSE_ACTIVITY` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_COURSE_TEACHER` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COMMENT='saves the courses details, start time and the subject studied, the teacher and the period of courses. The end date is optional because some courses is open.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (1,1,1,'130830','السبت , الاثنين , الاربعاء','2017-05-12','2017-07-05','08:30:00','10:30:00',10,NULL),(2,1,1,'131030','السبت , الاثنين , الاربعاء',NULL,NULL,'10:30:00','12:30:00',10,NULL),(3,1,1,'131330','السبت , الاثنين , الاربعاء',NULL,NULL,'13:30:00','15:30:00',10,NULL),(4,1,1,'131530','السبت , الاثنين , الاربعاء',NULL,NULL,'15:30:00','17:30:00',10,NULL),(5,1,2,'110830','السبت , الاثنين , الاربعاء',NULL,NULL,'08:30:00','10:30:00',10,NULL),(6,1,2,'111030','السبت , الاثنين , الاربعاء',NULL,NULL,'10:30:00','12:30:00',10,NULL),(7,1,2,'111330','السبت , الاثنين , الاربعاء',NULL,NULL,'13:30:00','15:30:00',10,NULL),(8,1,2,'111530','السبت , الاثنين , الاربعاء',NULL,NULL,'15:30:00','17:30:00',10,NULL),(9,2,3,'241800','السبت , الاثنين , الاربعاء',NULL,NULL,'18:00:00','19:15:00',10,NULL),(10,2,3,'241915','السبت , الاثنين , الاربعاء',NULL,NULL,'19:15:00','20:30:00',10,NULL),(11,2,3,'240800','الأحد , الثلاثاء , الخميس',NULL,NULL,'08:00:00','09:30:00',10,NULL),(12,2,3,'240930','الأحد , الثلاثاء , الخميس',NULL,NULL,'09:30:00','11:00:00',10,NULL),(13,3,6,'310800','الأحد , الثلاثاء , الخميس',NULL,NULL,'08:00:00','09:00:00',10,NULL),(14,3,6,'310900','الأحد , الثلاثاء , الخميس',NULL,NULL,'09:00:00','10:00:00',10,NULL),(15,3,13,'330800','الأحد , الثلاثاء , الخميس',NULL,NULL,'08:00:00','09:00:00',10,NULL),(16,3,13,'330900','الأحد , الثلاثاء , الخميس',NULL,NULL,'09:00:00','10:00:00',10,NULL),(17,3,13,'331000','الأحد , الثلاثاء , الخميس',NULL,NULL,'10:00:00','11:00:00',10,NULL),(18,3,7,'311630','الأحد , الثلاثاء , الخميس',NULL,NULL,'16:30:00','17:30:00',10,NULL),(19,3,7,'311730','الأحد , الثلاثاء , الخميس',NULL,NULL,'17:30:00','18:30:00',10,NULL),(22,3,4,'331630','الأحد , الثلاثاء , الخميس',NULL,NULL,'16:30:00','17:30:00',10,NULL),(23,3,4,'331730','الأحد , الثلاثاء , الخميس',NULL,NULL,'17:30:00','18:30:00',10,NULL),(24,3,4,'331830','الأحد , الثلاثاء , الخميس',NULL,NULL,'18:30:00','19:30:00',10,NULL),(25,3,4,'331930','الأحد , الثلاثاء , الخميس',NULL,NULL,'19:30:00','20:30:00',10,NULL),(26,3,11,'311530','الأحد , الثلاثاء , الخميس',NULL,NULL,'15:30:00','16:30:00',10,NULL),(27,3,8,'321630','الأحد , الثلاثاء , الخميس',NULL,NULL,'16:30:00','17:30:00',10,NULL),(28,3,12,'321730','الأحد , الثلاثاء , الخميس',NULL,NULL,'17:30:00','18:30:00',10,NULL),(29,3,5,'331530','الأحد , الثلاثاء , الخميس',NULL,NULL,'15:30:00','16:30:00',10,NULL),(30,3,9,'341600','الأحد , الثلاثاء , الخميس',NULL,NULL,'16:00:00','17:00:00',10,NULL),(31,3,10,'341700','الأحد , الثلاثاء , الخميس',NULL,NULL,'17:00:00','18:00:00',10,NULL),(32,3,10,'341800','الأحد , الثلاثاء , الخميس',NULL,NULL,'18:00:00','19:30:00',10,NULL),(33,4,6,'411000','الأحد , الثلاثاء , الخميس',NULL,NULL,'10:00:00','11:00:00',10,NULL),(34,4,7,'411830','الأحد , الثلاثاء , الخميس',NULL,NULL,'18:30:00','19:30:00',10,NULL),(35,4,7,'411930','الأحد , الثلاثاء , الخميس',NULL,NULL,'19:30:00','20:30:00',10,NULL),(36,5,16,'541130','السبت , الاثنين , الاربعاء',NULL,NULL,'11:30:00','13:30:00',10,NULL),(37,5,15,'541330','السبت , الاثنين , الاربعاء',NULL,NULL,'13:30:00','14:30:00',10,NULL),(38,5,15,'541430','السبت , الاثنين , الاربعاء',NULL,NULL,'14:30:00','15:30:00',10,NULL),(39,5,14,'531800','السبت , الاثنين , الاربعاء',NULL,NULL,'18:00:00','20:00:00',10,NULL),(40,6,17,'641600','السبت , الاثنين , الاربعاء',NULL,NULL,'16:00:00','18:00:00',10,NULL);
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_track`
--

DROP TABLE IF EXISTS `course_track`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course_track` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  `day_date` date NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `comments` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_COURSETRACK_STUDENT_idx` (`student_id`),
  KEY `FK_COURSETRACK_COURSE_idx` (`course_id`),
  CONSTRAINT `FK_CT_COURSE` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `FK_CT_STUDENT` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='saves the daily status of each student in the courses';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_track`
--

LOCK TABLES `course_track` WRITE;
/*!40000 ALTER TABLE `course_track` DISABLE KEYS */;
/*!40000 ALTER TABLE `course_track` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `house`
--

DROP TABLE IF EXISTS `house`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `house` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `capacity` int(11) NOT NULL,
  `occupied` int(11) NOT NULL DEFAULT '0',
  `comments` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='To define the houses available for students and thier max capacity and current number of setteled students.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `house`
--

LOCK TABLES `house` WRITE;
/*!40000 ALTER TABLE `house` DISABLE KEYS */;
INSERT INTO `house` VALUES (1,'القلعة',37,15,NULL),(2,'الشرابية',18,5,NULL),(3,'عبد الغفور',21,10,NULL);
/*!40000 ALTER TABLE `house` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_user_id` int(11) DEFAULT NULL,
  `to_user_id` int(11) DEFAULT NULL,
  `message` text,
  `createdat` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_MESSAGE_USER_TO_idx` (`to_user_id`),
  KEY `FK_MESSAGE_USER_FROM_idx` (`from_user_id`),
  CONSTRAINT `FK_MESSAGE_USER_FROM` FOREIGN KEY (`from_user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_MESSAGE_USER_TO` FOREIGN KEY (`to_user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message_attachments`
--

DROP TABLE IF EXISTS `message_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message_attachments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message_id` int(11) NOT NULL,
  `attachment` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_MESSAGEATTACHMENTS_MESSAGE_idx` (`message_id`),
  CONSTRAINT `FK_MESSAGEATTACHMENTS_MESSAGE` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message_attachments`
--

LOCK TABLES `message_attachments` WRITE;
/*!40000 ALTER TABLE `message_attachments` DISABLE KEYS */;
/*!40000 ALTER TABLE `message_attachments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` varchar(45) NOT NULL DEFAULT 'ADMIN',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='saves role of each student as USER or user as ADMIN';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ADMIN'),(2,'STUDENT');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `photo` varchar(100) DEFAULT NULL,
  `identity_id` varchar(45) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `birth_location` varchar(150) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL,
  `egypt_address` varchar(100) DEFAULT NULL,
  `home_address` varchar(100) DEFAULT NULL,
  `tel` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `facebook` varchar(50) DEFAULT NULL,
  `education` varchar(45) NOT NULL,
  `job` varchar(45) DEFAULT NULL,
  `comments` varchar(200) DEFAULT NULL,
  `house_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_STUDENT_HOUSE_idx` (`house_id`),
  CONSTRAINT `FK_STUDENT_HOUSE` FOREIGN KEY (`house_id`) REFERENCES `house` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COMMENT='Save all students info';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (1,'عمر فوزي',NULL,'123428004062103354','1980-04-06','Badrashin, Giza',59,'مزغونة - البدرشين - الجيزة','مزغونة - البدرشين - الجيزة','01119968990 - 01023005885','eng.omarfawzy@gmail.com',NULL,'بكالوريوس هندسة الكترونية','مهندس اتصالات','مصمم, ومطور برنامج مدير الحلقة',NULL),(2,'اسماعيل حسني عبدالله حسانين',NULL,'2960905220575','1996-09-05','الميمون, الوسطى, بني سويف',59,'الميمون, الوسطى, بني سويف','الميمون, الوسطى, بني سويف','01146439759','esmail999@gmail.com',NULL,'الفرقة الثانية , اصول الدين','طالب','يعمل بالمؤسسة حاليا',NULL),(3,'أشرف وجيه حسن عباس',NULL,'27509130104471','1975-09-13','مصر القديمة , القاهرة',59,'مساكن الزير المعلق مدخل 1 شقة 3 , عابدين, القاهرة','مساكن الزير المعلق مدخل 1 شقة 3 , عابدين, القاهرة','01110023886','no_email@no_email.com',NULL,'دبلوم ثانوي صناعي','موظف بالشركة المصرية للاتصالات','',1),(5,'حنان قريد',NULL,'152612912','1984-12-30','وهران , الجزائر',3,'مدينة البعوث الاسلامية, شارع احمد سعيد , القاهرة','16 نهج الامير خالد الكميل , وهران, الجزائر','01124441867','gridgrid84@hotmail.com',NULL,'ليسانس علوم تسيير','لا يعمل','',NULL),(6,'احمد يحي مناع الشريف',NULL,'02597576','1977-01-11','مأرب, اليمن',219,'10 ش حلمي عبد العاطي متفرع من طريق كفرطهرمس','منزل رقم 32, الشارع الرئيسي , المدينة , مديرية جريب , مأرب, اليمن','01119564848','alsharif18@gmail.com','المدرب احمد الشريف','مدرب تنمية بشرية , مهندس كمبيوتر','مدرب تنمية بشرية','no comments',2),(7,'حمزة محمد شريف',NULL,'O0046034','1995-03-07','خميس مشيط',1,'شقة 9, عمارة 5, العطوف , الجمالية, الدراسة, القاهرة','شارع احمد طيارة, حي النزلة , شارع التليفزيون, جدة  السعودية','01015399593','h2m1zw31@gmail.com','h2m1z2n1@hotmail.com','ثانوية عامة','طالب',NULL,1),(8,'حكيمة تايئ اسماعيل',NULL,'AA7382309','1996-12-03','مكة المكرمة',196,'مدينة البعوث الاسلامية, شارع احمد سعيد , العباسية , القاهرة','قرية كداي بارو, كوفور, فطاني, تايلاند','01202252596','haifoo.655m@hotmail.com','Zahrah Nison','كلية اصول الدين, جامعة الازهر','طالبة بالفرقة الاولى',NULL,3),(9,'يو نويه يان',NULL,'E38330111','1993-01-17','الصين',42,'شارع ناصر استير , عمارة رقم 2, الجيزة','China Ning xia Zin Chuan Citz JinFeng Tower; Liang tian country 017','01062783523','15109681761@ib3.com','Yan Xue Yu','ليسانس اللغة العربية في جامعة نيفشان','معلمة في جامعة القاهرة لتعليم اللغة الصينية',NULL,NULL),(10,'عمر محمد رؤوف حافظ محمود',NULL,'0006647','1995-04-05','مكة المكرمة',1,'شقة 9, عمارة 5, العطوف , الجمالية, الدراسة, القاهرة','شارع الجزائر, حي العتيبية, مكة المكرمة, السعودية','01014734671','omarmakkaw@gmail.com','omar__makkwkaw@hotmail.com','كلية الدراسات الاسلامية, جامعة القاهرة','طالب',NULL,3),(11,'آسيا',NULL,'E3752168','1994-04-04','الصين',42,'شارع احمد سعيد, العباسية , القاهرة','شارع ويي جو','01141997965','3530030570@qo.com','Ma Hw Xin','كلية الاداب قسم اللغة العربية','طالبة',NULL,3),(12,'عثمان محمد شريف حافظ',NULL,'O0006646','1996-10-17','خميس شبيط',1,'شقة 9, عمارة 5, العطوف , الجمالية, الدراسة, القاهرة','شارع احمد طيارة, حي النزلة , شارع التليفزيون, جدة  السعودية','01014734858','osman0124.os@gmail.com','osman0124.os@gmail.com','كلية الدراسات الاسلامية, جامعة الازهر','طالب بالفرقة الاولى',NULL,3),(13,'طالب عيد عباس',NULL,'AB646281','1993-11-20','تنزانيا',195,'مدينة البعوث الاسلامية, شارع احمد سعيد , العباسية , القاهرة','كرياكو, دار السلام, تنزانيا','01124113446','no_email@no_email.com','طالب الأشعري','كلية اصول الدين, جامعة الازهر','طالب بالفرقة الاولى',NULL,2),(14,'رابعة ييعت نهاد',NULL,'L106047905','1994-08-29','إزمير, علي أغا',202,'عمارة 40 , شارع محمد رضوان, الحي العاشر, مدينة نصر, القاهرة','الحي قرطولوش, شارع 318, رقم 3/1','01014711727','no_email@no_email.com','Rabia Yigit','كلية اصول الدين, قسم التفسير, جامعة الازهر','طالبة بالفرقة الثالثة',NULL,1),(15,'عبد العظيم بن الحاج عدنان بن الحاج عثمان',NULL,'C0126228','1994-01-31','بروناي دار السلام',31,'27 شارع جعفر الصادق , الحي السادس , مدينة نصر, الفاهرة','36 شارع بروناي دار السلام','01210907313','abdul@gmail.com',NULL,'كلية اللغة العربية','طالب بالفرقة الاولى',NULL,NULL),(16,'زليخة قربانوفا',NULL,'714604973','1984-02-26','روسيا',166,'حي السفارات','روسيا','01160247205','no_email@no_email.com',NULL,'الاعدادية','طالبة',NULL,3),(17,'جمال حسين علي قناو',NULL,'974349','1999-11-17','طرابلس, ليبيا',113,'مدينة 6 اكتوبر , الحي الرابع , المجاورة الاولى','طرابلس, ليبيا','01093078689','no_email@no_email.com',NULL,'الثانوية','طالب',NULL,1),(18,'حسين علي أحمد قناو',NULL,'974349','1959-01-01','طرابلس, ليبيا',113,'مدينة 6 اكتوبر , الحي الرابع , المجاورة الاولى','طرابلس, ليبيا','01008621976','book1965@gmail.com',NULL,'ماجستير علوم','طالب',NULL,NULL),(19,'الحسين مطرير',NULL,'WO6935891','1990-04-11','شفشاون',136,'مدينة البعوث الاسلامية, شارع احمد سعيد , العباسية , القاهرة','حي بعوث مدينة طنجة, زنقة 35','01024274940','fouad_hhr@hotmail.com',NULL,'كلية اللغة العربية ','طالب',NULL,3),(20,'معاذ بسقيان',NULL,'NL1010342','1990-03-08','المغرب',136,'ميدان عبده باشا شارع 10','تطوان , المغرب','01142948858','tararth@hotamil.com',NULL,'معهد','طالب',NULL,3),(21,'تيمورجان اتايف',NULL,'AA0234876','1988-10-23','اوزبكستان',211,'الحي الثامن , التعاونيات ع 25 ش13','اوزبكستان, محافظة نوائي','01150240534','no_email@no_email.com',NULL,'كلية الدعوة الاسلامية','طالب بالفرقة الثانية',NULL,1),(22,'حسن قيدرقولوف',NULL,'AA4042975','1982-11-02','اوزبكستان',211,'شقة 14, عمارة 55, شارع افريقيا, الحي الثامن, مدينة نصر, القاهرة','Home 4, Al-kharazmi st., Yangiyer city, Sirdarya Region, Uzbekistan','01123034444','no_email@no_email.com',NULL,'كلية اصول الدين','طالب بالفرقة الاولى',NULL,1),(23,'أيوب طاهر',NULL,'6286746','1989-12-05','الجزائر',3,'الجمالية , شارع محمد القزاز','حي عين عمار , بلدية عين البيضاء, الجزائر','01152563611','azoub1_h@yahoo.com',NULL,'ليسانس علوم تجارية ومالية, تخصص محاسبة','طالب',NULL,NULL),(24,'محمد بن نور محمد',NULL,'H0470910','1993-07-08','داعستان',166,'الباطنية , درب الاحمر','روسيا , داعستان','01020593483','munaddiss93@mail.tk',NULL,'الكلية , الحديث','طالب',NULL,NULL),(25,'أمينة شير علي',NULL,'M308009','1997-03-12','مصر',166,'شقة 32, عمارة 103, الحي السابع , مدينة نصر, القاهرة','موسكو, توفير, كوسلينا, جريسنداه, روسيا','01093834858','no_email@no_email.com',NULL,'الثانوية العامة','طالبة بالصف الاول',NULL,2),(26,'المكاشفي الحنو أحمد موسى',NULL,'721377','1976-01-01','السودان',187,'الحسين','السودان','01129993275','no_email@no_email.com',NULL,'الازهر الشريف','طالب بالحلقات الحرة',NULL,1),(27,'ثالث مالم ادريس ادم',NULL,'A05088764','1988-08-13','نيجيريا',147,'مدينة البعوث الاسلامية, شارع احمد سعيد , العباسية , القاهرة','Baweh, Nigeria','01146916831','no_email@no_email.com',NULL,'كلية اللغات والترجمة','طالب',NULL,1),(29,'حسن عبد الحفيظ',NULL,'A03249603','1992-01-09','نيجيريا',147,'محطة السلاب مدينة نصر','ابادن, نيجيريا','01150740869','hassanabdulafees@yahoo.com',NULL,'طالب','طالب',NULL,1),(30,'حورية بوشامة',NULL,'99CK99080','1980-12-24','فرنسا',69,'ش1 عمارة 11 اخر المترو','29 Rue Robert Reznier 69490 Saint-Fons, France','01122822797','houria.bouchama@gmail.com',NULL,'معهد المعصراوي','طالبة',NULL,3),(31,'غيل غزالي صالح',NULL,'N00033067','1988-08-11','كمبوديا',35,'الحي السابع , خليفة الراضي','Ph 5/KH Svaz khleang; kroch chhmar/kompong cham','01121241645','habibighazali@yahoo.com',NULL,'كلية الشريعة الاسلامية','طالب',NULL,1),(32,'نورحياتي وي هامأ',NULL,'AA7381161','1993-08-28','قطاني, تايلاند',196,'مدينة البعوث الاسلامية, شارع احمد سعيد , العباسية , القاهرة','سيساخان , تاراتيواس','01205770319','nur@hotmail.co.th',NULL,'كلية الشريعة الاسلامية, جامعة الازهر','طالبة بالفرقة الرابعة',NULL,1),(33,'أنيسة أحمد',NULL,'NBE272078','1998-12-23','سيما اسنراي, جزر القمر',44,'شارع شاذلي رقم 57, الاميرية','جزر القمر','01094310306','no_email@no_email.com',NULL,'معهد البعوث','طالبة',NULL,2),(34,'خديجة رحي',NULL,'E49998993','1996-06-16','الصين',42,'عبده باشا, شارع احمد سعيد','جيع يوان قويوان , الصين','01201945656','1496515687@99.com','01098769656','مركز رسالة','طالبة بالمستوى الاول',NULL,3),(35,'عائشة أبي موسى',NULL,'E14854048','1993-04-19','الصين',42,'عبده باشا, شارع احمد سعيد','شارع سور الصين العظيم , طريق رقم 15','01006737262','2308522949@99.com','01006237262','مركز النيل','طالبة بالمستوى الرابع',NULL,1),(36,'جومعان سمير بحعائي',NULL,'K0218396','1989-06-01','كلا',92,'مدينة البعوث الاسلامية, شارع احمد سعيد , العباسية , القاهرة','قرية كلا , مدينة ددودرة , محافظة عجرات','01005873489','sameerazhari186@gmail.com','Sameer Chishty Chauhan','كلية اللغة العربية','طالب بالفرقة الرابعة',NULL,3),(37,'شفاء راحمداني',NULL,'B4250298','1999-01-02','بيكادين, اندونيسيا',93,'شارع سيد محمد حسن رقم 14 الدراسة','بيكادين الشمالية , شارع 4 تلوك فو جوع رات م ','0115984598','sifa.rahenagoni@yahoo.com','Sifa Rahmadani','مركز لغة العربية الازهر','طالبة',NULL,1),(38,'نورحياتي منشور',NULL,'A7984770','1995-06-04','بوكتنجي, اندونيسيا',93,'الدراسة بجوار مضيفة شيخ سعيد عمران','شارع تالوك رقم 4 سومطرة الغربية اندونيسيا, قرية تالوك','01003221369','hayatinor63@rocketmail.com','Nurhayati','كلية الشريعة الاسلامية','طالبة بالفرقة الثالثة',NULL,1),(39,'اسراء فتحي محمود',NULL,'29009072102645','1990-09-07','مصر',59,'شارع 21 البحراوي الجيزة','21 شارع البحراوي الجيزة','01026363884','livesasa1990@yahoo.com',NULL,'بكالوريوس فنون تطبيقية','لا يعمل',NULL,2),(40,'وان محمد أمير الأقمار بن وان رشيد',NULL,'60','1992-05-26','ماليزيا',121,'20 شارع الفرماوي , دوران شبرا','Loi, 1345 Kampung Pacoh< Palekbang 1640 Tumpni Kelmatan','01143261121','no_email@no_email.com',NULL,'معهد القرآن , القرءات','طالب',NULL,1);
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_track`
--

DROP TABLE IF EXISTS `student_track`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student_track` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  `register_date` date NOT NULL,
  `start_date` date DEFAULT NULL,
  `status` varchar(45) NOT NULL DEFAULT 'WAITING',
  `evaluation` int(11) NOT NULL DEFAULT '0',
  `comments` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_STUDENT_STUDENTTRACK_idx` (`student_id`),
  KEY `FK_COURSE_STUDENTTRACK_idx` (`course_id`),
  CONSTRAINT `FK_COURSE_STUDENTTRACK` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `FK_STUDENT_STUDENTTRACK` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_track`
--

LOCK TABLES `student_track` WRITE;
/*!40000 ALTER TABLE `student_track` DISABLE KEYS */;
INSERT INTO `student_track` VALUES (3,1,25,'2016-10-15','2017-03-01','STUDYING',5,NULL),(4,1,34,'2017-05-01','2017-05-01','WAITING',0,NULL),(5,1,13,'2016-12-03','2016-12-17','CERTIFIED',10,NULL),(6,2,2,'2015-01-26','2015-09-24','FINAL_STOP',6,NULL),(7,2,10,'2017-03-01',NULL,'WAITING',0,NULL),(8,3,34,'2016-02-02','2016-06-03','STUDYING',0,NULL),(9,7,1,'2015-02-06','2016-10-15','STUDYING',5,NULL),(10,7,36,'2017-05-01',NULL,'WAITING',0,NULL),(11,10,33,'2016-05-01','2013-10-25','STUDYING',3,NULL),(12,12,3,'2016-09-10',NULL,'WAITING',0,NULL),(13,40,1,'2017-03-03','2017-03-04','STUDYING',5,NULL),(14,9,1,'2016-10-05','2017-03-05','STUDYING',4,NULL),(15,11,1,'2017-01-20','2017-03-10','STUDYING',7,NULL),(16,34,1,'2017-02-04','2017-03-20','WAITING',0,NULL);
/*!40000 ALTER TABLE `student_track` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `title` varchar(45) DEFAULT NULL,
  `description` text,
  `create_date` datetime(6) DEFAULT NULL,
  `due_date` datetime(6) DEFAULT NULL,
  `progress` int(11) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_TASK_USER_idx` (`user_id`),
  CONSTRAINT `FK_TASK_USER` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_test`
--

DROP TABLE IF EXISTS `tb_test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_test` (
  `id` int(11) NOT NULL,
  `st_time` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_test`
--

LOCK TABLES `tb_test` WRITE;
/*!40000 ALTER TABLE `tb_test` DISABLE KEYS */;
INSERT INTO `tb_test` VALUES (1,'10:30:00'),(2,'18:30:00');
/*!40000 ALTER TABLE `tb_test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teacher` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `tel` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `education` varchar(50) DEFAULT NULL,
  `job` varchar(45) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `is_working` tinyint(1) DEFAULT NULL,
  `comments` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='To save all teachers info, related to subject table as each teacher is teaching one subject';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES (1,'نعمة معوض','','','Applied Arts',NULL,NULL,NULL,1,NULL),(2,'نهاد عبد المحسن','3409',NULL,'شهادة جامعية',NULL,NULL,NULL,1,NULL),(3,'أحمد التيجاني',NULL,NULL,NULL,NULL,NULL,NULL,1,NULL),(4,'أحمد الحفني','01113230439','mo@gmail.com','ليسانس اداب',NULL,NULL,NULL,1,'جامعة القاهرة'),(5,'أحمد تاج الدين','0109 555 9874','mah@mah.com','ليسانس اداب قسم لغة عربية',NULL,NULL,NULL,1,'جامعة عين شمس'),(6,'حسنانين مختار',NULL,NULL,NULL,NULL,NULL,NULL,1,NULL),(7,'تيتو فؤاد','0122 859 5689','mah@mah.com','معهد الموسيقى العربية',NULL,NULL,NULL,1,'مدرب محترف'),(8,'زينون فأجيك',NULL,NULL,NULL,NULL,NULL,NULL,1,NULL),(9,'عبد القيوم',NULL,NULL,NULL,NULL,NULL,NULL,1,NULL),(10,'عمر ماهي دونغ',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(11,'فاطمة حاج محمدوفا',NULL,NULL,NULL,NULL,NULL,NULL,1,NULL),(12,'منير',NULL,NULL,NULL,NULL,NULL,NULL,1,NULL),(13,'نادر عبد القادر',NULL,NULL,NULL,NULL,NULL,NULL,1,NULL),(14,'اسلام منير',NULL,NULL,NULL,NULL,NULL,NULL,1,NULL),(15,'محمد ياسين المرعشلي',NULL,NULL,NULL,NULL,NULL,NULL,1,NULL),(16,'منى أحمد الأحمر',NULL,NULL,NULL,NULL,NULL,NULL,1,NULL),(17,'أمير حمامي',NULL,NULL,NULL,NULL,NULL,NULL,1,NULL);
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher_track`
--

DROP TABLE IF EXISTS `teacher_track`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teacher_track` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `teacher_id` int(11) NOT NULL,
  `day_date` date DEFAULT NULL,
  `vacation` tinyint(1) DEFAULT '0',
  `start_time` time DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  `comments` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_TEACHERTRACK_TEACHER_idx` (`teacher_id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='Track teacher daily existance';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher_track`
--

LOCK TABLES `teacher_track` WRITE;
/*!40000 ALTER TABLE `teacher_track` DISABLE KEYS */;
INSERT INTO `teacher_track` VALUES (1,4,'2017-05-14',0,'17:30:00','20:30:00',NULL),(2,4,'2017-05-16',0,'17:30:00','20:30:00',NULL),(3,4,'2017-05-18',0,'17:30:00','20:30:00',NULL),(4,4,'2017-05-21',0,'17:30:00','20:30:00',NULL),(5,4,'2017-05-23',0,'18:30:00','20:30:00','استئذان بالتليفون'),(6,4,'2017-05-25',1,'00:00:00',NULL,NULL),(7,4,'2017-05-28',0,'19:30:00','20:30:00','تأخر بدون استئذان');
/*!40000 ALTER TABLE `teacher_track` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `title` varchar(45) NOT NULL,
  `status` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='users for accessing database';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'ismail@halaka.org','ismail','Ismail Hosny','Hussien Office Coordinator',0),(2,'adnan@halaka.org','adnan','Adnan Ahmed','Azhar Office Coordinator',0),(3,'omar@halaka.org','omar','Omar Fawzy','Website Admin',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,1,1),(2,1,2),(3,1,3);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-06-17 21:46:43
