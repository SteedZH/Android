<?php 
    header("Content-Type: application/json; charset=UTF-8");
    set_time_limit(0);
    
    include_once '../database.php';

    $receive_json_obj = array();
    $return_json_obj = array();

    ob_flush();
    
    $receive_json_obj = json_decode(file_get_contents('php://input'), true);
    
    $user_id = 0;
    $daytime = array();
    //$weekday = 0;   //0 = Sunday .. 6 = Saturday
    //$time = 0;      //1 = AM, 2 = PM, 0 = Whole Day
    
    if (isset($receive_json_obj["user_id"])) {
        $user_id = $receive_json_obj["user_id"];
        
    }
    
    if (isset($receive_json_obj["daytime"])) {
        $daytime = $receive_json_obj["daytime"];
        
    }
    
    setTutorTime($user_id, $daytime);
    
    flush();
    ob_start();
    
    echo json_encode($return_json_arr, 10);
    
    
    
    function setTutorTime($user_id, $daytime) {
        global $return_json_arr;
        $isValueValid = true;
        $tutors = array();
        
        $return_json_arr['result'] = 'FAIL';
        
        try{
            // Check null
            if ($user_id <= 0) {
                $return_json_arr['code'] = 'NULL_USER_ID';
                $return_json_arr['details'] = 'A Tutor\'s user Id must be specified. ';
                return false;
            }
            
            // instantiate database connection
            $db = new Database();
            $conn = $db->getConnection();
            
            // Check connection
            if ($conn->connect_error) {
                $return_json_arr['code'] = 'DB_CONNECTION_FAIL';
                $return_json_arr['details'] = 'There is a server error when connecting to the database. ';
                $conn->close();
                return false;
                
            }
            
            
            if (sizeof($daytime)>0) {
                $sql =  "DELETE FROM TimePreference WHERE user_id = " . $user_id . ";";
                foreach($daytime as $next) {
                    $sql .= "INSERT INTO TimePreference (user_id, weekday, datetime) VALUES (" . $user_id . ", " . $next['weekday'] . ", " . $next['time'] . ");";
                }
                //echo $sql;
            }
            
            if ($conn->multi_query($sql) === TRUE) {
                //echo "New time preference created. ";
                $return_json_arr['result'] = 'SUCCESS';
                $return_json_arr['user_id'] = $user_id;
                $return_json_arr['daytime'] = $daytime;
                
            } else {
                //echo "Error: " . $sql . "<br>" . $conn->error;
                
                $return_json_arr['code'] = 'DB_INSERT_FAIL';
                $return_json_arr['details'] = 'There is a database error when inserting time preferences. ';
                $conn->close();
                return false;
                
            }
            
            
            $conn->close();
            
        }catch(Exception $e) {
            mysqli_rollback($conn);
            $return_json_arr['code'] = 'EXCEPTION';
            $return_json_arr['details'] = 'There is an unexpected exception. ';
            $conn->close();
            
            return false;
        }
        
        $return_json_arr['result'] = 'SUCCESS';
        return true;
        
    }
?>