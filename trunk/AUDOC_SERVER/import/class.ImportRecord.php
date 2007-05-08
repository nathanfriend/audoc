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
 * This file contains the Record import class
 *
 * @package    AuDoc.Modules
 * @author	   Jonathan Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   
 * @version    $Id: $
 */
class ImportRecord{

	private $connection;
	private $header;
	
	private $rows = 0;
	private $records = 0;
	private $errors = 0;
	
	private $seclevels = array();
	private $recTypes = array();
	private $caveats = array();
	private $classes = array();
	private $keywords = array();
	
	/** 
	 * Public contructor. Takes an ezpdo manager connection
	 */
	public function __construct($connection){
		$this->connection = $connection;
	}
	
	/**
	 * importCSV take a file path for import
	 * and then parses the lines one row/record at a time
	 */
	 public function importCSV($path){
		if(!file_exists($path)){
			die("File not found: $path/n");
		}
		$file = fopen($path, "r");
		$this->header = fgetcsv($file);
		$data = null;
		while(($data = fgetcsv($file)) !== false){
			$this->rows++;
			$cRecord = array();
			for($i=0;$i<count($data);$i++){
				$cRecord[$this->header[$i]] = $data[$i];
			}
			$metadata = $this->processRow($cRecord);
			if($metadata !== false){
				//create record
				$record = $this->createRecord($metadata);
				if ($record->commit()){
					$this->records++;
				}else{
					$this->errors++;
				}
				//increment records imported
			}
		}
		
		echo "Rows Processed    : " . $this->rows . "\n";
		echo "Records Imported  : " . $this->records . "\n";
		echo "Errors Encountered: " . $this->errors . "\n";
	 }
	
	private function processRecord($record){
		$hasErrors = false;
		$recType = null;
		$metadata = array();
		$metadata["UDFs"] = array();
		foreach($record as $field=>$value){
			switch($field){
				case "Title":
				case "Classification":
				case "Date Created":
				case "Date Registered":
				case "Last Modified":
				case "Review Date":
					//date
					$d = $this->isDate($value);
					if($d !== false){
						$metadata[$field] = $d;
					}else{
						$this->errors++;
						$hasErrors = true;
					}
				break;
				case "Record Number":
					$rn = $this->isRecordNumber($value);
					if($rn !== false){
						$metadata[$field] = $rn;
					}else{
						$this->errors++;
						$hasErrors = true;
					}
				break;
				case "Owner":
				case "Author":
				case "Notes":
					//string
					//don't need to do anything?
				break;
				case "Record Type":
					$rt = $this->isRecordType($value);
					if($rt !== false){
						$metadata[$field] = $rt;
						$recType = $rt;
					}else{
						$this->errors++;
						$hasErrors = true;
					}
				break;
				case "SecLevel":
					$sl = $this->isSecLevel($value);
					if($sl !== false){
						$metadata[$field] = $sl;
					}else{
						$this->errors++;
						$hasErrors = true;
					}
				break;
				case "Caveats":
					//multivalent!
					$cavs = explode(":",$value);
					for($j=0;$j<count($cavs);$j++){
						$cav = $this->isCaveat($cavs[$j]);
						if($cav !== false){
							$cavs[$j] = $cav;
						}else{
							$this->errors++;
							$hasErrors = true;
						}
					}
					$metadata[$field] = $cavs;
				break;
				default:
					//is a udf!
					if($recType != null){
						$u = $this->isUDF($recType, $field, $value);
						if($u !== false){
							$metadata["UDFs"][] = $u;
						}else{
							$this->errors++;
							$hasErrors = true;
						}
					}else{
						$this->errors++;
						$hasErrors = true;
					}
				break;
			}
		}
		if($hasErrors){
			return false;
		}else{
			return $record;
		}
	}
	

	/**
	 * Returns a date object if the date string passed is valid
	 * if not valid then false is returned
	 **/
	private function isDate($date){
		$pattern = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$";
		if(preg_match($date)){
			return strtotime($date);
		}else{
			return false;
		}
	}
	
	/**
	 * Returns a RecordType object if the string is a valid
	 * Record Type or false if it is invalid
	 **/
	private function isRecType($rectype){
		//check if it is in the cache first
		if(isset($this->recTypes[$rectype])){
			return $this->recTypes[$rectype];
		}else{
			$rTypes = $connection->find("FROM RecordType WHERE Name ==?", $rectype);
			if(count($rTypes) > 0){
				//add it to the cache
				$this->recTypes[$rectype] = $rTypes[0];
				return $rTypes[0];
			}else{
				return false;
			}
		}
	}

	/**
	 * Returns a SecLevel object if the string is a valid
	 * Security Level or false if it is invalid
	 **/	
	private function isSecLevel($seclevel){
		//check if it is in the cache first
		if(isset($this->secLevels[$seclevel])){
			return $this->secLevels[$seclevel];
		}else{
			$sLevels = $connection->find("FROM SecLevel WHERE Name ==?", $seclevel);
			if(count($sLevels) > 0){
				//add it to the cache
				$this->secLevels[$seclevel] = $sLevels[0];
				return $sLevels[0];
			}else{
				return false;
			}
		}
	}
	
