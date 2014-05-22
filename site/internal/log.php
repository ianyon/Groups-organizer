<?php 
//Multiline error log class 
// ersin güvenç 2008 eguvenc@gmail.com 
//For break use "\n" instead '\n' 

Class Log { 
  // 
  const USER_ERROR_DIR = 'internal/logs/user_logs.txt'; 
  const GENERAL_ERROR_DIR = 'internal/logs/general_logs.txt'; 

  /* 
   User Errors... 
  */ 
    public function user($msg,$username) 
    { 
    $date = date('d.m.Y h:i:s'); 
    $log = $msg."   |  Date:  ".$date."  |  User:  ".$username."\n"; 
    error_log($log, 3, self::USER_ERROR_DIR); 
    } 
    /* 
   General Errors... 
  */ 
    public function general($msg) 
    { 
    $date = date('d.m.Y h:i:s'); 
    $log = $msg."   |  Date:  ".$date."\n"; 
    error_log($msg."   |  Tarih:  ".$date, 3, self::GENERAL_ERROR_DIR); 
    } 

}