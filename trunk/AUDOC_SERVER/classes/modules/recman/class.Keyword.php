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
 * This file contains a class for handling keyword hierarchies
 *
 * @package    AuDoc.Modules
 * @author	   Khalid Adem <khalid.adem@audata.co.uk>
 * @copyright  2005 Audata Limited   

 */
class Keyword {
	
	private $microCore = null;
	
	public function __construct($mc){
		$this->microCore = $mc;
	}

	/**
	 * Returns the Keyword Hierarchy specified
	 */
	public function getKWH($uuid)
		{
			$con = $this->microCore->getConnection();
			$kwh = $con->find("from keywordHierarchy where uuid=?", $uuid);
			if (count($kwh < 1)){
				throw new Exception("Invalid keywordHierarchy id");
			}
			$kwh = $kwh[0];
			return $kwh;
		}
	
	/**
	 * Returns an array of all keyword hierarchies
	 */
	public function getKWHs()
		{
			$con = $this->microCore->getConnection();
			$kwhs = $con->get("KeywordHierarchy");
			return $kwhs;
		}
	
	/**	
	 * Deletes the specified kwh
	 */
	public function delKWH($uuid)
		{
			$con = $this->microCore->getConnection();
			$kwh = $con->find("from KeywordHierarchy where uuid=?", $uuid);
			if (count($kwh) != 1){
				throw new Exception("Invalid KeywordHierarchy uuid");
			}
			$kwh = $kwh[0];
			$udfd = $con->find("from UDFDefinition where KeywordHierarchy.uuid = ? ", $uuid);
			if (count($udfd) > 0){
				throw new Exception("Cannot delete Keyword Hierarchy because it is in use by one or more user defined fields");
				//todo: specify the actual UDFs using it
			}
			return $con->delete($kwh);			
		}
	
	/**
	 * Creates a new Keyword Hierarchy
	 */
	 public function addKWH($name){
		 $con = $this->microCore->getConnection();
		 $kwh = $con->create('KeywordHierarchy');
		 $kwh->Name = $name;
		 $con->commit($kwh);
		 return $kwh;
	 }
	 
	 /**
	  * Returns top level items of specified KWH
	  */
	 public function getKWTopLevel($uuid){
		 $con = $this->microCore->getConnection();
		 $kwh = $con->find('from KeywordHierarchy where uuid=?', $uuid);
		 if(count($kwh) < 1){
			 throw new Exception('Keyword Hierarchy specified does not exist');
		 }
		 $kwh = $kwh[0];
		 if(isset($kwh->TopLevel)){
		 	return $kwh->TopLevel;
		 }else{
		 	return null;
		 }
		 
	 }
	 
	 /**
	  * Returns an array of children KWData objects
	  */
	  public function getKWChildren($uuid){
		 $con = $this->microCore->getConnection();
		 $kwd = $con->find('from KeywordData where uuid=?', $uuid);
		 if(count($kwd) < 1){
			 throw new Exception('Keyword Data item specified does not exist');
		 }
		 $kwd = $kwd[0];
		 return $kwd->Children;
	  }
	  
	  /**
	   * Adds a new KeywordData item to a hierarchy
	   */
	   public function addKWData($huuid, $parent, $value){
		 $con = $this->microCore->getConnection();
		 
		 //get keyword hierarchy
		 $kwh = $con->find('from KeywordHierarchy where uuid=?', $huuid);
		 if(count($kwh) < 1){
			 throw new Exception('Keyword Hierarchy specified does not exist');
		 }
		 $kwh = $kwh[0];
		 
		 //get parent
		 if ($parent != null){
			 $parent = $con->find('from KeywordData where uuid=?', $parent);
			 if(count($parent) < 1){
				 throw new Exception('Parent specified does not exist');
			 }		 
			 $parent = $parent[0];
		 }
		 
		 //Create item
		 $item = $con->create('KeywordData');
		 $item->Value = $value;
		 $item->Hierarchy = $kwh;
		 $item->Parent = $parent;
		 $con->commit($item);
		 
		 if($parent == null){
		 	$kwh->TopLevel[] = $item;
		 } 
		 
		 return $item;
	   }
	   
	   /**
	    * returns a single KeywordData item
	    */
	    public function getKWData($uuid){
			$con = $this->microCore->getConnection();
			$kwd = $con->find('from KeywordData where uuid=?', $uuid);
			if(count($kwd) < 1){
				throw new Exception("KeywordData item specified does not exist");
			}
			return $kwd[0];
		}
		
		/**
		 * Modifies the specified KWData item
		 */
		 public function modKWData($uuid, $value){
			$con = $this->microCore->getConnection();
			$kwd = $con->find('from KeywordData where uuid=?', $uuid);
			if(count($kwd) < 1){
				throw new Exception("KeywordData item specified does not exist");
			}
			$kwd = $kwd[0]; 
			$kwd->Value = $value;
			$con->commit($kwd);
			return $kwd;
		 }
		 
		 /**
		  * Deletes the specifies KWData item
		  */
		  public function delKWData($uuid){
			$con = $this->microCore->getConnection();
			$kwd = $con->find('from KeywordData where uuid=?', $uuid);
			if(count($kwd) < 1){
				throw new Exception("KeywordData item specified does not exist");
			}
			$kwd = $kwd[0]; 
			return $con->delete($kwd);
		  }
}

?>