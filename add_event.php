<?php
require_once("internal/common_requires_session_check.php");

$name = $_POST['name'];
$description = $_POST['description'];
$location = $_POST['location'];
$datetime = $_POST['datetime'];
$invited_people = $_POST['invited_people'];

$conn->autocommit(false);
$conn->begin_transaction();

$stmt = $conn->prepare("INSERT INTO event (name, description, creator, location, datetime, datetime_creation) VALUES(?,?,?,?,?, NOW())");
$stmt->bind_param('sssss', $name, $description, $creator, $_SESSION['logged_username'], $datetime);
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