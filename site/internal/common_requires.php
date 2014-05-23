<?php
require_once("internal/log.php");
require_once("internal/conexion.php");
require_once("internal/Validator.php");
require_once("internal/functions.php");

$log = new Log();

if(!isset($_POST) or count($_POST) == 0) {
	$_POST = $_GET;
}