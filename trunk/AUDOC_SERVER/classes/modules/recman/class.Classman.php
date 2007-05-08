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
 * This file contains a class for dealing
 * with Classification objects
 *
 * @package    AuDoc.Modules
 * @author	   Khalid Adem <khalid.adem@audata.co.uk>
 * @copyright  2007 Audata Limited   
 * @version    $Id: $
 */
class Classman {
	
	private $microcore;
	
	public function __construct($mc){
		$this->microcore = $mc;
	}
	
	public function addClass($value, $parent, $retention, $seclevel, $caveats){
		$ret = null;
		$con = $this->microcore->getConnection();
		
		//get parent object
		if ($parent != null){
			$parents = $con->find("from Classification where uuid=?", $parent);
			if(count($parents) < 1){
				throw new Exception("Invalid Parent classification name");
			}
			$parent = $parents[0];
		}
		
		//default retention to parent if needed
		if ($retention == null){
			if($parent != null){
				$retention = $parent->Retention;
			}else{
				$retention = 7;
			}
		}
		
		//get seclevel object
		$seclevels = $con->find("from SecLevel where Name=?", $seclevel);
		if(count($seclevels) < 1){
			throw new Exception("The Security level " . $seclevel . " does not exist");
		}else{
			$seclevel = $seclevels[0];
		}
		
		//get caveat objects
		$cavs = array();
		if($caveats != null){
			foreach($caveats as $cav){
				$cavA = $con->find("from Caveat where Name=?", $cav);
				if(count($cavA) < 1){
					throw new Exception("The Caveat " . $cav. "does not exist");
				}else{
					$cavs[] = $cavA[0];
				}
			}
		}
		
		//create classification
		$classification = $con->Create("Classification");
		$classification->Value = $value;
		$classification->Parent = $parent;
		$classification->Retention = $retention;
		$classification->SecLevel = $seclevel;
		$classification->Caveats = $cavs;
		$con->commit($classification);
		return $classification;
	}
	
	public function getClass($uuid)
	{
			$con = $this->microcore->getConnection();
			$class = $con->find("from Classification where uuid=?", $uuid);
			if (count($class) < 1){
				throw new Exception("Invalid Classification uuid");
			}
			$class = $class[0];
			return $class;
	}
	
	public function modClass($uuid,$value,$parent,$retention,$seclevel,$caveats)
	{
		$con = $this->microcore->getConnection();
		$class = $con->find("from Classification where uuid=?", $uuid);
		if (count($class) < 1){
			throw new Exception("Invalid classification uuid");
		}
		$class = $class[0];
//		$parent = null;
		
		if($parent != null){
			$parent = $con->find('from Classification where uuid=?', $parent);
			if (count($parent)>0){
				$parent = $parent[0];
			}else{
				$parent = null;
			}
		}
			
		//get seclevel object
		$seclevels = $con->find("from SecLevel where Name=?", $seclevel);
		if(count($seclevels) < 1){
			throw new Exception("The Security level " . $seclevel . " does not exist");
		}else{
			$seclevel = $seclevels[0];
		}
		
		//get caveat objects
		$cavs = array();
		foreach($caveats as $cav){
			$cavA = $con->find("from Caveat where Name=?", $cav);
			if(count($cavA) < 1){
				throw new Exception("The Caveat " . $cav. "does not exist");
			}else{
				$cavs[] = $cavA[0];
			}
		}
			
		$class->Parent = $parent;
		$class->Retention = $retention;
		$class->SecLevel = $seclevel; 
		$class->Value = $value;
		//workaround for odd caching issue
		foreach($class->Caveats as $cav){
			$a = $cav;
		}
		$class->Caveats = $cavs;
		$con->commit($class);
		return $class;
	}
	
	public function delClass($uuid)
	{
		$ret = false;
		$con = $this->microcore->getConnection();
		$class = $con->find("from Classification where uuid=?", $uuid);
		if (count($class) < 1){
			throw new Exception("Invalid Classification uuid");
		}
		$class = $class[0];
		$record = $con->find("from Record where Classification.uuid=?", $uuid);
		if ((count($class->Children) > 0) OR (count($record) > 0)  ){
				throw new Exception("This object has children or is associated with records so cannot be deleted");
		}else{
			$ret = $con->delete($class);
		}
		return $ret;
	}
	public function getChildren($uuid)
	{
		$con = $this->microcore->getConnection();
		$classes = $con->find("from Classification where Parent.uuid=?", $uuid);
		return $classes;
	}
	
	/**
	 * Returns all top levl classification entries
	 */
	public function getTopLevel(){
		$con = $this->microcore->getConnection();
		$classes = $con->find('from Classification where Parent IS NULL');
		return $classes;
	}
}
?>