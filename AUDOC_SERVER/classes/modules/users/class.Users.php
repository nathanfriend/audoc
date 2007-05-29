<?php
/* +----------------------------------------------------------------------+
 * | AuDoc 2                                                              |
 * +----------------------------------------------------------------------+
 * | Copyright (c) 2004-2007 Audata Ltd                                   |
 * +----------------------------------------------------------------------+
 * | This source file is subject to version 2 of the Gnu Public License,  |
 * | that is bundled with this package in the file License.txt, and is    |
 * | available at through the world-wide-web at                           |
 * | http://www.gnu.org/licenses/gpl.txt.                                 |
 * | If you did not receive a copy of the GPL license and are unable to   |
 * | obtain it through the world-wide-web, please send a note to          |
 * | support@audata.co.uk so we can mail you a copy immediately.          |
 * +----------------------------------------------------------------------+
 * | Authors: Jonathan Moss <jon.moss@audata.co.uk>                       |
 * +----------------------------------------------------------------------+
 *
 * $Id: $
 */
 
 /**
 * This file contains the entry class for the Users modules which
 * manages users and user authentication
 *
 * @package    AuDoc.Modules
 * @author	   Jonathan Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */

class Users implements iModule{
	
	// --- ATTRIBUTES ---

    /**
     * A reference back to the MicroCore
     *
     * @access private
     * @var MicroCore
     */
    private $microCore = null;
	
	
	private $methodReg = array(
								'logout' => MicroCore::PERM_NONE,
								'adduser' => MicroCore::PERM_BOTH,
								'getuser' => MicroCore::PERM_LOGIN,
								'getuserbyid' => MicroCore::PERM_LOGIN,
								'authuser' => MicroCore::PERM_NONE,
								'moduser' => MicroCore::PERM_BOTH,
								'getme' => MicroCore::PERM_LOGIN,
								'getusers' => MicroCore::PERM_BOTH,
								'setpassword' => MicroCore::PERM_BOTH,
								'remuser' => MicroCore::PERM_BOTH
							   );
	
    // --- OPERATIONS ---
    
	/**
     * Constructor
     *
     * @access public
     */
	public function __construct(){}
	
	/**
	 * provides access to the MicroCore object
	 * 
	 * @param MicroCore $mc A reference to the active MicroCore object
	 */
	public function setParent(MicroCore $mc){
		$this->microCore = $mc;
	}
	
	/**
	 * addUser
	 * getUser
	 * modUser
	 * authUser
	 * logOut
	 * getMe
	 */
	public function execute($method, $params){
		$ret = null;
		$method = strtolower($method);
		switch($method){
			case 'adduser':
				if(count($params) == 7){
					$ret = $this->addUser($params);
				}else{
					throw new Exception("The method Users.addUser requires more parameters");
				}
				break;
			case 'getuser':
				if(count($params) == 1){
					$ret = $this->getUser(trim($params[0]));
				}else{
					throw new Exception("The method Users.getUser requires an id");
				}
				break;
			case 'getuserbyusername':
				if(count($params) == 1){
					$ret = $this->getUserByUsername(trim($params[0]));
				}else{
					throw new Exception("The method Users.getUserByUsername requires an username");
				}
				break;	
			case 'moduser':
				if(count($params) == 7){
					$ret = $this->modUser($params);
				}else{
					throw new Exception("The method Users.modUser requires more parameters");
				}
				break;
			
			case 'authuser':
				if(count($params) == 2){
					$ret = $this->authUser(trim($params[0]),trim($params[1]));
				}else{
					throw new Exception("The method Users.authUser requires more parameters");
				}
				break;
				
			case 'logout':
				$ret = $this->logOut();
				break;
			case 'getme':
				$ret = $this->getMe();
				break;
			case 'getusers':
				$ret = $this->getUsers();
				break;
			case 'setpassword':
				if(count($params) == 2){
					$ret = $this->setPassword($params[0], $params[1]);
				}else{
					throw new Exception('The method Users.setPassword requires two parameters');
				}
				break;
			case 'remuser':
				if(count($params) == 1){
					$ret = $this->remUser($params[0]);
				}else{
					throw new Exception('The method Users.remUser must have the UUID of the users specified');
				}
				break;
			default:
				throw new Exception("The module User does not support the method: " . $method);
		}
		return $ret;
	}
	
