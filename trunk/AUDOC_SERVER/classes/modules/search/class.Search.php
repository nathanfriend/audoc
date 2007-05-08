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
 * This file contains the entry class for the search modules which
 * handles record search operations
 *
 * @package    AuDoc.Modules
 * @author	   Jonathan Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   
 * @version    $Id: $
 */
class Search implements iModule{
	
	// --- ATTRIBUTES ---

    /**
     * A reference back to the MicroCore
     *
     * @access private
     * @var MicroCore
     */
    private $microCore = null;
	
	
	private $methodReg = array(
								'rawsearch' => MicroCore::PERM_LOGIN,
								'quicksearch' => MicroCore::PERM_LOGIN,
								'complexsearch' => MicroCore::PERM_LOGIN,
								'savesearch' => MicroCore::PERM_LOGIN,
								'getsavedsearches' => MicroCore::PERM_LOGIN,
								'runsavedsearch' => MicroCore::PERM_LOGIN,
								'delsavedsearch' => MicroCore::PERM_LOGIN
							   );
							   
	private $caveats = array();
	
	private $parser;
	
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
	
	/**
	 * Called by the microcore when one of this modules
	 * function is required
	 */
	public function execute($method, $params){
		$ret = null;
		$method = strtolower($method);
		switch($method){
			case 'rawsearch':
				if(count($params) == 1){
					$ret = $this->rawsearch($params[0]);
				}else{
					throw new Exception("The method Search.rawSearch requires more parameters");
				}
				break;
			case 'quicksearch':
				if(count($params) == 1){
					$ret = $this->quickSearch($params[0]);
				}else{
					throw new Exception("The method Search.quicksearch requires more parameters");
				}
				break;
			case 'complexsearch':
				if(count($params) == 1){
					$ret = $this->complexSearch($params[0]);
				}else{
					throw new Exception("The method Search.complexSearch requires more parameters");
				}
				break;
			case 'savesearch':
				if (count($params) == 3){
					$ret = $this->saveSearch($params[0], $params[1], $params[2]);
				}else{
					throw new Exception("The method Search.saveSearch Search requires 3 parameter");
				}
				break;
			case 'getsavedsearches':
				$ret = $this->getSavedSearches();
				break;
			case 'runsavedsearch':
				if(count($params) == 1){
					$ret = $this->runSavedSearch($params[0]);
				}else{
					throw new Exception("The method Search.runSaved Search requires one parameter");
				}
				break;
			case 'delsavedsearch':
				if(count($params) == 1){
					$ret = $this->delSavedSearch($params[0]);
				}else{
					throw new Exception("The method Search.delSaved Search requires one parameter");
				}
				break;
			default:
				throw new Exception("The module Search does not support the method: " . $method);
		}
		return $ret;
	}
	
	/**
  	* Central function of the Search module.
  	* All other functions call this one to perform the actual search
  	* This function also added security level and caveat contraints
  	*/
	private function rawsearch($query){
		//add user security level constraints
	
		$query .=  " AND SecLevel.Level <= " . $_SESSION['SecLevel'];
		//get current caveats
		$caveats = $this->microCore->callModuleFunc("Security", "getCaveats", null);
		if(count($caveats) > 0){
			foreach($caveats as $caveat){
				$this->caveats[] = $caveat->Name;
			}
		}else{
			$this->caveats = array();
		}
		//extract caveats that the user does not have
		$notPresentCavs = array_diff($this->caveats, $_SESSION['Caveats']);
		
		//add caveat contraints
		if(count($notPresentCavs) != 0){
			$query .= " AND (( Caveats.contains(c) AND (";
			foreach($notPresentCavs as $caveat){
				//todo: check one2many search syntax
				$query .= " c.Name != \"$caveat\" AND";
			}
			if (substr($query,-3,3) == "AND"){
				$query = substr($query,0,-3) . ")) OR c IS NULL)";
			}
		}
		//throw new Exception("QUERY: " . $query);
		
		//add query to logs if we are using the paranoid logging setting
		$this->microCore->callModuleFunc("Logging", "addItem", array("quick search using: $query", Logging::VERBOSITY_HIGH));
		
		//perform the search
		$con = $this->microCore->getConnection();
		$records = $con->find($query);
		return $records;
	}
	
	/**
	 * Performs a quick search of a list of words over
	 * a fixed number of fields:
	 * Title
	 * Notes
	 */
	private function quickSearch($searchString){
		//split out words
		$words = explode(" ", $searchString);
		$query = "from Record where ";
		foreach($words as $word){
			trim($word);
			if (strlen($word) > 0){
				$query .= "(Title like \"%$word%\" OR Notes like \"%$word%\") AND";
			}
		}
		//strip last and
		$query = substr($query,0,-4);
		$ret = $this->rawsearch($query);
		//if ($ret){
			return $ret;
		//}
		//throw new Exception("Invalid record search syntax");
	}
	
	/**
	 * Performs more complex searches
	 */
	 private function complexSearch($criteria){
		 //parse $criteria
		 if (!isset($this->parser)){
			$this->parser = new Parser($this->microCore);
		 }
		 $query = $this->parser->parse($criteria);
		 $results = $this->rawsearch($query);
		 return $results;
		 //search with constructed query string
	 }
	 
	 /**
	  * Saves the criteria of a search
	  */
	  private function saveSearch($title, $description, $criteria){
		  $con = $this->microCore->getConnection();
		  $owner = $con->find("from User where UserName=?",$_SESSION['Username']);
		  $owner = $owner[0];
		  $crit = "";
		  foreach($criteria as $c){
		  	$crit .= $c . "|";
		  }
		  $crit = substr($crit,0,-1);
		  $sSearch = $con->create("SavedSearch");
		  $sSearch->Title = $title;
		  $sSearch->Description = $description;		 
		  $sSearch->Criteria = $crit;
		  $sSearch->Owner = $owner;
		  $con->commit($sSearch);
		  return $sSearch;
	  }
	  
	  /**
	   * Returns a list of a users saved Searches
	   */
	  private function getSavedSearches(){
		  $con = $this->microCore->getConnection();
		  $sS = $con->find('from SavedSearch where Owner.UserName=?', $_SESSION['Username']);
		  return $sS;
	  }
	  
	  /**
	   * Runs the specified saved search
	   */
	   private function runSavedSearch($uuid){
		   $con = $this->microCore->getConnection();
		   $sS = $con->find('from SavedSearch where uuid=?',$uuid);
		   if(count($sS) < 1){
			   throw new Exception("The specified SavedSearch does not exit");
		   }
		   $sS = $sS[0];
		   if(($sS->Owner->UserName != $_SESSION['Username']) && (!$_SESSION['isAdmin'])){
			   throw new Exception("The Saved Search specified does not belong to you and you are not an administrator");
		   }
		   $criteria = array();
		   $token = strtok($sS->Criteria, "|");
		   while($token !== false){
		   		$criteria[] = $token;
		   		$token = strtok("|");
		   }
		   return $this->complexsearch($criteria);
	   }
	   
	   /**
	    * Deletes a saved search from the system
	    */
	   private function delSavedSearch($uuid){
		    $con = $this->microCore->getConnection();
		   $sS = $con->find('from SavedSearch where uuid=?',$uuid);
		   if(count($sS) < 1){
			   throw new Exception("The specified SavedSearch does not exit");
		   }
		   $sS = $sS[0];
		   return $con->delete($sS);
	   }
}
 
 ?>