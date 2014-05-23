<?php
require_once("internal/common_requires_session_check.php");

$saved_description = $_POST['description'];
$_POST = filter_array_with_default_flags($_POST, FILTER_UNSAFE_RAW, FILTER_FLAG_STRIP_LOW);
$_POST['description'] = $saved_description;

$validator = new Valitron\Validator($_POST);
$validator->rule('required',['name', 'description', 'users']);
$validator->rule('lengthMin', 'name', 3);
$validator->rule('lengthMax', 'name', 50);

$users = $json_decode($_POST['users']);

if(!$validator->validate() or json_last_error()) {
	$log->general("Invalid input in file login.php");
	echo "INPUT VERIFICATION FAILED\n";
	if(json_last_error()) {
		echo "Error parsing invited users";
	}
	echo format_validation_errors($validator);
	return;
}

$conn->autocommit(false);
$conn->begin_transaction();

$stmt = $conn->prepare("INSERT INTO group (name, description) VALUES(?,?)");
$stmt->bind_param('ss', $_POST['name'], $_POST['description']);
$stmt->execute();

if($stmt->affected_rows <= 0 or $stmt->errno) {
	$error_entry = "Group could not be added";
	if($stmt->errno) {
		$error_entry.= "(error code $stmt->errno): $stmt->error";
	}
	$log->general($error_entry);
	$conn->rollback();
	die("COULD NOT ADD GROUP");
}

$stmt = $conn->prepare("INSERT INTO user_in_group (user_id, group_id) VALUES(?,?)");
foreach($users as $user) {
	$stmt->bind_param('ss', $_POST['name'], $user);
	$stmt->execute();
	if($stmt->errno) {
		$error_entry = "User could not be added to group";
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