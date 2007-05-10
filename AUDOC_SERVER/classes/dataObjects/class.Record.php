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
 * The record object is the central object of AuDoc and represents
 * a single record within the system
 * @package    AuDoc.DataObjects
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */
class Record
    extends absAudocBase
{
    // --- ATTRIBUTES ---

    /**
     * @orm char(128) index(idx_recTitle)
     *
     * @access public
     * @var String
     */
    public $Title = null;

    /**
     * @orm has one Classification
     *
     * @access public
     * @var Classification
     */
    public $Classification = null;

    /**
     * @orm date
     *
     * @access public
     * @var Date
     */
    public $DateCreated = null;

    /**
     * @orm date
     *
     * @access public
     * @var Date
     */
    public $DateRegistered = null;

    /**
     * @orm date
     *
     * @access public
     * @var Date
     */
    public $LastModified = null;

    /**
     * @orm date
     *
     * @access public
     * @var Date
     */
    public $DateClosed = null;
    
    /**
     * @orm date
     * 
     * @access public
     * @var Date
     */
    public $DateReview = null;

    /**
     * @orm char(128)
     *
     * @access public
     * @var String
     */
    public $Owner = null;

    /**
     * @orm char(128)
     *
     * @access public
     * @var String
     */
    public $Author = null;

    /**
     * @orm clob(65000)
     *
     * @access public
     * @var String
     */
    public $Notes = null;

    /**
     * @orm integer(2)
     *
     * @access public
     * @var int
     */
    public $Status = 0;

    /**
     * @orm composed_of many Document
     *
     * @access public
     * @var Document
     */
    public $Documents = null;

    /**
     * @orm composed_of many UDF
     *
     * @access public
     * @var UDF
     */
    public $UDFs = null;

    /**
     * @orm has one RecordType
     *
     * @access public
     * @var RecordType
     */
    public $RecordType = null;

    /**
     * @orm has one SecLevel
     * added for efficient searching within users security
     *
     * @access public
     * @var SecLevel
     */
    public $SecLevel = null;

    /**
     * @orm has many Caveat
     * added for efficient searching within users security
     *
     * @access public
     * @var Caveat
     */
    public $Caveats = null;

    /**
     * @orm char(128) unique(idx_recNum)
     *
     * @access public
     * @var String
     */
    public $RecordNumber = null;

    /**
     * @orm integer
     *
     * @access public
     * @var int
     */
    public $LatestRev = 0;

    /**
     * @orm has one User
     *
     * @access public
     * @var User
     */
    public $CheckedOutTo = null;
    
    /**
     * @orm date
     * 
     * @access public
     * @var Date
     */
    public $CheckedOutDate = null;
    

    // --- OPERATIONS ---

    /**
     * returns the records properties as
     * an array
     *
     * @access public
     * @author firstname and lastname of author, <author@example.org>
     * @return void
     */
    public function toArray()
    {
        $ret = array();
		$ret['uuid'] = $this->uuid;
		$ret['Title'] = $this->Title;
		$ret['Status'] = $this->Status;
		$ret['Owner'] = $this->Owner;
		if (isset($this->RecordType)){
			$ret['RecordType'] = $this->RecordType->uuid;
		}
		$ret['Notes'] = $this->Notes;
		if (isset($this->SecLevel)){
			$ret['SecLevel'] = $this->SecLevel->toArray();
		}
		$ret['DateCreated'] = date("c",$this->DateCreated);
		$ret['Author'] = $this->Author;
		$ret['DateClosed'] = $this->DateClosed;
		$ret['DateReview'] = $this->DateReview;
		$ret['RecordNumber'] = $this->RecordNumber;
		if(isset($this->CheckedOutTo)){
			$ret['CheckedOutTo'] = $this->CheckedOutTo->UserName;
			$ret['CheckedOutDate'] = date("c", $this->CheckedOutDate);
		}else{
			$ret['CheckedOutTo'] = "";
			$ret['CheckedOutDate'] = "";
		}
		$ret['LastModified'] = date("c", $this->LastModified);
		$ret['DateRegistered'] = date("c", $this->DateRegistered);
		if (isset($this->Classification)){
			$ret['Classification'] = $this->Classification->uuid;
		}
		$ret['ClassPath'] = $this->getFullClassification();
		$ret['Caveats'] = array();
		foreach($this->Caveats as $cav){
			$ret['Caveats'][] = $cav->toArray();
		}
		$ret['UDFs'] = array();
		foreach($this->UDFs as $udf){
			$ret['UDFs'][] = $udf->toArray();
		}
		if(isset($this->Documents)){
			$ret['Documents'] = count($this->Documents);
		}else{
			$ret['Documents'] = 0;
		}
		return $ret;
    }

    /**
     * Short description of method __constuct
     *
     * @access public
     * @author firstname and lastname of author, <author@example.org>
     * @return void
     */
    public function __constuct()
    {
        parent::__construct();
    }

    /**
     * Returns the full record title
     * including its classification path
     *
     * @access public
     * @return string
     */
    public function getFullTitle()
    {
        $returnValue = $this->Classification->getClassification() . " - " . $this->Title;
        return $returnValue;
    }
    
    /**
     * Returns the full classification path
     * of the Record
     *
     * @access public
     * @return string
     */
    public function getFullClassification(){
    	$ret = "";
    	if(isset($this->Classification)){
    		$ret = $this->Classification->getClassification();
    	}
    	return $ret;
    }

} /* end of class Record */

?>