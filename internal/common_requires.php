<?php
require_once("internal/log.php");
require_once("internal/conexion.php");
$wd_was = getcwd();
require_once("Validator.php");

$log = new Log();

if(!isset($_POST) or count($_POST) == 0) {
	$_POST = $_GET;
}