<?php 
    header("Content-Type: application/json; charset=UTF-8");
    set_time_limit(0);
    
    include_once '../database.php';

    $receive_json_obj = array();
    $return_json_obj = array();

    ob_flush();
    
    $receive_json_obj = json_decode(file_get_contents('php://input'), true);
    
    $tutor_subject_id = 0;
    $tutor_location = "%";
    $tutor_name = "%";
    
    if (isset($receive_json_obj["tutorSubject"])) {
        $tutor_subject_id = $receive_json_obj["tutorSubject"];
        
    }
    
    if (isset($receive_json_obj["tutorLocation"])) {
        $tutor_location = $receive_json_obj["tutorLocation"];
        
    }
    
    if (isset($receive_json_obj["tutorName"])) {
        $tutor_name = $receive_json_obj["tutorName"];
        
    }
    
    searchTutors($tutor_subject_id, $tutor_location, $tutor_name);
    
    flush();
    ob_start();
    
    echo json_encode($return_json_arr, 10);
    
    
    
    function searchTutors($tutor_subject_id, $tutor_location, $tutor_name) {
        global $return_json_arr;
        $isValueValid = true;
        $tutors = array();
        
        $return_json_arr['result'] = 'FAIL';
        
        try{
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
            
            $subject_cause = ($tutor_subject_id <= 0) ? "subject_id >= 0" : "subject_id = " . $tutor_subject_id;
            $location_cause = ($tutor_location === "%") ? "postcode LIKE '%'" : "(postcode LIKE '" . $tutor_location . "%' OR address LIKE '%" . $tutor_location . "%')";
            $name_cause = ($tutor_name === "%") ? "first_name LIKE '%'" : "(first_name LIKE '%" . $tutor_name . "%' OR last_name LIKE '%" . $tutor_name . "%')";
            
            $sql =  "SELECT DISTINCT user_id, first_name, username, postcode,subject_id FROM View_Tutor WHERE is_approved > 0 AND " . $subject_cause . " AND " . $location_cause . " AND " . $name_cause . ";";
            //echo $sql;
            $result = $conn->query($sql);
            
            if ($result->num_rows > 0) {
                // output data of each row
                while($row = $result->fetch_assoc()) {
                    $tutor = array();
                    //echo "id: " . $row["id"]. " - Name: " . $row["firstname"]. " " . $row["lastname"]. "<br>";
                    $tutor['user_id'] = $row["user_id"];
                    $tutor['first_name'] = $row["first_name"];
                    $tutor['username'] = $row["username"];
                    $tutor['postcode'] = $row["postcode"];
                    $tutor['subject_id'] = $row["subject_id"];
                    $tutors[] = $tutor;
                }
                $return_json_arr['tutors'] = $tutors;
            } else {
                echo "Error: " . $sql . "<br>" . $conn->error;
                
                $return_json_arr['code'] = 'DB_SELECT_FAIL';
                $return_json_arr['details'] = 'There is no tutor records in the database. ';
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