<?php
/**
 * Test the audoc.modulemanager.Modulemanager class
**/
//include_once("classes/dataObjects/class.Caveat.php");
//require_once("class.SmartLoader.php");

class moduleManagerTest extends UnitTestCase{
		
	private $mc;

	public function __construct(){
		$this->_label = "Module Manager Test";
		$this->mc = new MicroCore("config/configuration.conf");
		$this->con = $this->mc->getConnection();
		
		//register the Modulemanager Module
		$mmmod = $this->con->create('Module');
		$mmmod->Name = "modulemanager";
		$mmmod->Version = 1.0;
		$mmmod->Description = "Modulemanager module";
		$mmmod->EntryClass = "Modulemanager";
		$this->con->commit($mmmod);
		
		//set session for user
		$_SESSION['Username'] = "jonm";
		$_SESSION['isAdmin'] = true;
	}
	
	function testExecuteInstallModule (){
		$params = array();
		$params['path'] = "path";
		$instMod = $this->mc->callModuleFunc("Modulemanager", "InstallModule", $params);
		$this->assertNotNull($instMod, "InstallModule passed");
	}

	function testExecuteRemoveModule (){
		$params = array();
		$params['name'] = "users";

		$remMod = $this->mc->callModuleFunc("Modulemanager", "RemoveModule", $params);
		$this->assertNotNull($remMod, "RemoveModule passed");
	}
}

?>