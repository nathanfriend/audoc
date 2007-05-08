<?php

class UDFTest extends UnitTestCase{
	
	private $mc;
	private $con;
	private $udf;
	
	public function __construct(){
		$this->_label = "UDF Test";
		$this->mc = new MicroCore("config/configuration.conf");
		$this->con = $this->mc->getConnection();
	}
	
	public function testCreate(){
		$params = array();
		$params[0] = "Licence";
		$params[1] = 3;
		$this->udf = $this->mc->callModuleFunc("recman","addUDF", $params);
		$this->assertNotNull($this->udf, "Create UDF Test");
	}
}

?>