	/**
	 * Returns a Caveat object if the string is a valid
	 * Caveat or false if it is invalid
	 **/	
	private function isCaveat($caveat){
		//check if it is in the cache first
		if(isset($this->caveats[$caveat])){
			return $this->caveats[$caveat];
		}else{
			$cavs = $connection->find("FROM Caveat WHERE Name ==?", $caveat);
			if(count($cavs) > 0){
				//add it to the cache
				$this->cavs[$caveat] = $cavs[0];
				return $cavs[0];
			}else{
				return false;
			}
		}
	}
	
	/**
	 * Returns a Classification object if the string is a valid
	 * Classification or false if it is invalid.
	 * TODO: add support for more than one class called the same thing (based on unique path)
	 */
	private function isClass($class){
		//check if it is in the cache first
		if(isset($this->classes[$class])){
			return $this->classes[$class];
		}else{
			$res = $connection->find("FROM Classification WHERE Name ==?", $class);
			if(count($res) > 0){
				//add it to the cache
				$this->classes[$class] = $res[0];
				return $res[0];
			}else{
				return false;
			}
		}
	}

	/**
	 * Returns a Classification object if the string is a valid
	 * Classification Path or false if it is invalid.
	 */	
	private function isClassPath($classpath){
		//check if it is in the cache first
		if(isset($this->classes[$classpath])){
			return $this->classes[$classpath];
		}else{
			//extract this class
			$sections = explode("-", $classpath);
			//strip any spaces
			for($i=0;$i<count($sections);$i++){
				$sections[$i] = trim($sections[$i]);
			}
			$class = $sections[count($sections)-1];
			$res = $connection->find("FROM Classification WHERE Name ==?", $class);
			if(count($res > 0)){
				foreach($res as $c){
					if($c->getClassification() == $classpath){
						//add it to the cache
						$this->classes[$classpath] = $c;
					}
				}
				//did we find it?
				if(isset($this->classes[$classpath])){
					//yes!!
					return $this->classes[$classpath];
				}else{
					//no :(
					return false;
				}
			}else{
				return false;
			}
		}
	}
	
	private function isUDF($recType, $field, $value){
		$udf = null;
		$udfdef = null;
		foreach($recType->UDFs as $cudf){
			if($cudf->Name == $field){
				$udfdef = $cudf;
			}
		}
		if($udfdef != null){
			$udf = $this->connection->create("UDF");
			$udf->Name = $field; 
			switch($udfdef->Type){
	  			case UDF::TYPE_INTEGER:
	  				$udf->Type = UDF::TYPE_INTEGER;
					$udf->ValueInt = intval($value);
				break;
				case UDF::TYPE_DECIMAL:
					$udf->ValueDec = floatval($value);
					break;
				case UDF::TYPE_DATE:
					$udf->Type = UDF::TYPE_DATE;
					$udf->ValueDate = strtotime($value);
					break;
				case UDF::TYPE_STRING:
					$udf->Type = UDF::TYPE_STRING;
					$udf->ValueString = $value;
					break;
				case UDF::TYPE_KEYWORD:
					$udf->Type = UDF::TYPE_KEYWORD;
					$keyword = $this->isKeyword($udfdef->KeywordHierarchy, $value);
					if($keyword !== false){
						$udf->ValueKeyword = $keyword;
					}else{
						$udf = false;
					}
					break;
			}
			return $udf;
		}else{
			return false;
		}
	}
	
	private function isKeyword($kwh, $keyword){
		$key = $kwh->uuid . $keyword;
		if(isset($this->keywords[$key])){
			return $this->keywords[$key];
		}else{
			$kw = $this->connection->find("FROM KeywordData WHERE Value = ? AND Hierarchy.uuid = ?", $keyword, $kwh->uuid);
			if(count($kw) > 0){
				$this->keywords[$key] = $kw[0];
				return $kw[0];
			}else{
				return false;
			}
		}
	}
	
	private function isRecordNumber($recnum){
		$recs = $this->connection->find("FROM Record WHERE RecordNumber = ?", $recnum);
		if(count($recs) == 0){
			return $recnum;
		}else{
			return false;
		}
	}
	
	private function createRecord($metadata){
		$record = $this->connection->create("Record");
		$record->RecordNumber = $metadata["Record Number"];
		$record->Title = $metadata["Title"];
		$record->Classification = $metadata["Classification"];
		$record->DateCreated = $metadata["Date Created"];
		$record->DateRegistered = $metadata["Date Registered"];
		$record->LastModified = $metadata["Last Modified"];
		$record->DateReview = $metadata["Review Date"];
		$record->Owner = $metadata["Owner"];
		$record->Author = $metadata["Author"];
		$record->Notes = $metadata["Notes"];
		$record->RecordType = $metadata["Record Type"];
		$record->UDFs = $metadata["UDFs"];
		$record->SecLevel = $metadata["SecLevel"];
		$record->Caveats = $metadata["Caveats"];
		return $record;
	}

}
?>
