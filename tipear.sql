-- MySQL dump 10.19  Distrib 10.3.29-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: tipear
-- ------------------------------------------------------
-- Server version	10.3.29-MariaDB-0ubuntu0.20.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (224);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `texts`
--

DROP TABLE IF EXISTS `texts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `texts` (
  `text_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `title` varchar(256) DEFAULT NULL,
  `author` varchar(150) DEFAULT NULL,
  `category` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`text_id`),
  UNIQUE KEY `title` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `texts`
--

LOCK TABLES `texts` WRITE;
/*!40000 ALTER TABLE `texts` DISABLE KEYS */;
INSERT INTO `texts` VALUES (2,'No one knows what its like. To be mistreated, to be defeated. Behind blue eyes. And no one knows how to say. That they\'re sorry and don\'t worry. I\'m not telling lies. But my dreams they aren\'t as empty. As my conscience seems to be. I have hours, only lonely. My love is vengeance. That\'s never free.','Behind blue eyes','Limp Bizkit','song'),(3,'Tired of lying in the sunshine, staying home to watch the rain. You are young and life is long, and there is time to kill today. And then one day you find ten years have got behing you. No one told you when to run, you missed the starting gun. An you run, and you run to catch up with the sun but it\'s sinking. Racing around to come up behind ou again. The sun is the same in a relative way but you\'re older. Shorter of breath and one day closer to death','Time','Pink Floyd','song'),(4,'Life, it seems, will fade away. Drifting further, every day. Getting lost within myself. Nothing matters, no one else. I have lost the will to live. Simply nothing more to give. There is nothing more for me. Need the end to set me free. Things not what they used to be. Missing one inside of me. Deathly loss, this can\'t be real. I cannot stand this hell I feel. Emptiness is filling me. To the point of agony.','Fade to black','Metallica','song'),(5,'He was turned to steel. In the great magnetic field. When he traveled time. For the future of manking. Nobody wants him. He just stares at the world. Planning his vengeance. THat he will soon unfurl. Now the time is here. For iron man to spread fear. Vengeance from the grave. Kills the people he once saved.','Iron Man','Black Sabbath','song'),(6,'His palms are sweaty, knees weak, arms are heavy. There\'s vomit on his sweather already, mom\'s spaghetti. He\'s nerveous, but on the surface he looks calm and ready. To drop bombs, but he keeps on forgettin\'. What he wrote down, the whole crowd goes so loud. He opens his mouth, but the words won\'t come out.','Lose Yourself','Eminem','song'),(7,'Well I can promise you paradise. No need to serve on your knees. And when you\'re lost in the darkest of hours. Take a moment and tell me who you see. Won\'t tell ya who not to be.','Shepherd of Fire','Avenged Sevenfold','song'),(8,'So how does it feel to know that someone\'s kid in the heart of America has blood on their hands. Fighting to defend your rights, so you can maintain a lifestyle that insults his family\'s existence','Critical Acclaim','Avenged Sevenfold','song'),(9,'Like walking into a dream, so unlike what you\'ve seen. So unsure it seems, \'cause we\'ve been waiting for you fallen into this place. Just giving you a small taste of your afterlife here to stay, you\'ll be back her soon anyway. I see a distant light, but girl, this can\'t be right. Such a surreal place to see, so how did this come to be?','Afterlife','Avenged Sevenfold','song'),(10,'Before the story begins, is it such a sin, for me to take what\'s mine, until the end of time. We were more than friends, before the story ends, and I will take what\'s mine, create what God would never design','A little peace of Heaven','Avenged Sevenfold','song'),(11,'There were people who lied for gain, people who lied from pain, people who lied simply because the concept of telling the truth was utterly alien to them... and then there were people who lied because they were waiting for it to be time to tell the truth.','Needful Things','Stephen King','book'),(12,'Yet one fine day, in a fit of euphoria, after he had picked up the telephone and taken an order for bonds that had brought him a fifty thousand dollar commission, just like that, this very phrase had bubbled up into his brain. On Wall Street he and a few others had become precisely that - Masters of the Universe.','Bonfire of the Vanities','Tom Wolfe','book'),(13,'The main thing to do is pay attention. Pay close attention to everything, notice what no one else notices. Then you\'ll know what no one else knows, and that\'s always useful.','The City of Ember','Jeanne DuPrau','book'),(14,'My tea\'s gone cold, I\'m wondering why I got out of ed at all. The morning rain clouds up my window, and I can\'t see at all. And even if I could it\'d all be gray, but your picture on my wall. It reminds me that it\'s not so bad, it\'s not so bad.','Stan','Eminem','song'),(15,'At first we were silent, but finally we considered ourselves under the necessity of protesting against such an unjust and serious accusation, before the face of the whole of intellectual society.','The Outrage - A True Story','Aleksandr Kuprin','book'),(16,'You can always build a shrine to all the terrible things in the world, but it doesn\'t mean that you have to destroy anything that is good.','Hereditary','Ari Aster','movie'),(17,'We were the people who were not in the papers. We lived in the blank white spaces at the edges of print.','The Handmaid\'s Tale','Margaret Atwood','book'),(18,'As long as we don\'t have magic, they will never treat us with respect. They need to know we can hit them back. If they burn our homes, we burn theirs, too.','Children of Blood and Bone','Tomi Adeyemi','book'),(19,'The invention of the wheel represented a major turning point in human civilization. Besides its use in transportation, the wheel went on to become the basic principle behind almost every mechanical device.','Civilization IV','Sid Meier','game'),(20,'They had taken it all away from him now, they had turned away from him and there was nothing for him now. He was alone and there was nothing for him.','Hatchet','Gary Paulsen','book'),(21,'He canceled all his commitments and pulled together the most important of his books, and now here he was sitting inside a dusty, smelly warehouse. Outside, a huge caravan was being prepared for a crossing of the Sahara, and was scheduled to pass through Al-Fayoum.','The Alchemist','Paulo Coelho','book'),(22,'It\'s a pity that folk as talk about fighting the Enemy can\'t let others do their bit in their own way without interfering. He\'d be mighty pleased, if he could see you now. Think he\'d got a new friend, he would.','The Two Towers','J.R.R Tolkien','book'),(23,'The only people for me are the mad ones, the ones who are mad to live, mad to talk, mad to be saved, desirous of everything at the same time, the ones who never yawn or say a commonplace thing, but burn, burn, burn, like fabulous yellow Roman candles exploding like spiders across the stars, and in the middle, you see the blue center-light pop, and everybody goes ahh...','On the Road','Jack Kerouac','book'),(24,'Death really did not matter to him but life did, and therefore the sensation he felt when they gave their decision was not a feeling of fear but of nostalgia.','One Hundred Years of Solitude','Gabriel Garcia Marquez','book'),(25,'Just let me wake up in the morning to the smell of new mown hay, to laugh and cry, to live and die in the brightness of my day. I want to hear the pealing bell of distant churches sing. But most of all please free me from this aching metal ring and open out this cage towards the sun.','Skyline Pigeon','Elton John','song'),(27,'The wonder is, not that the field of stars is so vast, but that man has measured it.','Civilization V','2k Games','game'),(28,'At last, we finally meet. I have something for you, Chancellor; a farewell gift. For all the things you\'ve done, for the things you might have done, and for the only thing you have left.','V for Vendetta','Warner Home Video','book'),(29,'All right I think we\'ve been down here in the dark long enough. There\'s a whole other world upstairs. Take my hand, Constant Reader, and I\'ll be happy to lead you back into the sunshine. I\'m happy to go there because I believe most people are essentially good. I know that I am. It\'s you I\'m not entirely sure of.','Full Dark, No Stars','Stephen King','book'),(30,'The wonders of life and the universe are mere reflections of microscopic particles engaged in a pointless dance fully choreographed by the laws of physics.','The Elegant Universe: Superstrings, Hidden Dimensions, and the Quest for the Ultimate Theory','Brian Greene','book');
/*!40000 ALTER TABLE `texts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tokens`
--

DROP TABLE IF EXISTS `tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tokens` (
  `token_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `token` varchar(256) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `expires_at` datetime DEFAULT NULL,
  `expired` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`token_id`),
  KEY `FK2dylsfo39lgjyqml2tbe0b0ss` (`user_id`),
  CONSTRAINT `FK2dylsfo39lgjyqml2tbe0b0ss` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=205 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tokens`
--

LOCK TABLES `tokens` WRITE;
/*!40000 ALTER TABLE `tokens` DISABLE KEYS */;
/*!40000 ALTER TABLE `tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_tests`
--

DROP TABLE IF EXISTS `user_tests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_tests` (
  `test_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `score` int(11) DEFAULT NULL,
  `text_id` bigint(20) NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`test_id`),
  KEY `user_id` (`user_id`),
  KEY `text_id` (`text_id`),
  CONSTRAINT `user_tests_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `user_tests_ibfk_2` FOREIGN KEY (`text_id`) REFERENCES `texts` (`text_id`)
) ENGINE=InnoDB AUTO_INCREMENT=224 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_tests`
--

LOCK TABLES `user_tests` WRITE;
/*!40000 ALTER TABLE `user_tests` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_tests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(256) NOT NULL,
  `token_id` bigint(20) DEFAULT NULL,
  `activated` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `token_id` (`token_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`token_id`) REFERENCES `tokens` (`token_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=203 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-22  6:50:13
