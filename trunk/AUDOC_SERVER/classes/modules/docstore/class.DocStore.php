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
 * This is the entry class for the Docstore module which handles
 * the movement of electronic documents in and out of the document store
 * @package    AuDoc.Modules
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   
 * @version    $Id: $
 */
class DocStore implements iModule{

		// --- ATTRIBUTES ---

    /**
     * A reference back to the MicroCore
     *
     * @access private
     * @var MicroCore
     */
    private $microCore = null;
	
	/**
	 * A connection to the database
	 * @var epManager
	 */
	private $connection = null;
	
	/**
	 * Access rights for exposed methods
	 * @var array
	 */
	private $methodReg = array(
								'storeDoc' => MicroCore::PERM_LOGIN,
								'getDoc' => MicroCore::PERM_LOGIN,
								'getRevisions' => MicroCore::PERM_LOGIN,
								'delDoc' => MicroCore::PERM_BOTH,
								'getLatest' => MicroCore::PERM_LOGIN,
								'delOldrevs' => MicroCore::PERM_BOTH
							   );
	
	/**
	 * document storage location
	 * @var string
	 */
	private $docstoreLoc = "docstore";
	
	/**
	 * Maxmimum number of files in a given folder
	 */
	private $maxfiles = 2000;
	
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
		$this->connection = $this->microCore->getConnection();
		//get location of docstore
		try{
			$this->docstoreLoc = $this->microCore->getConfigItem("docstore.location");
		}catch(Exception $e){}//use the default
		
