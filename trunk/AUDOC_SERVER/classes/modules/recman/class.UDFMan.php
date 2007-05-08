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
 * This file contains a class for creating UDFDefinitions
 *
 * @package    AuDoc.Modules
 * @author	   Khalid Adem <khalid.adem@audata.co.uk>
 * @copyright  2005 Audata Limited   
 * @version    $Id: $
 */

class UDFMan {

		private $microcore;

	public function __construct($mc) {
		$this->microcore = $mc;
	}

	public function getUDF($uuid) {
		$con = $this->microcore->getConnection();
		$udfd = $con->find("from UDFDefinition where uuid=?", $uuid);
		if (count($udfd) < 1) {
			throw new Exception("Invalid UDFDefinition uuid");
		}
		$udfd = $udfd[0];
		return $udfd;
	}
	
	public function getUDFs() {
		$con = $this->microcore->getConnection();
		$udfds = $con->find("from UDFDefinition order by Name");
		return $udfds;

	}
	
	public function modUDF($uuid, $name) {
		$con = $this->microcore->getConnection();
		$udfd = $con->find("from UDFDefinition where uuid=?", $uuid);
		if (count($udfd) < 1) {
			throw new Exception("Invalid UDFDefinition uuid");
		}
		$udfd = $udfd[0];
		$udfd->Name = $name;
		return $udfd;
	}

	public function delUDF($uuid) {
		$con = $this->microcore->getConnection();
		$udfd = $con->find("from UDFDefinition where uuid=?", $uuid);
		if (count($udfd) < 1) {
			throw new Exception("Invalid UDFDefinition uuid");
		}
		$udfd = $udfd[0];
		$con->delete($udfd);
		return null;
	}

	public function addUDF($name, $type, $kwuuid = null) {
		$ret = null;
		if (($type >= 0) && ($type <= 4)) {
			$con = $this->microcore->getConnection();
			$ret = $con->create('UDFDefinition');
			$ret->Name = $name;
			$ret->Type = $type;
			if ($kwuuid != null) {
				if ($type != UDF::TYPE_KEYWORD) {
					throw New Exception("You cannot associate a Keyword hierarchy with a non-keyword UDF");
				}
				$kwh = $con->find("from KeywordHierarchy where uuid == ?", $kwuuid);
				if (count($kwh) > 0) {
					$kwh = $kwh[0];
					$ret->KeywordHierarchy = $kwh;
				} else {
					throw new Exception('Invalid keywordHierarchy uuid');
				}
			}
			$con->commit($ret);
		}
		return $ret;
	}

}
?>
