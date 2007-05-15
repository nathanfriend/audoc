<html>
<head><title>AuDoc Configuration</title></head>

<body>
<img src="install/welcome.jpg" />
<?php
flush();ob_flush();
if(isset($_POST['install'])){
	//process
	echo "<h1>Installing AuDoc Configuration</h1>";
	flush();ob_flush();
	$db_type = $_POST['sqlType'];
	$db_server = $_POST['dbserver'];
	$db_name = $_POST['dbname'];
	$db_username = $_POST['dbuser'];
	$db_password = $_POST['dbpass'];
	$audoc_pass = $_POST['adminpass'];
	$type = 'blank';
	if($_POST['installType'] == blank){
		$type = 'blank';
	}else{
		$type = 'example';
	}
	install($db_type, $db_server, $db_name, $db_username, $db_password, $audoc_pass, $type);
	echo "<h1>Configuration Complete - <a href=\"../AUDOC_CLIENT/AuDoc.html\">Click here to go to login page</a></h1>";
}else{
	showForm();
}

function install($db_type, $db_server, $db_name, $db_user, $db_pass, $admin_pass, $type){
	
	if(test_connection($db_type, $db_server, $db_name, $db_user, $db_pass)){
		setDSN($db_type, $db_server, $db_name, $db_user, $db_pass);
		if($type == "blank"){
			//blank install
			createBlank($admin_pass);
		}else{
			//load config!
			createExample($db_server, $db_name, $db_user, $db_pass, $admin_pass);
		}
	}else{
		die();
	}
}

function test_connection($db_type, $db_server, $db_name, $db_user, $db_pass){
		switch($db_type){
			case 'mysql':
				$connection = new mysqli($db_server, $db_user, $db_pass, $db_name);
				if($connection !== false){
					echo 'Connection made to database<br/>';
					flush();ob_flush();
					return true;
				}else{
					echo 'Connection to database failed (' . mysqli_connect_error() . ')<br/>';
					flush();ob_flush();
					return false;
				}
				break;
			case 'sqlite':
				$connection = new SQLiteDatabase($db_name, 0666, $error);
				if ($error){
					echo 'Connection to database failed (' . $error . ')<br/>';
					flush();ob_flush();
					return false;
				}else{
					echo 'Connection made to database <br/>';
					flush();ob_flush();
					return true;
				}
				break;
		}
}

function setDSN($db_type, $db_server, $db_name, $db_user, $db_pass){
	$dsn = "";
	switch($db_type){
		case 'mysql':
			if($db_pass == ""){
				$dsn = "mysql://$db_user@$db_server/$db_name";
			}else{
				$dsn = "mysql://$db_user:$db_pass@$db_server/$db_name";
			}
		break;
		case 'sqlite':
			$dsn = "sqlite://audoc.db";
		break;
	}
	
	$conf = fopen("config/configuration.conf", "w");
	fwrite($conf,"ezpdo.dsn=\"". $dsn ."\"\n");
	fwrite($conf,"ezpdo.backup=\"false\"\n");
	fwrite($conf,"ezpdo.splitRelation=\"true\"\n");
	fwrite($conf,"ezpdo.autoCompile=\"true\"\n");
	fwrite($conf,"ezpdo.autoFlush=\"true\"\n");
	fwrite($conf,"ezpdo.dbLib=\"adodb\"\n");
	fwrite($conf,"ezpdo.logConsole=\"true\"\n");
	fwrite($conf,"ezpdo.logFile=\"cache/ezpdo_log.txt\"\n");
	fwrite($conf,"logging.level=\"2\"\n");
	fwrite($conf,"docstore.location=\"docstore\"\n");
	fwrite($conf,"docstore.maxnumber=\"2000\"\n");
	fclose($conf);
	echo "Database Configuration written to file <br/>";
	flush();ob_flush();
}