		//get maximum files per folder
		try{
			$this->maxfiles = $this->microCore->getConfigItem("docstore.maxnumber");
		}catch(Exception $e){}
	}
	
	/**
	 * Provides access to the exposed functions of this module
	 * 
	 */
	public function execute($method, $params){
		$ret = null;
		switch($method){
			case 'storeDoc':
				if (count($params) == 2){
					$ret=$this->storeDoc($params[0], $params[1]);	
				}else{
					throw new Exception("The method DocStore.storeDoc requires 3 parameters");
				}
				break;
			case 'getDoc':
				if(count($params) == 1){
					$ret = $this->getDoc($params[0]);
				}else{
					throw new Exception("The method DocStore.getDoc requires 1 parameter");
				}
				break;
			case 'getRevisions':
				if(count($params) == 1){
					$ret = $this->getRevisions($params[0]);
				}else{
					throw new Exception("The method DocStore.getRevisions requires 1 parameter");
				}
				break;
			case 'delDoc':
				if(count($params) == 1){
					$ret = $this->delDoc($params[0]);
				}else{
					throw new Exception("The method DocStore.delDoc requires 1 parameter");
				}
				break;
			case 'getLatest':
				if(count($params) == 1){
					$ret = $this->getLatest($params[0]);
				}else{
					throw new Exception("The method DocStore.getLatest requires 1 parameter");
				}
				break;
				
			case 'delOldrevs':
				if(count($params) == 1){
					$ret = $this->delOldrevs($params[0]);
				}else{
					throw new Exception("The method DocStore.delOldrevs requires 1 parameter");
				}
				break;
			default:
				throw new Exception("The module DocStore does not support the method: " . $method);
		}
		return $ret;
	}
	
	/**
	 * Stores the specified document in the document store
	 * and added it to a record as a new revision
	 */
	private function storeDoc($recid, $extension){
		
			$record = $this->connection->find("from Record where uuid=?", $recid);
			if (count($record) != 1){
				throw new Exception("Invalid record uuid");
			}
			$record = $record[0];
			
			//extract details
			$name = $record->Title;
			$folder = $this->getLatestFolder();
			
			//get next revision number
			$rev = array();
			foreach($record->Documents as $doc){
				$rev[] = $doc->Revision;
			}
			if (count($rev) >= 1){
				$rev = max($rev)+1;
			}else{
				$rev = 0;
			}
			
			$newDocument = $this->connection->create('Document');
			//add document details
			$uuid = $newDocument->uuid;
			$newDocument->Name = $name;
			$newDocument->Extension=$extension;
			$newDocument->Revision=$rev;
			$newDocument->Folder=$folder;
			//update record
			$record->Documents[] = $newDocument;		
			$record->LatestRev = $rev;
			$this->connection->commit($record);
			
			//put file in folder
			$dest = $this->docstoreLoc."/$folder/$uuid.$extension";
			
			if (!move_uploaded_file($_FILES["document"]["tmp_name"],$dest)){
				if(isset($php_errormsg)){
					throw new Exception("unable to save file $php_errormsg");
				}else{
					throw new Exception("unable to save file");
				}
			}
			$this->microCore->callModuleFunc("logging", "addItem", array("new document stored ($record->uuid)",Logging::VERBOSITY_NORMAL));
			return $newDocument;
	}
	
	/**
	 * Returns the specified document object
	 */
	private function getDoc($docid){
			$ret=null;
			$document = $this->connection->find("from Document where uuid=?", $docid);
			if (count($document) != 1){
				throw new Exception("Invalid document docid");
			}
			$this->microCore->callModuleFunc("logging", "addItem", array("document $docid retrieved",Logging::VERBOSITY_NORMAL));
			return $document[0];
	}
	
	/**
	 * Returns an array of of document objects associated
	 * with specified record
	 */
	private function getRevisions($recid){ 
		$ret=null;
		$record = $this->connection->find("from Record where uuid=?", $recid);
		$ret['Document'] = array();
		if (count($record < 1)){
			throw new Exception("Invalid record uuid");
		}
		$record = $record[0];
		if (count($record->Documents) > 0){
			$ret = $record->Documents;
		}
		$this->microCore->callModuleFunc("logging", "addItem", array("revisions associated with record $uuid retrieved",Logging::VERBOSITY_NORMAL));
		return $ret;	
	}
	
	/**
	 * Deletes the specified document
	 */
	private function delDoc($docid){
			
			$document = $this->connection->find("from Document where uuid=?", $docid);
			if (count($document) != 1){
				throw new Exception("Invalid document docid");
			}
			$document = $document[0];
			$name = $document->Name;
			$ext = $document->Extension;
			$folder = $document->Folder;
			$path = $this->docstoreLoc ."/$folder/$name.$ext";
			
			//delete the actual file
			$this->microCore->callModuleFunc("logging", "addItem", array("document $docid deleted",Logging::VERBOSITY_NORMAL));
			unlink($path);
			$this->connection->delete($document);
			
	}
	
	/**
	 * Gets the most recent revision of the document associated
	 * with specified record
	 */
	private function getLatest($recid){

			$record = $this->connection->find("from Record where uuid=?", $recid);
			if (count($record) != 1){
				throw new Exception("Invalid record uuid");
			}
			$record = $record[0];
			if (count($record->Documents) > 0){
				foreach($record->Documents as $doc){
					if ($doc->Revision == $record->LatestRev){
						$this->microCore->callModuleFunc("logging", "addItem", array("Latest revision for record $recid retrieved",Logging::VERBOSITY_NORMAL));
						return $doc;
					}
				}
			}
			return null;
	}
	
	/**
	 * Deletes all but the latest revision of the document
	 * associated with specified record
	 * TODO: double check logic
	 * TODO: logging
	 */
		private function delOldRevs($recid){
		
			$record = $this->connection->find("from Record where uuid=?", $recid);
			if (count($record < 1)){
				throw new Exception("Invalid recort uuid");
			}
			$record = $record[0];
			if (count($record->Documents) > 0){
				foreach($record->Documents as $doc){
					if ($doc->Revision != $record->LatestRev){
						$connection->delete($record->Documents);
						$path = $this->docstoreLoc ."/$folder/$name.$ext";
						unlink($path);
					}
				}
			}
			return $this->connection->commit($record);
		}
	
	/**
	 * Return the currently active sotrage folder
	 * @return string the current folder number
	 */
	private function getLatestFolder(){
				$store = "";
		$dirs = array();
		$files = scandir($this->docstoreLoc);
		foreach($files as $file){
			if(strstr($file, "STORE")){
				$dirs[] = $file;
			}
		}
		if(count($dirs) == 0){
			mkdir($this->docstoreLoc."/STORE0",0700);
			$store = "STORE0";
		}else{
			$store = max($dirs);
			$count = count(scandir($this->docstoreLoc."/".$store));
			//the +2 is for . and ..
			$max = $this->maxfiles + 2;
			if ($count >= $max){
				$n = substr($store,5,strlen($store));
				$n++;
				$store = "STORE".$n;
				mkdir($this->docstoreLoc ."/". $store,0700);
				
			}
		}
		return $store;
	}
	
	/**
	 * returns true if current user is permitted to 
	 * call specified function or false if not;
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