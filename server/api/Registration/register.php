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
	
	if(isset($_POST['REGISTER'])) {
		$receive_json_arr = json_decode($_POST["REGISTER"], true);
		
		$username = "";
		$firstname = "";
		$lastname = "";
		$email = "";
		$gender = "";
		$phone = "";
		$password = "";
		$dob = "";
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
		
		if (isset($receive_json_arr["phone"])) {
			$phone = $receive_json_arr["phone"];
			
		}
		
		if (isset($receive_json_arr["password"])) {
			$password = $receive_json_arr["password"];
			
		}
		
		if (isset($receive_json_arr["year"]) && isset($receive_json_arr["month"]) && isset($receive_json_arr["day"]) ) {
			$year = $receive_json_arr["year"];
			$month = $receive_json_arr["month"];
			$day = $receive_json_arr["day"];
			
		}
		
		register($username, $firstname, $lastname, $email, $gender, $password, $year, $month, $day);
		
	}elseif (isset($_POST['CHECK'])){
		$return_json_arr['flag'] = 'CHECK';
		$return_json_arr['result'] = 'DONE';
		$receive_json_arr = json_decode($_POST["CHECK"], true);
		
		if (isset($receive_json_arr["username"])) {
			checkUserName($receive_json_arr["username"]);
			
		}
		
		if (isset($receive_json_arr["firstname"])) {
			checkFirstName($receive_json_arr["firstname"]);
			
		}
		
		if (isset($receive_json_arr["lastname"])) {
			checkLastName($receive_json_arr["lastname"]);
			
		}
		
		if (isset($receive_json_arr["gender"])) {
			checkGender($receive_json_arr["gender"]);
			
		}
		
		if (isset($receive_json_arr["email"])) {
			checkEmail($receive_json_arr["email"]);
			
		}
		
		if (isset($receive_json_arr["phone"])) {
			checkPhone($receive_json_arr["phone"]);
			
		}
		
		if (isset($receive_json_arr["password"])) {
			checkPassword($receive_json_arr["password"]);
			
		}
		
		if (isset($receive_json_arr["year"]) && isset($receive_json_arr["month"]) && isset($receive_json_arr["day"]) ) {
			checkDob($receive_json_arr["year"], $receive_json_arr["month"], $receive_json_arr["day"]);
			
		}
		
	}else {
		exceptionLog("Unsupport request received. " . file_get_contents('php://input') . json_encode($_REQUEST));
	}
	
	flush();
	ob_start();
	
	echo json_encode($return_json_arr, 10);
	
	function checkUserName($username) {
		global $dbservername, $dbusername, $dbpassword, $dbname, $return_json_arr;
		
		//Check null value
		if(empty($username)) {
			//User Name is null
			$return_json_arr['username']['result'] = 'false';
			$return_json_arr['username']['details'] = 'Username cannot be empty.';
			
			return false;
			
		}
		
		//Check format
		if (!preg_match("/^[A-Za-z0-9_()-]{3,16}$/", $username)) {
			//User Name format incorrect
			$return_json_arr['username']['result'] = 'false';
			$return_json_arr['username']['details'] = 'Your username must be 3 - 16 characters. You may use letters, numbers, -, _, (, or ) only. ';
			
			return false;
			
		}
		
		//Check database records
		try{
			//Create player account records into Database
			// Create connection
			$conn = new mysqli($dbservername, $dbusername, $dbpassword, $dbname);
			
			// Check connection
			if ($conn->connect_error) {
				$return_json_arr['result'] = 'DB_CONNECTION_FAIL';
				exceptionLog("Connection failed. " . $conn->connect_error);
				$conn->close();
				return false;
					
			}
				
			$sql = 	"SELECT userName FROM Player_View WHERE userName = '" . $username . "' OR playerName = '" . $username . "';";
			
			$result = $conn->query($sql);
			if ($result->num_rows > 0) {
				$return_json_arr['username']['result'] = 'false';
				$return_json_arr['username']['details'] = 'The username ' . $username . ' has been registered. ';
				$conn->close();
				return false;
				
			}

			$conn->close();
			
		} catch (Exception $e) {
			$return_json_arr['result'] = 'DB_QUERY_EXCEPTION';
			exceptionLog("Check Username exception. Using username '" . $username . "'. ");
			return false;
				
		}
		
		$return_json_arr['username']['result'] = 'true';
		$return_json_arr['username']['details'] = $username . ' is available. ';
		
		return true;
		
	}
	
	function checkFirstName($firstname) {
		global $return_json_arr;
		
		//Check null value
		if(empty($firstname)) {
			//User Name is null
			$return_json_arr['firstname']['result'] = 'false';
			$return_json_arr['firstname']['details'] = 'Firstname cannot be empty.';
			
			return false;
			
		}
		
		//Check format
		if (!preg_match("/^[a-zA-Z ]*$/",$firstname)) {
			//First Name format incorrect
			$return_json_arr['firstname']['result'] = 'false';
			$return_json_arr['firstname']['details'] = 'Only letters and white space allowed.';
			
			return false;
			
		}
		
		$return_json_arr['firstname']['result'] = 'true';
		$return_json_arr['firstname']['details'] = '';
		
		return true;
		
	}
	
	function checkLastName($lastname) {
		global $return_json_arr;
		
		//Check null value
		if(empty($lastname)) {
			//User Name is null
			$return_json_arr['lastname']['result'] = 'false';
			$return_json_arr['lastname']['details'] = 'Lastname cannot be empty.';
			
			return false;
			
		}
		
		//Check format
		if (!preg_match("/^[a-zA-Z]*$/", $lastname)) {
			//First Name format incorrect
			$return_json_arr['lastname']['result'] = 'false';
			$return_json_arr['lastname']['details'] = 'Only letters allowed.';
			
			return false;
			
		}
		
		$return_json_arr['lastname']['result'] = 'true';
		$return_json_arr['lastname']['details'] = '';
		
		return true;
		
	}
	
	function checkGender($gender) {
		global $return_json_arr;
		
		//Check null value
		if(empty($gender)) {
			//User Name is null
			$return_json_arr['gender']['result'] = 'false';
			$return_json_arr['gender']['details'] = 'Please select a gander.';
			
			return false;
			
		}
		
		//Check format
		if (!($gender == 'M' || $gender == 'F')) {
			//First Name format incorrect
			$return_json_arr['gender']['result'] = 'false';
			$return_json_arr['gender']['details'] = 'Please select a gander.';
			
			return false;
			
		}
		
		$return_json_arr['gender']['result'] = 'true';
		$return_json_arr['gender']['details'] = '';
		
		return true;
		
	}
	
	function checkEmail($email) {
		global $dbservername, $dbusername, $dbpassword, $dbname, $return_json_arr;
		
		//Check null value
		if(empty($email)) {
			//User Name is null
			$return_json_arr['email']['result'] = 'false';
			$return_json_arr['email']['details'] = 'You must provide a valid email address.';
			
			return false;
			
		}
		
		//Check format
		if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
			//Email format incorrect
			$return_json_arr['email']['result'] = 'false';
			$return_json_arr['email']['details'] = 'Invalid email format detected.';
			
			return false;
			
		}
		
		//Check database records
		try{
			//Create player account records into Database
			// Create connection
			$conn = new mysqli($dbservername, $dbusername, $dbpassword, $dbname);
			
			// Check connection
			if ($conn->connect_error) {
				$return_json_arr['result'] = 'DB_CONNECTION_FAIL';
				exceptionLog("Connection failed. " . $conn->connect_error);
				$conn->close();
				return false;
					
			}
				
			$sql = 	"SELECT username FROM Player_View WHERE email = '" . $email . "';";
			
			$result = $conn->query($sql);
			if ($result->num_rows > 0) {
				$return_json_arr['email']['result'] = 'false';
				$return_json_arr['email']['details'] = 'The email ' . $email . ' was registered. ';
				$conn->close();
				return false;
				
			}

			$conn->close();
			
		} catch (Exception $e) {
			$return_json_arr['result'] = 'DB_QUERY_EXCEPTION';
			exceptionLog("Check email exception. Using email '" . $email . "'. ");
			return false;
				
		}
		
		$return_json_arr['email']['result'] = 'true';
		$return_json_arr['email']['details'] = '';
		
		return true;
		
	}
	
	function checkPhone($phone) {
		global $return_json_arr;
		
		//Check format
		if (!preg_match("/^(\+44\s?7\d{3}|\(?07\d{3}\)?)\s?\d{3}\s?\d{3}$/", $phone)) {
			//First Name format incorrect
			$return_json_arr['phone']['result'] = 'false';
			$return_json_arr['phone']['details'] = 'Invalid UK phone number.';
			
			return false;
			
		}
		
		$return_json_arr['phone']['result'] = 'true';
		$return_json_arr['phone']['details'] = '';
		
		return true;
		
	}
	
	function checkPassword($password) {
		global $return_json_arr;
		
		//Check null value
		if(empty($password)) {
			//User Name is null
			$return_json_arr['password']['result'] = 'false';
			$return_json_arr['password']['details'] = 'You must provide a password for your account.';
			
			return false;
		}
		
		//Check format
		if (!preg_match("/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d\\-_ ]{8,20}$/", $password)) {
			//Password format incorrect
			$return_json_arr['password']['result'] = 'false';
			$return_json_arr['password']['details'] = 'Your password must be 8 - 20 characters, contains at least 1 number, 1 upper and lower case letter. You may use -, _, or space only. ';
			
			return false;
			
		}
		
		$return_json_arr['password']['result'] = 'true';
		$return_json_arr['password']['details'] = '';
		
		return true;
		
	}
	
	function checkDob($year, $month, $day) {
		global $return_json_arr;
		
		if(empty($year) || empty($month) || empty($day)) {
			$return_json_arr['dob']['result'] = 'false';
			$return_json_arr['dob']['details'] = 'You must provide your date of birth.';
			
			return false;
		}
		
		//Check date format
		if(!checkdate ($month, $day, $year)) {
			$return_json_arr['dob']['result'] = 'false';
			$return_json_arr['dob']['details'] = $year . '-' . $month . '-' . $day . ' is an invalid date.';
			
			return false;
		}
		
		//Check valid date
		$dob = new Datetime(($year + 3) . "-01-01 00:00:00");
		$now = new Datetime();
		if($dob >= $now) {
			$return_json_arr['dob']['result'] = 'false';
			$return_json_arr['dob']['details'] = 'You must be aged 3 years old or above.';
			
			return false;
		}
		
		$return_json_arr['dob']['result'] = 'true';
		$return_json_arr['dob']['details'] = '';
		
		return true;
		
	}
	
	function register($username, $firstname, $lastname, $email, $gender, $password, $year, $month, $day) {
		global $dbservername, $dbusername, $dbpassword, $dbname, $return_json_arr;
		$return_json_arr = array();
		$isValueValid = true;
		
		$return_json_arr['flag'] = 'REGISTER';
		$return_json_arr['result'] = 'PASS';
		
		$isValueValid = checkUserName($username) && $isValueValid;
		$isValueValid = checkFirstName($firstname) && $isValueValid;
		$isValueValid = checkLastName($lastname) && $isValueValid;
		$isValueValid = checkEmail($email) && $isValueValid;
		$isValueValid = checkGender($gender) && $isValueValid;
		//$isValueValid = checkPhone($phone) && $isValueValid;
		$isValueValid = checkPassword($password) && $isValueValid;
		$isValueValid = checkDob($year, $month, $day) && $isValueValid;
		
		if ($isValueValid == true) {
			try{
				//Create player account records into Database
				// Create connection
				$conn = new mysqli($dbservername, $dbusername, $dbpassword, $dbname);
				
				// Check connection
				if ($conn->connect_error) {
					$return_json_arr['result'] = 'DB_CONNECTION_FAIL';
					exceptionLog("Connection failed. " . $conn->connect_error);
					$conn->close();
					return false;
					
				}
				
				$sql = 	"INSERT INTO User (userName, passwordHash) VALUES ('" . $username . "', SHA2('" . $password . "', 256)); " . 
						"INSERT INTO Player (idUser, playerName, firstName, lastName, email, gender, DOB) VALUES (" . 
						"(SELECT idUser FROM User_View WHERE userName = '" . $username . "'), '" . 
						$username . "', '" . $firstname . "', '" . $lastname . "', '" . $email . "', '" . $gender . "', '" . $year . "-" . $month . "-" . $day . "')";
				
				//echo "<p>" . $sql . "</p>";
				/*
				if ($conn->query($sql) !== TRUE) {
					$return_json_arr['result'] = 'DB_INSERT_FAIL';
					exceptionLog("Error: " . $sql . ". " . $conn->error);
					$conn->close();
					return false;
					
				}
				*/
				
				if ($conn->multi_query($sql) !== TRUE) {
					$return_json_arr['result'] = 'DB_INSERT_FAIL';
					exceptionLog("Error: " . $sql . ". " . $conn->error);
					$conn->close();
					return false;
					
				}

				$conn->close();
				
			} catch (Exception $e) {
				$return_json_arr['result'] = 'DB_INSERT_EXCEPTION';
				exceptionLog(	"Register exception. Using username '" . $username . 
								"', first name '" . $firstname . 
								"', last name '" . $lastname . 
								"', email '" . $email . 
								"', phone '" . $phone . 
								"', password '" . $password . 
								"', dob '" . $year . "/" . $month . "/" . $day . 
								"'. "
							);
				$conn->close();
				return false;
				
			}
			
		}else {
			$return_json_arr['result'] = 'FAIL';
			return false;
			
		}
		
		return true;
		
	}
	
	function exceptionLog($msg) {
		$ip = "";
		
		if(!empty($_SERVER['HTTP_CLIENT_IP'])) {
			$ip = $_SERVER['HTTP_CLIENT_IP'];
		} elseif(!empty($_SERVER['HTTP_X_FORWARDED_FOR'])) {
			$ip = $_SERVER['HTTP_X_FORWARDED_FOR'];
		} else {
			$ip = $_SERVER['REMOTE_ADDR'];
		}
		
		$logfile = fopen("./registerlog.txt", "a");
		$txt = date("Y-m-d (h:i:sa)") . " - " . $ip . ": " . $msg . "\n";
		fwrite($logfile, $txt);
		fclose($logfile);
		
	}
	
?>