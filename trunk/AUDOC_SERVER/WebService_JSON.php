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
 */
 
 /**
 * This file provides access to the AuDoc 2 server component
 * via JSON encoded Remote Procedure Calls (JSON-RPC)
 *
 * @package    AuDoc.Modules
 * @author	   Jonathan Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   
 */
//Avoid caching issues
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");      // Date in the past
header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT");  // always modified
header("Cache-Control: no-store, no-cache, must-revalidate");   // HTTP/1.1
header("Cache-Control: post-check=0, pre-check=0", false);
header("Pragma: no-cache");           // HTTP/1.0 
 
//include the class loader and load the MicroCore
require_once("classes/includes/class.SmartLoader.php");
$mc = new MicroCore("config/configuration.conf");

//get message
$params[] = file_get_contents('php://input');
	//Process request
	$mc->callModuleFunc("logging", "addItem", array("JSON Call: $params[0]",Logging::VERBOSITY_DEBUG));
	$ret = $mc->callModuleFunc("JSON_Handler", "processRequest", $params);

	
	//return response from JSON_Handler
	if ($ret != null){
		$mc->callModuleFunc("logging", "addItem", array("JSON Call: $ret",Logging::VERBOSITY_DEBUG));
		echo $ret;
	}
?>