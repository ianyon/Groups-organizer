<?php
require_once("internal/common_requires.php");
require_once("internal/password_compat.php");

$validator = new Valitron\Validator($_POST);
$validator->rule('required',['user', 'pass', 'email', 'name', 'age', 'gender']);
$validator->rule('email', 'email');
$validator->rule('lengthMin', array('user', 'name', 'pass'), 5);
$validator->rule('lengthMax', array('email', 'name'), 60);
$validator->rule('lengthMax', 'user', 30);
$validator->rule('in', 'gender', array("1", "0"));
$validator->rule('integer', 'age');
$validator->rule('min', 'age', 1);
$validator->rule('max', 'age', 120);

verify_logged($validator, basename(__FILE__));

$_POST = filter_array_with_default_flags($_POST, FILTER_UNSAFE_RAW, FILTER_FLAG_STRIP_LOW);


$hashedPass = password_hash($_POST['pass'], PASSWORD_DEFAULT);

$stmt = $conn->prepare("INSERT INTO user(user_name, pass, email, name, age, gender) VALUES(?,?,?,?,?,?)");
$stmt->bind_param('ssssii', $_POST['user'], $hashedPass, $_POST['email'],
			$_POST['name'], $_POST['age'], $_POST['gender']);
if($stmt->execute() and $stmt->errno === 0) {
	if($stmt->affected_rows !== 0) {
		echo "OK";
	} else {
		$log->general("Tried to add user $_POST[user] but no rows were affected in database.");
		echo "ERROR";
	}
} else {	
	if($stmt->errno == 1062) { //Duplicate key
		$log->general("Tried to add user $_POST[user] but no rows were affected in database.");
		die("DUP_USER");
	} else {
		$error_entry = "A user could not be registered";
		if($stmt->errno) {
			$error_entry.= "(error code $stmt->errno): $stmt->error";
		}
		$log->general($error_entry);
		die("ERROR");
	}
}