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
 *  This in the entry class to the Module Manager Module
 * @package    AuDoc.Modules
 * @authorKhalid Adem <khalid.adem@audata.co.uk>
 * @copyright  2005 Audata Limited   

 */
class ModuleManager implements iModule{
	
	// --- ATTRIBUTES ---

    /**
     * A reference back to the MicroCore
     *
     * @access private
     * @var MicroCore
     */
    private $microCore = null;
	
	private $connection=null;
	
	private $methodReg = array(
								'installModule' => MicroCore::PERM_BOTH,
								'removeModule' => MicroCore::PERM_BOTH
							  );
	
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
	 * 
	 */
	public function setParent(MicroCore $mc){
		$this->microCore = $mc;
		$this->connection = $this->microCore->getConnection();
	}
	
	/**
	 * Standard Execute function to provide entry to the modules
	 * function
	 * @param string $method The name of the method to call
	 * @param array $params An array of key value pairs used as parameters to the method called
	 */
	public function execute($method, $params){
		$ret = null;
		$method = strtolower($method);
		switch($method){
			case 'installmodule':
				if(count($params) == 1){
					$ret = $this->installModule($params[0]);
				}else{
					throw new Exception("The method Module.installModule requires more parameters");
				}
				break;
		   case 'removemodule':
				if(count($params) == 1){
					$ret = $this->removeModule($params[0]);
				}else{
					throw new Exception("The method Module.removeModule requires more parameters");
				}
				break;
			default:
				throw new Exception("The module Modulemanager does not support the method: " . $method);
		}
		return $ret;
	}
	

	
		/**
	  	* This function installs a module	  	
	  	*/
	private function installModule($path){
		
			//Parse the install.ini into an array
		    $Pini_array = $this->parseIni($path);
			//get module details
			$moduleName_ini = $Pini_array["details"]["name"];
			$version_ini=$Pini_array["details"]["version"];
			$entryClass_ini =$Pini_array["details"]["entryClass"];
			$description_ini=$Pini_array["details"]["description"];
			
			//get module config items
			$config = $Pini_array["config"];
			
			$mods = $connection->find("from Module where name=?", $moduleName_ini);
			$mod = null;
			//if none already present. Create new
			if (count($mod < 1)){
					$mod = $this->connection->create('Module');
			}else{
				//already installed but is it a new version?
				if ($version_ini > $mods[0]->Version){
					//$mod = $mods[0];
					//remove old module
					$this->removeModule($mods[0]->Name);
					$mod = $this->connection->create('Module');
				}else{
					//newest version already installed
					throw new Exception("Version installed is already up to date");
				}
			}
			//update or add module details
			$mod->Name = $moduleName_ini;
			$mod->Version = $version_ini;
			$mod->EntryClass = $entryClass_ini;
			$mod->Description = $description_ini;
			$connection->commit($mod);
			//update or add config items
			$this->addConfigItems($config);	
			//unzip the module
			$this->unZip($path);
			//delete the zipfile
			unlink($path);
			$this->microCore->callModuleFunc("logging", "addItem", array("$moduleName_ini module installed",Logging::VERBOSITY_NORMAL));
			return $mod;
		}	
	/**
	 * Takes an array of key value pairs and
	 * adds them to the config file
	 */
	private function addConfigItems($config){
		foreach($config as $key => $value){
			$this->microCore->setConfigItem($key,$value);
		}
	}		
					
	/**
	 * Removes a modules from the database
	 * TODO: clear the old files out.
	 * @param string $moduleName The name of the module to be removed
	 */
	private function removeModule($moduleName){
		$mods = $this->connection->find("from Module where Name=?", $moduleName);
		$mod = $mods[0];
		$this->microCore->callModuleFunc("logging", "addItem", array("$moduleName module removed",Logging::VERBOSITY_NORMAL));
		$this->connection->delete($mod);
		return null;
	}
	
	/**
	 * Extracts install.ini from the specified zipfile and
	 * returns it as an array
	 * @param string $zipfile Path to module zipfile
	 */		
	private function parseIni($zipfile){
			$file = "cache/" . md5(uniqid(rand(), true)) . "ini";
			$zip = zip_open($zipfile);
			$ini_array = null;
			if ($zip) {
				//read file entry from zip
				while ($zip_entry = zip_read($zip)) {
					//if able to open zip entry and name equals install.ini then process
					if ((zip_entry_open($zip, $zip_entry, "r"))
						&& (zip_entry_name($zip_entry) == "install.ini")) {
						
						//extract data from zip entry
						$data = zip_entry_read($zip_entry, zip_entry_filesize($zip_entry));
						if (!$file_handle = fopen($file,"w")) {
							throw new Exception("Cannot open module ini file");
						}   
						
						//write data to temp file
						if (!fwrite($file_handle, $data)) {
							throw new Exception("Cannot write module ini file");
						}   
						
						//echo "You have successfully written data to $file\n"; 
						fclose($file_handle);
						//parses the ini file into an array
						$ini_array = parse_ini_file($file,TRUE);
						//delete temp inifile
						unlink($file);
						zip_entry_close($zip_entry);
					}
				}
				zip_close($zip);
				return $ini_array;
		}else{
			throw new Exception("Could not open module install file");
		}
	}

	/**
	 * Extracts the contents of a module zip file
	 * (exluding install.ini)
	 * @param string $zipfile Path to the module zipfile
	 */
	public function unZip($zipfile){
		$archive = new PclZip($zipfile);
		$archive->extract(PCLZIP_OPT_BY_EREG, '[^install.ini]');
		/*
		 * todo: create ini with config items and zip file contents so the module 
		 * can be removed cleanly. Store using docstore.storedoc method.
		 */ 
	}

	/**
	 * Creates module content file
	 */
	 private function createContent($zipfile){
		 $archive = new PclZip($zipfile);	 
	 }
	 
	/**
	 * returns true only if the method supplied is 
	 * permitted
	 */ 
	public function isPermitted($method, $state){
		$ret = false;
		if (isset($this->methodReg[$method])){
			if ($state >= $this->MethodReg[$method]){
				$ret = true;
			}
		}
		return $ret;
	}
}
 
 ?>