<?php
/**
 * Test the audoc.docstore.DocStore class
**/

class DocStoreTest extends UnitTestCase{

	private $mc;

	public function __construct(){
		$this->_label = "DocStore Test";
		$this->mc = new MicroCore("config/configuration.conf");
		$this->con = $this->mc->getConnection();
		
		//register the DocStore Module
		$dmod = $this->con->create('Module');
		$dmod->Name = "docstore";
		$dmod->Version = 1.0;
		$dmod->Description = "docstore module";
		$dmod->EntryClass = "DocStore";
		$this->con->commit($dmod);
		
		//set session for user
		$_SESSION['username'] = "jonm";
		$_SESSION['isAdmin'] = true;
	}
/*
	function testExecuteStoreDoc (){
		$params = array();
		$params['recid'] = "";
		$params['path'] = "";
		
		$store = $this->mc->callModuleFunc("Docstore", "storeDoc", $params);
		$this->assertNotNull($store, "addUser passed");
	}
	*/
}
?>