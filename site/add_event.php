<?php
require_once("internal/common_requires_session_check.php");

$saved_description = $_POST['description'];
$_POST = filter_array_with_default_flags($_POST, FILTER_UNSAFE_RAW, FILTER_FLAG_STRIP_LOW);
$_POST['description'] = $saved_description;

$validator = new Valitron\Validator($_POST);
$validator->rule('required',['name', 'description', 'location', 'datetime', 'invited_people']);
$validator->rule('lengthMin', array('name', 'location'), 3);
$validator->rule('lengthMax', 'name', 50);
$validator->rule('lengthMax', 'location', 100);
$validator->rule('datetime', 'dateFormat', 'Y-m-d H:i');


verify_logged($validator, basename(__FILE__));

$_POST = filter_array_with_default_flags($_POST, FILTER_UNSAFE_RAW, FILTER_FLAG_STRIP_LOW, array('description'));

$invited_people = json_decode_log($_POST['invited_people'], basename(__FILE__));


$conn->autocommit(false);

$stmt = $conn->prepare("INSERT INTO event (name, description, creator, location, datetime, datetime_creation) VALUES(?,?,?,?,?, NOW())");
$stmt->bind_param('sssss', $_POST['name'], $_POST['description'],
		$_SESSION['logged_username'],$_POST['location'], $_POST['datetime']);
$stmt->execute();

if($stmt->affected_rows <= 0 or $stmt->errno) {
	$error_entry = "Event could not be added";
	if($stmt->errno) {
		$error_entry.= "(error code $stmt->errno): $stmt->error";
	}
	$log->general($error_entry);
	$conn->rollback();
	die("COULD NOT ADD EVENT");
}

$id = $stmt->insert_id;

$stmt = $conn->prepare("INSERT INTO event_invited_users (event_id, user_id) VALUES(?,?)");
foreach($invited_people as $user) {
	$stmt->bind_param('ss', $id, $user);
	$stmt->execute();
	if($stmt->errno) {
		$error_entry = "User could not be added to event";
		if($stmt->errno) {
			$error_entry.= "(error code $stmt->errno): $stmt->error";
		}
		$log->general($error_entry);
		$conn->rollback();
		die("USER NOT ADDED\n$user");
	}
}
$conn->commit();
echo "OK";