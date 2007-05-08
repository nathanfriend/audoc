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
 * This file contains the entry class for the tray modules which
 * handles the favourites like Tray mechanism
 *
 * @package    AuDoc.Modules
 * @author	   Jonathan Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   
 * @version    $Id: $
 */
class Trays implements iModule{
	
	// --- ATTRIBUTES ---
	
    /**
     * A reference back to the MicroCore
     *
     * @access private
     * @var MicroCore
     */
    private $microCore = null;
	
	
	private $methodReg = array(
								'addTray' => MicroCore::PERM_LOGIN,
								'modTray' => MicroCore::PERM_LOGIN,
								'delTray' => MicroCore::PERM_LOGIN,
								'getTrays' => MicroCore::PERM_LOGIN,
								'addItem' => MicroCore::PERM_LOGIN,
								'getItems' => MicroCore::PERM_LOGIN,
								'remItem' => MicroCore::PERM_LOGIN
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
	
	public function execute($method, $params){
		$ret = null;
		switch($method){
			case 'addTray':
				if(count($params) == 2){
					$ret = $this->addTray($params[0], $params[1]);
				}else{
					throw new Exception("The method Trays.addTray requires 2 parameters");
				}
				break;
			case 'modTray':
				if(count($params) == 3){
					$ret = $this->modTray($params[0], $params[1], $params[2]);
				}else{
					throw new Exception("The method Trays.modTray requires 3 parameters");
				}
				break;
			case 'delTray':
				if(count($params) == 1){
					$ret = $this->delTray($params[0]);
				}else{
					throw new Exception("The method Trays.delTray requires 1 parameter");
				}
				break;
			case 'getTrays':
				$ret = $this->getTrays();
				break;
			case 'addItem':
				if(count($params) == 2){
					$ret = $this->addItem($params[0], $params[1]);
				}else{
					throw new Exception("The method Trays.addItem requires 2 parameters");
				}
				break;
			case 'getItems':
				if(count($params) == 1){
					$ret = $this->getItems($params[0]);
				}else{
					throw new Exception("The method Trays.getItems requires 1 parameter");
				}
				break;
			case 'remItem':
				if(count($params) == 2){
					$ret = $this->remItem($params[0], $params[1]);
				}else{
					throw new Exception("The method Trays.remItem requires 2 parameters");
				}
				break;
			default:
				throw new Exception("The module Trays does not support the method: " . $method);
		}
		return $ret;
	}
	
	/**
	 * adds a new Tray in the users name
	 */
	 private function addTray($name, $description){
		 $con = $this->microCore->getConnection();
		 //get the user
		 $owner = $con->find('from User where UserName=?',$_SESSION['Username']);
		 if(count($owner) < 1){
			 throw new Exception("Invalid user specified");
		 }
		 $owner = $owner[0];
		 $tray = $con->create("Tray");
		 $tray->Name = $name;
		 $tray->Description = $description;
		 $tray->Owner = $owner;
		 $con->commit($tray);
		 
		 //log it
		 $this->microCore->callModuleFunc("logging", "addItem", array("New Tray ($name) added",Logging::VERBOSITY_NORMAL));
		 return $tray;
	 }
	 
	 /**
	  * Modifies a Tray
	  */
	 private function modTray($uuid, $name, $description){
		 $con = $this->microCore->getConnection();
		 $tray = $con->find('from Tray where uuid=?',$uuid);
		 if(count($tray) < 1){
			 throw new Exception("The tray specified does not exist");
		 }
		 $tray = $tray[0];
		 if ($tray->Owner->Username != $_SESSION['Username']){
			 throw new Exception("You are not the owner of the tray specified");
		 }
		 
		 $tray->Name = $name;
		 $tray->Description = $description;
		 $con->commit($tray);
		 $this->microCore->callModuleFunc("logging", "addItem", array("Tray ($name) modified",Logging::VERBOSITY_NORMAL));
		 return $tray;
	 }
	 
	 /**
	  * Deletes the specified tray
	  */
	 private function delTray($uuid){
		 $con = $this->microCore->getConnection();
		 $tray = $con->find('from Tray where uuid=?',$uuid);
		 if(count($tray) < 1){
			 throw new Exception("The tray specified does not exist");
		 }
		 $tray = $tray[0];
		 if ($tray->Owner->UserName != $_SESSION['Username']){
			 throw new Exception("You are not the owner of the tray specified");
		 }
		 $this->microCore->callModuleFunc("logging", "addItem", array("Tray ($tray->Name) deleted",Logging::VERBOSITY_NORMAL));
		 return $con->delete($tray);
		 
	 }
	 
	 /**
	  * Returns an array of all Trays belonging to current user
	  */
	 private function getTrays(){
		 $con = $this->microCore->getConnection();
		 $trays= $con->find('from Tray where Owner.UserName=?', $_SESSION['Username']);
		 $this->microCore->callModuleFunc("logging", "addItem", array("Trays retrieved",Logging::VERBOSITY_HIGH));
		 return $trays;
	 }
	 
	 /**
	  * Adds the specified record to the specified tray
	  */
	 private function addItem($tray, $record){
		 $con = $this->microCore->getConnection();
		 
		 //get Tray
		 $tray = $con->find('from Tray where uuid=?',$tray);
		 if(count($tray) < 1){
			 throw new Exception("The tray specified does not exist");
		 }
		 $tray = $tray[0];
		 if ($tray->Owner->UserName != $_SESSION['Username']){
			 throw new Exception("You are not the owner of the tray specified");
		 }
		 
		 //get Record
		 $record = $con->find('from Record where uuid=?', $record);
		 if(count($record) < 1){
			 throw new Exception("The record specified does not exist");
		 }
		 $record = $record[0];
		 $uuid = $record->uuid;
		 $recs = $tray->Records;
		 $present = false;
		 foreach($recs as $r){
		 	if($r->uuid == $uuid){
		 		$present = true;
		 	}
		 }
		 if (!$present){
			 $recs[] = $record;
			 $tray->Records = $recs;
			 $con->commit($tray);
			 $this->microCore->callModuleFunc("logging", "addItem", array("Record $record added to tray $tray->Name",Logging::VERBOSITY_HIGH));
		 }
		 return $tray;
	 }
	 
	 /**
	  * Returns an array of records currently in the tray
	  * specified
	  */
	 private function getItems($tray){
		$con = $this->microCore->getConnection();
		 
		 //get Tray
		 $tray = $con->find('from Tray where uuid=?',$tray);
		 if(count($tray) < 1){
			 throw new Exception("The tray specified does not exist");
		 }
		 $tray = $tray[0];
		 if ($tray->Owner->UserName != $_SESSION['Username']){
			 throw new Exception("You are not the owner of the tray specified");
		 }
		 $this->microCore->callModuleFunc("logging", "addItem", array("contents of tray $tray->Name retrieved",Logging::VERBOSITY_HIGH));
		 if (count($tray->Records) <= 0){
			 return array();
		 }else{
			return $tray->Records; 
		 }
	 }
	 
	 /**
	  * Removes a Record from the specified tray
	  */
	 private function remItem($tray, $item){
		 $con = $this->microCore->getConnection();
		 
		 //get Tray
		 $tray = $con->find('from Tray where uuid=?',$tray);
		 if(count($tray) < 1){
			 throw new Exception("The tray specified does not exist");
		 }
		 $tray = $tray[0];
		 if ($tray->Owner->UserName != $_SESSION['Username']){
			 throw new Exception("You are not the owner of the tray specified");
		 }
		 for($i=0; $i<count($tray->Records); $i++){
			$rec = $tray->Records[$i];
			if($rec->uuid == $item){
				unset($tray->Records[$i]);
			} 
		 }
		 $con->commit($tray);
		 $this->microCore->callModuleFunc("logging", "addItem", array("Record $item removed from the tray $tray->Name",Logging::VERBOSITY_HIGH));
		 return $tray;
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