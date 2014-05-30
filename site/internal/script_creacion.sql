-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 30-05-2014 a las 16:25:43
-- Versión del servidor: 5.6.12-log
-- Versión de PHP: 5.4.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `groups_organizer`
--
CREATE DATABASE IF NOT EXISTS `groups_organizer` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `groups_organizer`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `event`
--

CREATE TABLE IF NOT EXISTS `event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` text NOT NULL,
  `creator` varchar(30) NOT NULL,
  `location` varchar(100) NOT NULL,
  `datetime` varchar(20) NOT NULL,
  `datetime_creation` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `creator` (`creator`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=10 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `event_invited_users`
--

CREATE TABLE IF NOT EXISTS `event_invited_users` (
  `event_id` int(11) NOT NULL,
  `user_id` varchar(30) NOT NULL,
  `confirmed` tinyint(1) NOT NULL,
  KEY `event_id` (`event_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `group`
--

CREATE TABLE IF NOT EXISTS `group` (
  `name` varchar(50) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `user_name` varchar(30) NOT NULL,
  `pass` varchar(256) NOT NULL,
  `email` varchar(60) NOT NULL,
  `name` varchar(60) NOT NULL,
  `age` tinyint(4) NOT NULL,
  `gender` tinyint(1) NOT NULL,
  PRIMARY KEY (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user_in_group`
--

CREATE TABLE IF NOT EXISTS `user_in_group` (
  `user_id` varchar(30) NOT NULL,
  `group_id` varchar(50) NOT NULL,
  KEY `user_id` (`user_id`),
  KEY `group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `event`
--
ALTER TABLE `event`
  ADD CONSTRAINT `event_ibfk_1` FOREIGN KEY (`creator`) REFERENCES `user` (`user_name`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `event_invited_users`
--
ALTER TABLE `event_invited_users`
  ADD CONSTRAINT `event_invited_users_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`),
  ADD CONSTRAINT `event_invited_users_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_name`);

--
-- Filtros para la tabla `user_in_group`
--
ALTER TABLE `user_in_group`
  ADD CONSTRAINT `user_in_group_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_name`),
  ADD CONSTRAINT `user_in_group_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `group` (`name`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
