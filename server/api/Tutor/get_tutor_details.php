<?php 
    header("Content-Type: application/json; charset=UTF-8");
    set_time_limit(0);
    
    include_once '../database.php';

    $receive_json_obj = array();
    $return_json_obj = array();

    ob_flush();
    
    $receive_json_obj = json_decode(file_get_contents('php://input'), true);
    
    $tutor_id = 0;
    
    if (isset($receive_json_obj["tutor_id"])) {
        $tutor_id = $receive_json_obj["tutor_id"];
        
    }
    
    getTutorDetails($tutor_id);
    
    flush();
    ob_start();
    
    echo json_encode($return_json_arr, 10);
    
    
    
    function getTutorDetails($tutor_id) {
        global $return_json_arr;
        $isValueValid = true;
        $tutors = array();
        
        $return_json_arr['result'] = 'FAIL';
        
        try{
            // Check null
            if ($tutor_id <= 0) {
                $return_json_arr['code'] = 'NULL_TUTOR_ID';
                $return_json_arr['details'] = 'A Tutor Id must be specified. ';
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
            
            $sql =  "SELECT * FROM View_Tutor WHERE user_id = " . $tutor_id . ";";
            
            $result = $conn->query($sql);
            
            if ($result->num_rows > 0) {
                // output data of each row
                $datetimes = array();
                while($row = $result->fetch_assoc()) {
                    $return_json_arr['user_id'] = $row["user_id"];
                    $return_json_arr['username'] = $row["username"];
                    $return_json_arr['email'] = $row["email"];
                    $return_json_arr['subject_id'] = $row["subject_id"];
                    $return_json_arr['first_name'] = $row["first_name"];
                    $return_json_arr['last_name'] = $row["last_name"];
                    $return_json_arr['dob'] = $row["dob"];
                    $return_json_arr['gender'] = $row["gender"];
                    $return_json_arr['postcode'] = $row["postcode"];
                    $return_json_arr['address'] = $row["address"];
                    $return_json_arr['educations'] = $row["educations"];
					$return_json_arr['price'] = $row["price"];
                    $return_json_arr['is_approved'] = $row["is_approved"];
                    if(isset($row["weekday"]) && isset($row["datetime"])) {
                        $datetime['weekday'] = $row["weekday"];
                        $datetime['datetime'] = $row["datetime"];
                        $datetimes[]=$datetime;
                    }
                    
                    
                }
                $return_json_arr['daytime'] = $datetimes;
            } else {
                echo "Error: " . $sql . "<br>" . $conn->error;
                
                $return_json_arr['code'] = 'DB_SELECT_FAIL';
                $return_json_arr['details'] = 'There is no such tutor record in the database. ';
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