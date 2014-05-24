<?php
require_once("internal/common_requires_session_check.php");


$rs = $conn->query("SELECT user_name FROM user");
if($rs) {
	$rows = array();
	while($row = $rs->fetch_assoc()) {
		$rows[] = $row;
	}
	echo json_encode($rows);
} else {
	die("ERROR");
}