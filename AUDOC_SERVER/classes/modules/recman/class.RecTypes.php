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
 * This file contains a class for dealing with record types
 *
 * @package    AuDoc.Modules
 * @author	   Jonathan Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   
 * @version    $Id: $
 */
class RecTypes {

	private $microcore;

	public function __construct($mc) {
		$this->microcore = $mc;
	}

	public function getRecType($uuid) {
		$con = $this->microcore->getConnection();
		$rt = $con->find("from RecordType where uuid=?", $uuid);
		if (count($rt) < 1) {
			throw new Exception("Invalid RecordType uuid");
		}
		
		return $rt[0];
	}
	
	public function getRecTypes() {
		$con = $this->microcore->getConnection();
		$r = $con->find("from RecordType order by Name");
		return $r;

	}
	
	public function modRecType($uuid, $name, $desc, $udfs) {
		$con = $this->microcore->getConnection();
		$rt = $con->find("from RecordType where uuid=?", $uuid);
		if (count($rt) < 1) {
			throw new Exception("Invalid RecordType uuid");
		}
		$rt = $rt[0];
		$rt->Name = $name;
		$rt->Description = $desc;
		
		//deal with cachine issues?
		foreach($rt->UDFs as $u){
			$a = $u;
		}
		
		$rt->UDFs = array();
		foreach($udfs as $udf){
			$u = $con->find("from UDFDefinition where Name == ?", $udf);
			if(count($u) < 1){
				throw new Exception("Specified UDF ($udf) is not value");
			}
			$rt->UDFs[] = $u[0];
		}
		$con->commit($rt);
		return $rt;
	}

	public function delRecType($uuid) {
		$con = $this->microcore->getConnection();
		$rt = $con->find("from RecordType where uuid=?", $uuid);
		if (count($rt) < 1) {
			throw new Exception("Invalid RecordType uuid");
		}
		$rt = $rt[0];
		$recs = $con->find("from Record where RecordType.Name == ?", $rt->Name);
		if (count($recs) > 0) {
			throw new Exception("You cannot delete a record type that is in use");
		}
		return $con->delete($rt);
	}

	/**
	 * creates a new record typ
	 * TODO: implement
	 */
	public function addRecType($name, $desc, $udfs) {
		$con = $this->microcore->getConnection();
		$rt = $con->find("from RecordType where Name=?", $name);
		if (count($rt) > 0) {
			throw new Exception("RecordType with the name $name already exists");
		}
		$rt = $con->create("RecordType");
		$rt->Name = $name;
		$rt->Description = $desc;
		$rt->UDFs = array();
		foreach($udfs as $udf){
			$u = $con->find("from UDFDefinition where Name == ?", $udf);
			if(count($u) < 1){
				throw new Exception("Specified UDF ($udf) is not value");
			}
			$u = $u[0];
			$rt->UDFs[] = $u;
		}
		$con->commit($rt);
		return $rt;
	}

}
?>