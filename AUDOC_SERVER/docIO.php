<?php/* +----------------------------------------------------------------------+
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
 * This file deals with document IO
 *
 * @package    AuDoc
 * @author	   Jonathan Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   
 */ 
 
//include the class loader and load the MicroCore
require_once("classes/includes/class.SmartLoader.php");
$mc = new MicroCore("config/configuration.conf");

$docstore = "docstore";
try{
	$docstoreLoc = $mc->getConfigItem("docstore.location");
}catch(Exception $e){}//use the default

//get message
$method = "";
if(isset($_POST['method'])){
	$method = $_POST['method'];
}
if(isset($_GET['method'])){
	$method = $_GET['method'];
}

if($method == ""){
	$msg = "Invalid document request";
	if(isset($_FILES['document']['error'])){
		$msg .= " (Errorcode: ".$_FILES['document']['error'].")";
	}
	die($msg);
}
switch($method){
	case 'view':
		getDoc($_GET['id'], true);
		break;
	case 'get':
		getDoc($_GET['id'], false);
		break;
	case 'put':
		$recid = null;
		if (isset($_POST['recid'])){
			$recid = $_POST['recid'];
		}
		echo putDoc($recid);
		break;
}

/**
 * Gets the specified document by it id and dumps it to
 * the output buffer.
 */
function getDoc($id, $isView){
	global $mc;
	global $docstore;
	$disposition = "attachment";
	if($isView){
		$disposition = "inline";
	}
	$doc = $mc->callModuleFunc("docstore", "getLatest", array($id));

	$uuid = $doc->uuid;
	$name = $doc->Name;
	$folder = $doc->Folder;
	$ext = $doc->Extension;
	$mime = new Mime();
	$type = $mime->getTypeFromExt($ext);
	$path = "$docstore/$folder/$uuid.$ext";
	$fp = fopen($path, 'rb');

	// send the right headers	
	header("Cache-Control: ");// leave blank to avoid IE errors
	header("Pragma: ");// leave blank to avoid IE errors
	header("Content-type: $type");
	header("Content-Disposition: $disposition; filename=\"$name.$ext\"");
	header("Content-length:".(string)(filesize($path)));

	// dump the file to the output buffer
	fpassthru($fp);
}

/**
 * Takes an uploaded file and stores it in the docstore
 */
function putDoc($recid){
	if($_FILES['document']['error'] == 0){
		global $mc;
		$name = $_FILES["document"]["name"];
		$pos = strripos($name, ".");
		$ext = "";
		if($pos != -1){
			$ext = substr($name,$pos+1);
		}
		try{
			$doc = $mc->callModuleFunc("docstore", "storeDoc", array($recid, $ext));
		}catch(Exception $e){
			return $e->getMessage();
		}
	}else{
		if(isset($_FILES['userfile']['error'])){
			die("Unable to upload file (errorcode: " . $_FILES['userfile']['error'] . ")");
		}else{
			die("Unable to upload file");
		}
	}
}
?>