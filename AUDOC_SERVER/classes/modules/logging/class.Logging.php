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
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */
class Logging implements iModule{
	
	// --- ATTRIBUTES ---
	
	//Logging verbosity constants
	const VERBOSITY_NONE = 0;
	const VERBOSITY_NORMAL = 2;
	const VERBOSITY_HIGH = 4;
	const VERBOSITY_DEBUG = 8;
	
    /**
     * A reference back to the MicroCore
     *
     * @access private
     * @var MicroCore
     */
    private $microCore = null;
	
	/**
	 * Current verbosity level
	 * 
	 * @access private
	 * @var Verbosity
	 */
	 private $Verbosity = Logging::VERBOSITY_NORMAL;
	
	private $methodReg = array(
								'addItem' => MicroCore::PERM_NONE,
								'getItems' => MicroCore::PERM_LOGIN
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
		try{
			$this->Verbosity = $this->microCore->getConfigItem("logging.level");
		}catch(Exception $e){}
	}
	
	public function execute($method, $params){
		$ret = null;
		$method = strtolower($method);
		switch($method){
			case 'additem':
				if (count($params) == 2){
					$ret = $this->addItem($params[0], $params[1]);	
				}else{
					throw new Exception("The method logging.addItem requires both a username and a descriptions");
				}
				break;
			case 'getitems':
				if (count($params) == 2){
					$ret = $this->dumpItems($params[0], $params[1]);
				}else{
					throw new Exception("the method logging.dumpItems requires both a start date and an end date");
				}
				break;
			default:
				throw new Exception("The module Logging does not support the method: " . $method);
		}
		return $ret;
	}
	
	/**
     * Adds and item to the log 
     *
     * @access private
     * @param String $user The user who performed the action
     * @param String $description A description of the function performed
     * @return LogItem the Log item created or null is ignored due to verbosity level
     */
	private function addItem($description, $level){
		if ($this->Verbosity >= $level){
			$user = "<unknown>";
			if(isset($_SESSION['Username'])){
				$user = $_SESSION['Username'];
			}
			$con = $this->microCore->getConnection();
			$lItem = $con->create("LogItem");
			$lItem->User = $user;
			$lItem->Description = $description;
			$lItem->DateTime = time();
			$con->commit($lItem);
			return $lItem;
		}else{
			return null;
		}
	}
	
	/**
	 * Returns an array of LogItem object between the dates specified.
	 * 
	 * @access private
	 * @param Integer $start The start of the date time range
	 * @param Integer $end The end of the date time range
	 */
	 private function dumpItems($start, $end){
		 $ret = null;
		 $con = $this->microCore->getConnection();
		 $ret = $con->find("FROM LogItem WHERE DateTime >= ? AND DateTime <= ?", $start, $end);
		 return $ret;
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