function createBlank($admin_pass){
	include_once("classes/includes/class.SmartLoader.php");
	$mc = new MicroCore("config/configuration.conf");
	$con = $mc->getConnection();
	addModule($con, "recman", 1.0, "records management module", "Recman");
	addModule($con, "docstore", 1.0, "docstore module", "DocStore");
	addModule($con, "json_handler", 1.0, "json rpc module module", "JSON_Handler");
	addModule($con, "logging", 1.0, "logging module", "Logging");
	addModule($con, "modulemanager", 1.0, "module management module", "Modulemanager");
	addModule($con, "search", 1.0, "search module", "Search");
	addModule($con, "security", 1.0, "security module", "Security");
	addModule($con, "trays", 1.0, "trays management module", "Trays");
	addModule($con, "users", 1.0, "user management module", "Users");
	addModule($con, "reporting", 1.0, "reporting module", "Reporting");
	addModule($con, "reccentre", 1.0, "record centre module", "RecCentre");
	
	//add admin user
	$user = $con->create("User");
	$user->Forename = "Admin";
	$user->Surname = "User";
	$user->UserName = "admin";
	$user->Password = $admin_pass;
	$user->isAdmin = true;
	$con->commit($user);
	echo "Admin users added<br/>";
	flush();ob_flush();
}

function addModule($connection, $name, $version, $description, $entryClass){
	$mod = $connection->create("Module");
	$mod->Name = $name;
	$mod->Version = $version;
	$mod->Description = $description;
	$mod->EntryClass = $entryClass;
	$connection->commit($mod);
	echo "Module <strong>$name</strong> added </br>";
	flush();ob_flush();
}

function createExample($db_server, $db_name, $db_user, $db_pass, $admin_pass){
	$connection = new mysqli($db_server, $db_user, $db_pass, $db_name);
	
	$imports = array();
	$files = scandir("install");
	foreach($files as $file){
		if(strpos($file, ".sql") !== false){
			$imports[basename($file, ".sql")] = addslashes(dirname(__FILE__). "/install/" .$file);
		}
	}
	foreach($imports as $table=>$path){
		$query = "LOAD DATA INFILE '$path' INTO TABLE $table";
		if($connection->query($query) === true){
			echo "Successfully imported the table <strong>" . $table . "</strong><br/>";
			flush();ob_flush();
		}else{
			echo "Unable to import table " . $table . "(".$connection->error.")<br/>";
			flush();ob_flush();
		}
	}
	$connection->close();
	
	include_once("classes/includes/class.SmartLoader.php");
	$mc = new MicroCore("config/configuration.conf");
	$con = $mc->getConnection();
	$user = $con->find("FROM User WHERE Username=?", 'admin');
	if(count($user) != 0){
		$user = $user[0];
		$user->Password = $admin_pass;
		$con->commit($user);
		echo "Admin Password Changed<br/>";
		flush();ob_flush();
	}else{
		echo "Unable change admin password";
		flush();ob_flush();
	}
	
	echo "incomplete!!";	
	flush();ob_flush();
}

function showForm(){
?>
<form method="post" action="install.php">
<input type="hidden" name="install" value="true"/>
<h3>Database Setup:</h3>
<table>
<tr>
<td>SQL Server Type:</td>
<td>
<select name="sqlType">
<option value="mysql">MySQL</option>
<option value="sqlite">SQLite</option>
</select>
</td>
</tr>
<tr>
<td>Database Server:</td>
<td><input type="text" name="dbserver" value="localhost"/></td>
</tr><tr>
<td>Database Name:</td>
<td><input type="text" name="dbname" value="audoc"/></td>
</tr><tr>
<td>Database Username:</td>
<td><input type="text" name="dbuser"/></td>
</tr><tr>
<td>Database Password:</td>
<td><input type="text" name="dbpass"/></td>
</tr>
</table>
<hr/>
<h3>System Security</h3>
AuDoc Admin Password: <input type="text" name="adminpass"/><br/>
<hr/>
<h3>Install Type</h3>
<input type="radio" name="installType" value="blank" checked /> Blank Installation<br/>
<input type="radio" name="installType" value="example"/> Install example classification and record types (MySQL only)<br/>
<hr/>
<input type="submit" value="Install">
</form>
<?php
}
?>
</body>
</html>