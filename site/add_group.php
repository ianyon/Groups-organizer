<?php
require_once("internal/common_requires_session_check.php");

$validator = new Valitron\Validator($_POST);
$validator->rule('required',['name', 'description', 'users']);
$validator->rule('lengthMin', 'name', 3);
$validator->rule('lengthMax', 'name', 50);


verify_logged($validator, basename(__FILE__));

$_POST = filter_array_with_default_flags($_POST, FILTER_UNSAFE_RAW, FILTER_FLAG_STRIP_LOW, array('description'));

$users = json_decode_log($_POST['users'], basename(__FILE__));


$conn->autocommit(false);
$stmt = $conn->prepare("INSERT INTO `group` (name, description) VALUES(?,?)");
$stmt->bind_param('ss', $_POST['name'], $_POST['description']);

if($stmt->execute() and $stmt->affected_rows === 1 and $stmt->errno === 0) {
	$stmt = $conn->prepare("INSERT INTO user_in_group (user_id, group_id) VALUES(?,?)");
	$i = 0;
	foreach($users as $user) {
		$stmt->bind_param('ss', $user, $_POST['name']);
		$stmt->execute();
		if($stmt->errno === 0) {
			$i++;
		} else {
			$error_entry = "User could not be added to group";
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
		$log->general("Not all users could be added to group. Unknown error. Tried to add users:".json_encode($users)." to event $name");
		$conn->rollback();
		echo "Not all users could be added to group. Unknown error.";
	}
} else {
	$error_entry = "Group could not be added";
	if($stmt->errno) {
		$error_entry.= "(error code $stmt->errno): $stmt->error";
	}
	$log->general($error_entry);
	$conn->rollback();
	die("COULD NOT ADD GROUP");
}