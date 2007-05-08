<?php
/**
 * Test the audoc.microcore.MicroCore class
**/
//include_once("classes/dataObjects/class.Caveat.php");
//require_once("class.SmartLoader.php");

class MicroCoreTest extends UnitTestCase{

	private $mc;
	
	/**
	 * Tests the constructor
	 **/
	function testCreate(){
		$this->_label = "MicroCore Test";
		$this->mc = new MicroCore("config/configuration.conf");
		$this->assertNotNull($this->mc, "Create MicroCore");
	}
	
	function testGetConfigItem(){
		$res = $this->mc->getConfigItem("ezpdo.dbLib");
		$this->assertEqual("adodb", $res, "Get Config Item");
	}
	
	function testSetConfigItem(){
		$this->mc->setConfigItem("Test", "Test");
		$res = $this->mc->getConfigItem("Test");
		$this->assertEqual("Test", $res, "Set Config Item");
	}
	
	function testRemoveConfigItem(){
		$this->mc->removeConfigItem("Test");
		$result = false;
		try{
			$res = $this->mc->getConfigItem("Test");
		}catch(Exception $e){
			$result = true;
		}
		$this->assertTrue($result, "Remove Config Item");
	}
	
	function testGetConnection(){
		$con = $this->mc->getConnection();
		$this->assertNotNull($con,"EZPDO Connection");
	}
	
	function testStoreObject(){
		$con = $this->mc->getConnection();
		$cav = $con->create("Caveat");
		$cav->Name = "CAVEAT1";
		$this->assertTrue($con->commit($cav), "Store Data Object");
	}
	
	function testGetStoredObject(){
		$con = $this->mc->getConnection();
		$res = $con->find("from Caveat where Name=?","CAVEAT1");
		if(count($res) > 0){
			$res = $res[0];
		}
		$this->assertEqual("CAVEAT1", $res->Name, "Get Data Object");
	}
	
	function testDelStoredObject(){
		$con = $this->mc->getConnection();
		$res = $con->find("from Caveat where Name=?","CAVEAT1");
		if(count($res) > 0){
			$res = $res[0];
		}
		$this->assertTrue($con->delete($res),"Delete Stored Object");
	}
}

?>