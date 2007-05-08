<?php
/**
 * Test the audoc.search.Search class
**/
//include_once("classes/dataObjects/class.Caveat.php");
//require_once("class.SmartLoader.php");

class SearchTest extends UnitTestCase{

	private $mc;

	public function __construct(){
		$this->_label = "Search Test";
		$this->mc = new MicroCore("config/configuration.conf");
		$this->con = $this->mc->getConnection();
		
		//register the Search Module
		$smod = $this->con->create('Module');
		$smod->Name = "search";
		$smod->Version = 1.0;
		$smod->Description = "search module";
		$smod->EntryClass = "Search";
		$this->con->commit($smod);
		
		//set session for user
		$_SESSION['username'] = "admin";
		$_SESSION['isAdmin'] = true;
	}
	
	/*
	function testExecuteSearch (){
		$params = array();
		$params['criteria'] = "from record where Title = \" title1\" ";
		$search = $this->mc->callModuleFunc("search", "search", $params);
		$this->assertNotNull($search, "Search passed");
	}
	**/
	function testExecuteQuickSearch (){
		$params = array();
		$params['criteria'] = "title1 title2 notes1";
		$qsearch = $this->mc->callModuleFunc("search", "quickSearch", $params);
		$this->assertNotNull($qsearch, "QuickSearch passed");
	}
	/*
	function testExecuteComplexSearch (){
		$params = array();
		$params['criteria'] = "Title = \" title2\"";
		$csearch = $this->mc->callModuleFunc("search", "complexSearch", $params);
		$this->assertNotNull($csearch, "ComplexSearch passed");
	}
	*/
}
?>