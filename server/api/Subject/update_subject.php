<?php 
    header("Content-Type: application/json; charset=UTF-8");
    set_time_limit(0);
    
    include_once '../database.php';

    $receive_json_obj = array();
    $return_json_obj = array();

    ob_flush();
    
    $receive_json_obj = json_decode(file_get_contents('php://input'), true);
    
    $subject_id = "";
    $name = "";
    
    if (isset($receive_json_obj["subject_id"])) {
        $subject_id = $receive_json_obj["subject_id"];
    }
    
    if (isset($receive_json_obj["name"])) {
        $name = $receive_json_obj["name"];
    }
    
    updateSubject($subject_id, $name);
    
    flush();
    ob_start();
    
    echo json_encode($return_json_obj, 10);
    
    
    
    function updateSubject($subject_id, $name) {
        global $return_json_obj;
        $isValueValid = true;
        
        $return_json_obj['result'] = 'FAIL';
        $subjects = new array();
        
        try{
            // Check null
            if($name === ""){
                $return_json_obj['code'] = 'EMPTY_SUBJECT_NAME)';
                $return_json_obj['details'] = 'A Subject name cannot be empty. ';
                return false;
            }
            if($subject_id === ""){
                $return_json_obj['code'] = 'EMPTY_SUBJECT_ID)';
                $return_json_obj['details'] = 'A Subject id cannot be empty. ';
                return false;
            }
            
            // instantiate database connection
            $db = new Database();
            $conn = $db->getConnection();
            
            
            // Check connection
            if ($conn->connect_error) {
                $return_json_obj['code'] = 'DB_CONNECTION_FAIL';
                $return_json_obj['details'] = 'There is a server error when connecting to the database. ';
                $conn->close();
                return false;
                
            }
            
            $sql =  "UPDATE Subject SET name = '" . $name . "' WHERE subject_id = " . $subject_id . ";";
            
            if ($conn->query($sql) === TRUE) {
                echo "New records updated. ";
                $return_json_obj['result'] = 'SUCCESS';
                $return_json_obj['subject_id'] = $subject_id;
                $return_json_obj['name'] = $name;
                
            } else {
                echo "Error: " . $sql . "<br>" . $conn->error;
                
                $return_json_obj['code'] = 'DB_UPDATE_FAIL';
                $return_json_obj['details'] = 'There is a databaser error when updating to a subject record. ';
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
                $return_json_obj['subjects'] = $subjects;
            } else {
                echo "Error: " . $sql . "<br>" . $conn->error;
                
                $return_json_obj['code'] = 'DB_SELECT_FAIL';
                $return_json_obj['details'] = 'There is no subject records in the database. ';
                $conn->close();
                return false;
                
            }
            
            $conn->close();
            
        }catch(Exception $e) {
            mysqli_rollback($conn);
            $return_json_obj['code'] = 'EXCEPTION';
            $return_json_obj['details'] = 'There is an unexpected exception. ';
            $conn->close();
            
            return false;
        }
        
        $return_json_obj['result'] = 'SUCCESS';
        return true;
        
    }
?>