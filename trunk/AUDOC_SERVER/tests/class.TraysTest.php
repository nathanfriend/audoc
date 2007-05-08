<?php
/**
 * Test the Tays class
**/
//include_once("classes/dataObjects/class.Caveat.php");
//require_once("class.SmartLoader.php");

class TraysTest extends UnitTestCase{

	private $mc;

	public function __construct(){
		$this->_label = "Trays Test";
		$this->mc = new MicroCore("config/configuration.conf");
		$this->con = $this->mc->getConnection();
		

		
		//register the Users Module
		$umod = $this->con->create('Module');
		$umod->Name = "trays";
		$umod->Version = 1.0;
		$umod->Description = "trays module";
		$umod->EntryClass = "Trays";
		$this->con->commit($umod);
		
		$rec = $this->con->create("Record");
		$rec->Title = "Record Title";
		$rec->RecordNumber = "REC1111";
		$this->recId = $rec->uuid;
		$this->con->commit($rec);
		
		$rec = $this->con->create("Record");
		$rec->Title = "Record Title2";
		$rec->RecordNumber = "REC2222";
		$this->recId = $rec->uuid;
		$this->con->commit($rec);
		
	}

	function testExecuteAddTray (){
		$tray = $this->mc->callModuleFunc("Trays", "addTray", array("My Tray", "About my tray"));
		$this->id = $tray->uuid;
		$this->assertNotNull($tray, "addTray");
	}
	
	function testExecuteGetTrays (){
		$trays = $this->mc->callModuleFunc("Trays", "getTrays", null);
		$this->assertNotNull($trays, "getTrays");
	}
	
	function testExecuteAddItems (){
		$tray = $this->mc->callModuleFunc("Trays", "addItem", array($this->id, $this->recId));
		$this->assertNotNull($tray, "addItem");
	}
	
	function testExecuteGetItems (){
		$items = $this->mc->callModuleFunc("Trays", "getItems", array($this->id));
		$this->assertNotNull($items, "getItems");
	}
}
?>