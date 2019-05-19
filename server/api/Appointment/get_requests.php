<?php 
    header("Content-Type: application/json; charset=UTF-8");
    set_time_limit(0);
    
    include_once '../database.php';

    $receive_json_obj = array();
    $return_json_obj = array();

    ob_flush();
    
    $receive_json_obj = json_decode(file_get_contents('php://input'), true);
    $tutor_id = 0;
	$student_id = 0;
    
    if (isset($receive_json_obj["tutor_id"])) {
        $tutor_id = $receive_json_obj["tutor_id"];
    }
	
	if (isset($receive_json_obj["student_id"])) {
        $student_id = $receive_json_obj["student_id"];
    }
    
    getRequests($tutor_id, $student_id);
    
    flush();
    ob_start();
    
    echo json_encode($return_json_arr, 10);
    
    
    
    function getRequests($tutor_id, $student_id) {
        global $return_json_arr;
        $isValueValid = true;
        $appointments = array();
        
        $return_json_arr['result'] = 'FAIL';
        
        try{
            // Check null
            if ($tutor_id <= 0 && $student_id <= 0) {
                $return_json_arr['code'] = 'NULL_TUTOR_ID';
                $return_json_arr['details'] = 'A tutor Id or student Id must be specified. ';
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
            
            $sql =  "SELECT * FROM View_Request WHERE tutor_user_id = " . $tutor_id . " OR student_user_id = " . $student_id . ";";
            //echo $sql;
            $result = $conn->query($sql);
            
            if ($result->num_rows > 0) {
                // output data of each row
                while($row = $result->fetch_assoc()) {
                    $appointment = array();
                    $appointment['appointment_id'] = $row["appointment_id"];
                    $appointment['student_user_id'] = $row["student_user_id"];
					$appointment['tutor_user_id'] = $row["tutor_user_id"];
                    $appointment['s_username'] = $row["s_username"];
                    $appointment['s_firstname'] = $row["s_firstname"];
                    $appointment['s_gender'] = $row["s_gender"];
					$appointment['t_username'] = $row["t_username"];
                    $appointment['t_firstname'] = $row["t_firstname"];
                    $appointment['t_gender'] = $row["t_gender"];
                    $appointment['start_time'] = $row["start_time"];
                    $appointment['end_time'] = $row["end_time"];
                    $appointments[] = $appointment;
                }
                $return_json_arr['requests'] = $appointments;
            } else {
                echo "Error: " . $sql . "<br>" . $conn->error;
                
                $return_json_arr['code'] = 'DB_SELECT_FAIL';
                $return_json_arr['details'] = 'There is no non-approved appointment records in the database. ';
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