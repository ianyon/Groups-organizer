<?php
require_once("internal/common_requires_session_check.php");

$_POST = filter_array_with_default_flags($_POST, FILTER_UNSAFE_RAW, FILTER_FLAG_STRIP_LOW, array('description'));

if(isset($_POST['only_invited_to']) && $_POST['only_invited_to'] == 1) {
	$stmt = $conn->prepare(
		"SELECT event.id id, event.creator creator, event.location, event.name name, COALESCE(EIU.guest_count,0) guestListCount
		FROM 
			(SELECT event_id,COUNT(user_id) guest_count 
			FROM event_invited_users 
			GROUP BY event_id) EIU
		RIGHT JOIN event ON event.id = EIU.event_id
		RIGHT JOIN event_invited_users ON event_invited_users.event_id = EIU.event_id
		WHERE event_invited_users.user_id = ?");
	$stmt->bind_param('s', $_SESSION['logged_username']);
	if($stmt->execute() and $stmt->errno === 0) {
		$result = $stmt->get_result();
		$rows = array();
		while($row = $result->fetch_assoc()) {
			$rows[] = $row;
		}
		echo json_encode($rows);
	} else {
		$log->general("Could not get list of events the user was invited to from the database.");
		die("ERROR");
	}
} else {
	$rs = $conn->query(
			"SELECT event.id id, event.creator creator, event.location, event.name name, COALESCE(EIU.guest_count,0) guestListCount
			FROM 
				(SELECT event_id,COUNT(user_id) guest_count 
				FROM event_invited_users 
				GROUP BY event_id) EIU
			RIGHT JOIN event ON event.id = EIU.event_id");
	if($rs) {
		$rows = array();
		while($row = $rs->fetch_assoc()) {
			$rows[] = $row;
		}
		echo json_encode($rows);
	} else {
		$log->general("Could not get list of events from database.");
		die("ERROR");
	}
}








