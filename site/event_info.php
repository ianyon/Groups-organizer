<?php
require_once("internal/common_requires_session_check.php");

$validator = new Valitron\Validator($_POST);
$validator->rule('required','event_id');
$validator->rule('integer', 'event_id');
$validator->rule('min', 'event_id', 0);

verify_logged($validator, basename(__FILE__));

$_POST = filter_array_with_default_flags($_POST, FILTER_UNSAFE_RAW, FILTER_FLAG_STRIP_LOW, array('description'));

$stmt = $conn->prepare(
	"SELECT id, name, creator, location, description, datetime FROM event WHERE id=?");
$stmt->bind_param('i', $_POST['event_id']);
if($stmt->execute() and $stmt->errno === 0) {
	$result = $stmt->get_result();
	if($result->num_rows === 0) {
		$log->general("Info for event $_POST[event_id] not found.");
		die("EVENT NOT FOUND");
	}
	$event = $result->fetch_assoc();
	$stmt = $conn->prepare("SELECT user_id, confirmed, name
							FROM event_invited_users join user on user_id = user_name  
							WHERE event_id = ?");
	$stmt->bind_param('s', $_POST['event_id']);
	if($stmt->execute() and $stmt->errno === 0) {
		$result = $stmt->get_result();
		$guests = array();
		while($guest = $result->fetch_assoc()) {
			$guest['confirmed'] = $guest['confirmed'] == '1' ? true : false;
			$guests[] = $guest;
		}
		$event['guests'] = $guests;
	} else {
		$log->general("Could not get list of users in event $_POST[event_id] from database.");
		die("ERROR");
	}
	echo json_encode($event);
} else {
	$log->general("Error occurred while trying to fetch info from event $_POST[event_id]");
	die("ERROR");
}
