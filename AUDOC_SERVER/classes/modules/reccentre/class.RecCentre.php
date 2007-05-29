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
 * This is the entry class to the logging modules which handles the logging
 * of system events to the database
 * @package    AuDoc.Modules
 * @author     Jonathan Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */
class RecCentre implements iModule{
	
	// --- ATTRIBUTES ---
	
    /**
     * A reference back to the MicroCore
     *
     * @access private
     * @var MicroCore
     */
    private $microCore = null;
	
	private $methodReg = array(
								'checkOut' => MicroCore::PERM_LOGIN,
								'checkIn' => MicroCore::PERM_LOGIN
							   );
    
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
	
	public function execute($method, $params){
		$ret = null;
		switch($method){
			case 'checkOut':
				if (count($params) == 2){
					$ret = $this->checkOut($params[0], $params[1]);	
				}else{
					throw new Exception("The method RecCentre.checkOut requires 2 parameters");
				}
				break;
			case 'checkIn':
				if (count($params) == 1){
					$ret = $this->checkIn($params[0]);
				}else{
					throw new Exception("the method RecCentre.CheckIn requires 2 parameters");
				}
				break;
			default:
				throw new Exception("The module Logging does not support the method: " . $method);
		}
		return $ret;
	}
	
	/**
	 * Checks out a record to the specified user.
	 * Admins can checkout records to anyone.
	 * Regular users can only check out records to themselves
	 */
	private function checkout($recNum, $username){
		$con = $this->microCore->getConnection();
		$rec = $con->find("FROM Record WHERE RecordNumber=?", $recNum);
		if(count($rec) > 0){
			$rec = $rec[0];
		}else{
			throw new Exception("$recNum does not exist");
		}
		if(($_SESSION['isAdmin']) || ($_SESSION['Username'] == "$username")){
			$user = $con->find("FROM User WHERE UserName=?", $username);
			if(count($user) > 0){
				$user = $user[0];
			}else{
				throw new Exception("$username does not exist");
			}
			//set checked out to
			$rec->CheckedOutTo = $user;
			$rec->CheckedOutDate = strtotime(date('Y-m-d'));
			//commit change
			$con->commit($rec);
			
			//logging
			$params = array();
			$params[] = "Record {$rec->uuid} checked out to $username";
			$params[] = Logging::VERBOSITY_NORMAL;
			$this->microCore->callModuleFunc("Logging", "addItem", $params);
			return $rec;	
		}else{
			throw new Exception("You cannot checkout records to other users");
		}
	}
	
	private function checkin($recNum){
		$con = $this->microCore->getConnection();
		$rec = $con->find("FROM Record WHERE RecordNumber=?", $recNum);
		if(count($rec) > 0){
			$rec = $rec[0];
			$rec->CheckedOutTo = null;
			$rec->CheckedOutDate = null;
			$con->commit($rec);
			
			//logging
			$params = array();
			$params[] = "Record {$rec->uuid} checked in";
			$params[] = Logging::VERBOSITY_NORMAL;
			$this->microCore->callModuleFunc("Logging", "addItem", $params);
			
			return $rec;
		}else{
			throw new Exception("$recNum does not exist");
		}
	}
	
	/**
	 * returns true only if the method supplied is 
	 * permitted
	 */ 
	public function isPermitted($method, $state){
		$ret = false;
		if (isset($this->methodReg[$method])){
			if ($state >= $this->methodReg[$method]){
				$ret = true;
			}
		}
		return $ret;
	}
}
 
 ?>