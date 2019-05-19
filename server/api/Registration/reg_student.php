<?php 
    header("Content-Type: application/json; charset=UTF-8");
    set_time_limit(0);
    
    include_once '../database.php';

    $receive_json_obj = array();
    $return_json_obj = array();

    ob_flush();
    
    $receive_json_obj = json_decode(file_get_contents('php://input'), true);
    
    $username = "";
    $password = "";
    $email = "";
    $firstname = "";
    $lastname = "";
    $gender = "";
    $dob = "";
    
    if (isset($receive_json_obj["username"])) {
        $username = $receive_json_obj["username"];
        
    }
    
    if (isset($receive_json_obj["password"])) {
        $password = $receive_json_obj["password"];
        
    }
    
    
    if (isset($receive_json_obj["email"])) {
        $email = $receive_json_obj["email"];
        
    }
    
    if (isset($receive_json_obj["firstname"])) {
        $firstname = $receive_json_obj["firstname"];
        
    }
    
    if (isset($receive_json_obj["lastname"])) {
        $lastname = $receive_json_obj["lastname"];
        
    }
    
    if (isset($receive_json_obj["gender"])) {
        $gender = $receive_json_obj["gender"];
        
    }
    
    
    
    if (isset($receive_json_obj["dob"])) {
        $dob = $receive_json_obj["dob"];
        //$year = $receive_json_arr["year"];
        //$month = $receive_json_arr["month"];
        //$day = $receive_json_arr["day"];
        
    }
    
    registerStudent($username, $password, $email, $firstname, $lastname, $gender, $dob);
    
    flush();
    ob_start();
    
    echo json_encode($return_json_arr, 10);
    
    
    
    function registerStudent($username, $password, $email, $firstname, $lastname, $gender, $dob) {
        global $return_json_arr;
        $isValueValid = true;
        
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
            
            $sql =  "INSERT INTO User (username, email, password, password_hash) VALUES ('" . $username . "', '" . $email . "', '" . $password . "', SHA2('" . $password . "', 256));" . 
                    "INSERT INTO Student (user_id, first_name, last_name, dob, gender) VALUES (" . 
                    "(SELECT user_id FROM View_User WHERE username = '" . $username . "'), '" . $firstname . "', '" . $lastname . "', '" . $dob . "', '" . $gender . "');";
            
            if ($conn->multi_query($sql) === TRUE) {
                //echo "New records created. ";
            } else {
                //echo "Error: " . $sql . "<br>" . $conn->error;
                
                $return_json_arr['code'] = 'DB_INSERT_FAIL';
                $return_json_arr['details'] = 'There is a databaser error when inserting a student account record. ';
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