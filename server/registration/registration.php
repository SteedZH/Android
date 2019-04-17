<?php 
    header("Content-Type: application/json; charset=UTF-8");
    set_time_limit(0);
    $dbservername = "192.168.56.2"; //192.168.56.2 OR 192.168.1.13
    $dbusername = "mysqlwb";
    $dbpassword = "passw0rd";
    $dbname = "csp354";

    $receive_json_arr = array();
    $return_json_arr = array();

    ob_flush();

    if(isset($_POST['REG_STUDENT'])) {
        $receive_json_arr = json_decode($_POST["REG_STUDENT"], true);
        
        $username = "";
        $firstname = "";
        $lastname = "";
        $email = "";
        $gender = "";
        $password = "";
        $year = "";
        $day = "";
        $month = "";
        
        if (isset($receive_json_arr["username"])) {
            $username = $receive_json_arr["username"];
            
        }
        
        if (isset($receive_json_arr["firstname"])) {
            $firstname = $receive_json_arr["firstname"];
            
        }
        
        if (isset($receive_json_arr["lastname"])) {
            $lastname = $receive_json_arr["lastname"];
            
        }
        
        if (isset($receive_json_arr["email"])) {
            $email = $receive_json_arr["email"];
            
        }
        
        if (isset($receive_json_arr["gender"])) {
            $gender = $receive_json_arr["gender"];
            
        }
        
        if (isset($receive_json_arr["password"])) {
            $password = $receive_json_arr["password"];
            
        }
        
        if (isset($receive_json_arr["year"]) && isset($receive_json_arr["month"]) && isset($receive_json_arr["day"]) ) {
            $year = $receive_json_arr["year"];
            $month = $receive_json_arr["month"];
            $day = $receive_json_arr["day"];
            
        }
        
        registerStudent($username, $firstname, $lastname, $email, $gender, $password, $year, $month, $day);
        
    }elseif (isset($_POST['REG_TUTOR'])){
        $receive_json_arr = json_decode($_POST["REG_TUTOR"], true);
        
        $username = "";
        $firstname = "";
        $lastname = "";
        $email = "";
        $gender = "";
        $password = "";
        $year = "";
        $day = "";
        $month = "";
        $address = "";
        $postcode = "";
        
        if (isset($receive_json_arr["username"])) {
            $username = $receive_json_arr["username"];
            
        }
        
        if (isset($receive_json_arr["firstname"])) {
            $firstname = $receive_json_arr["firstname"];
            
        }
        
        if (isset($receive_json_arr["lastname"])) {
            $lastname = $receive_json_arr["lastname"];
            
        }
        
        if (isset($receive_json_arr["email"])) {
            $email = $receive_json_arr["email"];
            
        }
        
        if (isset($receive_json_arr["gender"])) {
            $gender = $receive_json_arr["gender"];
            
        }
        
        if (isset($receive_json_arr["password"])) {
            $password = $receive_json_arr["password"];
            
        }
        
        if (isset($receive_json_arr["year"]) && isset($receive_json_arr["month"]) && isset($receive_json_arr["day"]) ) {
            $year = $receive_json_arr["year"];
            $month = $receive_json_arr["month"];
            $day = $receive_json_arr["day"];
            
        }
        
        registerTutor($username, $firstname, $lastname, $email, $gender, $password, $year, $month, $day);
    }
?>