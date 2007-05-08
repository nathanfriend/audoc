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
 * This file contains the entry class for the security module which
 * handles the AuDoc security model
 *
 * @package    AuDoc.Modules
 * @author	   Jonathan Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   
 * @version    $Id: $
 */

class Security implements iModule{
	
	// --- ATTRIBUTES ---

    /**
     * A reference back to the MicroCore
     *
     * @access private
     * @var MicroCore
     */
    private $microCore = null;
	
	private $methodReg = array(
								'addseclevel' => MicroCore::PERM_BOTH,
								'modseclevel' => MicroCore::PERM_BOTH,
								'getseclevels' => MicroCore::PERM_LOGIN,
								'delseclevel' => MicroCore::PERM_BOTH,
								'addcaveat' => MicroCore::PERM_BOTH,
								'modcaveat' => MicroCore::PERM_BOTH,
								'getcaveats' => MicroCore::PERM_LOGIN,
								'delcaveat' => MicroCore::PERM_BOTH
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
		$method = strtolower($method);
		switch($method){
			case 'addseclevel':
				if (count($params) == 2){
					$ret = $this->addSecLevel($params[0], $params[1]);
				}else{
					throw new Exception("The method Security.addSecLevel requires more parameters");
				}
				break;
				
			case 'modseclevel':	
				if (count($params) == 3){
					$ret = $this->modSecLevel($params[0], $params[1], $params[2]);
				}else{
					throw new Exception("The method Security.modSecLevel requires 3 parameters");
				}	
				break;
				
			case 'getseclevels':
				$ret = $this->getSecLevels();
				break;	
			
			case 'delseclevel':
				if (count($params) == 1){
					$ret = $this->delSecLevel($params[0]);
				}else{
					throw new Exception("The method Security.delSecLevel requires 1 parameter");
				}
				break;
				
			case 'addcaveat':
				if (count($params) == 1){
					$ret = $this->addCaveat($params[0]);
				}else{
					throw new Exception("The method Security.addCaveat requires more parameters");
				}
				break;
			
			case 'modcaveat':
				if (count($params) == 2){
					$ret = $this->modCaveat($params[0], $params[1]);
				}else{
					throw new Exception("The method Security.modCaveat requires 2 parameters");
				}
				break;
				
			case 'getcaveats':
				$ret = $this->getCaveats();
				break;
			
			case 'delcaveat':
				if (count($params) == 1){
					$ret = $this->delCaveat($params[0]);
				}else{
					throw new Exception("The method Security.delCaveat requires 1 parameter");
				}
				break;
			default:
				throw new Exception("The module Security does not support the method: " . $method);
		}
		return $ret;
	}
	
	/**
	 * Creates a new Security Level
	 */
	 private function addSecLevel($name, $level){
		 $ret = null;
		 //$level = intval($level);
		 if (($level > 0) && ($level <= 99)){
			 $con = $this->microCore->getConnection();
			 $sLevels = $con->find("from SecLevel where Name=? or Level=?",$name,$level);
			 if (count($sLevels) > 0){
				 throw new Exception("Security Level specified is not unique");
			 }
			 $sl = $con->create("SecLevel");
			 $sl->Name = $name;
			 $sl->Level = $level;
			 $con->commit($sl);
			 $ret = $sl;
		 }else{
			 //level out of permissable range
			 throw new Exception("The security level specified (" .$level. ") is outside the permissible range (0-99)");
		 }
		 $this->microCore->callModuleFunc("logging", "addItem", array("New security level ($name) added",Logging::VERBOSITY_NORMAL));
		 return $ret;
	 }
	 
