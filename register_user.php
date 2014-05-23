<?php
require_once("internal/common_requires.php");

header("Content-Type: text/plain");

$validator = new Valitron\Validator($_POST);
$validator->rule('required',['user', 'pass', 'email', 'name', 'age', 'gender']);
$validator->rule('email', 'email');
$validator->rule('lengthMin', array('user', 'name', 'pass'), 5);
$validator->rule('lengthMax', array('email', 'name'), 60);
$validator->rule('lengthMax', 'user', 30);
$validator->rule('in', 'gender', array('M', 'F'));
$validator->rule('integer', 'age');
$validator->rule('min', 'age', 1);
$validator->rule('max', 'age', 120);

if(!$validator->validate()) {
	$log->general("Invalid input in file register_user.php");
	echo "INPUT VERIFICATION FAILED\n";
	foreach($validator->errors() as $error) {
		echo implode('\n', $error);
	}
	return;
}

$hashedPass = password_hash($_POST['userPass'], PASSWORD_DEFAULT);

$stmt = $conn->prepare("INSERT INTO user(user_name, pass, email, name, age, gender) VALUES(?,?,?,?,?,?)");
$stmt->bind_param('ssssii', $_POST['userName'], $hashedPass, $_POST['email'],
			$_POST['name'], $_POST['age'], $_POST['gender'] === 'M' ? 1 : 0);
$stmt->execute();
if($stmt->affected_rows <= 0 or $stmt->errno) {
	$error_entry = "A user could not be registered";
	if($stmt->errno) {
		$error_entry.= "(error code $stmt->errno): $stmt->error";
	}
	$log->general($error_entry);
	
	if($stmt->errno == 1062) { //Duplicate key
		die("DUP_USER");
	} else {
		die("ERROR");
	}
}
echo "OK";