<?php
require_once("internal/common_requires_session_check.php");

header("Content-Type: text/plain");

$rs = $conn->query("SELECT * FROM event ORDER BY datetime");
if($rs) {
	$rows = array();
	while($row = $rs->fetch_assoc()) {
		$rows[] = $row;
	}
	echo json_encode($rows);
} else {
	die("ERROR");
}