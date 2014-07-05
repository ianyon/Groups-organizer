<?php
function filter_array_with_default_flags($loopThrough, $filter, $flags, $leave = array()) {
	$args = array();
	$left = array();
    foreach ($loopThrough as $key=>$value) {
		if(!in_array($key, $leave))
			$args[$key] = array('filter'=>$filter, 'flags'=>$flags);
		else
			$left[$key] = $value;
    }
    
    return array_merge(filter_var_array($loopThrough, $args, true),$left);
}

function format_validation_errors(&$validator) {
	$str = "";
	foreach($validator->errors() as $error) {
		$str.= implode("\n", $error);
		$str.= "\n";
	}
	return $str;
}

function verify_logged(&$validator, $file) {
	global $log;
	if(!$validator->validate()) {
		$log->general("Invalid input in file $file");
		echo "INPUT VERIFICATION FAILED\n";
		die(format_validation_errors($validator));
	}
}

function json_decode_log($str, $file) {
	global $log;
	$json = json_decode($str);
	if(json_last_error()) {
		$log->general("Error parsing json in " . $file);
		die("Error parsing json");
	}
	return $json;
}
?>