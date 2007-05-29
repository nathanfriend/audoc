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
 * This file contains a search criteria parser and generate valid ezpdo
 * search strings (ezoql) for the AuDoc system
 *
 * @package    AuDoc.Modules
 * @author	   Jonathan Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */
class Parser {
 	
 	private $mc;
 	private $query = "FROM Record WHERE ";
 	private $crits = array();
 	private $hasUDF = false;
 	
 	
 	public function __construct(MicroCore $microcore){
 		$this->mc = $microcore;
 	}
 	
 	public function parse($criteria){
 		$this->query = "FROM Record WHERE ";
		$this->crits = array();
		$this->hasUDF = false;
 		foreach($criteria as $line){
 			$line = trim($line);
 			$split = explode(" : ", $line, 2);
 			$items = explode(" ", $split[0]); 
 			
 			$op = $this->getOperator($items);
 			$isUDF = $this->getIsUDF($items);
 			$field = $this->getField($items);
 			$type = null;
 			$type = $this->getType($field, $isUDF);
 			$values = explode(" - ", $split[1]);
 			$value1 = $values[0];
 			$value2 = null;
 			if(count($values) > 1){
 				$value2 = $values[1];
 			}	
 			$this->crits[] = new Criteria($field, $op, $isUDF, $type, $value1, $value2);			
 		}
 		foreach($this->crits as $c){
 			$this->query .= $c->getSearchString() . " ";
 		}
 		return $this->query;
 	}
 	
 	private function getOperator($items){
 		if((trim($items[0]) == "and") || (trim($items[0]) == "or")){
 			return trim($items[0]);
 		}else{
 			return "";
 		}
 	}
 	
 	private function getIsUDF($items){
 		$ret = false;
 		foreach($items as $item){
 			if (strpos($item, "(udf)") !== false){
 				$ret = true;
 				$this->hasUDF = true;
 			}
 		}
 		return $ret;
 	}
 	
 	private function getUDFType($fieldName){
 		$con = $this->mc->getConnection();
 		$def = $con->find("From UDFDefinition Where Name=?",$fieldName);
 		if(count($def) > 0){
 			$def = $def[0];
 			return $def->Type;
 		}else{
 			throw new Exception("User field $fieldName does not exist");
 		}
 	}
 	
 	private function getType($fieldName, $isUDF){
 		if($isUDF){
 			return $this->getUDFType($fieldName);
 		}else{
 			switch($fieldName){
 				case "RecordNumber":
 					return UDF::TYPE_STRING;
 					break;
 				case "Title":
 					return UDF::TYPE_STRING;
 					break;
 				case "DateCreated":
 					return UDF::TYPE_DATE;
 					break;
 				case "DateRegistered":
 					return UDF::TYPE_DATE;
 					break;
 				case "LastModified":
 					return UDF::TYPE_DATE;
 					break;
 				case "DateReview":
 					return UDF::TYPE_DATE;
 					break;
 				case "Owner":
 					return UDF::TYPE_STRING;
 					break;
 				case "Author":
 					return UDF::TYPE_STRING;
 					break;
 				case "Notes":
 					return UDF::TYPE_STRING;
 					break;
 				case "CheckedOutDate":
 					return UDF::TYPE_DATE;
 					break;
 				default:
 					return UDF::TYPE_STRING;
 					break;		
 			}
 		}
 	}
 	
 	private function getField($items){
 		$field = "";
 		foreach($items as $item){
 			if((trim($item) == "and") || (trim($item) == "or")){
 				
 			}else{
 				if (strpos($item, "(udf)") !== false){
 					$pos = strpos($item, "(udf)");
 					$field .= substr($item, $pos+5) . " "; 
 				}else{
 					$field .= $item . " ";
 				}
 			}
 		}
 		return trim($field);
 	}
 }
?>
