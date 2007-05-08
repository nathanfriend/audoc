<?php
/**
 * Test the audoc.microcore.MicroCore class
**/
//require_once("classes/audoc/class.ModuleMan.php");
//require_once("classes/dataObjects/class.absAuDocBase.php");
//require_once("classes/dataObjects/class.Caveat.php");

class ClassificationTest extends UnitTestCase{

	private $mc;
	private $con;
	private $class;
	
	public function __construct(){
		$this->_label = "Classification Test";
		$this->mc = new MicroCore("config/configuration.conf");
		$this->con = $this->mc->getConnection();
		
		//register the RecMan Module
		$umod = $this->con->create('Module');
		$umod->Name = "recman";
		$umod->Version = 1.0;
		$umod->Description = "records management module";
		$umod->EntryClass = "Recman";
		$this->con->commit($umod);
	}
	/**
	 * Tests the constructor
	 **/
	function testCreate(){
		$c = array();
		$c[0] = "Manage The Business"; //Value
		$c[1] = null; //parent
		$c[2] = 5; //retention
		$c[3] = "Classified"; //secLevel
		$c[4] = null; //caveats
		$this->class = $this->mc->callModuleFunc("RecMan","addClass", $c);
		$this->assertNotNull($this->class);
	}
	
	function testCreate2(){
		$c = array();
		$c[0] = "Quality Systems"; //Value
		$c[1] = $this->class->uuid; //parent
		$c[2] = null; //retention
		$c[3] = "Classified"; //secLevel
		$c[4] = null; //caveats
		$class = $this->mc->callModuleFunc("RecMan","addClass", $c);
		$this->assertNotNull($class);
	}
}

?>