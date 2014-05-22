<?php
session_start();
if(!isset($_SESSION) or !isset($_SESSION['logged_username']) or !$_SESSION['logged_username']) {
	die("USER_NOT_LOGGED_IN");
}