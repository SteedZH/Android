<?php 
    header("Content-Type: application/json; charset=UTF-8");
    set_time_limit(0);
    
    include_once '../database.php';

    $receive_json_obj = array();
    $return_json_obj = array();

    ob_flush();
    
    $receive_json_obj = json_decode(file_get_contents('php://input'), true);
    $appointment_id = 0;
    $is_confirm = 0;
    
    if (isset($receive_json_obj["appointment_id"])) {
        $appointment_id = $receive_json_obj["appointment_id"];
    }
    
    if (isset($receive_json_obj["is_confirm"])) {
        $is_confirm = $receive_json_obj["is_confirm"];
    }
    
    updateAppointmentApproval($appointment_id, $is_confirm);
    
    flush();
    ob_start();
    
    echo json_encode($return_json_arr, 10);
    
    
    
    function updateAppointmentApproval($appointment_id, $is_confirm) {
        global $return_json_arr;
        $isValueValid = true;
        
        $return_json_arr['result'] = 'FAIL';
        
        try{
            // Check null
            if ($appointment_id <= 0) {
                $return_json_arr['code'] = 'NULL_APPOINTMENT_ID';
                $return_json_arr['details'] = 'An appointment Id must be specified. ';
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
            
            $sql =  "UPDATE Appointment SET is_confirm = " . $is_confirm . " WHERE appointment_id = " . $appointment_id . ";";
            
            if ($conn->query($sql) === TRUE) {
                echo "Appointment records updated. ";
                $return_json_arr['result'] = 'SUCCESS';
                $return_json_arr['appointment_id'] = $appointment_id;
                $return_json_arr['is_confirm'] = $is_confirm;
                
            } else {
                echo "Error: " . $sql . "<br>" . $conn->error;
                
                $return_json_arr['code'] = 'DB_INSERT_FAIL';
                $return_json_arr['details'] = 'There is a database error when updating an appointment record. ';
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