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
 * This file contains a class for dealing with records
 *
 * @package    AuDoc.Modules
 * @author	   Jonathan Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */
class RecordMan {
	
	private $microcore;
	
	public function __construct($mc){
		$this->microcore = $mc;
	}
	
	/**
	 * Returns the specified record object
	 */
	public function getRecord($uuid){
		$con = $this->microcore->getConnection();
		$rec = $con->find("from Record where uuid==?", $uuid);
		if(count($rec) < 1){
			throw new Exception("Record $uuid does not exist");
		}
		return $rec[0];
	}
	
	/**
	 * Creates a new records
	 */
	public function addRecord($title, $classID, $datec, $owner, $author, $notes, $rTypeID, $recNum, $UDFs){
		if($recNum == ""){
			throw new Exception("You must specify a record number");
		}
	
		$con = $this->microcore->getConnection();
		//check that the record number isn't already in use
		$recs = $con->find("from Record where RecordNumber =?", $recNum);
		if(count($recs) > 0){
			throw new Exception("The Record Number $recNum is already in use");
		}
		
		//start extracting the objects
		$classification = $this->microcore->callModuleFunc("RecMan","getClass",array($classID));
		$secLevel = $classification->SecLevel;
		$caveats = $classification->Caveats;
		$recType = $this->microcore->callModuleFunc("RecMan","getRecType",array($rTypeID));
		
		//sorting out UDF complexity!!
		$newUDFs = array();
		//loop through and create all UDFs
		foreach($recType->UDFs as $u){
			$udf = $con->create('UDF');;
			//Create the correct type of UDF
			switch($u->Type){
				case UDF::TYPE_INTEGER:
					$udf->Type = UDF::TYPE_INTEGER;
					if(isset($UDFs->{$u->Name})){
						$udf->ValueInt = intval($UDFs->{$u->Name});
					}
					break;
				case UDF::TYPE_DECIMAL:
					$udf->Type = UDF::TYPE_DECIMAL;
					if(isset($UDFs->{$u->Name})){
						$udf->ValueDec = floatval($UDFs->{$u->Name});
					}
					break;
				case UDF::TYPE_DATE:
					$udf->Type = UDF::TYPE_DATE;
					if(isset($UDFs->{$u->Name})){
						$udf->ValueDate = strtotime($UDFs->{$u->Name});
					}
					break;
				case UDF::TYPE_STRING:
					$udf->Type = UDF::TYPE_STRING;
					if(isset($UDFs->{$u->Name})){
						$udf->ValueString = $UDFs->{$u->Name};
					}
					break;
				case UDF::TYPE_KEYWORD:
					$udf->Type = UDF::TYPE_KEYWORD;
					if(isset($UDFs->{$u->Name})){
						$kwd = $con->find("from KeywordData WHERE uuid=?", $UDFs->{$u->Name});
							if(count($kwd) != 1){
								throw new Exception("Invalid Keyword chosen");
							}else{
								$udf->ValueKeyword = $kwd[0];
							}
					}
					break;
			}
			//populate udf data
			$udf->Name = $u->Name;
			

			//add to array of new udfs
			$newUDFs[] = $udf;
			
		}
		
		$retention = "+" . $classification->Retention . " years"; 
		$now = strtotime(date('Y-m-d'));
		
		//create and populate the record
		$record = $con->create("Record");
		$record->Title = $title;
		$record->Classification = $classification;
		$record->DateCreated = strtotime($datec);
		$record->DateRegistered = $now;
		$record->LastModified = $now;
		$record->DateReview = strtotime($retention, $record->LastModified);
		$record->Owner = $owner;
		$record->Author = $author;
		$record->Notes = $notes;
		$record->RecordType = $recType;
		$record->SecLevel = $secLevel;
		$record->Caveats = $caveats;
		$record->RecordNumber = $recNum;
		$record->LatestRev = 0;
		$record->UDFs = $newUDFs;
		$con->commit($record);
		
		
		$u = $record->uuid;
		$r = $con->find("from Record where uuid =?", $u);
		$r = $r[0];
		return $r;
	}
	
	/**
	 * Modifies the specifed existing record
	 */
	public function modRecord($uuid, $title, $classID, $datec, $owner, $author, $notes, $recNum, $UDFs){
		$con = $this->microcore->getConnection();
		
		//check that the record number isn't already in use
		$recs = $con->find("from Record where RecordNumber=? AND uuid !=?", $recNum, $uuid);
		if(count($recs) != 0){
			throw new Exception("The Record Number $recNum is already in use");
		}
		//get record
		$record = $con->find("from Record where uuid=?", $uuid);
		if (count($record) < 1){
			throw new Exception("Record specified does not exist");
		}
		$record = $record[0];
		
		//start extracting the objects
		$classification = $this->microcore->callModuleFunc("RecMan","getClass",array($classID));
		$recType = $record->RecordType;
		
		//sorting out UDF complexity!!
		foreach($UDFs as $name=>$value){
			$found = false;
			foreach($record->UDFs as $currentUDF){
				if($currentUDF->Name == $name){
					$found = true;
					//set value;
					switch($currentUDF->Type){
						case UDF::TYPE_INTEGER:
							$currentUDF->ValueInt = intval($value);
							break;
						case UDF::TYPE_DECIMAL:
							$currentUDF->ValueDec = floatval($value);
							break;
						case UDF::TYPE_DATE:
							$currentUDF->ValueDate = strtotime($value);
							break;
						case UDF::TYPE_STRING:
							$currentUDF->ValueString = $value;
							break;
						case UDF::TYPE_KEYWORD:
							if($value != ""){
								$kwd = $con->find("from KeywordData WHERE uuid=?", $value);
								if(count($kwd) != 1){
									throw new Exception("Invalid Keyword chosen");
								}else{
									$currentUDF->ValueKeyword = $kwd[0];
								}
							}
							break;
					}
				}
			}
			//the UDF didn't already exist!!
			if(!$found){
				$record->UDFs[] = $this->createUDF($record, $name, $value);
			}
		}
		$retention = "+" . $classification->Retention . " years";
		$now = strtotime(date('Y-m-d'));
		
		$record->Title = $title;
		$record->Classification = $classification;
		$record->DateCreated = strtotime($datec);
		$record->LastModified = $now;
		$record->DateReview = strtotime($retention, $record->LastModified);
		$record->Owner = $owner;
		$record->Author = $author;
		$record->Notes = $notes;
		$record->RecordNumber = $recNum;
		$con->commit($record);
		return $record;
	}
	
