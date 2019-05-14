<?php 
    header("Content-Type: application/json; charset=UTF-8");
    set_time_limit(0);
    
    include_once '../database.php';

    $receive_json_obj = array();
    $return_json_obj = array();

    ob_flush();
    
    $receive_json_obj = json_decode(file_get_contents('php://input'), true);
    $tutor_id = 0;
    
    if (isset($receive_json_obj["appointment_id"])) {
        $appointment_id = $receive_json_obj["appointment_id"];
    }
    
    getAppointments($appointment_id);
    
    flush();
    ob_start();
    
    echo json_encode($return_json_arr, 10);
    
    
    
    function getAppointments($appointment_id) {
        global $return_json_arr;
        $isValueValid = true;
        $appointments = array();
        
        $return_json_arr['result'] = 'FAIL';
        
        try{
            // Check null
            if ($appointment_id <= 0) {
                $return_json_arr['code'] = 'NULL_TUTOR_ID';
                $return_json_arr['details'] = 'A tutor Id must be specified. ';
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
            
            $sql =  "SELECT * FROM View_Appointment WHERE appointment_id = " . $appointment_id . ";";
            //echo $sql;
            $result = $conn->query($sql);
            
            if ($result->num_rows > 0) {
                // output data of each row
                while($row = $result->fetch_assoc()) {
                    //$appointment = array();
                    $return_json_arr['appointment_id'] = $row["appointment_id"];
                    $return_json_arr['student_user_id'] = $row["student_user_id"];
                    $return_json_arr['username'] = $row["username"];
                    $return_json_arr['first_name'] = $row["first_name"];
                    $return_json_arr['last_name'] = $row["last_name"];
                    $return_json_arr['gender'] = $row["gender"];
                    $return_json_arr['start_time'] = $row["start_time"];
                    $return_json_arr['end_time'] = $row["end_time"];
                }
            } else {
                echo "Error: " . $sql . "<br>" . $conn->error;
                
                $return_json_arr['code'] = 'DB_SELECT_FAIL';
                $return_json_arr['details'] = 'There is no appointment records in the database. ';
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