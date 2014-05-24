<?php
require_once("internal/common_requires_session_check.php");

$rs = $conn->query(
		"SELECT event.id id, event.name name, EIU.guest_count
		FROM 
			(SELECT event_id,COUNT(user_id) guest_count 
			FROM event_invited_users 
			GROUP BY event_id) EIU
		JOIN event ON event.id = EIU.event_id");
if($rs) {
	$rows = array();
	while($row = $rs->fetch_assoc()) {
		$rows[] = $row;
	}
	echo json_encode($rows);
} else {
	die("ERROR");
}