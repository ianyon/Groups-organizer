<?php
function filter_array_with_default_flags($loopThrough, $filter, $flags, $add_empty = true) {
   
    $args = array();
    foreach ($loopThrough as $key=>$value) {
        $args[$key] = array('filter'=>$filter, 'flags'=>$flags);
    }
    
    return filter_var_array($loopThrough, $args, $add_empty);
}