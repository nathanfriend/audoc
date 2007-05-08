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
 * This file contains a class the hold data about a single search criteria
 *
 * @package    AuDoc.Modules
 * @author	   Jonathan Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   
 * @version    $Id: $
 */
class Criteria {
 	
 	private $field;
 	private $isUDF;
 	private $value1;
 	private $value2;
 	private $operator;
 	private $type;
 	
 	private static $udfCounter = 0;
  	
 	public function __construct($field, $operator, $isUDF, $type, $value1, $value2){
 		$this->field = $field;
 		$this->operator = $operator;
 		$this->isUDF = $isUDF;
		$this->type = $type;
 		
 		//is this a date time search?
 		if($this->type == UDF::TYPE_DATE){
 			$this->value1 = strtotime($value1);
 		}else{
 			$this->value1 = $value1;
 		}
 		if(($value2 != null) && ($this->type == UDF::TYPE_DATE)){
 			$this->value2 = strtotime($value2);
 		}else{
 			$this->value2 = $value2;
 		} 
 	}
 	
 	public function getSearchString(){
 		$ret = "";
 		$comp = null;
 		if($this->type == UDF::TYPE_STRING){
 			$comp = 'like';
 		}else{
 			$comp = '==';
 		}
 		
 		if($this->type == UDF::TYPE_STRING || $this->type == UDF::TYPE_KEYWORD){
 			$this->value1 = "\"" . $this->value1 . "\"";
 			if($this->value2 != null){
 				$this->value2 = "\"" . $this->value2 . "\"";	
 			}
 		}
 		
 		if($this->isUDF){
 			Criteria::$udfCounter++;
 			$uNum = "u" . Criteria::$udfCounter;
 			$fType = null;
 			switch($this->type){
	 			case UDF::TYPE_INTEGER:
	 				$fType = "ValueInt";
	 				break;
	 			case UDF::TYPE_DECIMAL:
	 				$fType = "ValueDec";
	 				break;
	 			case UDF::TYPE_DATE:
	 				$fType = "ValueDate";
	 				break;
	 			case UDF::TYPE_STRING:
	 				$fType = "ValueString";
	 				break;
	 			case UDF::TYPE_KEYWORD:
	 				$fType = "ValueKeyword.uuid";
	 				break;
 			}
 			if($this->value2 == null){
 				$ret = " $this->operator UDFs.contains($uNum) and ( $uNum.Name = '$this->field' and $uNum.$fType $comp $this->value1) ";
 			}else{
 				$ret = " $this->operator UDFs.contains($uNum) and ( $uNum.Name = '$this->field' and $uNum.$fType >= $this->value1 and $uNum.$fType <= $this->value2)";
 			}
 		}else{
 			if($this->value2 == null){
 				$ret = " $this->operator $this->field $comp $this->value1 ";
 			}else{
 				$ret = " $this->operator ($this->field >= $this->value1 and $this->field <= $this->value2)";
 			}
 		}
 		return trim($ret);
 	}
 	
 }
?>
