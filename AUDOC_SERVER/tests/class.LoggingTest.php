<?php
/**
 * Test the audoc.logging.Logging class
**/
//include_once("classes/dataObjects/class.Caveat.php");
//require_once("class.SmartLoader.php");

class LoggingTest extends UnitTestCase{
		
	private $mc;

	public function __construct(){
		$this->_label = "Logging Test";
		$this->mc = new MicroCore("config/configuration.conf");
		$this->con = $this->mc->getConnection();
		
		//register the json module
		$jmod = $this->con->create('Module');
		$jmod->Name = "json_handler";
		$jmod->Version = 1.0;
		$jmod->Description = "json rpc module module";
		$jmod->EntryClass = "JSON_Handler";
		$this->con->commit($jmod);
		
		
		//register the logging module
		$logmod = $this->con->create('Module');
		$logmod->Name = "logging";
		$logmod->Version = 1.0;
		$logmod->Description = "logging module";
		$logmod->EntryClass = "Logging";
		$this->con->commit($logmod);
				
		//set session for user
		$_SESSION['Username'] = "jonm";
		$_SESSION['isAdmin'] = true;
	}
	
	function testExecuteAddItem (){
		$params = array();
		$params[0] = "Change something";
		$params[1] = Logging::VERBOSITY_NORMAL;
		$addItem = $this->mc->callModuleFunc("Logging", "addItem", $params);
		$this->assertNotNull($addItem, "addItem");
	}

	function testExecuteGetItems (){
		$params = array();
		$params[0] = 0;
		$params[1] = time();
		$dump = $this->mc->callModuleFunc("Logging", "getItems", $params);
		$this->assertNotNull($dump, "dumpItems passed");
	}
}

?>