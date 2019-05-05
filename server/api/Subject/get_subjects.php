<?php 
    header("Content-Type: application/json; charset=UTF-8");
    set_time_limit(0);
    
    include_once '../database.php';

    $receive_json_obj = array();
    $return_json_obj = array();

    ob_flush();
    
    //$receive_json_obj = json_decode(file_get_contents('php://input'), true);
    
    getSubjects();
    
    flush();
    ob_start();
    
    echo json_encode($return_json_arr, 10);
    
    
    
    function getSubjects() {
        global $return_json_arr;
        $isValueValid = true;
        $subjects = array();
        
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
            
            $sql =  "SELECT * FROM View_Subject;";
            $result = $conn->query($sql);
            
            if ($result->num_rows > 0) {
                // output data of each row
                while($row = $result->fetch_assoc()) {
                    $subject = array();
                    //echo "id: " . $row["id"]. " - Name: " . $row["firstname"]. " " . $row["lastname"]. "<br>";
                    $subject['name'] = $row["name"];
                    $subject['subject_id'] = $row["subject_id"];
                    $subjects[] = $subject;
                }
                $return_json_arr['subjects'] = $subjects;
            } else {
                echo "Error: " . $sql . "<br>" . $conn->error;
                
                $return_json_arr['code'] = 'DB_SELECT_FAIL';
                $return_json_arr['details'] = 'There is no subject records in the database. ';
                $return_json_arr['subjects'] = $subjects;
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