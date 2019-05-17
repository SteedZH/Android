<?php 
    header("Content-Type: application/json; charset=UTF-8");
    set_time_limit(0);
    
    include_once '../database.php';

    $receive_json_obj = array();
    $return_json_obj = array();

    ob_flush();
    
    //$receive_json_obj = json_decode(file_get_contents('php://input'), true);
    
    
    searchTutorRequests();
    
    flush();
    ob_start();
    
    echo json_encode($return_json_arr, 10);
    
    
    
    function searchTutorRequests() {
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
            
            $sql =  "SELECT * FROM Tutor, User WHERE Tutor.user_id = User.user_id AND is_approved = 0;";
            //echo $sql;
            $result = $conn->query($sql);
            
            if ($result->num_rows > 0) {
                // output data of each row
                while($row = $result->fetch_assoc()) {
                    $tutor = array();
                    //echo "id: " . $row["id"]. " - Name: " . $row["firstname"]. " " . $row["lastname"]. "<br>";
                    $tutor['user_id'] = $row["user_id"];
                    $tutor['first_name'] = $row["first_name"];
                    $tutor['email'] = $row["email"];
                    $tutor['subject_id'] = $row["subject_id"];
                    $tutors[] = $tutor;
                }
                $return_json_arr['tutors'] = $tutors;
            } else {
                echo "Error: " . $sql . "<br>" . $conn->error;
                
                $return_json_arr['code'] = 'DB_SELECT_FAIL';
                $return_json_arr['details'] = 'There is no tutor application records in the database. ';
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