	/**
	   * Add a message to the log
	   */
	   private function addLogItem($msg){
		   $params = array($msg, Logging::VERBOSITY_NORMAL);
		   $this->microCore->callModuleFunc("logging", "addItem", $params);
	   }
	
	/**
	 * returns true only if the method supplied is 
	 * permitted
	 */ 
	public function isPermitted($method, $state){
		$ret = false;
		$method = strtolower($method);
		if (isset($this->methodReg[$method])){
			if ($state >= $this->methodReg[$method]){
				$ret = true;
			}
		}
		return $ret;
	}
	 
	 //function handlers for external methods   
	   
	/**
     * Adds and User to the system
     *
     * @access private
     */
	private function addUser($params){
		$con = $this->microCore->getConnection();
		//Check they don't already exist
		$existing = $con->find("from User where UserName = ?", $params[2]);
		if(count($existing) != 0){
			throw new Exception("User ".$params[2]." already exists");
		}
		
		//Get specified security level object
		$secObj = null;		
		if(isset($params[5])){
			$secObj = $con->find("from SecLevel where Name=?", $params[5]);
			if(count($secObj) == 0){
				throw new Exception("Invalid security level specified");
			}else{
				$secObj = $secObj[0];
			}
		}

		//Get relevent caveats if needed
		$cavs = array();
		if (isset($params[6])){
			foreach($params[6] as $cav){
				$c = $con->find("from Caveat where Name = ?" , $cav);
				if (count($c) != 0){
					$cavs[] = $c[0];
				}else{
					throw new Exception("The caveat '$cav' could not be found");
				}
			}
		}
		
		//Create user object
		$user = $con->create('User');
		$user->Forename = $params[0];
		$user->Surname = $params[1];
		$user->UserName = $params[2];
		$user->Password = $params[3];
		$user->isAdmin = $params[4];
		$user->SecLevel = $secObj;
		$user->Caveats = $cavs;
		$con->commit($user);
		
		$this->microCore->callModuleFunc("logging", "addItem", array("user $params[2] added",Logging::VERBOSITY_NORMAL));
		return $user;
	}
	
	/**
	 * Returns a user object
	 * 
	 * @access private
	 * @param String $id id of the user
	 * @return User the requested user 
	 */
	 private function getUser($id){
		 $this->microCore->callModuleFunc("logging", "addItem", array("user details for user with ID: $id requested",Logging::VERBOSITY_HIGH));
		 $ret = null;
		 $con = $this->microCore->getConnection();
		 $ret = $con->find("from User WHERE uuid=?", $id);
		 if (count($ret)<1){
			 throw new Exception("Requested (".$id.") user was not found");
		 }
		 $ret = $ret[0];
		 return $ret;
	 }
	 
	 /**
	  * Returns a user obbject by username
	  */
	  private function getUserByUsername($username){
		 $this->microCore->callModuleFunc("logging", "addItem", array("user details for user with username: $username requested",Logging::VERBOSITY_HIGH));
		 $ret = null;
		 $con = $this->microCore->getConnection();
		 $ret = $con->find("from User WHERE Username=?", $username);
		 if (count($ret)<1){
			 throw new Exception("Requested user ($username) was not found");
		 }
		 $ret = $ret[0];
		 return $ret;
	  }
	 
	 private function authUser($username, $password){
		
		$ret = null;
		$con = $this->microCore->getConnection();
		$user = $con->find("from User where UserName=? and Password=?", $username, $password);
		if (count($user) == 0){
			$this->microCore->callModuleFunc("logging", "addItem", array("$username failed to login",Logging::VERBOSITY_NORMAL));
			throw new Exception("Could not authorise the user ".$username);
		}else{
			//set session variable so everyone else knows as well
			$user = $user[0];
			$_SESSION['Username'] = $user->UserName;
			$_SESSION['isAdmin'] = $user->isAdmin;
			if (isset($user->SecLevel->Level)){
				$_SESSION['SecLevel'] = $user->SecLevel->Level;
			}else{
				$_SESSION['SecLevel'] = 0;
			}
			if (isset($user->Caveats)){
				$_SESSION['Caveats'] = array();
				foreach($user->Caveats as $caveat){
					$_SESSION['Caveats'][] = $caveat->Name;
				}
			}else{
				$_SESSION['Caveat'] = null;
			}
		}
		
		$this->microCore->callModuleFunc("logging", "addItem", array("$username logged in",Logging::VERBOSITY_NORMAL));
		return $user; 
	 }
	 
