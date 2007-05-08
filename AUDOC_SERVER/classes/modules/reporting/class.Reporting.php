<?php
/* +----------------------------------------------------------------------+
 * | AuDoc 2                                                              |
 * +----------------------------------------------------------------------+
 * | Copyright (c) 2004-2007 Audata Ltd                                   |
 * +----------------------------------------------------------------------+
 * | This source file is subject to version 2 of the Gnu Public License,  |
 * | that is bundled with this package in the file License.txt, and is    |
 * | available at through the world-wide-web at                           |
 * | http://www.gnu.org/licenses/gpl.txt.                                 |
 * | If you did not receive a copy of the GPL license and are unable to   |
 * | obtain it through the world-wide-web, please send a note to          |
 * | support@audata.co.uk so we can mail you a copy immediately.          |
 * +----------------------------------------------------------------------+
 * | Authors: Jonathan Moss <jon.moss@audata.co.uk>                       |
 * +----------------------------------------------------------------------+
 *
 * $Id: $
 */
 
 /**
 * This file contains the entry class for the reporting modules which
 * handles report generation
 *
 * @package    AuDoc.Modules
 * @author	   Jonathan Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   
 * @version    $Id: $
 */
class Reporting implements iModule{
	
	// --- ATTRIBUTES ---

    /**
     * A reference back to the MicroCore
     *
     * @access private
     * @var MicroCore
     */
    private $microCore = null;
	
	
	private $methodReg = array(
								'genReport' => MicroCore::PERM_LOGIN,
								'getReports' => MicroCore::PERM_LOGIN,
								'addReport' => MicroCore::PERM_BOTH,
								'modReport' => MicroCore::PERM_BOTH,
								'delReport' => MicroCore::PERM_BOTH
							   );
							   
	private $docstorePath = "docstore";
	
    // --- OPERATIONS ---
    
	/**
     * Constructor
     *
     * @access public
     */
	public function __construct(){}
	
	/**
	 * provides access to the MicroCore object
	 * 
	 * @param MicroCore $mc A reference to the active MicroCore object
	 */
	public function setParent(MicroCore $mc){
		$this->microCore = $mc;
		try{
			$this->docstorePath = $mc->getConfigItem("docstore.location");
		}catch(Exception $e){}
	}
	
	/**
	 * genReport
	 */
	public function execute($method, $params){
		$ret = null;
		switch($method){
			case 'genReport':
				if(count($params) == 1){
					$ret = $this->createReport($params[0]);
				}else{
					throw new Exception("The method Reporting.genReport requires more parameters");
				}
				break;
			case 'getReports':
				$ret = $this->getReports();
				break;
			case 'addReport':
				if(count($params) == 3){
					$ret = $this->addReport($params[0], $params[1], $params[2]);
				}else{
					throw new Exception("The method Reporting.addReport requires more parameters");
				}
				break;
			case 'modReport':
				if(count($params) == 4){
					$ret = $this->modReport($params[0], $params[1], $params[2], $params[3]);
				}else{
					throw new Exception("The method Reporting.modReport requires more parameters");
				}
				break;
			case 'delReport':
				if(count($params) == 1){
					$ret = $this->delReport($params[0]);
				}else{
					throw new Exception("The method Reporting.delReport requires more parameters");
				}
				break;
			default:
				throw new Exception("The module Reporting does not support the method: " . $method);
		}
		return $ret;
	}
	
		/**
	  	* this function creates a report
	  	* call a createPDF method wich takes 
	  	* a template,a record array 
	  	* and an ouput pdf file and generates 
	  	* a pdf file
	  	*/
		private function createReport($uuid){
			
			//retrieve the report settings rom the DB
			$con = $this->microCore->getConnection();
			$report = $con->find("from Report where uuid=?", $uuid);
			if (count($report) != 1){
				throw new Exception("Specified report does not exist");
			}
			$report = $report[0];
			$title = $report->Name;
			$template=$report->Template;
			
			//get the records
			$param = array($report->SearchString);
			$records = $this->microCore->callModulefunc("Search", "rawsearch", $param);
			$gen = new GenerateReport($title, $this->docstorePath . "/reporting/" . $template);
			$report->Output = $gen->generate($records);
			$this->microCore->callModuleFunc("logging", "addItem", array("Report ($report->Name) generated",Logging::VERBOSITY_NORMAL));
			return $report;
			
		}
	
	/**
	 * returns an array of all reports
	 */
	 private function getReports(){
		 $con = $this->microCore->getConnection();
		 $reports = $con->get('Report');
		 $this->microCore->callModuleFunc("logging", "addItem", array("Reports retrieved",Logging::VERBOSITY_HIGH));
		 return $reports;
	 }
	
	/**
	 * returns true only if the method supplied is 
	 * permitted
	 */ 
	public function isPermitted($method, $state){
		$ret = false;
		if (isset($this->methodReg[$method])){
			if ($state >= $this->methodReg[$method]){
				$ret = true;
			}
		}
		return $ret;
	}
	
	 
	 private function addReport($title, $template, $criteria){
	 	$con = $this->microCore->getConnection();
	 	$report = $con->create("Report");
	 	$report->Name = $title;
	 	$report->Template = $template;
	 	$report->SearchString = $criteria;
	 	$con->commit($report);
	 	return $report;
	 }
	 
	 private function modReport($uuid, $title, $template, $criteria){
	 	$con = $this->microCore->getConnection();
	 	
	 	$report = $con->find("from Report where uuid=?", $uuid);
	 	if(count($report) != 1){
	 		throw new Exception("The specified report does not exist");
	 	}
	 	$report = $report[0];
	 	$report->Name = $title;
	 	$report->Template = $template;
	 	$report->SearchString = $criteria;
	 	$con->commit($report);
	 	return $report;
	 }
	 
	 private function delReport($uuid){
	 	$con = $this->microCore->getConnection();
	 	$report = $con->find("from Report where uuid=?", $uuid);
	 	if(count($report) != 1){
	 		throw new Exception("The specified report does not exist");
	 	}
	 	$report = $report[0];
	 	$con->delete($report);
	 }
}
 
 ?>