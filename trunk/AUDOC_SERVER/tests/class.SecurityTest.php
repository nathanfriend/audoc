<?php
/**
 * Test the audoc.security.Security class
**/
//include_once("classes/dataObjects/class.Caveat.php");
//require_once("class.SmartLoader.php");

class SecurityTest extends UnitTestCase{
		
	private $mc;

	public function __construct(){
		$this->_label = "Security Test";
		$this->mc = new MicroCore("config/configuration.conf");
		$this->con = $this->mc->getConnection();
		
		//register the SecLevel Module
		$secmod = $this->con->create('Module');
		$secmod->Name = "security";
		$secmod->Version = 1.0;
		$secmod->Description = "security module";
		$secmod->EntryClass = "Security";
		$this->con->commit($secmod);
		
		//set session for user
		$_SESSION['Username'] = "jonm";
		$_SESSION['isAdmin'] = true;
	}
	
	function testExecuteAddSecLevel (){
		$params = array();
		$params[] = "Classified";
		$params[] = 20;
		$secLevel = $this->mc->callModuleFunc("Security", "addSecLevel", $params);
		$this->assertNotNull($secLevel, "addSecLevel passed");
	}

	function testExecuteGetSecLevels (){
		$params = array();
		$secLevel = $this->mc->callModuleFunc("Security", "getSecLevels", $params);
		$this->assertTrue(count($secLevel) > 0, "getSecLevels passed");
	}
	
	function testExecuteAddCaveat (){
		$params = array();
		$params[] = "Finance";
		$caveat = $this->mc->callModuleFunc("Security", "addCaveat", $params);
		$this->assertNotNull($caveat, "addCaveat passed");
	}

	function testExecuteGetCaveats (){
		$params = array();
		$caveat = $this->mc->callModuleFunc("Security", "getCaveats", $params);
		$this->assertTrue(count($caveat) > 0, "getCaveats passed");
	}
}

?>