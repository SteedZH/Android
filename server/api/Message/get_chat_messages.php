<?php 
    header("Content-Type: application/json; charset=UTF-8");
    set_time_limit(0);
    
    include_once '../database.php';

    $receive_json_obj = array();
    $return_json_obj = array();

    ob_flush();
    
    $receive_json_obj = json_decode(file_get_contents('php://input'), true);
    
    $sender_user_id = 0;
    $receiver_user_id = 0;
    
    if (isset($receive_json_obj["sender_user_id"])) {
        $sender_user_id = $receive_json_obj["sender_user_id"];
    }
    if (isset($receive_json_obj["receiver_user_id"])) {
        $receiver_user_id = $receive_json_obj["receiver_user_id"];
    }
    
    getMessages($sender_user_id, $receiver_user_id);
    
    flush();
    ob_start();
    
    echo json_encode($return_json_obj, 10);
    
    
    
    function getMessages($sender_user_id, $receiver_user_id) {
        global $return_json_obj;
        $isValueValid = true;
        $messages = array();
        
        $return_json_obj['result'] = 'FAIL';
        
        try{
            // instantiate database connection
            $db = new Database();
            $conn = $db->getConnection();
            
            // Check null
            if($details === ""){
                $return_json_obj['code'] = 'EMPTY_SUBJECT_NAME)';
                $return_json_obj['details'] = 'A message must have contents and cannot be empty. ';
                return false;
            }
            
            // Check connection
            if ($conn->connect_error) {
                $return_json_obj['code'] = 'DB_CONNECTION_FAIL';
                $return_json_obj['details'] = 'There is a server error when connecting to the database. ';
                $conn->close();
                return false;
                
            }
            
            $sql =  "SELECT * FROM Message WHERE " . 
                    "(sender_user_id = " . $sender_user_id . " AND receiver_user_id = " . $receiver_user_id . ") OR " . 
                    "(sender_user_id = " . $receiver_user_id . " AND receiver_user_id = " . $sender_user_id . ") ORDER BY send_time DESC LIMIT 0, 20;";
            $result = $conn->query($sql);
            
            if ($result->num_rows > 0) {
                // output data of each row
                while($row = $result->fetch_assoc()) {
                    $message = array();
                    //echo "id: " . $row["id"]. " - Name: " . $row["firstname"]. " " . $row["lastname"]. "<br>";
                    $message['sender_user_id'] = $row["sender_user_id"];
                    $message['receiver_user_id'] = $row["receiver_user_id"];
                    $message['details'] = $row["details"];
                    $message['send_time'] = $row["send_time"];
                    $message['is_read'] = $row["is_read"];
                    $messages[] = $message;
                }
                $return_json_obj['messages'] = $messages;
            } else {
                echo "Error: " . $sql . "<br>" . $conn->error;
                
                $return_json_obj['code'] = 'DB_SELECT_FAIL';
                $return_json_obj['details'] = 'There is no related message records in the database. ';
                $return_json_obj['messages'] = $messages;
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