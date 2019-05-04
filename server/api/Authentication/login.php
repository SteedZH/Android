<?php 
    /*
        permission
            0:  Administrator
            1:  Tutor
            2:  Student
    */
    header("Content-Type: application/json; charset=UTF-8");
    set_time_limit(0);
    
    include_once '../database.php';

    $receive_json_obj = array();
    $return_json_obj = array();

    ob_flush();
    
    $receive_json_obj = json_decode(file_get_contents('php://input'), true);
    
    $username = "";
    $password = "";
    
    if (isset($receive_json_obj["username"])) {
        $username = $receive_json_obj["username"];
    }
    
    if (isset($receive_json_obj["password"])) {
        $password = $receive_json_obj["password"];
    }
    
    login($username, $password);
    
    flush();
    ob_start();
    
    echo json_encode($return_json_arr, 10);
    
    function login($username, $password) {
        global $return_json_arr;
        $isValueValid = true;
        $user_id="0";
        $sys_password="";
        
        $permission="0";
        $email="";
        $create_time="";
        
        $first_name="";
        $last_name="";
        $gender;
        $dob="";
        $gender="";
        
        //Tutor
        $subject_id="-1";
        $postcode="";
        $address="";
        $education="";
        $is_approved="0";
        
        //Student
        $descripion="";
        
        
        $return_json_arr['result'] = 'FAIL';
        
        try{
            // instantiate database connection
            $db = new Database();
            $conn = $db->getConnection();
            
            $return_json_arr['username']=$username;
            
            // Check null
            if($username === "") {
                $return_json_arr['code'] = 'NULL_USERNAME';
                $return_json_arr['details'] = 'The login username cannot be empty. ';
                return false;
            }
            if($password === "") {
                $return_json_arr['code'] = 'NULL_PASSWORD';
                $return_json_arr['details'] = 'The login password cannot be empty. ';
                return false;
            }
            
            // Check connection
            if ($conn->connect_error) {
                $return_json_arr['code'] = 'DB_CONNECTION_FAIL';
                $return_json_arr['details'] = 'There is a server error when connecting to the database. ';
                $conn->close();
                return false;
                
            }
            
            $sql =  "SELECT user_id, email, password, create_time FROM View_User WHERE username = '" . $username . "';";
            $result = $conn->query($sql);
            
            if ($result->num_rows > 0) {
                // output data of each row
                while($row = $result->fetch_assoc()) {
                    //echo "id: " . $row["id"]. " - Name: " . $row["firstname"]. " " . $row["lastname"]. "<br>";
                    $user_id = $row["user_id"];
                    $sys_password=$row["password"];
                    $email=$row['email'];
                }
                
            } else {
                //echo "Error: " . $sql . "<br>" . $conn->error;
                
                $return_json_arr['code'] = 'DB_SELECT_FAIL';
                $return_json_arr['details'] = 'There is not any matcing user records in the database. ';
                $conn->close();
                return false;
                
            }
            
            if ($password === $sys_password) {
                $return_json_arr['user_id']=$user_id;
                $return_json_arr['username']=$username;
                $return_json_arr['email']=$email;
                
                //Administrator
                $sql =  "SELECT user_id FROM View_Administrator WHERE user_id = " . $user_id . ";";
                $result = $conn->query($sql);
                
                if ($result->num_rows > 0) {
                    $permission="1";
                    $is_approved="1";
                    
                    $return_json_arr['result'] = 'SUCCESS';
                    $return_json_arr['permission'] = $permission;
                    $return_json_arr['is_approved'] = $is_approved;
                    $conn->close();
                    return true;
                }
                
                //Tutor
                $sql =  "SELECT * FROM View_Tutor WHERE user_id = " . $user_id . ";";
                $result = $conn->query($sql);
                
                if ($result->num_rows > 0) {
                    while($row = $result->fetch_assoc()) {
                        //echo "id: " . $row["id"]. " - Name: " . $row["firstname"]. " " . $row["lastname"]. "<br>";
                        $is_approved=$row["is_approved"];
                        
                        $postcode=$row["postcode"];
                        $address=$row["address"];
                        $educations=$row["educations"];
                        
                        $first_name=$row["first_name"];
                        $last_name=$row["last_name"];
                        $dob=$row["dob"];
                        $gender=$row["gender"];
                    }
                    $permission="2";
                    
                    $return_json_arr['result'] = 'SUCCESS';
                    $return_json_arr['permission'] = $permission;
                    $return_json_arr['is_approved'] = $is_approved;
                    
                    $return_json_arr['postcode'] = $postcode;
                    $return_json_arr['address'] = $address;
                    $return_json_arr['educations'] = $educations;
                    
                    $return_json_arr['first_name'] = $first_name;
                    $return_json_arr['last_name'] = $last_name;
                    $return_json_arr['dob'] = $dob;
                    $return_json_arr['gender'] = $gender;
                    
                    $conn->close();
                    return true;
                }
                
                //Student
                $sql =  "SELECT * FROM View_Student WHERE user_id = " . $user_id . ";";
                $result = $conn->query($sql);
                
                if ($result->num_rows > 0) {
                    while($row = $result->fetch_assoc()) {
                        //echo "id: " . $row["id"]. " - Name: " . $row["firstname"]. " " . $row["lastname"]. "<br>";
                        
                        $descripion = $row["descripion"];
                        
                        $first_name=$row["first_name"];
                        $last_name=$row["last_name"];
                        $dob=$row["dob"];
                        $gender=$row["gender"];
                    }
                    $permission="3";
                    $is_approved="1";
                    $return_json_arr['result'] = 'SUCCESS';
                    $return_json_arr['permission'] = $permission;
                    $return_json_arr['is_approved'] = $is_approved;
                    
                    $return_json_arr['descripion'] = $descripion;
                    
                    $return_json_arr['first_name'] = $first_name;
                    $return_json_arr['last_name'] = $last_name;
                    $return_json_arr['dob'] = $dob;
                    $return_json_arr['gender'] = $gender;
                    
                    $conn->close();
                    return true;
                }
                
                $permission="0";
                $is_approved="0";
                
            }else {
                $return_json_arr['code'] = 'DB_SELECT_FAIL';
                $return_json_arr['details'] = 'There is not any matcing user records in the database. ';
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
        
        return true;
        
    }
?>