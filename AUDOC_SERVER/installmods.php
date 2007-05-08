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
 * This file contains the quick module setup routines to install
 * all the core modules
 *
 * @package    AuDoc.Modules
 * @author	   Jonathan Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   
 */

include_once("classes/includes/class.SmartLoader.php");
echo "Module Registration Utility\n^^^^^^^^^^^^^^^^^^^^^^^^^^^\n\n";
//echo "Loading MicroCore\n";
$mc = new MicroCore("config/configuration.conf");
$con = $mc->getConnection();

echo "Registering the RecMan module...\n";
		$mod = $con->create('Module');
		$mod->Name = "recman";
		$mod->Version = 1.0;
		$mod->Description = "records management module";
		$mod->EntryClass = "Recman";
		$con->commit($mod);

echo "Register the DocStore module...\n";
		$mod = $con->create('Module');
		$mod->Name = "docstore";
		$mod->Version = 1.0;
		$mod->Description = "docstore module";
		$mod->EntryClass = "DocStore";
		$con->commit($mod);

echo "Register the json module...\n";
		$mod = $con->create('Module');
		$mod->Name = "json_handler";
		$mod->Version = 1.0;
		$mod->Description = "json rpc module module";
		$mod->EntryClass = "JSON_Handler";
		$con->commit($mod);
		
echo "Register the logging module...\n";
		$mod = $con->create('Module');
		$mod->Name = "logging";
		$mod->Version = 1.0;
		$mod->Description = "logging module";
		$mod->EntryClass = "Logging";
		$con->commit($mod);

echo "register the ModuleManager module...\n";
		$mod = $con->create('Module');
		$mod->Name = "modulemanager";
		$mod->Version = 1.0;
		$mod->Description = "Modulemanager module";
		$mod->EntryClass = "Modulemanager";
		$con->commit($mod);

echo "register the Search Module...\n";
		$mod = $con->create('Module');
		$mod->Name = "search";
		$mod->Version = 1.0;
		$mod->Description = "search module";
		$mod->EntryClass = "Search";
		$con->commit($mod);

echo "register the SecLevel Module...\n";
		$mod = $con->create('Module');
		$mod->Name = "security";
		$mod->Version = 1.0;
		$mod->Description = "security module";
		$mod->EntryClass = "Security";
		$con->commit($mod);

echo "register the Trays Module...\n";
		$mod = $con->create('Module');
		$mod->Name = "trays";
		$mod->Version = 1.0;
		$mod->Description = "trays module";
		$mod->EntryClass = "Trays";
		$con->commit($mod);

echo "register the Users Module..\n";
		$mod = $con->create('Module');
		$mod->Name = "users";
		$mod->Version = 1.0;
		$mod->Description = "users module";
		$mod->EntryClass = "Users";
		$con->commit($mod);

echo "register the Reporting Module..\n";
		$mod = $con->create('Module');
		$mod->Name = "reporting";
		$mod->Version = 1.0;
		$mod->Description = "reporting module";
		$mod->EntryClass = "Reporting";
		$con->commit($mod);
echo "\nRegistration Complete";
echo "Adding admin user...";
	$user = $con->create("User");
	$user->Forename = "Admin";
	$user->Surname = "User";
	$user->UserName = "admin";
	$user->Password = "admin";
	$user->isAdmin = true;
	$con->commit($user);
echo "Setup Complete";
?>