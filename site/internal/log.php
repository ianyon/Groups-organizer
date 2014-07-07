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
      logSynchronized("[$date][$username] $msg\n", self::USER_ERROR_DIR);
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

    logSynchronized("[$date] $msg\n", self::GENERAL_ERROR_DIR);
    }

    function logSynchronized($msg, $file){
      $f = fopen($file);

      if (flock($f, LOCK_EX)) {  // adquirir un bloqueo de lectura

        error_log($msg, 3, $file); 

        flock($f, LOCK_UN);    // libera el bloqueo
      } else {
          echo "¡No se pudo obtener el bloqueo!";
      }

      fclose($f);
    }

}