	/**
	 * Modifies the specified SecLevel
	 */
	 private function modSecLevel($uuid, $name, $level){
		 $con = $this->microCore->getConnection();
		 $seclevel = $con->find("from SecLevel where uuid=?", $uuid);
		 if(count($seclevel) < 1){
			 throw new Exception("Specified Security level does not exist");
		 }
		 if (($level > 0) && ($level <= 99)){
			throw new Exception("The security level specified (" .$level. ") is outside the permissible range (0-99)");
		 }
		 $seclevel = $seclevel[0];
		 $sLevels = $con->find("from SecLevel where Name=? or Level=? and uuid != ?",$name,$level, $seclevel->uuid);
		 if(count($sLevels) != 0){
			 throw new Exception("Security Level specified is not unique");
		 }
		 $seclevel->Name = $name;
		 $seclevel->Level = $level;
		 
		 $this->microCore->callModuleFunc("logging", "addItem", array("Security Level $seclevel->Name modified",Logging::VERBOSITY_NORMAL));
		 $con->commit($seclevel);
		 return $seclevel;
	 }
	
	/**
	 * Returns all existing Security Levels
	 */
	private function getSecLevels(){
		$ret = null;
		$con = $this->microCore->getConnection();
		//$ret = $con->get('SecLevel');
	    $ret = $con->find("from SecLevel order by Level");
		$this->microCore->callModuleFunc("logging", "addItem", array("Security levels retrieved",Logging::VERBOSITY_HIGH));
		return $ret;
	}
	
	/**
	 * Deletes the specified Security Level
	 */
	 private function delSecLevel($uuid){
		 $con = $this->microCore->getConnection();
		 $s = $con->find("from SecLevel where uuid=?",$uuid);
		 if(count($s) < 1){
			 throw new Exception("SecLevel specified does not exist");
		 }
		 $s = $s[0];
		 $this->microCore->callModuleFunc("logging", "addItem", array("Security Level $s->Name deleted",Logging::VERBOSITY_NORMAL));
		 return $con->delete($s);
	 }
	
	/**
	 * Adds a new caveats
	 */
	private function addCaveat($name){
		$ret = null;
		$con = $this->microCore->getConnection();
		$cavs = $con->find("from Caveat where Name=?", $name);
		if (count($cavs) <= 0){
			$cav = $con->create("Caveat");
			$cav->Name = $name;
			$con->commit($cav);
			$ret = $cav;
		}else{
			throw new Exception("The caveat ". $name ." already exists");
		}
		$this->microCore->callModuleFunc("logging", "addItem", array("Caveat ($name) added",Logging::VERBOSITY_NORMAL));
		return $ret;
	}
	
	/**
	 * Modifies the specified caveat
	 */
	 private function modCaveat($uuid, $name){
		$con = $this->microCore->getConnection();
		$cav = $con->find("from Caveat where uuid=?", $uuid);
		if(count($cav) < 1){
			throw new Exception("Caveat specified does not exist");
		}
		$cav = $cav[0];
		
		$cavs = $con->find("from Caveat where Name=? and uuid !=?", $name, $uuid);
		if(count($cavs) > 0){
			throw new Exception("The caveat name $name is not unique");
		}
		$oldName = $cav->Name;
		$cav->Name = $name;
		$con->commit($cav);
		$this->microCore->callModuleFunc("logging", "addItem", array("Caveat $oldName modified to $name",Logging::VERBOSITY_NORMAL));
		return $cav;
	 }

	/**
	 * Returns all existing Caveats
	 */
	private function getCaveats(){
		$ret = null;
		$con = $this->microCore->getConnection();
		//$ret = $con->getAll('Caveat');
		$ret = $con->find("from Caveat order by Name");
		if($ret == false){
			$ret = null;
		}
		$this->microCore->callModuleFunc("logging", "addItem", array("All Caveats retrieved",Logging::VERBOSITY_HIGH));
		return $ret;
	}
	
	/**
	 * Deletes the specified Caveat
	 */
	 private function delCaveat($uuid){
		$con = $this->microCore->getConnection();
		$cav = $con->find("from Caveat where uuid=?", $uuid);
		if(count($cav) < 1){
			throw new Exception("Caveat specified does not exist");
		}
		$cav = $cav[0];
		$this->microCore->callModuleFunc("logging", "addItem", array("Caveat ($cav->Name) deleted",Logging::VERBOSITY_HIGH));
		return $con->delete($cav);
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
}
 
 ?>