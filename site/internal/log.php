<?php 
//Multiline error log class 
// ersin güvenç 2008 eguvenc@gmail.com 
//For break use "\n" instead '\n' 

Class Log { 
  // 
  const USER_ERROR_DIR = 'internal/logs/user_logs.txt'; 
  const GENERAL_ERROR_DIR = 'internal/logs/general_logs.txt'; 
  var $timezone;
  
	public function Log() {
		$this->timezone = new DateTimeZone('America/Santiago');
	}
  /* 
   User Errors... 
  */ 
    public function user($msg,$username) 
    {
		$msg = filter_var($msg, FILTER_UNSAFE_RAW, array('flags' => FILTER_FLAG_STRIP_LOW));
		$date = new DateTime();
		$date->setTimezone($this->timezone);
		$date = $date->format('d.m.Y H:i:s');
		error_log("[$date][$username] $msg\n", 3, self::USER_ERROR_DIR); 
    } 
    /* 
   General Errors... 
  */ 
    public function general($msg) 
    {
		$msg = filter_var($msg, FILTER_UNSAFE_RAW, array('flags' => FILTER_FLAG_STRIP_LOW));
		$date = new DateTime();
		$date->setTimezone($this->timezone);
		$date = $date->format('d.m.Y H:i:s');
		error_log("[$date] $msg\n", 3, self::GENERAL_ERROR_DIR); 
    }

}