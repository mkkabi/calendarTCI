-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Sep 26, 2023 at 05:33 PM
-- Server version: 5.7.34
-- PHP Version: 7.3.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `schoolsql`
--

-- --------------------------------------------------------

--
-- Table structure for table `autorities`
--

CREATE TABLE IF NOT EXISTS `autorities` (
  `id` bigint(20) NOT NULL,
  `authority` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `classtypes`
--

CREATE TABLE IF NOT EXISTS `classtypes` (
  `id` bigint(20) NOT NULL,
  `color` varchar(255) DEFAULT NULL,
  `donotcalculate_hours` bit(1) DEFAULT NULL,
  `show_on_schedule` bit(1) DEFAULT NULL,
  `class_type` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `classtypes`
--

INSERT INTO `classtypes` (`id`, `color`, `donotcalculate_hours`, `show_on_schedule`, `class_type`) VALUES
(1, '#fffhhh', b'0', b'0', 'Лекція'),
(2, '#fe4343', b'0', b'1', 'Екзамен'),
(3, '#ffc7c7', b'0', b'1', 'Залік'),
(4, '#c4efb9', b'1', b'1', 'Відпустка'),
(20, '#5dd2f8', b'0', b'1', 'Консультація');

-- --------------------------------------------------------

--
-- Table structure for table `classtype_sequence`
--

CREATE TABLE IF NOT EXISTS `classtype_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `classtype_sequence`
--

INSERT INTO `classtype_sequence` (`next_val`) VALUES
(21);

-- --------------------------------------------------------

--
-- Table structure for table `disciplinename_sequence`
--

CREATE TABLE IF NOT EXISTS `disciplinename_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `disciplinename_sequence`
--

INSERT INTO `disciplinename_sequence` (`next_val`) VALUES
(99);

-- --------------------------------------------------------

--
-- Table structure for table `disciplines`
--

CREATE TABLE IF NOT EXISTS `disciplines` (
  `id` bigint(20) NOT NULL,
  `credits` int(11) NOT NULL,
  `semester` int(11) NOT NULL,
  `control_form_id` bigint(20) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `education_form_id` bigint(20) DEFAULT NULL,
  `discipline_name_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `disciplines`
--

INSERT INTO `disciplines` (`id`, `credits`, `semester`, `control_form_id`, `group_id`, `education_form_id`, `discipline_name_id`) VALUES
(21, 6, 5, 3, 30, 1, 21),
(22, 4, 3, 2, 31, 1, 19),
(23, 4, 3, 3, 31, 1, 17),
(24, 4, 3, 2, 31, 1, 9),
(25, 6, 3, 3, 31, 1, 22),
(26, 4, 3, 3, 31, 1, 20),
(27, 10, 5, 2, 30, 1, 22),
(28, 4, 5, 3, 30, 1, 6),
(29, 4, 5, 3, 30, 1, 10),
(30, 2, 5, 2, 30, 1, 35),
(31, 4, 3, 3, 31, 1, 2),
(32, 1, 3, 2, 31, 1, 3),
(33, 1, 5, 2, 30, 1, 15),
(34, 4, 6, 2, 37, 2, 25),
(35, 4, 6, 2, 37, 2, 97),
(36, 4, 6, 2, 37, 2, 14),
(37, 4, 6, 2, 37, 2, 17),
(38, 4, 6, 3, 37, 2, 24),
(39, 4, 6, 3, 37, 2, 23),
(40, 1, 6, 3, 37, 2, 15),
(41, 4, 6, 3, 37, 2, 20),
(42, 4, 6, 3, 37, 2, 98);

-- --------------------------------------------------------

--
-- Table structure for table `disciplines_teachers`
--

CREATE TABLE IF NOT EXISTS `disciplines_teachers` (
  `teacher_id` bigint(20) NOT NULL,
  `discipline_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `disciplines_teachers`
--

INSERT INTO `disciplines_teachers` (`teacher_id`, `discipline_id`) VALUES
(11, 21),
(9, 22),
(8, 23),
(5, 24),
(11, 25),
(10, 26),
(11, 27),
(4, 28),
(5, 29),
(6, 30),
(1, 31),
(7, 32),
(7, 33),
(31, 34),
(5, 35),
(7, 36),
(8, 37),
(32, 38),
(12, 39),
(7, 40),
(10, 41),
(4, 42);

-- --------------------------------------------------------

--
-- Table structure for table `discipline_names`
--

CREATE TABLE IF NOT EXISTS `discipline_names` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `discipline_names`
--

INSERT INTO `discipline_names` (`id`, `name`) VALUES
(1, 'Англ мова'),
(2, 'Англ.мова. Поглибленний курс'),
(3, 'Навчальна практика'),
(4, 'Сучасні інформаційні технології'),
(5, 'Загальна психологія'),
(6, 'Менеджмент'),
(7, 'Конституційне право'),
(8, 'Духовне формування християнина'),
(9, 'Євангелізм та учнівство'),
(10, 'Екзегетика книги Ісуса Навина'),
(11, 'Організація нових церков'),
(12, 'Біблійна герменевтика'),
(13, 'Історія Укр. та укр. культури'),
(14, 'Історія христ-ва'),
(15, 'Виробнича практика'),
(16, 'Релігієзнавство'),
(17, 'Філософія'),
(18, 'Логіка'),
(19, 'Основи грецької мови НЗ'),
(20, 'Історія Єванг. руху в Україні'),
(21, 'Систематичне богослов’я'),
(22, 'Ісагогіка та огляд Нового Завіту'),
(23, 'Біблійна географія'),
(24, 'Основи наукових досліджень'),
(25, 'Українська мова за проф. спрямуван'),
(26, 'Педагогіка'),
(27, 'Ісагогіка та огляд Старого Завіту'),
(28, 'Методика викладання предметів духовно-морального спрямування'),
(29, 'Патристика'),
(30, 'Пастирське богослов’я'),
(31, 'Християнські принципи розпорядження фінансами'),
(32, 'Екзегетика книги Суддів'),
(33, 'Богослов’я та практика молитви'),
(34, 'Богослов’я та практика поклоніння'),
(35, 'Курсова з герменевтики'),
(36, 'Служіння для здобувачів освіти'),
(97, 'Християнська етика'),
(98, 'Шлюб та сім\'я');

-- --------------------------------------------------------

--
-- Table structure for table `discipline_sequence`
--

CREATE TABLE IF NOT EXISTS `discipline_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `discipline_sequence`
--

INSERT INTO `discipline_sequence` (`next_val`) VALUES
(43);

-- --------------------------------------------------------

--
-- Table structure for table `educationform_sequence`
--

CREATE TABLE IF NOT EXISTS `educationform_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `educationform_sequence`
--

INSERT INTO `educationform_sequence` (`next_val`) VALUES
(30);

-- --------------------------------------------------------

--
-- Table structure for table `education_form`
--

CREATE TABLE IF NOT EXISTS `education_form` (
  `id` bigint(20) NOT NULL,
  `title` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `education_form`
--

INSERT INTO `education_form` (`id`, `title`) VALUES
(1, 'Денна'),
(2, 'Заочна');

-- --------------------------------------------------------

--
-- Table structure for table `events`
--

CREATE TABLE IF NOT EXISTS `events` (
  `id` bigint(20) NOT NULL,
  `day_of_week` varchar(255) NOT NULL,
  `duration_minutes` bigint(20) DEFAULT NULL,
  `end_time` datetime NOT NULL,
  `start_time` datetime NOT NULL,
  `title` varchar(255) NOT NULL,
  `week_number` int(11) NOT NULL,
  `owner_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `event_attendees`
--

CREATE TABLE IF NOT EXISTS `event_attendees` (
  `attendee_id` bigint(20) NOT NULL,
  `event_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `groups`
--

CREATE TABLE IF NOT EXISTS `groups` (
  `id` bigint(20) NOT NULL,
  `admittance_year` varchar(20) NOT NULL,
  `alternative_name` varchar(255) DEFAULT NULL,
  `course_number` int(11) NOT NULL,
  `graduated` bit(1) DEFAULT NULL,
  `group_number` varchar(32) NOT NULL,
  `session_end` date DEFAULT NULL,
  `session_start` date DEFAULT NULL,
  `education_form_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `groups`
--

INSERT INTO `groups` (`id`, `admittance_year`, `alternative_name`, `course_number`, `graduated`, `group_number`, `session_end`, `session_start`, `education_form_id`) VALUES
(30, '2021', '', 3, b'0', '3', '2024-01-12', '2023-12-18', 1),
(31, '2022', '', 2, b'0', '2', '2024-01-12', '2023-12-18', 1),
(37, '2019', '', 3, b'0', '3 - Заочна', NULL, NULL, 2);

-- --------------------------------------------------------

--
-- Table structure for table `groups_teachers`
--

CREATE TABLE IF NOT EXISTS `groups_teachers` (
  `teacher_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `group_sequence`
--

CREATE TABLE IF NOT EXISTS `group_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `group_sequence`
--

INSERT INTO `group_sequence` (`next_val`) VALUES
(38);

-- --------------------------------------------------------

--
-- Table structure for table `hibernate_sequence`
--

CREATE TABLE IF NOT EXISTS `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(1);

-- --------------------------------------------------------

--
-- Table structure for table `lessons`
--

CREATE TABLE IF NOT EXISTS `lessons` (
  `id` bigint(20) NOT NULL,
  `auditorium_number` varchar(255) DEFAULT NULL,
  `day_of_week` varchar(255) NOT NULL,
  `end_date_time` datetime DEFAULT NULL,
  `month` int(11) NOT NULL,
  `online` bit(1) DEFAULT NULL,
  `start_date_time` datetime DEFAULT NULL,
  `week_number` int(11) NOT NULL,
  `year` int(11) NOT NULL,
  `classtype_id` bigint(20) NOT NULL,
  `discipline_id` bigint(20) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `teacher_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lessons`
--

INSERT INTO `lessons` (`id`, `auditorium_number`, `day_of_week`, `end_date_time`, `month`, `online`, `start_date_time`, `week_number`, `year`, `classtype_id`, `discipline_id`, `group_id`, `teacher_id`) VALUES
(21, '', 'MONDAY', '2023-09-18 10:20:00', 9, b'0', '2023-09-18 09:00:00', 38, 2023, 1, 21, 30, 11),
(22, '', 'MONDAY', '2023-09-18 12:00:00', 9, b'0', '2023-09-18 10:40:00', 38, 2023, 1, 21, 30, 11),
(24, '', 'MONDAY', '2023-09-18 13:40:00', 9, b'0', '2023-09-18 12:20:00', 38, 2023, 1, 21, 30, 11),
(25, '', 'MONDAY', '2023-09-04 13:40:00', 9, b'0', '2023-09-04 12:20:00', 36, 2023, 1, 26, 31, 10),
(26, '', 'MONDAY', '2023-09-04 16:00:00', 9, b'0', '2023-09-04 14:40:00', 36, 2023, 1, 26, 31, 10),
(27, '', 'MONDAY', '2023-09-04 17:40:00', 9, b'0', '2023-09-04 16:20:00', 36, 2023, 1, 22, 31, 9),
(28, '', 'TUESDAY', '2023-09-05 12:00:00', 9, b'1', '2023-09-05 10:40:00', 36, 2023, 1, 24, 31, 5),
(29, '', 'TUESDAY', '2023-09-05 13:40:00', 9, b'1', '2023-09-05 12:20:00', 36, 2023, 1, 24, 31, 5),
(30, '', 'TUESDAY', '2023-09-05 16:00:00', 9, b'1', '2023-09-05 14:40:00', 36, 2023, 1, 23, 31, 8),
(31, '', 'TUESDAY', '2023-09-05 17:40:00', 9, b'1', '2023-09-05 16:20:00', 36, 2023, 1, 23, 31, 8),
(32, '', 'WEDNESDAY', '2023-09-06 13:40:00', 9, b'0', '2023-09-06 12:20:00', 36, 2023, 1, 22, 31, 9),
(33, '', 'THURSDAY', '2023-09-07 12:00:00', 9, b'0', '2023-09-07 10:40:00', 36, 2023, 1, 25, 31, 11),
(34, '', 'THURSDAY', '2023-09-07 13:40:00', 9, b'0', '2023-09-07 12:20:00', 36, 2023, 1, 25, 31, 11),
(35, '', 'THURSDAY', '2023-09-07 16:00:00', 9, b'0', '2023-09-07 14:40:00', 36, 2023, 1, 25, 31, 11),
(36, '', 'FRIDAY', '2023-09-08 12:00:00', 9, b'0', '2023-09-08 10:40:00', 36, 2023, 1, 26, 31, 10),
(37, '', 'FRIDAY', '2023-09-08 13:40:00', 9, b'0', '2023-09-08 12:20:00', 36, 2023, 1, 26, 31, 10),
(38, '', 'MONDAY', '2023-09-11 13:40:00', 9, b'0', '2023-09-11 12:20:00', 37, 2023, 1, 26, 31, 10),
(39, '', 'MONDAY', '2023-09-11 16:00:00', 9, b'0', '2023-09-11 14:40:00', 37, 2023, 1, 26, 31, 10),
(40, '', 'MONDAY', '2023-09-11 17:40:00', 9, b'0', '2023-09-11 16:20:00', 37, 2023, 1, 22, 31, 9),
(41, '', 'TUESDAY', '2023-09-12 12:00:00', 9, b'1', '2023-09-12 10:40:00', 37, 2023, 1, 24, 31, 5),
(42, '', 'TUESDAY', '2023-09-12 13:40:00', 9, b'1', '2023-09-12 12:20:00', 37, 2023, 1, 24, 31, 5),
(43, '', 'TUESDAY', '2023-09-12 16:00:00', 9, b'1', '2023-09-12 14:40:00', 37, 2023, 1, 23, 31, 8),
(44, '', 'TUESDAY', '2023-09-12 17:40:00', 9, b'1', '2023-09-12 16:20:00', 37, 2023, 1, 23, 31, 8),
(45, '', 'WEDNESDAY', '2023-09-13 13:40:00', 9, b'0', '2023-09-13 12:20:00', 37, 2023, 1, 22, 31, 9),
(46, '', 'THURSDAY', '2023-09-14 13:40:00', 9, b'0', '2023-09-14 12:20:00', 37, 2023, 1, 25, 31, 11),
(47, '', 'THURSDAY', '2023-09-14 16:00:00', 9, b'0', '2023-09-14 14:40:00', 37, 2023, 1, 25, 31, 11),
(48, '', 'THURSDAY', '2023-09-14 17:40:00', 9, b'0', '2023-09-14 16:20:00', 37, 2023, 1, 25, 31, 11),
(74, '', 'FRIDAY', '2023-09-22 13:40:00', 9, b'0', '2023-09-22 12:20:00', 38, 2023, 1, 26, 31, 10),
(73, '', 'FRIDAY', '2023-09-22 12:00:00', 9, b'0', '2023-09-22 10:40:00', 38, 2023, 1, 26, 31, 10),
(51, '', 'MONDAY', '2023-09-18 13:40:00', 9, b'0', '2023-09-18 12:20:00', 38, 2023, 1, 26, 31, 10),
(52, '', 'MONDAY', '2023-09-18 16:00:00', 9, b'0', '2023-09-18 14:40:00', 38, 2023, 1, 26, 31, 10),
(53, '', 'MONDAY', '2023-09-18 17:40:00', 9, b'0', '2023-09-18 16:20:00', 38, 2023, 1, 22, 31, 9),
(54, '', 'TUESDAY', '2023-09-19 12:00:00', 9, b'1', '2023-09-19 10:40:00', 38, 2023, 1, 24, 31, 5),
(55, '', 'TUESDAY', '2023-09-19 13:40:00', 9, b'1', '2023-09-19 12:20:00', 38, 2023, 1, 24, 31, 5),
(56, '', 'TUESDAY', '2023-09-19 16:00:00', 9, b'1', '2023-09-19 14:40:00', 38, 2023, 1, 23, 31, 8),
(57, '', 'TUESDAY', '2023-09-19 17:40:00', 9, b'1', '2023-09-19 16:20:00', 38, 2023, 1, 23, 31, 8),
(58, '', 'WEDNESDAY', '2023-09-20 13:40:00', 9, b'0', '2023-09-20 12:20:00', 38, 2023, 1, 22, 31, 9),
(59, '', 'THURSDAY', '2023-09-21 10:20:00', 9, b'0', '2023-09-21 09:00:00', 38, 2023, 1, 25, 31, 11),
(60, '', 'THURSDAY', '2023-09-21 12:00:00', 9, b'0', '2023-09-21 10:40:00', 38, 2023, 1, 25, 31, 11),
(61, '', 'THURSDAY', '2023-09-21 13:40:00', 9, b'0', '2023-09-21 12:20:00', 38, 2023, 1, 25, 31, 11),
(62, '', 'WEDNESDAY', '2023-09-06 10:20:00', 9, b'0', '2023-09-06 09:00:00', 36, 2023, 1, 31, 31, 1),
(63, '', 'WEDNESDAY', '2023-09-06 12:00:00', 9, b'0', '2023-09-06 10:40:00', 36, 2023, 1, 31, 31, 1),
(64, '', 'WEDNESDAY', '2023-09-13 10:20:00', 9, b'0', '2023-09-13 09:00:00', 37, 2023, 1, 31, 31, 1),
(65, '', 'WEDNESDAY', '2023-09-13 12:00:00', 9, b'0', '2023-09-13 10:40:00', 37, 2023, 1, 31, 31, 1),
(66, '', 'WEDNESDAY', '2023-09-20 10:20:00', 9, b'0', '2023-09-20 09:00:00', 38, 2023, 1, 31, 31, 1),
(67, '', 'WEDNESDAY', '2023-09-20 12:00:00', 9, b'0', '2023-09-20 10:40:00', 38, 2023, 1, 31, 31, 1),
(72, '', 'FRIDAY', '2023-09-15 13:40:00', 9, b'0', '2023-09-15 12:20:00', 37, 2023, 1, 26, 31, 10),
(69, '', 'THURSDAY', '2023-09-07 10:20:00', 9, b'0', '2023-09-07 09:00:00', 36, 2023, 20, 32, 31, 7),
(71, '', 'FRIDAY', '2023-09-15 12:00:00', 9, b'0', '2023-09-15 10:40:00', 37, 2023, 1, 26, 31, 10),
(75, '', 'MONDAY', '2023-09-04 10:20:00', 9, b'0', '2023-09-04 09:00:00', 36, 2023, 1, 21, 30, 11),
(76, '', 'MONDAY', '2023-09-04 12:00:00', 9, b'0', '2023-09-04 10:40:00', 36, 2023, 1, 21, 30, 11),
(77, '', 'MONDAY', '2023-09-04 13:40:00', 9, b'0', '2023-09-04 12:20:00', 36, 2023, 1, 21, 30, 11),
(78, '', 'TUESDAY', '2023-09-05 10:20:00', 9, b'0', '2023-09-05 09:00:00', 36, 2023, 1, 27, 30, 11),
(79, '', 'TUESDAY', '2023-09-05 12:00:00', 9, b'0', '2023-09-05 10:40:00', 36, 2023, 1, 27, 30, 11),
(80, '', 'TUESDAY', '2023-09-05 13:40:00', 9, b'0', '2023-09-05 12:20:00', 36, 2023, 1, 27, 30, 11),
(81, '', 'WEDNESDAY', '2023-09-06 10:20:00', 9, b'0', '2023-09-06 09:00:00', 36, 2023, 20, 33, 30, 7),
(82, '', 'WEDNESDAY', '2023-09-06 12:00:00', 9, b'0', '2023-09-06 10:40:00', 36, 2023, 1, 27, 30, 11),
(83, '', 'WEDNESDAY', '2023-09-06 13:40:00', 9, b'0', '2023-09-06 12:20:00', 36, 2023, 1, 27, 30, 11),
(84, '', 'WEDNESDAY', '2023-09-06 16:00:00', 9, b'0', '2023-09-06 14:40:00', 36, 2023, 1, 27, 30, 11),
(85, '', 'THURSDAY', '2023-09-07 10:20:00', 9, b'1', '2023-09-07 09:00:00', 36, 2023, 1, 30, 30, 6),
(86, '', 'THURSDAY', '2023-09-07 12:00:00', 9, b'1', '2023-09-07 10:40:00', 36, 2023, 1, 29, 30, 5),
(87, '', 'THURSDAY', '2023-09-07 13:40:00', 9, b'1', '2023-09-07 12:20:00', 36, 2023, 1, 29, 30, 5),
(88, '', 'FRIDAY', '2023-09-08 12:00:00', 9, b'0', '2023-09-08 10:40:00', 36, 2023, 1, 28, 30, 4),
(89, '', 'FRIDAY', '2023-09-08 13:40:00', 9, b'0', '2023-09-08 12:20:00', 36, 2023, 1, 28, 30, 4),
(90, '', 'MONDAY', '2023-09-11 10:20:00', 9, b'0', '2023-09-11 09:00:00', 37, 2023, 1, 21, 30, 11),
(91, '', 'MONDAY', '2023-09-11 12:00:00', 9, b'0', '2023-09-11 10:40:00', 37, 2023, 1, 21, 30, 11),
(92, '', 'MONDAY', '2023-09-11 13:40:00', 9, b'0', '2023-09-11 12:20:00', 37, 2023, 1, 21, 30, 11),
(93, '', 'MONDAY', '2023-09-11 16:00:00', 9, b'0', '2023-09-11 14:40:00', 37, 2023, 1, 27, 30, 11),
(94, '', 'TUESDAY', '2023-09-12 10:20:00', 9, b'0', '2023-09-12 09:00:00', 37, 2023, 1, 27, 30, 11),
(95, '', 'TUESDAY', '2023-09-12 12:00:00', 9, b'0', '2023-09-12 10:40:00', 37, 2023, 1, 27, 30, 11),
(96, '', 'WEDNESDAY', '2023-09-13 10:20:00', 9, b'0', '2023-09-13 09:00:00', 37, 2023, 1, 27, 30, 11),
(97, '', 'WEDNESDAY', '2023-09-13 12:00:00', 9, b'0', '2023-09-13 10:40:00', 37, 2023, 1, 27, 30, 11),
(98, '', 'THURSDAY', '2023-09-14 10:20:00', 9, b'1', '2023-09-14 09:00:00', 37, 2023, 1, 30, 30, 6),
(99, '', 'THURSDAY', '2023-09-14 12:00:00', 9, b'0', '2023-09-14 10:40:00', 37, 2023, 1, 27, 30, 11),
(100, '', 'THURSDAY', '2023-09-14 13:40:00', 9, b'1', '2023-09-14 12:20:00', 37, 2023, 1, 29, 30, 5),
(101, '', 'THURSDAY', '2023-09-14 16:00:00', 9, b'1', '2023-09-14 14:40:00', 37, 2023, 1, 29, 30, 5),
(102, '', 'FRIDAY', '2023-09-15 12:00:00', 9, b'0', '2023-09-15 10:40:00', 37, 2023, 1, 28, 30, 4),
(103, '', 'FRIDAY', '2023-09-15 13:40:00', 9, b'0', '2023-09-15 12:20:00', 37, 2023, 1, 28, 30, 4),
(104, '', 'MONDAY', '2023-09-18 16:00:00', 9, b'0', '2023-09-18 14:40:00', 38, 2023, 1, 27, 30, 11),
(105, '', 'TUESDAY', '2023-09-19 10:20:00', 9, b'0', '2023-09-19 09:00:00', 38, 2023, 1, 27, 30, 11),
(106, '', 'TUESDAY', '2023-09-19 12:00:00', 9, b'0', '2023-09-19 10:40:00', 38, 2023, 1, 27, 30, 11),
(107, '', 'WEDNESDAY', '2023-09-20 10:20:00', 9, b'0', '2023-09-20 09:00:00', 38, 2023, 1, 27, 30, 11),
(108, '', 'WEDNESDAY', '2023-09-20 12:00:00', 9, b'0', '2023-09-20 10:40:00', 38, 2023, 1, 27, 30, 11),
(109, '', 'WEDNESDAY', '2023-09-20 13:40:00', 9, b'0', '2023-09-20 12:20:00', 38, 2023, 1, 27, 30, 11),
(110, '', 'THURSDAY', '2023-09-21 10:20:00', 9, b'1', '2023-09-21 09:00:00', 38, 2023, 1, 30, 30, 6),
(111, '', 'THURSDAY', '2023-09-21 12:00:00', 9, b'1', '2023-09-21 10:40:00', 38, 2023, 1, 29, 30, 5),
(112, '', 'THURSDAY', '2023-09-21 13:40:00', 9, b'1', '2023-09-21 12:20:00', 38, 2023, 1, 29, 30, 5),
(113, '', 'FRIDAY', '2023-09-22 12:00:00', 9, b'0', '2023-09-22 10:40:00', 38, 2023, 1, 28, 30, 4),
(114, '', 'FRIDAY', '2023-09-22 13:40:00', 9, b'0', '2023-09-22 12:20:00', 38, 2023, 1, 28, 30, 4),
(117, '', 'MONDAY', '2023-09-25 13:40:00', 9, b'0', '2023-09-25 12:20:00', 39, 2023, 1, 26, 31, 10),
(118, NULL, 'MONDAY', '2023-09-25 16:00:00', 9, b'0', '2023-09-25 14:40:00', 39, 2023, 1, 26, 31, 10),
(119, NULL, 'FRIDAY', '2023-09-29 12:00:00', 9, b'0', '2023-09-29 10:40:00', 39, 2023, 1, 26, 31, 10),
(120, NULL, 'FRIDAY', '2023-09-29 13:40:00', 9, b'0', '2023-09-29 12:20:00', 39, 2023, 1, 26, 31, 10),
(121, '', 'MONDAY', '2023-09-25 17:40:00', 9, b'0', '2023-09-25 16:20:00', 39, 2023, 1, 22, 31, 9),
(122, NULL, 'WEDNESDAY', '2023-09-27 13:40:00', 9, b'0', '2023-09-27 12:20:00', 39, 2023, 1, 22, 31, 9),
(125, '', 'THURSDAY', '2023-09-28 17:40:00', 9, b'1', '2023-09-28 16:20:00', 39, 2023, 1, 23, 31, 8),
(126, NULL, 'TUESDAY', '2023-09-26 17:40:00', 9, b'1', '2023-09-26 16:20:00', 39, 2023, 1, 23, 31, 8),
(127, '', 'THURSDAY', '2023-09-28 13:40:00', 9, b'1', '2023-09-28 12:20:00', 39, 2023, 1, 24, 31, 5),
(128, NULL, 'TUESDAY', '2023-09-26 16:00:00', 9, b'1', '2023-09-26 14:40:00', 39, 2023, 1, 24, 31, 5),
(129, '', 'MONDAY', '2023-09-25 12:00:00', 9, b'0', '2023-09-25 10:40:00', 39, 2023, 1, 31, 31, 1),
(130, NULL, 'WEDNESDAY', '2023-09-27 12:00:00', 9, b'0', '2023-09-27 10:40:00', 39, 2023, 1, 31, 31, 1),
(131, '', 'TUESDAY', '2023-09-26 13:40:00', 9, b'0', '2023-09-26 12:20:00', 39, 2023, 1, 25, 31, 11),
(132, NULL, 'THURSDAY', '2023-09-28 16:00:00', 9, b'0', '2023-09-28 14:40:00', 39, 2023, 1, 25, 31, 11),
(133, NULL, 'WEDNESDAY', '2023-09-27 16:00:00', 9, b'0', '2023-09-27 14:40:00', 39, 2023, 1, 25, 31, 11),
(134, '', 'TUESDAY', '2023-09-26 12:00:00', 9, b'0', '2023-09-26 10:40:00', 39, 2023, 1, 21, 30, 11),
(135, NULL, 'WEDNESDAY', '2023-09-27 12:00:00', 9, b'0', '2023-09-27 10:40:00', 39, 2023, 1, 21, 30, 11),
(136, NULL, 'THURSDAY', '2023-09-28 12:00:00', 9, b'0', '2023-09-28 10:40:00', 39, 2023, 1, 21, 30, 11),
(137, '', 'MONDAY', '2023-09-25 12:00:00', 9, b'0', '2023-09-25 10:40:00', 39, 2023, 1, 27, 30, 11),
(138, NULL, 'TUESDAY', '2023-09-26 10:20:00', 9, b'0', '2023-09-26 09:00:00', 39, 2023, 1, 27, 30, 11),
(139, NULL, 'WEDNESDAY', '2023-09-27 13:40:00', 9, b'0', '2023-09-27 12:20:00', 39, 2023, 1, 27, 30, 11),
(140, NULL, 'THURSDAY', '2023-09-28 13:40:00', 9, b'0', '2023-09-28 12:20:00', 39, 2023, 1, 27, 30, 11),
(141, NULL, 'FRIDAY', '2023-09-29 16:00:00', 9, b'0', '2023-09-29 14:40:00', 39, 2023, 1, 27, 30, 11),
(143, '', 'THURSDAY', '2023-09-28 10:20:00', 9, b'1', '2023-09-28 09:00:00', 39, 2023, 1, 30, 30, 6),
(145, '', 'TUESDAY', '2023-09-26 13:40:00', 9, b'1', '2023-09-26 12:20:00', 39, 2023, 1, 29, 30, 5),
(146, NULL, 'MONDAY', '2023-09-25 13:40:00', 9, b'1', '2023-09-25 12:20:00', 39, 2023, 1, 29, 30, 5),
(147, '', 'FRIDAY', '2023-09-29 12:00:00', 9, b'0', '2023-09-29 10:40:00', 39, 2023, 1, 28, 30, 4),
(148, NULL, 'FRIDAY', '2023-09-29 13:40:00', 9, b'0', '2023-09-29 12:20:00', 39, 2023, 1, 28, 30, 4),
(149, NULL, 'MONDAY', '2023-10-02 13:40:00', 10, b'0', '2023-10-02 12:20:00', 40, 2023, 1, 26, 31, 10),
(150, NULL, 'MONDAY', '2023-10-02 16:00:00', 10, b'0', '2023-10-02 14:40:00', 40, 2023, 1, 26, 31, 10),
(151, NULL, 'FRIDAY', '2023-10-06 12:00:00', 10, b'0', '2023-10-06 10:40:00', 40, 2023, 1, 26, 31, 10),
(152, NULL, 'FRIDAY', '2023-10-06 13:40:00', 10, b'0', '2023-10-06 12:20:00', 40, 2023, 1, 26, 31, 10),
(153, NULL, 'MONDAY', '2023-10-02 17:40:00', 10, b'0', '2023-10-02 16:20:00', 40, 2023, 1, 22, 31, 9),
(154, NULL, 'WEDNESDAY', '2023-10-04 13:40:00', 10, b'0', '2023-10-04 12:20:00', 40, 2023, 1, 22, 31, 9),
(280, '', 'MONDAY', '2023-10-09 10:20:00', 10, b'0', '2023-10-09 09:00:00', 41, 2023, 2, 36, 37, 7),
(156, NULL, 'TUESDAY', '2023-10-03 17:40:00', 10, b'1', '2023-10-03 16:20:00', 40, 2023, 1, 23, 31, 8),
(157, NULL, 'THURSDAY', '2023-10-05 13:40:00', 10, b'1', '2023-10-05 12:20:00', 40, 2023, 1, 24, 31, 5),
(158, NULL, 'TUESDAY', '2023-10-03 16:00:00', 10, b'1', '2023-10-03 14:40:00', 40, 2023, 1, 24, 31, 5),
(159, NULL, 'MONDAY', '2023-10-02 12:00:00', 10, b'0', '2023-10-02 10:40:00', 40, 2023, 1, 31, 31, 1),
(160, NULL, 'WEDNESDAY', '2023-10-04 12:00:00', 10, b'0', '2023-10-04 10:40:00', 40, 2023, 1, 31, 31, 1),
(161, NULL, 'TUESDAY', '2023-10-03 13:40:00', 10, b'0', '2023-10-03 12:20:00', 40, 2023, 1, 25, 31, 11),
(162, NULL, 'THURSDAY', '2023-10-05 16:00:00', 10, b'0', '2023-10-05 14:40:00', 40, 2023, 1, 25, 31, 11),
(163, NULL, 'WEDNESDAY', '2023-10-04 16:00:00', 10, b'0', '2023-10-04 14:40:00', 40, 2023, 1, 25, 31, 11),
(195, '', 'THURSDAY', '2023-10-05 13:40:00', 10, b'0', '2023-10-05 12:20:00', 40, 2023, 1, 34, 37, 31),
(196, NULL, 'TUESDAY', '2023-10-03 13:40:00', 10, b'0', '2023-10-03 12:20:00', 40, 2023, 1, 34, 37, 31),
(197, NULL, 'FRIDAY', '2023-10-06 12:00:00', 10, b'0', '2023-10-06 10:40:00', 40, 2023, 1, 34, 37, 31),
(198, NULL, 'SATURDAY', '2023-10-07 13:40:00', 10, b'0', '2023-10-07 12:20:00', 40, 2023, 1, 34, 37, 31),
(199, '', 'SATURDAY', '2023-10-07 16:00:00', 10, b'0', '2023-10-07 14:40:00', 40, 2023, 20, 34, 37, 31),
(201, NULL, 'THURSDAY', '2023-10-05 17:40:00', 10, b'1', '2023-10-05 16:20:00', 40, 2023, 1, 23, 31, 8),
(215, '', 'MONDAY', '2023-10-02 12:00:00', 10, b'0', '2023-10-02 10:40:00', 40, 2023, 1, 35, 37, 5),
(216, NULL, 'TUESDAY', '2023-10-03 12:00:00', 10, b'0', '2023-10-03 10:40:00', 40, 2023, 1, 35, 37, 5),
(217, NULL, 'THURSDAY', '2023-10-05 10:20:00', 10, b'0', '2023-10-05 09:00:00', 40, 2023, 1, 35, 37, 5),
(218, NULL, 'WEDNESDAY', '2023-10-04 12:00:00', 10, b'0', '2023-10-04 10:40:00', 40, 2023, 1, 35, 37, 5),
(219, '', 'THURSDAY', '2023-10-05 12:00:00', 10, b'0', '2023-10-05 10:40:00', 40, 2023, 20, 35, 37, 5),
(220, '', 'FRIDAY', '2023-10-06 10:20:00', 10, b'0', '2023-10-06 09:00:00', 40, 2023, 2, 35, 37, 5),
(221, '', 'SATURDAY', '2023-10-07 10:20:00', 10, b'0', '2023-10-07 09:00:00', 40, 2023, 1, 42, 37, 4),
(222, NULL, 'SATURDAY', '2023-10-07 12:00:00', 10, b'0', '2023-10-07 10:40:00', 40, 2023, 1, 42, 37, 4),
(223, '', 'SATURDAY', '2023-10-14 10:20:00', 10, b'0', '2023-10-14 09:00:00', 41, 2023, 1, 42, 37, 4),
(224, '', 'SATURDAY', '2023-10-14 12:00:00', 10, b'0', '2023-10-14 10:40:00', 41, 2023, 3, 42, 37, 4),
(225, '', 'WEDNESDAY', '2023-10-04 16:00:00', 10, b'0', '2023-10-04 14:40:00', 40, 2023, 1, 37, 37, 8),
(253, NULL, 'TUESDAY', '2023-10-10 13:40:00', 10, b'0', '2023-10-10 12:20:00', 41, 2023, 1, 25, 31, 11),
(252, NULL, 'WEDNESDAY', '2023-10-11 12:00:00', 10, b'0', '2023-10-11 10:40:00', 41, 2023, 1, 31, 31, 1),
(242, NULL, 'MONDAY', '2023-10-09 13:40:00', 10, b'0', '2023-10-09 12:20:00', 41, 2023, 1, 26, 31, 10),
(243, NULL, 'MONDAY', '2023-10-09 16:00:00', 10, b'0', '2023-10-09 14:40:00', 41, 2023, 1, 26, 31, 10),
(244, NULL, 'FRIDAY', '2023-10-13 12:00:00', 10, b'0', '2023-10-13 10:40:00', 41, 2023, 1, 26, 31, 10),
(245, NULL, 'FRIDAY', '2023-10-13 13:40:00', 10, b'0', '2023-10-13 12:20:00', 41, 2023, 1, 26, 31, 10),
(246, NULL, 'MONDAY', '2023-10-09 17:40:00', 10, b'0', '2023-10-09 16:20:00', 41, 2023, 1, 22, 31, 9),
(247, NULL, 'WEDNESDAY', '2023-10-11 13:40:00', 10, b'0', '2023-10-11 12:20:00', 41, 2023, 1, 22, 31, 9),
(248, NULL, 'TUESDAY', '2023-10-10 17:40:00', 10, b'1', '2023-10-10 16:20:00', 41, 2023, 1, 23, 31, 8),
(249, NULL, 'THURSDAY', '2023-10-12 13:40:00', 10, b'1', '2023-10-12 12:20:00', 41, 2023, 1, 24, 31, 5),
(250, NULL, 'TUESDAY', '2023-10-10 16:00:00', 10, b'1', '2023-10-10 14:40:00', 41, 2023, 1, 24, 31, 5),
(251, NULL, 'MONDAY', '2023-10-09 12:00:00', 10, b'0', '2023-10-09 10:40:00', 41, 2023, 1, 31, 31, 1),
(254, NULL, 'THURSDAY', '2023-10-12 16:00:00', 10, b'0', '2023-10-12 14:40:00', 41, 2023, 1, 25, 31, 11),
(255, NULL, 'WEDNESDAY', '2023-10-11 16:00:00', 10, b'0', '2023-10-11 14:40:00', 41, 2023, 1, 25, 31, 11),
(256, NULL, 'THURSDAY', '2023-10-12 17:40:00', 10, b'1', '2023-10-12 16:20:00', 41, 2023, 1, 23, 31, 8),
(301, NULL, 'THURSDAY', '2023-10-05 16:00:00', 10, b'0', '2023-10-05 14:40:00', 40, 2023, 1, 37, 37, 8),
(272, '', 'MONDAY', '2023-10-09 13:40:00', 10, b'0', '2023-10-09 12:20:00', 41, 2023, 20, 37, 37, 8),
(273, '', 'WEDNESDAY', '2023-10-11 10:20:00', 10, b'0', '2023-10-11 09:00:00', 41, 2023, 2, 37, 37, 8),
(274, '', 'WEDNESDAY', '2023-10-04 10:20:00', 10, b'0', '2023-10-04 09:00:00', 40, 2023, 1, 36, 37, 7),
(296, '', 'FRIDAY', '2023-10-06 16:00:00', 10, b'0', '2023-10-06 14:40:00', 40, 2023, 20, 36, 37, 7),
(276, NULL, 'MONDAY', '2023-10-02 10:20:00', 10, b'0', '2023-10-02 09:00:00', 40, 2023, 1, 36, 37, 7),
(277, NULL, 'TUESDAY', '2023-10-03 10:20:00', 10, b'0', '2023-10-03 09:00:00', 40, 2023, 1, 36, 37, 7),
(295, NULL, 'FRIDAY', '2023-10-06 13:40:00', 10, b'0', '2023-10-06 12:20:00', 40, 2023, 1, 36, 37, 7),
(279, NULL, 'TUESDAY', '2023-10-03 16:00:00', 10, b'0', '2023-10-03 14:40:00', 40, 2023, 1, 37, 37, 8),
(298, NULL, 'FRIDAY', '2023-10-13 13:40:00', 10, b'0', '2023-10-13 12:20:00', 41, 2023, 1, 38, 37, 32),
(282, NULL, 'THURSDAY', '2023-10-12 12:00:00', 10, b'0', '2023-10-12 10:40:00', 41, 2023, 1, 38, 37, 32),
(283, NULL, 'TUESDAY', '2023-10-10 13:40:00', 10, b'0', '2023-10-10 12:20:00', 41, 2023, 1, 38, 37, 32),
(284, '', 'SATURDAY', '2023-10-14 13:40:00', 10, b'0', '2023-10-14 12:20:00', 41, 2023, 3, 38, 37, 32),
(300, NULL, 'WEDNESDAY', '2023-10-04 13:40:00', 10, b'0', '2023-10-04 12:20:00', 40, 2023, 1, 39, 37, 12),
(286, NULL, 'MONDAY', '2023-10-09 12:00:00', 10, b'0', '2023-10-09 10:40:00', 41, 2023, 1, 39, 37, 12),
(302, '', 'MONDAY', '2023-10-02 16:00:00', 10, b'0', '2023-10-02 14:40:00', 40, 2023, 1, 37, 37, 8),
(288, '', 'FRIDAY', '2023-10-13 12:00:00', 10, b'0', '2023-10-13 10:40:00', 41, 2023, 3, 39, 37, 12),
(289, '', 'THURSDAY', '2023-10-12 13:40:00', 10, b'0', '2023-10-12 12:20:00', 41, 2023, 1, 41, 37, 10),
(290, NULL, 'WEDNESDAY', '2023-10-11 13:40:00', 10, b'0', '2023-10-11 12:20:00', 41, 2023, 1, 41, 37, 10),
(291, NULL, 'WEDNESDAY', '2023-10-11 12:00:00', 10, b'0', '2023-10-11 10:40:00', 41, 2023, 1, 41, 37, 10),
(292, '', 'THURSDAY', '2023-10-12 16:00:00', 10, b'0', '2023-10-12 14:40:00', 41, 2023, 3, 41, 37, 10),
(293, '', 'THURSDAY', '2023-10-12 10:20:00', 10, b'0', '2023-10-12 09:00:00', 41, 2023, 3, 40, 37, 7),
(297, '', 'TUESDAY', '2023-10-10 12:00:00', 10, b'0', '2023-10-10 10:40:00', 41, 2023, 2, 34, 37, 31),
(299, '', 'MONDAY', '2023-10-02 13:40:00', 10, b'0', '2023-10-02 12:20:00', 40, 2023, 1, 39, 37, 12);

-- --------------------------------------------------------

--
-- Table structure for table `lesson_sequence`
--

CREATE TABLE IF NOT EXISTS `lesson_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lesson_sequence`
--

INSERT INTO `lesson_sequence` (`next_val`) VALUES
(303);

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE IF NOT EXISTS `roles` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
(1, 'ADMIN'),
(2, 'TEACHER'),
(3, 'STUDENT GROUP');

-- --------------------------------------------------------

--
-- Table structure for table `role_sequence`
--

CREATE TABLE IF NOT EXISTS `role_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `role_sequence`
--

INSERT INTO `role_sequence` (`next_val`) VALUES
(10);

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

CREATE TABLE IF NOT EXISTS `settings` (
  `id` bigint(20) NOT NULL,
  `onlineclass_color` varchar(255) NOT NULL,
  `start_date` date NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `settings`
--

INSERT INTO `settings` (`id`, `onlineclass_color`, `start_date`) VALUES
(1, '#fbbcbc', '2023-09-05');

-- --------------------------------------------------------

--
-- Table structure for table `study_year_settings`
--

CREATE TABLE IF NOT EXISTS `study_year_settings` (
  `id` bigint(20) NOT NULL,
  `start_date` date NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `study_year_settings`
--

INSERT INTO `study_year_settings` (`id`, `start_date`) VALUES
(1, '2023-09-05');

-- --------------------------------------------------------

--
-- Table structure for table `subjects`
--

CREATE TABLE IF NOT EXISTS `subjects` (
  `id` bigint(20) NOT NULL,
  `title` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `teachers`
--

CREATE TABLE IF NOT EXISTS `teachers` (
  `id` bigint(20) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `fio` varchar(255) NOT NULL,
  `role_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `teachers`
--

INSERT INTO `teachers` (`id`, `comment`, `contact`, `fio`, `role_id`) VALUES
(1, NULL, NULL, 'Синя Л.В.', 2),
(2, NULL, NULL, 'Штангей С.Г.', 2),
(3, NULL, NULL, 'Мельник В.Д.', 2),
(4, NULL, NULL, 'Марюхно В.В.', 2),
(5, NULL, NULL, 'Деркаченко О.А.', 2),
(6, NULL, NULL, 'Станкевич В.А.', 2),
(7, NULL, NULL, 'Казарян І.І.', 2),
(8, NULL, NULL, 'Стрижачук Ф.В.', 2),
(9, NULL, NULL, 'Кібіт Я.В.', 2),
(10, NULL, NULL, 'Марюхно Н.М.', 2),
(11, NULL, NULL, 'Городецький М.О.', 2),
(12, NULL, NULL, 'Оверковський С.М.', 2),
(30, '', '', 'Бірзов Ю.П.', NULL),
(31, '', '', 'Рязанцева Т.О.', NULL),
(32, '', '', 'Ашихміна М.В.', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `teachers_seq`
--

CREATE TABLE IF NOT EXISTS `teachers_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `teachers_seq`
--

INSERT INTO `teachers_seq` (`next_val`) VALUES
(1);

-- --------------------------------------------------------

--
-- Table structure for table `teacher_sequence`
--

CREATE TABLE IF NOT EXISTS `teacher_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `teacher_sequence`
--

INSERT INTO `teacher_sequence` (`next_val`) VALUES
(33);

-- --------------------------------------------------------

--
-- Table structure for table `timeframes`
--

CREATE TABLE IF NOT EXISTS `timeframes` (
  `id` bigint(20) NOT NULL,
  `color` varchar(255) DEFAULT NULL,
  `end_time` time NOT NULL,
  `order_number` int(11) NOT NULL,
  `start_time` time NOT NULL,
  `title` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `timeframes`
--

INSERT INTO `timeframes` (`id`, `color`, `end_time`, `order_number`, `start_time`, `title`) VALUES
(1, '#ffffff', '10:20:00', 1, '09:00:00', 'Перший урок'),
(2, '#ffffff', '12:00:00', 2, '10:40:00', 'Другий урок'),
(3, '#ffffff', '13:40:00', 3, '12:20:00', 'Третій урок'),
(4, '#ffffff', '16:00:00', 4, '14:40:00', 'Четвертий урок'),
(5, '#ffffff', '17:40:00', 5, '16:20:00', 'П`ятий урок');

-- --------------------------------------------------------

--
-- Table structure for table `timeframe_sequence`
--

CREATE TABLE IF NOT EXISTS `timeframe_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `timeframe_sequence`
--

INSERT INTO `timeframe_sequence` (`next_val`) VALUES
(30);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `role_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `user_sequence`
--

CREATE TABLE IF NOT EXISTS `user_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user_sequence`
--

INSERT INTO `user_sequence` (`next_val`) VALUES
(10);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `autorities`
--

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
