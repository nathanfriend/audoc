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
 * Holds the data for a user defined field.
 * @package    AuDoc.DataObjects
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */
 class UDF extends absAudocBase {
 	    // --- ATTRIBUTES ---

	const TYPE_INTEGER = 0;
	const TYPE_DECIMAL = 1;
	const TYPE_DATE = 2;
	const TYPE_STRING = 3;
	const TYPE_KEYWORD = 4;

    /**
     * @orm integer
     *
     * @access public
     * @var int
     */
    public $Type = 0;

    /**
     * Short description of attribute Name
     * @orm char(128)
     * @access public
     * @var String
     */
    public $Name = null;

    /**
     * @access public
     * @var String
     * @orm integer
     */
    public $ValueInt = null;
 	
 	/**
     * @access public
     * @var float
     * @orm float
     */
    public $ValueDec = null;
    
    /**
     * @access public
     * @var date
     * @orm date
     */
    public $ValueDate = null;
    
    /**
     * @access public
     * @var String
     * @orm char(65000)
     */
    public $ValueString = null;
    
    /**
     * @access public
     * @var String
     * @orm has one KeywordData
     */
    public $ValueKeyword = null;
    
    public function __construct(){
    	parent::__construct();
    }
    
	public function toArray(){
 		$ret = array();
 		$ret['Name'] = $this->Name;
 		$ret['Type'] = $this->Type;
 		$ret['Value'] = "";
 		switch($this->Type){
 			case UDF::TYPE_INTEGER:
 				$ret['Value'] = $this->ValueInt;
 				break;
 			case UDF::TYPE_DECIMAL:
 				$ret['Value'] = $this->ValueDec;
 				break;
 			case UDF::TYPE_DATE:
 				$ret['Value'] = date("Y-m-d", $this->ValueDate);
 				break;
 			case UDF::TYPE_STRING:
 				$ret['Value'] = $this->ValueString;
 				break;
 			case UDF::TYPE_KEYWORD:
 				if(isset($this->ValueKeyword)){
 					$ret['Value'] = $this->ValueKeyword->toArray();
 				}	
 				break;
 		}
 		return $ret;
 	}
 }
?>