	/**
	 * Deletes the specified record
	 * (complete delete, as in never existed)
	 */
	public function delRecord($uuid){
		$con = $this->microcore->getConnection();
		$record = $con->find('from Record where uuid=?', $uuid);
		if(count($record) < 1){
			throw new Exception("Specified record does not exist");
		}
		$record = $record[0];
		//Delete documents
		foreach($record->Documents as $doc){
			$params = array();
			$params[] = $doc->uuid;
			$this->microcore->callModuleFunc("docstore", "delDoc", $params);
		}
		$this->addLogItem("Record $uuid deleted");
		return $con->delete($record);
	}
	
	/**
	 * Checkout an electronic document
	 */
	public function checkout($uuid){
		$con = $this->microcore->getConnection();
		$record = $con->find('from Record where uuid=?', $uuid);
		if(count($record) < 1){
			throw new Exception("Specified record does not exist");
		}
		$record = $record[0];
		$user = $this->microcore->callModulefunc("users", "getme",array());
		$record->CheckedOutTo = $user;
		$con->commit($record);
		$username = $_SESSION['Username'];
		$this->addLogItem("Record $uuid checked out to $username");
		return $record;
	}
	
	/**
	 * mark the records as Checked in
	 */
	 public function checkin($uuid){
		$con = $this->microcore->getConnection();
		$record = $con->find('from Record where uuid=?', $uuid);
		if(count($record) < 1){
			throw new Exception("Specified record does not exist");
		}
		$record = $record[0];
		$record->CheckedOutTo = null;
		$con->commit($record);
		$this->addLogItem("Record $uuid checked in");
		return $record; 
	 }
	 
	/**
	 * Adds an item to the log
	 */
	 private function addLogItem($description){
		 $params = array();
		 $params[] = $description;
		 $params[] = Logging::VERBOSITY_NORMAL;
		 $this->microcore->callModuleFunc("Logging", "addItem", $params);
	 }
	 
	 /**
	  * Returns an array of records checked out to the current user
	  */
	  public function getCheckedOut(){
		  $username = $_SESSION['Username'];
		  $con = $this->microcore->getConnection();
		  $recs = $con->find("from Record where CheckedOutTo.UserName == ?", $username);
		  $this->microcore->callModuleFunc("Logging", "addItem", array("Retreived records checkout out to $username",Logging::VERBOSITY_HIGH));
		  return $recs;
	  }
	  
	  /**
	   * Modifies the record number of an existing record
	   */
	  public function modRecordNumber($uuid, $recNum){
	  	$con = $this->microcore->getConnection();
	  	$check = $con->find("from Record where RecordNumber=? AND uuid !=?", $recNum, $uuid);
	  	if(count($check) != 0){
	  		throw new Exception("The record number $recNum is already in use");
	  	}
	  	$recs = $con->find("from Record WHERE uuid=?", $uuid);
	  	if(count($recs) != 0){
	  		$rec = $recs[0];
	  		$rec->RecordNumber = $recNum;
	  		$con->commit($rec);
	  		return $rec;
	  	}else{
	  		throw new Exception("Specified record does not exist");
	  	}
	  }
	  
	  	private function createUDF($record, $name,$value){
	  		$con = $this->microcore->getConnection();
	  		$type = UDF::TYPE_STRING;
	  		foreach($record->RecordType->UDFs as $udfDef){
	  			if($udfDef->Name == $name){
	  				$type = $udfDef->Type;
	  			}
	  		}
	  		$udf = $con->create("UDF");
	  		$udf->Name = $name;
	  		switch($type){
	  			case UDF::TYPE_INTEGER:
					$udf->Type = UDF::TYPE_INTEGER;
					$udf->ValueInt = intval($value);
				break;
				case UDF::TYPE_DECIMAL:
					$udf->Type = UDF::TYPE_DECIMAL;
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
					$kwd = $con->find("from KeywordData WHERE uuid=?", $UDFs->{$u->Name});
					if(count($kwd) != 1){
						throw new Exception("Invalid Keyword chosen");
					}else{
						$udf->ValueKeyword = $kwd[0];
					}
					break;
			}
			return $udf;
	  	}
}
?>
