<?php
require_once("internal/common_requires.php");
require_once("internal/password_compat.php");

$validator = new Valitron\Validator($_POST);
$validator->rule('required',['user', 'pass']);
$validator->rule('lengthMin', array('user', 'pass'), 5);
$validator->rule('lengthMax', 'user', 30);

verify_logged($validator, basename(__FILE__));

// Quitar caracteres especiales (código de caracter < 32)
$_POST = filter_array_with_default_flags($_POST, FILTER_UNSAFE_RAW, FILTER_FLAG_STRIP_LOW);


$stmt = $conn->prepare("SELECT * FROM user WHERE user_name = ?");
$stmt->bind_param('s', $_POST['user']);
if($stmt->execute() and $stmt->errno === 0) {
	$row = $stmt->get_result()->fetch_assoc();
	$hashedPass = $row['pass'];
	if(password_verify($_POST['pass'],$hashedPass)){
		echo "OK\n$row[name]";
	} else {
		$log->general("Failed to login user $_POST[user]");
		die("LOGIN_FAILED");
	}
	session_start();
	$_SESSION['logged_username'] = $_POST['user'];
} else {
	$log->general("Could not log user $_POST[user].");
	echo "ERROR";
}
//$now = new DateTime();
//echo "time since last login ".($now->getTimestamp()-$_SESSION['logged_time'])."s";
//$_SESSION['logged_time'] = $now->getTimestamp();