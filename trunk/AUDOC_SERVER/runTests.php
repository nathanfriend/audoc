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
 * This file kick starts the unit test
 *
 * @package    AuDoc.Modules
 * @author	   Jonathan Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   
 * @version    $Id: $
 */
if(!isset($_SESSION)){
session_start();
}
include_once("classes/includes/class.SmartLoader.php");

//delete old DB
if(file_exists("audoc2.db")){
	unlink("audoc2.db");
};

$t = new GroupTest('All AuDoc Server Tests');
$t->_label = "All AuDoc Server Tests";
/*
$t->addTestFile("tests/class.MicroCoreTest.php");
$t->addTestFile("tests/class.LoggingTest.php");
$t->addTestFile("tests/class.SecurityTest.php");
$t->addTestFile("tests/class.UsersTest.php");
$t->addTestFile("tests/class.ClassificationTest.php");
$t->addTestFile("tests/class.TraysTest.php");
$t->addTestFile("tests/class.UDFTest.php");
$t->addTestFile("tests/class.SearchTest.php");
*/
$t->addTestFile("tests/class.DocStoreTest.php");
$status = $t->run(new CliReporter());

?>

