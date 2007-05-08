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
 * This file contains the entry class for the recman modules which
 * handles record operations
 *
 * @package    AuDoc.Modules
 * @author	   Jonathan Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */
class Recman implements iModule{
	
	// --- ATTRIBUTES ---
	
    /**
     * A reference back to the MicroCore
     *
     * @access private
     * @var MicroCore
     */
    private $microCore = null;
	
	private $methodReg = array(
								'addUDF' => MicroCore::PERM_BOTH,
								'modUDF' => MicroCore::PERM_BOTH,
								'delUDF' => MicroCore::PERM_BOTH,
								'getUDF' => MicroCore::PERM_LOGIN,
								'getUDFs' => MicroCore::PERM_LOGIN,
								'addClass' => MicroCore::PERM_BOTH,
								'getClass' => MicroCore::PERM_LOGIN,
								'modClass' => MicroCore::PERM_BOTH,
								'delClass' => MicroCore::PERM_BOTH,
								'getChildren' => MicroCore::PERM_LOGIN,
								'getTopLevel' => MicroCore::PERM_LOGIN,								
								'getKWH' => MicroCore::PERM_LOGIN,
								'getKWHs' => MicroCore::PERM_LOGIN,
								'delKWH' => MicroCore::PERM_BOTH,
								'addKWH' => MicroCore::PERM_BOTH,
								'getKWTopLevel' => MicroCore::PERM_LOGIN,
								'getKWChildren' => MicroCore::PERM_LOGIN,
								'addKWData' => MicroCore::PERM_BOTH,
								'getKWData' => MicroCore::PERM_LOGIN,
								'modKWData' => MicroCore::PERM_BOTH,
								'delKWData' => MicroCore::PERM_BOTH,
								'getRecType' => MicroCore::PERM_LOGIN,
								'getRecTypes' => MicroCore::PERM_LOGIN,
								'modRecType' => MicroCore::PERM_BOTH,
								'delRecType' => MicroCore::PERM_BOTH,
								'addRecType' => MicroCore::PERM_BOTH,
								'getRecord' => MicroCore::PERM_LOGIN,
								'addRecord' => MicroCore::PERM_LOGIN,
								'modRecord' => MicroCore::PERM_LOGIN,
								'delRecord' => MicroCore::PERM_BOTH,
								'checkout' => MicroCore::PERM_LOGIN,
								'checkin' => MicroCore::PERM_LOGIN,
								'getCheckedOut' => MicroCore::PERM_LOGIN,
								'modRecordNumber' => MicroCore::PERM_BOTH
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
	 * marshalls function requests. This is large class
	 * so all function requests for a given dataObject
	 * are sent to a second indirect function to process
	 * 
	 * @param String $method The name of the method to call
	 * @param array $params An array of parameters
	 */
	public function execute($method, $params){
		$ret = null;
		//$method = strtolower($method);
		switch($method){
			//udf functions
			case 'addUDF':
			case 'getUDF':
			case 'getUDFs':
			case 'modUDF':
			case 'delUDF':
				$ret = $this->doUDF($method, $params);
				break;
			//classification
			case 'addClass':
			case 'getClass':
			case 'getChildren':
			case 'modClass':
			case 'delClass':	
			case 'getTopLevel':
				$ret = $this->doClass($method,$params);
				break;
			case 'getKWH':
			case 'getKWHs':
			case 'delKWH':
			case 'addKWH':
			case 'getKWTopLevel':
			case 'getKWChildren':
			case 'addKWData':
			case 'getKWData':
			case 'modKWData':
			case 'delKWData':
				$ret = $this->doKWH($method, $params);
				break;
			case 'getRecType':
			case 'getRecTypes':
			case 'modRecType':
			case 'delRecType':
			case 'addRecType':
				$ret = $this->doRecType($method, $params);
				break;
			case 'getRecord':
			case 'modRecord':
			case 'addRecord':
			case 'delRecord':
			case 'checkout':
			case 'checkin':
			case 'getCheckedOut':
			case 'modRecordNumber':
				$ret = $this->doRec($method, $params);
				break;
			default:
				throw new Exception("The module Recman does not support the method: " . $method);	
		}
		return $ret;
	}
	
	/**
	 * Handles UDF function calls
	 */
	private function doUDF($method, $params){
		$ret = null;
		switch($method){
			case 'addUDF':
				if (count($params) >= 2){
					$c = new UDFMan($this->microCore);
					if (count($params) == 3){
						//kwh is set
						$ret = $c->addUDF($params[0],$params[1], $params[2]);
					}else{
						//not a kwh
						$ret = $c->addUDF($params[0],$params[1]);
					}
				}else{
					throw new Exception("The method RecMan.addUDF requires at least two parameters");
				}
				break;
			case 'modUDF':
				if (count($params) == 2){
					$c = new UDFMan($this->microCore);
					$ret = $c->modUDF($params[0],$params[1]);
				}else{
					throw new Exception("The method Recman.modUDF requires two parameters");
				}
				break;				
			case 'delUDF':
				if (count($params) == 1){
					$c = new UDFMan($this->microCore);
					$ret = $c->delUDF($params[0]);
				}else{
					throw new Exception("The method Recman.delUDF requires one parameter");
				}
				break;
			case 'getUDF':
				if (count($params) == 1){
					$c = new UDFMan($this->microCore);
					$ret = $c->getUDF($params[0]);
				}else{
					throw new Exception("The method Recman.getUDF requires one parameter");
				}
				break;
			case 'getUDFs':
				$c = new UDFMan($this->microCore);
				$ret = $c->getUDFs();
				break;		
		}
		return $ret;
	}
	
	/**
	 * Handles classification function calls
	 */
	private function doClass($method, $params){
		$ret = null;
		switch($method){
			case 'addClass':
				if(count($params) == 5){
					$c = new Classman($this->microCore);
					$ret = $c->addClass($params[0],$params[1],$params[2],$params[3],$params[4]);
				}else{
					throw new Exception("invalid number of argument for RecMan.addClass");
				}
				break;
			case 'modClass':
				if(count($params) == 6){
					$c = new Classman($this->microCore);
					$ret = $c->modClass($params[0],$params[1],$params[2],$params[3],$params[4],$params[5]);
				}else{
					throw new Exception("invalid number of argument for RecMan.modClass");
				}
				break;
			case 'getClass':
				if(count($params) == 1){
					$c = new Classman($this->microCore);
					$ret = $c->getClass($params[0]);
				}else{
					throw new Exception("invalid number of argument for RecMan.getClass");
				}
				break;
			case 'delClass':
				if(count($params) == 1){
					$c = new Classman($this->microCore);
					$ret = $c->delClass($params[0]);
				}else{
					throw new Exception("invalid number of argument for RecMan.delClass");
				}
				break;
			case 'getChildren':
				if(count($params) == 1){
					$c = new Classman($this->microCore);
					$ret = $c->getChildren($params[0]);
				}else{
					throw new Exception("invalid number of argument for RecMan.getChildren");
				}
				break;
			case 'getTopLevel':
				$c = new Classman($this->microCore);
				$ret = $c->getTopLevel();
				break;
		}
		return $ret;
	}
	
	private function doKWH($method, $params){
		$ret = null;
		$k = new Keyword($this->microCore);
		switch($method){
			case 'getKWH':
				if(count($params) == 1){
					$ret = $k->getKWH($params[0]);
				}else{
					throw new Exception("The method Recman.getKWD requires one parameter");
				}
				break;
			case 'getKWHs':
				$ret = $k->getKWHs();
				break;
			case 'delKWH':
				if(count($params) == 1){
					$ret = $k->delKWH($params[0]);
				}else{
					throw new Exception("The method Recman.delKWD requires one parameter");
				}
				break;
			case 'addKWH':
				if(count($params) == 1){
					$ret = $k->addKWH($params[0]);
				}else{
					throw new Exception("The method Recman.addKWD requires one parameter");
				}
				break;
			case 'getKWTopLevel':
				if(count($params) == 1){
					$ret = $k->getKWTopLevel($params[0]);
				}else{
					throw new Exception("The method Recman.getTopLevel requires one parameter");
				}
				break;
			case 'getKWChildren':
				if(count($params) == 1){
					$ret = $k->getKWChildren($params[0]);
				}else{
					throw new Exception("The method Recman.getChildren requires one parameter");
				}
				break;
			case 'addKWData':
				if(count($params) == 3){
					$ret = $k->addKWData($params[0], $params[1], $params[2]);
				}else{
					throw new Exception("The method Recman.addKWData requires 3 parameters");
				}
				break;
			case 'getKWData':
				if(count($params) == 1){
					$ret = $k->getKWData($params[0]);
				}else{
					throw new Exception("The method Recman.getKWData requires one parameter");
				}
				break;								
			case 'modKWData':
				if(count($params) == 2){
					$ret = $k->modKWData($params[0], $params[1]);
				}else{
					throw new Exception("The method Recman.modKWData requires 2 parameter");
				}
				break;
			case 'delKWData':
				if(count($params) == 1){
					$ret = $k->delKWData($params[0]);
				}else{
					throw new Exception("The method Recman.delKWData requires one parameter");
				}
				break;
		}
		return $ret;
	}
	
	/**
	 * Handles RecordType method calls
	 */
	 private function doRecType($method, $params){
		 $ret = null;
		 $rt = new RecTypes($this->microCore);
		 switch($method){
			case 'getRecType':
				if(count($params) < 1){
					throw new Exception("Recman.getRecType needs more parameters");
				}
				$ret = $rt->getRecType($params[0]);
				break;
			case 'getRecTypes':
				$ret = $rt->getRecTypes();
				break;
			case 'modRecType':
				if(count($params) < 4){
					throw new Exception("Recman.modRecType needs more parameters");
				}
				$ret = $rt->modRecType($params[0], $params[1], $params[2], $params[3]);
				break;
			case 'delRecType':
				if(count($params) < 1){
					throw new Exception("Recman.delRecType needs more parameters");
				}
				$ret = $rt->delRecType($params[0]);
				break;
			case 'addRecType':
				if(count($params) < 3){
					throw new Exception("Recman.addRecType needs more parameters");
				}
				$ret = $rt->addRecType($params[0], $params[1], $params[2]);
				break;
		 }
		 return $ret;
	 }
	 
	/**
	 * 
	 */
	 private function doRec($method, $params){
		 $ret = null;
		 $record = new RecordMan($this->microCore);
		 switch($method){
			case 'getRecord':
				if (count($params) < 1){
					throw new Exception("The getRecord method requires one parameter");
				}else{
					$ret = $record->getRecord($params[0]);
				}
				break;
			case 'modRecord':
				//TODO: Complete modRecord method
				if (count($params) < 9){
					throw new Exception("The modRecord method requires X parameter");
				}else{
					$ret = $record->modRecord($params[0],$params[1],$params[2],$params[3],$params[4],$params[5],$params[6],$params[7],$params[8]);
				}
				break;
			case 'addRecord':
				if (count($params) < 9){
					throw new Exception("The addRecord method requires nine parameter");
				}else{
					$ret = $record->addRecord($params[0],$params[1],$params[2],$params[3],$params[4],$params[5],$params[6],$params[7],$params[8]);
				}
				break;
			case 'delRecord':
				if (count($params) < 1){
					throw new Exception("The delRecord method requires one parameter");
				}else{
					$ret = $record->delRecord($params[0]);
				}
				break;
			case 'checkout':
				if (count($params) < 1){
					throw new Exception("The checkout method requires one parameter");
				}else{
					$ret = $record->checkout($params[0]);
				}
				break;
			case 'checkin':
				if (count($params) < 1){
					throw new Exception("The checkin method requires one parameter");
				}else{
					$ret = $record->checkin($params[0]);
				}
				break;
			case 'getCheckedOut':
				$ret = $record->getCheckedOut();
				break;
			case 'modRecordNumber':
				if (count($params) < 2){
					throw new Exception("The modRecNumber method requires two parameters");
				}else{
					$ret = $record->modRecordNumber($params[0],$params[1]);
				}
				break;
		 }
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