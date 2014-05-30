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
$stmt = $conn->prepare("UPDATE `group` SET description = ? WHERE name = ?");
$stmt->bind_param('ss', $_POST['description'], $_POST['name']);
$stmt->execute();

if($stmt->affected_rows <= 0 or $stmt->errno) {
	$error_entry = "Group could not be edited";
	if($stmt->errno) {
		$error_entry.= "(error code $stmt->errno): $stmt->error";
	}
	$log->general($error_entry);
	$conn->rollback();
	die("COULD NOT EDIT GROUP");
}


$stmt = $conn->prepare("DELETE FROM user_in_group WHERE group_id = ?");
$stmt->bind_param('s', $_POST['name']);
if(!$stmt->execute()) {
	$error_entry = "Could not delete members list";
	if($stmt->errno) {
		$error_entry.= "(error code $stmt->errno): $stmt->error";
	}
	$log->general($error_entry);
	$conn->rollback();
	die("COULD NOT UPDATE MEMBERS LIST");
}

$stmt = $conn->prepare("INSERT INTO user_in_group (user_id, group_id) VALUES(?,?)");
foreach($users as $user) {
	$stmt->bind_param('ss', $user, $_POST['name']);
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