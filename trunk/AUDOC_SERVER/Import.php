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
 * This file contains the command line interface to the import toold
 *
 * @package    AuDoc
 * @author	   Jonathan Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   
 */
if ($argc != 3 || $argv[1] == '--help') {
	showHelp();
	die();
}else{
	if(!in_array($argv[1],array('r','c','k'))){
		echo "$argv[1] is not a valid option\n";
		showHelp();
		die();
	}
	if(!file_exists($argv[2])){
		echo "$argv[2] is not a valid file\n";
		showHelp();
		die();
	}else{
		switch($argv[1]){
			case 'r':
				importRecords($argv[2]);
				break;
			case 'k':
				//keyword import
				break;
			case 'c':
				//classification import
				break;
		}
	}
}

function showHelp(){
?>
This is a command line import script for AuDoc

  Usage:
  <?php echo $argv[0]; ?> <type> <file>

  <type> is the import type and can be one of the
  		 following:
  		 	r	for records
  		 	k	for keyword hierarchies
  		 	c	for classifications
  <file> is the path to import CSV file
<?php
}

function importRecords($path){
	echo "Importing Records from $path\n";
	require_once("classes/includes/class.SmartLoader.php");
	$mc = new MicroCore("config/configuration.conf");
	$importer = new ImportRecord($mc->getConnection());
	$importer->importCSV($path);
}
?>