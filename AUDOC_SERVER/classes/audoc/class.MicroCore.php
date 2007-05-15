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
 
//Ensure session support is started unless we are running in cli
if(!isset($_SESSION) && !PHP_SAPI == "cli"){
	session_start();
}
 
/**
 * The MicroCore class provides a light weight kernel
 * to the AuDoc system by providing module and configuration
 * management functions
 *
 * @package    AuDoc.MicroCore
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */
class MicroCore
{
    // --- ATTRIBUTES ---

	/**
	 * Permission constant for a user that has not
	 * logged in and is not an admin
	 */
	const PERM_NONE = 0;
	
	/**
	 * Permission constant for a user that has
	 * logged in but is not an admin
	 */
	const PERM_LOGIN = 2;
	
	/**
	 * Permission constant for a user that has not
	 * logged in but is an admin (should never really happen
	 * by itself). Used mostly for addition
	 */
	const PERM_ADMIN = 4;
	
	/**
	 * Permission constant for a user that has
	 * logged in and is an admin
	 */
	const PERM_BOTH = 6;

    /**
     * An associative array of loaded modules
     *
     * @access private
     * @var iModule
     */
    private $modules = array();

    /**
     * The path to the configuration file
     *
     * @access private
     * @var config_path
     */
    private $config_path = null;
	
	/**
	 * The parsed configuration items
	 * 
	 * @access private
	 * @var config_items
	 */
	private $config_items = null;

    /**
     * A reference to the database management object
     *
     * @access public
     * @var DBMan
     */
   // public $DBMan = null;

	/**
	 * Contains a reference to the Ezpdo manager
	 * 
	 * @access private
	 * @var ezpdoManager
	 */
	 private $ezpdoManager;
	 
    /**
     * A reference to the module management object
     *
     * @access public
     * @var ModuleMan
     */
    public $ModuleMan = null;

	/**
	 * The EZOQL statement that returns a modules
	 * entry class
	 * 
	 * @access private
	 * @var EntryClassStatement
	 */
	private $EntryClassStatement = "FROM Module WHERE Name = ?";
	
    // --- OPERATIONS ---

    /**
     * Constructor
     * A valid path to the configuration file must be provided
     * @access public
     * @param String
     * @return MicroCore
     */
    public function __construct($configPath)
    {
		//load configuration
		$this->config_path = $configPath;
		$this->parseConfig();
		
		//have to include this rather than rely on SmartLoader to avoid errors
		include_once(dirname(__FILE__) . '/../includes/ezpdo/ezpdo_runtime.php');
		
		//Create the db connection
		$this->ezpdoManager = epManager::instance();
		$this->ezpdoManager->setConfig($this->getEZPDOConfig());
    }
	
	/**
	 * Generates EZPDO configuration file
	 * @access private
	 * @return void
	 */
	private function getEZPDOConfig(){
		//Setup defaults
		$cnfg = array(
			'source_dirs' => "./classes/dataObjects",
			'default_dsn' => "sqlite://audoc2.db",
			'recursive' => "true",
			'compiled_dir' => "cache",
			'compiled_file' => "data_objects.ezpdo",
			'backup_compiled' => "true",
			'default_oid_column' => "e_oid",
			'split_relation_table' => "true",
			'auto_compile' => "true",
			'auto_flush' => "true",
			'db_lib' => "adodb",
			'log_console' => "false",
			'log_file' => "cache/ezpdo_log.txt",
			'autoload' => "false"
		);
		
		//Attempt to get settings from config
		//If failed ignore and use default
		
		try{
			$cnfg['default_dsn'] =	$this->getConfigItem("ezpdo.dsn");
		}catch(Exception $e){}
		try{
			$cnfg['backup_compiled'] =	$this->getConfigItem("ezpdo.backup");
		}catch(Exception $e){}
		try{
			$cnfg['split_relation_table'] =	$this->getConfigItem("ezpdo.splitRelation");
		}catch(Exception $e){}
		try{
			$cnfg['auto_compile'] =	$this->getConfigItem("ezpdo.autoCompile");
		}catch(Exception $e){}
		try{
			$cnfg['auto_flush'] = $this->getConfigItem("ezpdo.autoFlush");
		}catch(Exception $e){}
		try{
			$cnfg['db_lib'] = $this->getConfigItem("ezpdo.dbLib");
		}catch(Exception $e){}
		try{
			$cnfg['log_console'] =	$this->getConfigItem("ezpdo.logCon");
		}catch(Exception $e){}
		try{
			$cnfg['log_file'] =	$this->getConfigItem("ezpdo.logFile");
		}catch(Exception $e){}
		return $cnfg;
	}

