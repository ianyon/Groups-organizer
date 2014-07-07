<?php
require_once("internal/common_requires_session_check.php");

$_POST = filter_array_with_default_flags($_POST, FILTER_UNSAFE_RAW, FILTER_FLAG_STRIP_LOW, array('description'));

if(isset($_POST['only_member_of']) && $_POST['only_member_of'] == 1) {
	$stmt = $conn->prepare(
		"SELECT `group`.name name,`group`.description description,COALESCE(UIG.member_count,0) member_count
		FROM 
			(SELECT group_id,COUNT(user_id) member_count 
			FROM user_in_group 
			GROUP BY group_id) UIG
		RIGHT JOIN `group` ON `group`.name = UIG.group_id
		RIGHT JOIN user_in_group ON user_in_group.group_id = UIG.group_id
		WHERE user_in_group.user_id=?");
	$stmt->bind_param('s', $_SESSION['logged_username']);
	if($stmt->execute() and $stmt->errno === 0) {
		$result = $stmt->get_result();
		$rows = array();
		while($row = $result->fetch_assoc()) {
			$rows[] = $row;
		}
		echo json_encode($rows);
	} else {
		$log->general("Could not get list of groups the user is member of from the database.");
		die("ERROR");
	}
} else {
	$rs = $conn->query("SELECT `group`.name name, description, COALESCE(COUNT(user_id),0) member_count 
			FROM user_in_group JOIN `group` on `group`.name = user_in_group.group_id
			GROUP BY group_id");
	if($rs) {
		$rows = array();
		while($row = $rs->fetch_assoc()) {
			$rows[] = $row;
		}
		echo json_encode($rows);
	} else {
		$log->general("Could not get list of groups from the database.");
		die("ERROR");
	}
}