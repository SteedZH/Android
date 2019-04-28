<?php
class Database{
 
    // specify your own database credentials
    private $host = "localhost";    // steedzh.cb5bdohx0yuo.eu-west-2.rds.amazonaws.com:3306
    private $db_name = "COMP6239";
    private $username = "root";     // admin
    private $password = "";         // pwCOMP6239
    public $conn;
 
    // get the database connection
    public function getConnection(){
 
        $this->conn = null;
 
        try{
            //$this->conn = new mysqli($host, $username, $password, $db_name);
            $this->conn = new mysqli("steedzh.cb5bdohx0yuo.eu-west-2.rds.amazonaws.com:3306", "admin", "pwCOMP6239", "COMP6239");
            //$this->conn = new PDO("mysql:host=" . $this->host . ";dbname=" . $this->db_name, $this->username, $this->password);
            //$this->conn->exec("set names utf8");
        }catch(PDOException $exception){
            echo "Connection error: " . $exception->getMessage();
        }
 
        return $this->conn;
    }
}
?>