    /**
     * Returns a reference to the specified module
     *
     * @access public
     * @param String
     * @return iModule
     */
    private function getModule($name)
    {
		$returnValue = null;
		$name = strtolower($name);
		//has the module already been loaded
		if (isset($this->modules[$name])){
			//yes, just send reference to existing module
			$returnValue = $this->modules[$name];
		}else{
			//no, then get instantiation details from database and create it
			try{
				$entryClass = $this->getModEntryClass($name);
				//$returnValue = eval('new ' . $entryClass . '($this);');
				$returnValue = new $entryClass;
				$returnValue->setParent($this);
			}catch(Exception $e){
				die("Fatal Error: A required module (".$name.") is not correctly registered in the database\n" . $e);
			}
			//remember to add it to the list of module so we don't have to load it again!
			$this->modules[$name] = $returnValue;
		}
        return $returnValue;
    }
	
	/**
     * parseConfig opens the config file and parses it into
     * the configItem array
     *
     * @access private
     * @return void
     */
    private function parseConfig()
    {
		if (($this->config_path != null) && (file_exists($this->config_path))){
			$items = file($this->config_path);
			foreach($items as $item){
				$item = trim($item);
				//check this is not a blank line or comment "//"
				if(($item != "") && (substr($item, 0, 2) != "//")){
					$split = explode("=", $item,2);
					$value = substr($split[1],1,-1);
					$this->config_items[$split[0]] = $value;
				}
			}
		}else{
			throw new Exception("Could not open the configuration file: " . $this->config_path);
		}
	}
	
	/**
     * saveConfig saves any changes to the config items
     * back to the config file
     *
     * @access private
     * @return void
     */
    private function saveConfig()
    {
		if (($this->config_path != null) && (file_exists($this->config_path))){
			$file = fopen($this->config_path, "w");
			foreach($this->config_items as $key=>$value){
				fwrite($file, $key . "=\"" . $value . "\"\n");
			}
			fclose($file);
		}
    }
	
	/**
	 * Calls a method within a function if permissions
	 * allow. Throws and exception if permission is
	 * denied.
	 */
	public function callModuleFunc($name, $method, $params){
		$ret = null;
		$name = strtolower($name);
		$mod = $this->getModule($name);
		
		//check it really is a module
		if (!($mod instanceof iModule)){
			throw new Exception("$name is not a valid module (iModule not implemented)");
		}
		
		//determine users current permission level
		$perm = 0;
		if (isset($_SESSION['Username'])){
			$perm = MicroCore::PERM_LOGIN;
		}
		if ($perm == MicroCore::PERM_LOGIN){
			if ($_SESSION['isAdmin']){
				$perm += MicroCore::PERM_ADMIN;
			}
		}
		//make sure the user has the permission to perform the task
		
		if ($mod->isPermitted($method, $perm)){
			$ret = $mod->execute($method, $params);
		}else{
			throw new Exception('Permission Denied user did not have sufficient access to the method ' . $method);
		}
		return $ret;
	}
	
	/**
	 * notifies all listeners of a specific type
	 * that an event has occured and the number
	 * of the object modified
	 * STUB: provide listener system
	 */
//	function notifyListeners($event, $objType, $obId){
//		$conn = $this->DBMan->getConnection();
//		$listeners = $conn->find("FROM Listeners as l WHERE l.Type = ?", $event);
//		foreach($listeners as $listener){
//			$mod = $this->getModule($listener->Module->Name);
//			$mod->onEvent($event, $objType, $obId);
//		}
//	}
	

	
	/**
     * Returns the ezpdoManager object
     *
     * @access public
     * @return ezpdoManager
     */
    public function getConnection()
    {
       return $this->ezpdoManager;
    }
	
	/**
     * getConfigItem returns the value of the specified
     * key or throws an exception if it is not found
     *
     * @access public
     * @param String
     * @return String
     */
    public function getConfigItem($key)
    {
        $returnValue = null;
		if (array_key_exists($key,$this->config_items)){
			$returnValue = $this->config_items[$key];
		}else{
			throw new Exception("The configuration item " . $key . " was not found");
		}
        return $returnValue;
    }

    /**
     * setConfigItem is used to set the value of the specified key
     *
     * @access public
     * @param String
     * @param String
     * @return void
     */
    public function setConfigItem( $key, $value)
    {
		$this->config_items[$key] = $value;
		$this->saveConfig();
    }
	/**
	 * Removes the specified configuration entry
	 * from the config file
	 * 
	 * @access public
	 * @param string $key The key to be removed
	 */
	public function removeConfigItem( $key){
		if (array_key_exists($key,$this->config_items)){
			unset($this->config_items[$key]);
		}
		$this->saveConfig();
	}
	
	/**
     * returns the name of the entry class of a module.
     * If the module is not registered in the DB an exception
     * is thrown
     *
     * @access private
     * @param String
     */
    private function getModEntryClass($name)
    {
		$ret = null;
		$con = $this->getConnection();
		$results = $con->find($this->EntryClassStatement,$name);
		if (count($results) > 0){
			$ret = $results[0]->EntryClass;
		}else{
			throw new Exception("ZenError #1\nAn error occured\nmodule requested not found\nadmin should be told\ndetails(module name: ".$name.")");
		}
		return $ret;
	}

} /* end of class MicroCore */

?>