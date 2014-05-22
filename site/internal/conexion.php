<?php
require_once("internal/log.php");
$conn = new mysqli("localhost", "groups_organizer", "TcxBAedrqX2b3sML", "groups_organizer");
if($conn->connect_errno) {
	$log->general("Error mysql(cod. error $conn->connect_errno):\n$mysqli->connect_error\n",3,"internal/error_log.php");
}