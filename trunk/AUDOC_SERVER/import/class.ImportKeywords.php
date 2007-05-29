<?php
/**
 * Description
 *
 * @author		Jon Moss <jon.moss@audata.co.uk>
 * @copyright	2005 Audata Limited   
 * @version		CVS: $Id: $
 */
class ImportKeywords
{
	
	private $mc;
	private $connection;
	private $hierarchy;
	
	private $logfile;
	private $errors;
	private $rows = 0;
	private $entries = 0;
	
	private $hierarchies = array();
	private $root;
	
	public function __construct($mc){
		$this->mc = $mc;
		$this->connection = $this->mc->getConnection();
	}
	
	public function importCSV($path){
		if(!file_exists($path)){
			die("File not found: $path/n");
		}
		$this->logfile = fopen('import_errors.log', 'w');
		$file = file($path);		
		$h = $this->connection->find("From KeywordHierarchy Where Name=?", trim($file[0]));
		if(count($h) != 0){
			$this->hierarchy = $h[0];
		}
		if($this->hierarchy == null){
			fwrite($this->logfile,"The Hierarchy {$file[0]} was not found");
			die("The Hierarchy {$file[0]} was not found");
		}
		echo "Importing Keywords for {$file[0]}\n";
		for($i=1;$i<count($file);$i++){
			$line = $file[$i];
			$this->rows++;
			$this->createEntry($line);
		}
		echo "Rows Processed: " . $this->rows . "\n";
		echo "Entries Made: " . $this->entries . "\n";
	}
	
	private function createEntry($line){
		$entries = explode("-", $line);
		for($i=0;$i<count($entries);$i++){
			$entries[$i] = trim($entries[$i]);
		}
		$curPath = "";
		$parent = null;
		foreach($entries as $entry){
			$curPath = $curPath . " - " . $entry;
			if(substr($curPath, 0, 3) == " - "){
				$curPath = substr($curPath, 3);
			}
			if(isset($this->hierarchies[$curPath])){
				$parent = $this->hierarchies[$curPath];
			}else{
				$e = $this->mc->callModuleFunc("recman","addKWData",array($this->hierarchy->uuid, $parent, $entry));
				$parent = $e->uuid;
				$this->hierarchies[$curPath] = $e->uuid;
				$this->entries++;
				echo "Entry added: $curPath \n";
			}
		}
	}
}
?>
