<?php
/**
 * Test the audoc.users.Users class
**/
//include_once("classes/dataObjects/class.Caveat.php");
//require_once("class.SmartLoader.php");

class UsersTest extends UnitTestCase{

	private $mc;

	public function __construct(){
		$this->_label = "User Test";
		$this->mc = new MicroCore("config/configuration.conf");
		$this->con = $this->mc->getConnection();
		

		
		//register the Users Module
		$umod = $this->con->create('Module');
		$umod->Name = "users";
		$umod->Version = 1.0;
		$umod->Description = "users module";
		$umod->EntryClass = "Users";
		$this->con->commit($umod);
		
		//set session for user
		$_SESSION['Username'] = "jonm";
		$_SESSION['isAdmin'] = true;
	}

	function testExecuteAddUser (){
		$params = array();
		$params[0] = "Admin"; //forename
		$params[1] = "User"; //surname
		$params[2] = "admin"; //username
		$params[3] = "PASSWORD"; //password
		$params[4] = true; //isAdmin
		$params[5] = "Classified"; //secLEvel
		
		$cave = array();
		$cave [0] = "Finance";
		$params[6] = $cave; //caveats
		
		$user = $this->mc->callModuleFunc("Users", "addUser", $params);
		$this->id = $user->uuid;
		$this->assertNotNull($user, "addUser passed");
	}
	
	function testExecuteGetUser (){
		$params = array();
		$params[0] = $this->id;
		$user = $this->mc->callModuleFunc("Users", "getUser", $params);
		$this->assertNotNull($user, "getUser passed");
	}
	
	function testExecuteModUser (){
		$params = array();
		$params[0] = $this->id;
		$params[1] = "Admin"; //forename
		$params[2] = "User"; //surname
		$params[3] = "admin"; //username
		$params[4] = true; //isAdmin
		$params[5] = "Classified"; //secLevel
		
		$cave = array();
		$cave [] = "Finance";
		$params[6] = $cave; //caveats
		
		$user = $this->mc->callModuleFunc("Users", "modUser", $params);
		$this->username = $user->UserName;
		$this->password = $user->Password;
		$this->assertEqual($this->id, $user->uuid, "Users.modUser");
	}
	
	function testExecuteAuthUser (){
		$params = array();
		$params[0] = $this->username;
		$params[1] = $this->password;
		$user = $this->mc->callModuleFunc("Users", "authUser", $params);
		$this->assertNotNull($user, "modUser passed");
	}	

	function testExecuteLogOut (){
		$params = array();
		$user = $this->mc->callModuleFunc("Users", "logOut", $params);
		$this->assertTrue($user, "LogOut Test");
	}
	
	function testExecuteSetPassword(){
		$params = array();
		$params[0] = $this->id;
		$params[1] = "admin";
		$user = $this->mc->callModuleFunc("Users", "setPassword", $params);
		$this->assertEqual($user->Password, $params[1]);
	}
}
?>