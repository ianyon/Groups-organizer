<?php
require_once("internal/common_requires_session_check.php");

$saved_description = $_POST['description'];
$_POST = filter_array_with_default_flags($_POST, FILTER_UNSAFE_RAW, FILTER_FLAG_STRIP_LOW);
$_POST['description'] = $saved_description;

$validator = new Valitron\Validator($_POST);
$validator->rule('required',['id', 'name', 'description', 'location', 'datetime', 'invited_people']);
$validator->rule('lengthMin', array('name', 'location'), 3);
$validator->rule('lengthMax', 'name', 50);
$validator->rule('lengthMax', 'location', 100);
$validator->rule('lengthMax', 'datetime', 20);
$validator->rule('integer', 'id');
$validator->rule('min', 'id', 0);
//$validator->rule('dateFormat', 'datetime', 'Y-m-d H:i');


verify_logged($validator, basename(__FILE__));

$_POST = filter_array_with_default_flags($_POST, FILTER_UNSAFE_RAW, FILTER_FLAG_STRIP_LOW, array('description'));

$invited_people = json_decode_log($_POST['invited_people'], basename(__FILE__));


$conn->autocommit(false);

$stmt = $conn->prepare("UPDATE event SET name=?, description=?, location=?, datetime=? WHERE id = ?");
$stmt->bind_param('ssssi', $_POST['name'], $_POST['description'],
		$_POST['location'], $_POST['datetime'], $_POST['id']);


if($stmt->execute() and $stmt->errno === 0) {
	$stmt = $conn->prepare("DELETE FROM event_invited_users WHERE event_id = ?");
	$stmt->bind_param('i', $_POST['id']);
	if($stmt->execute() and $stmt->errno === 0) {
		$i = 0;
		$stmt = $conn->prepare("INSERT INTO event_invited_users (event_id, user_id) VALUES(?,?)");
		foreach($invited_people as $user) {
			$stmt->bind_param('ss', $_POST['id'], $user);
			if($stmt->execute() and $stmt->errno === 0) {
				$i++;
			} else {
				$error_entry = "User could not be added to event";
				if($stmt->errno) {
					$error_entry.= "(error code $stmt->errno): $stmt->error";
				}
				$log->general($error_entry);
				$conn->rollback();
				die("USER NOT ADDED\n$user");
			}
		}
		if($i === count($invited_people)) {
			$conn->commit();
			echo "OK";
		} else {
			$log->general("Not all users could be invited to event. Unknown error. Tried to add users:".json_encode($invited_people)." to event $_POST[id]");
			$conn->rollback();
			echo "Not all users could be invited to event. Unknown error.";
		}
	} else {
		$error_entry = "Could not delete guest list";
		if($stmt->errno) {
			$error_entry.= "(error code $stmt->errno): $stmt->error";
		}
		$log->general($error_entry);
		$conn->rollback();
		die("COULD NOT UPDATE GUEST LIST");	
	}

} else {
	$error_entry = "Event could not be edited";
	if($stmt->errno) {
		$error_entry.= "(error code $stmt->errno): $stmt->error";
	}
	$log->general($error_entry);
	$conn->rollback();
	die("COULD NOT EDIT EVENT");
}
