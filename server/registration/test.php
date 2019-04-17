<?php
	/*
	$test_name = array();
	$test_name['flag'] = "CHECK";
	$test_name['firstname'] = "Test Player";
	$test_name['lastname'] = "Foundation";
	
	$test_dob = array();
	$test_dob['flag'] = "CHECK";
	$test_dob['year'] = "2010";
	$test_dob['month'] = "3";
	$test_dob['day'] = "31";
	
	
	echo "<h2>Test Username: existing user </h2>";
	
	$test_username['username'] = "testplayer_1";
	$json_test_username = json_encode($test_username, 10);
	$data = array('CHECK' => $json_test_username);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_username . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Username: non-existing user </h2>";
	
	$test_username['username'] = "Hello";
	$json_test_username = json_encode($test_username);
	$data = array('CHECK' => $json_test_username);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_username . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Username: empty username </h2>";
	
	$test_username['username'] = "";
	$json_test_username = json_encode($test_username);
	$data = array('CHECK' => $json_test_username);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_username . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Username: illegal format </h2>";
	
	$test_username['username'] = "OR 1=1";
	$json_test_username = json_encode($test_username);
	$data = array('CHECK' => $json_test_username);
	
	$curl = curl_init("http://127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_username . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Email: existing email </h2>";
	
	$test_email['email'] = "testplayer1@mentalcalc.co.uk";
	$json_test_email = json_encode($test_email, 10);
	$data = array('CHECK' => $json_test_email);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_email . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Email: non-existing email </h2>";
	
	$test_email['email'] = "new@mentalcalc.co.uk";
	$json_test_email = json_encode($test_email, 10);
	$data = array('CHECK' => $json_test_email);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_email . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Email: empty email </h2>";
	
	$test_email['email'] = "";
	$json_test_email = json_encode($test_email, 10);
	$data = array('CHECK' => $json_test_email);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_email . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Email: illegal format </h2>";
	
	$test_email['email'] = "helloworld++";
	$json_test_email = json_encode($test_email, 10);
	$data = array('CHECK' => $json_test_email);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_email . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Names: legal format </h2>";
	
	$test_name['firstname'] = "hello world";
	$test_name['lastname'] = "Surname";
	$json_test_name = json_encode($test_name, 10);
	$data = array('CHECK' => $json_test_name);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_name . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Names: illegal format </h2>";
	
	$test_name['firstname'] = "hello 1++@";
	$test_name['lastname'] = "world++@ lol";
	$json_test_name = json_encode($test_name, 10);
	$data = array('CHECK' => $json_test_name);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_name . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Names: empty </h2>";
	
	$test_name['firstname'] = "";
	$test_name['lastname'] = "";
	$json_test_name = json_encode($test_name, 10);
	$data = array('CHECK' => $json_test_name);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_name . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Date of Birth: valid date (lower than 4 years old) </h2>";
	
	$test_dob['year'] = "2018";
	$test_dob['month'] = "3";
	$test_dob['day'] = "10";
	$json_test_dob = json_encode($test_dob, 10);
	$data = array('CHECK' => $json_test_dob);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_dob . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Date of Birth: empty </h2>";
	
	$test_dob['year'] = "";
	$test_dob['month'] = "";
	$test_dob['day'] = "";
	$json_test_dob = json_encode($test_dob, 10);
	$data = array('CHECK' => $json_test_dob);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_dob . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Gender: empty </h2>";
	
	$test_gender['gender'] = "";
	$json_test_gender = json_encode($test_gender, 10);
	$data = array('CHECK' => $json_test_gender);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_gender . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Gender: M </h2>";
	
	$test_gender['gender'] = "M";
	$json_test_gender = json_encode($test_gender, 10);
	$data = array('CHECK' => $json_test_gender);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_gender . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Gender: F </h2>";
	
	$test_gender['gender'] = "F";
	$json_test_gender = json_encode($test_gender, 10);
	$data = array('CHECK' => $json_test_gender);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_gender . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Gender: Invalid </h2>";
	
	$test_gender['gender'] = "A";
	$json_test_gender = json_encode($test_gender, 10);
	$data = array('CHECK' => $json_test_gender);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_gender . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Password: no between 8 - 20 length </h2>";
	
	$test_gender['password'] = "a";
	$json_test_gender = json_encode($test_gender, 10);
	$data = array('CHECK' => $json_test_gender);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_gender . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Password: length fufill (digits only)  </h2>";
	
	$test_gender['password'] = "12345678";
	$json_test_gender = json_encode($test_gender, 10);
	$data = array('CHECK' => $json_test_gender);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_gender . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Password: length fufill </h2>";
	
	$test_gender['password'] = "123456aA";
	$json_test_gender = json_encode($test_gender, 10);
	$data = array('CHECK' => $json_test_gender);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_gender . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Password: length fufill with illegal character </h2>";
	
	$test_gender['password'] = "123456aA-";
	$json_test_gender = json_encode($test_gender, 10);
	$data = array('CHECK' => $json_test_gender);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_gender . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Register: empty fields </h2>";
	
	$test_register = array();
	$json_test_register = json_encode($test_register, 10);
	$data = array('REGISTER' => $json_test_register);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_register . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Register: invalid data provided </h2>";
	
	$test_register = array();
	$test_register['username'] = "hi";
	$test_register['firstname'] = "1st";
	$test_register['lastname'] = "2nd";
	$test_register['gender'] = "M";
	$test_register['year'] = "2018";
	$test_register['month'] = "1";
	$test_register['day'] = "3";
	$test_register['email'] = "hi@";
	$test_register['password'] = "hi";
	$json_test_register = json_encode($test_register, 10);
	$data = array('REGISTER' => $json_test_register);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_register . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
	echo "<h2>Test Register: duplicated data provided </h2>";
	
	$test_register = array();
	$test_register['username'] = "testplayer_1";
	$test_register['firstname'] = "Registration Testing";
	$test_register['lastname'] = "PHP";
	$test_register['gender'] = "M";
	$test_register['year'] = "2015";
	$test_register['month'] = "1";
	$test_register['day'] = "1";
	$test_register['email'] = "testplayer1@mentalcalc.co.uk";
	$test_register['password'] = "123456aA";
	$json_test_register = json_encode($test_register, 10);
	$data = array('REGISTER' => $json_test_register);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_register . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	*/
	echo "<h2>Test Register: Completed </h2>";
	
	$test_register = array();
	$test_register['username'] = "testplayer_2";
	$test_register['firstname'] = "Registration Testing";
	$test_register['lastname'] = "PHP";
	$test_register['gender'] = "F";
	$test_register['year'] = "2015";
	$test_register['month'] = "1";
	$test_register['day'] = "1";
	$test_register['email'] = "testplayer2@mentalcalc.co.uk";
	$test_register['password'] = "123456aA";
	$json_test_register = json_encode($test_register, 10);
	$data = array('REGISTER' => $json_test_register);
	
	$curl = curl_init("127.0.0.1/MentalCalculation/server/register.php");
	curl_setopt($curl, CURLOPT_HEADER, false);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_POST, true);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	
	$json_response = curl_exec($curl);
	$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	echo $json_test_register . "<br>";
	echo $json_response . "<br><br>";
	
	curl_close($curl);
	
?>