<?php
require_once("internal/common_requires_session_check.php");

$validator = new Valitron\Validator($_POST);
$validator->rule('required',['event_id', 'confirm']);
$validator->rule('in', 'confirm', array("1", "0"));
$validator->rule('integer', 'event_id');
$validator->rule('min', 'event_id', 1);

verify_logged($validator, basename(__FILE__));

$_POST = filter_array_with_default_flags($_POST, FILTER_UNSAFE_RAW, FILTER_FLAG_STRIP_LOW, array('description'));

$stmt = $conn->prepare(
	"UPDATE event_invited_users SET confirmed = ? WHERE event_id = ? AND user_id = ?");
$stmt->bind_param('iis', $_POST['confirm'], $_POST['event_id'], $_SESSION['logged_username']);
if($stmt->execute()) {
	die("OK");
} else {
	die("ERROR");
}
