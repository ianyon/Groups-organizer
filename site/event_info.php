<?php
require_once("internal/common_requires_session_check.php");

$validator = new Valitron\Validator($_POST);
$validator->rule('required','event_id');
$validator->rule('integer', 'event_id');
$validator->rule('min', 'event_id', 0);

$stmt = $conn->prepare(
	"SELECT id, name, location, description, datetime FROM `group` WHERE id=?");
$stmt->bind_param('s', $_POST['event_id']);
if($stmt->execute()) {
	$result = $stmt->get_result();
	if($result->num_rows === 0) {
		die("EVENT NOT FOUND");
	}
	$event = $result->fetch_assoc();
	
	$stmt = $conn->prepare("SELECT user_id, confirmed FROM event_invited_users WHERE event_id = ?");
	$stmt->bind_param('s', $_POST['event_id']);
	if($stmt->execute()) {
		$result = $stmt->get_result();
		$guests = array();
		while($guest = $result->fetch_assoc()) {
			$guest['confirmed'] = $guest['confirmed'] == '1' ? true : false;
			$guests[] = $guest;
		}
		$event['guests'] = $guests;
	}
	echo json_encode($event);
} else {
	die("ERROR");
}
