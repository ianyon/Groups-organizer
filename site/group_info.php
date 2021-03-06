<?php
require_once("internal/common_requires_session_check.php");

$validator = new Valitron\Validator($_POST);
$validator->rule('required','group_id');

verify_logged($validator, basename(__FILE__));

$_POST = filter_array_with_default_flags($_POST, FILTER_UNSAFE_RAW, FILTER_FLAG_STRIP_LOW);

$stmt = $conn->prepare(
	"SELECT name, description FROM `group` WHERE name=?");
$stmt->bind_param('s', $_POST['group_id']);
if($stmt->execute() and $stmt->errno === 0) {
	$result = $stmt->get_result();
	if($result->num_rows === 0) {
		$log->general("Info for group $_POST[group_id] not found.");
		die("GROUP NOT FOUND");
	}
	$group = $result->fetch_assoc();
	
	$stmt = $conn->prepare("SELECT user_id user_name FROM user_in_group WHERE group_id = ?");
	$stmt->bind_param('s', $_POST['group_id']);
	if($stmt->execute() and $stmt->errno === 0) {
		$result = $stmt->get_result();
		$members = array();
		while($member = $result->fetch_assoc()) {
			$members[] = $member;
		}
		$group['members'] = $members;
	} else {
		$log->general("Could not get list of users in group $_POST[group_id] from database.");
		die("ERROR");
	}
	echo json_encode($group);
} else {
	$log->general("Error occurred while trying to fetch info from group $_POST[group_id]");
	die("ERROR");
}
