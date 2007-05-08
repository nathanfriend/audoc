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
 * This class encapsulates a JSON response returned to
 * a client
 * @package    AuDoc.Modules
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */
class JSON_Handler_Response {
	
	// --- ATTRIBUTES ---

    /**
     * The messages id
     *
     * @access public
     * @var int
     */
    public $id;

	 /**
     * The messages response
     *
     * @access public
     * @var mixed
     */
    public $result;
	
	    /**
     * The messages id
     *
     * @access public
     * @var mixed
     */
    public $error;
	
    // --- OPERATIONS ---
    
	/**
     * Constructor
     *
     * @access public
     * @param int $id The message identifier sent from the client
     * @param string $method The method requested by client
     * @param array $params An array of parameters sent by the client
     */
	function __construct($id, $result, $error){
		$this->id = $id;
		$this->result = $result;
		$this->error = $error;
	}
	
	/**
     * Constructor
     *
     * @access public
     */	
	function __contruct(){
	}
	
	/**
     * Sets the id of the response
     *
     * @access public
     * @param int $id The message identifier sent from the client
     */
	public function setID($id){
		$this->id = $id;
	}
	
	/**
     * Sets the result of the method called.
     * Also sets the error to null
     *
     * @access public
     * @param mixed $response The response to be sent
     */
	public function setResult($response){
		$this->result = $response;
		$this->error = null;
	}
	
	/**
     * Sets the error message of the response.
     * Also sets the result to null
     *
     * @access public
     * @param mixed $error The error message to be sent
     */
	public function setError($error){
		$this->error = $error;
		$this->response = null;
	}
}
 
 ?>