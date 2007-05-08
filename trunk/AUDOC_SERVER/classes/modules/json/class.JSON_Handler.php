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
 * This is the entry class for the JSON module which handles
 * communication between the client and the server
 * @package    AuDoc.Modules
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */
 class JSON_Handler implements iModule{
	
	// --- ATTRIBUTES ---

    /**
     * A reference back to the MicroCore
     *
     * @access private
     * @var MicroCore
     */
    private $microCore = null;

	/**
     * The JSON encode/decode library object
     *
     * @access private
     * @var Services_JSON
     */
    private $json = null;
	
	private $methodReg = array(
								'processRequest' => MicroCore::PERM_NONE
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
		$this->json = new Services_JSON();
	}
	
	public function execute($method, $params){
		$ret = null;
		//$method = strtolower($method);
		switch($method){
			case 'processRequest':
				$request = $this->json->decode($params[0]);
				$ret = $this->procRequest($request);
				//todo: handle arrays and null's
				if ($ret != null){
					$ret = $this->json->encode($ret);
				}
				break;
			default:
				throw new Exception("The module JSON_Handler does not support the method: " . $method);
		}
		return $ret;
	}
	
	/**
     * Processes requests
     *
     * @access private
     * @param JSON_Handler_Request $raw_msg The message to be handled
     * @return JSON_Handler_Response The response from the 
     * 		   method called or null if the request was a 
     * 		   notification
     */
	private function procRequest($request){
		$ret = null;
		
		//Extract basic info
		$id = $request->id;
		$params = $request->params;
		
		//extract module and method names
		$method = explode(".", $request->method, 2);
		
		//get module from MicroCore and execute method specified in request
		try{
			$result = $this->microCore->callModuleFunc($method[0], $method[1], $params);
			//convert objects to arrays to avoid json encoding issues
			if ( (is_array($result)) || (get_class($result) == "epArray")){
				$res = array();
				for($i=0; $i<count($result); $i++){
					$res[] = $result[$i]->toArray(); 
				}
				$result = $res;
			}else{
				//check it isn't a boolean value
				if (is_object($result)){
					$result = $result->toArray();
				}
			}
			$ret = new JSON_Handler_Response($id, $result, null);
		}catch(Exception $e){
			//if an Exception is thrown generate an error response
			$ret = new JSON_Handler_Response($id, null, $e->getMessage());
		}
		if ($id != null){
			return $ret;
		}else{
			return null;
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