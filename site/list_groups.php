<?php
require_once("internal/common_requires_session_check.php");

if(isset($_POST['only_member_of']) && $_POST['only_member_of'] == 1) {
	$stmt = $conn->prepare(
		"SELECT `group`.name name,`group`.description description,UIG.member_count
		FROM 
			(SELECT group_id,COUNT(user_id) member_count 
			FROM user_in_group 
			GROUP BY group_id) UIG
		JOIN `group` ON `group`.name = UIG.group_id
		JOIN user_in_group ON user_in_group.group_id = UIG.group_id
		WHERE user_in_group.user_id=?");
	$stmt->bind_param('s', $_SESSION['logged_username']);
	if($stmt->execute()) {
		$result = $stmt->get_result();
		while($row = $result->fetch_assoc()) {
			$rows[] = $row;
		}
		echo json_encode($rows);
	} else {
		die("ERROR");
	}
} else {
	$rs = $conn->query("SELECT group_id, description, COUNT(user_id) member_count 
			FROM user_in_group JOIN `group` on `group`.name = user_in_group.group_id
			GROUP BY group_id");
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