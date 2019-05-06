<?php 
    header("Content-Type: application/json; charset=UTF-8");
    set_time_limit(0);
    
    include_once '../database.php';

    $receive_json_obj = array();
    $return_json_obj = array();

    ob_flush();
    
    $receive_json_obj = json_decode(file_get_contents('php://input'), true);
    $student_id = 0;
    $tutor_id = 0;
    $start_time = "";
    $end_time = "";
    
    if (isset($receive_json_obj["student_id"])) {
        $student_id = $receive_json_obj["student_id"];
    }
    
    if (isset($receive_json_obj["tutor_id"])) {
        $tutor_id = $receive_json_obj["tutor_id"];
    }
    
    if (isset($receive_json_obj["start_time"])) {
        $start_time = $receive_json_obj["start_time"];
    }
    
    if (isset($receive_json_obj["end_time"])) {
        $end_time = $receive_json_obj["end_time"];
    }
    
    setRequests($student_id, $tutor_id, $start_time, $end_time);
    
    flush();
    ob_start();
    
    echo json_encode($return_json_arr, 10);
    
    
    
    function setRequests($student_id, $tutor_id, $start_time, $end_time) {
        global $return_json_arr;
        $isValueValid = true;
        $appointments = array();
        
        $format = 'Y-m-d H:i:s';
        $weekday_format = array('Sun','Mon','Tue','Wed','Thu','Fri','Sat'); //0(Sun) - 6(Sat)
        $sd = date($format);
        $ed = date($format);
        $weekday = 0;
        
        $return_json_arr['result'] = 'FAIL';
        
        try{
            // Check null
            if ($student_id <= 0) {
                $return_json_arr['code'] = 'NULL_STUDENT_ID';
                $return_json_arr['details'] = 'A student\'s user Id must be specified. ';
                return false;
            }
            
            if ($tutor_id <= 0) {
                $return_json_arr['code'] = 'NULL_TUTOR_ID';
                $return_json_arr['details'] = 'A tutor\'s user Id must be specified. ';
                return false;
            }
            
            if ($start_time === "" or $end_time === "") {
                $return_json_arr['code'] = 'NULL_APPOINTMENT_DATE';
                $return_json_arr['details'] = 'The start time and end time of an appointment must be specified. ';
                return false;
            }else {
                $sd = date_create_from_format($format, $start_time);
                $ed = date_create_from_format($format, $end_time);
                $weekday = date('w', strtotime($start_time));
                
                if ($ed <= $sd or $sd <= date($format)) {
                    $return_json_arr['code'] = '*INVALID_APPOINTMENT_DATE';
                    $return_json_arr['details'] = 'The start time and end time of an appointment must be valid. ';
                    return false;
                }
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
            
            //Check TimePreference
            $sql =  "SELECT * FROM TimePreference WHERE tutor_user_id = " . $tutor_id . ";";
            $result = $conn->query($sql);
            
            if ($result->num_rows > 0) {
                // output data of each row
                while($row = $result->fetch_assoc()) {
                    $preference = array();
                    $preference['weekday'] = (int)$row["weekday"];
                    $preference['datetime'] = (int)$row["datetime"];
                    $preferences[] = $preference;
                }
                
                foreach ($preferences as $p) {
                    if ($p['weekday'] === $weekday) {
                        switch ($p['datetime']) {
                            case 1:
                                if (((int)date('H', $sd)) >= 12) {
                                    $return_json_arr['details'] = 'Your requested appointment date and time is outside the tutor\'s time preferences. You may be rearranged for another day and time. Please wait for your tutor to confirm your appointment. ';
                                }
                                break;
                            case 2:
                                if (((int)date('H', $sd)) < 12) {
                                    $return_json_arr['details'] = 'Your requested appointment date and time is outside the tutor\'s time preferences. You may be rearranged for another day and time. Please wait for your tutor to confirm your appointment. ';
                                }
                                break;
                            default:
                                $return_json_arr['details'] = "Please wait for your tutor to confirm your appointment. ";
                        }
                    }
                }
                if (!isset($return_json_arr['details'])) {
                    $return_json_arr['details'] = 'Your requested appointment date and time is outside the tutor\'s time preferences. You may be rearranged for another day and time. Please wait for your tutor to confirm your appointment. ';
                }
            }
            
            $sql =  "INSERT INTO Appointment (tutor_user_id, student_user_id, start_time, end_time, is_confirm) VALUES (" . $tutor_id . ", " . $student_id . ", '" . $start_time . "', '" . $end_time . "', 0);";
            
            if ($conn->query($sql) === TRUE) {
                echo "New records created. ";
                $return_json_arr['result'] = 'SUCCESS';
                $return_json_arr['student_id'] = $student_id;
                $return_json_arr['tutor_id'] = $tutor_id;
                
            } else {
                echo "Error: " . $sql . "<br>" . $conn->error;
                
                $return_json_arr['code'] = 'DB_INSERT_FAIL';
                $return_json_arr['details'] = 'There is a database error when inserting an appointment request record. ';
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