	 private function modUser($params){
		$con = $this->microCore->getConnection();
		//Check  already exist
		$user = $con->find("from User where uuid=?", $params[0]);
		if(count($user) < 1){
			throw new Exception("User $id doesn't exists");
		}
		$user = $user[0];
		
		//Get specified security level object
		$secObj = null;		
		if(isset($params[5])){
			$secObj = $con->find("from SecLevel where Name=?", $params[5]);
			if(count($secObj) == 0){
				throw new Exception("Invalid security level specified");
			}else{
				$secObj = $secObj[0];
			}
		}

		//Get relevent caveats if needed
		$cavs = array();
		if (isset($params[6])){
			foreach($params[6] as $cav){
				$c = $con->find("from Caveat where Name=?" , $cav);
				if (count($c) != 0){
					$cavs[] = $c[0];
				}else{
					throw new Exception("The caveat '$cav' could not be found");
				}
			}
		}
		
		//Create user object
		$user->Forename = $params[1];
		$user->Surname = $params[2];
		$user->UserName = $params[3];
		$user->isAdmin = $params[4];
		$user->SecLevel = $secObj;
		//fix for obscure pointer issue
		if(isset($user->Caveats[0]->Name)){
			$dump = $user->Caveats[0]->Name;
		}
		$user->Caveats = $cavs;
		$con->commit($user);
		
		$this->microCore->callModuleFunc("logging", "addItem", array("user " .$params[3]." updated",Logging::VERBOSITY_NORMAL));
		return $user;
	 }
	 
	 /**
	  * Logs a user out by clearing the session
	  */
	  private function logOut(){
		  $msg = "user " . $_SESSION['Username'] . " logged out";
		  $this->microCore->callModuleFunc("logging", "addItem", array($msg, Logging::VERBOSITY_NORMAL));
		  return session_destroy();
	  }	  
	  
	  private function getMe(){
		  if(isset($_SESSION['Username'])){
			  $con = $this->microCore->getConnection();
			  $user = $con->find("from User where UserName = ?", $_SESSION['Username']);
			  if(count($user) > 0){
				  return $user[0];
			  }else{
				  throw new Exception("The user does not seem to exist any longer");
			  }
		  }else{
			  throw new Exception("The user is no logged in");
		  }
	  }
	  
	  private function getUsers(){
		$conn = $this->microCore->getConnection();
		$this->microCore->callModuleFunc("logging", "addItem", array("Got list of Users", Logging::VERBOSITY_NORMAL));
		//return $conn->getAll("User");
		return $conn->find("FROM User Where 1=1 order by Forename");
	  }
	  
	  private function setPassword($uuid, $password){
		$conn = $this->microCore->getConnection();
		$user = $conn->find("from User where uuid = ?", $uuid);
		if(count($user) > 0){
			$user = $user[0];
		}else{
			throw new Exception("The user specified does not exist");
		}
		$user->Password = $password;
		$conn->commit($user);
		$this->microCore->callModuleFunc("logging", "addItem", array("Password changed for ". $user->UserName, Logging::VERBOSITY_NORMAL));
		return $user;
	  }
	  
	  private function remUser($uuid){
		$conn = $this->microCore->getConnection();
		$user = $conn->find("from User where uuid = ?", $uuid);
		if(count($user) > 0){
			$user = $user[0];
		}else{
			throw new Exception("The user specified does not exist");
		}
		if($user->UserName == "admin"){
			throw new Exception("You cannot delete the admin user");
		}else{
			$conn->delete($user);
		}
	  }
}
 
 ?>