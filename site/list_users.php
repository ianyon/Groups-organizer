<?php
require_once("internal/common_requires_session_check.php");

$_POST = filter_array_with_default_flags($_POST, FILTER_UNSAFE_RAW, FILTER_FLAG_STRIP_LOW);

if(isset($_POST['group_name'])) {
	$stmt = $conn->prepare(
		"SELECT user.user_name user_name
		FROM user
			JOIN user_in_group ON user_in_group.user_id = user.user_name
		WHERE user_in_group.group_id = ?");
	$stmt->bind_param('s', $_POST['group_name']);
	if($stmt->execute()) {
		$result = $stmt->get_result();
		$rows = array();
		while($row = $result->fetch_assoc()) {
			$rows[] = $row;
		}
		echo json_encode($rows);
	} else {
		die("ERROR");
	}

} else {
	$rs = $conn->query("SELECT user_name FROM user");
	if($rs) {
		$rows = array();
		while($row = $rs->fetch_assoc()) {
			$rows[] = $row;
		}
		echo json_encode($rows);
	} else {
		die("ERROR");
	}
}