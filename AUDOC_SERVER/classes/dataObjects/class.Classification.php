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
 * A single class within the classification
 * @package    AuDoc.DataObjects
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */


class Classification
    extends absAudocBase
{
    // --- ATTRIBUTES ---

    /**
     * @orm char(128) index(idx_classValue)
     *
     * @access public
     * @var String
     */
    public $Value = null;

    /**
     * @orm has one Classification inverse(Children)
     *
     * @access public
     * @var Classification
     */
    public $Parent = null;

    /**
     * @orm has many Classification inverse(Parent)
     *
     * @access public
     * @var Classification
     */
    public $Children = null;

    /**
     * @orm integer
     *
     * @access public
     * @var int
     */
    public $Retention = 0;

    /**
     * @orm has one SecLevel
     *
     * @access public
     * @var SecLevel
     */
    public $SecLevel = null;

    /**
     * @orm has many Caveat
     *
     * @access public
     * @var Caveat
     */
    public $Caveats = null;

    // --- OPERATIONS ---

    /**
     * Return the objects properties as an array
     *
     * @access public
     * @author Jon Moss, <jon.moss@audata.co.uk>
     * @return associative array of object properties
     */
    public function toArray()
    {
        $ret = array();
		$ret['uuid'] = $this->uuid;
		$ret['Value'] = $this->Value;
		$ret['Retention'] = $this->Retention;
		if (isset($this->SecLevel)){
			$ret['SecLevel'] = $this->SecLevel->toArray();
		}
		$ret['Caveats'] = array();
		foreach($this->Caveats as $Caveat){
			$ret['Caveats'][] = $Caveat->toArray();
		}
		if (isset($this->Parent)){
			$ret['Parent'] = $this->Parent->uuid;
		}
		if (count($this->Children) > 0){
			$ret['HasChildren'] = true;
		}else{
			$ret['HasChildren'] = false;
		}
		$ret['Path'] = $this->getClassification();
		
		return $ret;

    }

    /**
     * Object constructor
     *
     * @access public
     * @author Jon Moss, <jon.moss@audata.co.uk>
     * @return void
     */
    public function __construct()
    {
        parent::__construct();
    }

    /**
     * Returns the class path
     *
     * @access public
     * @author Jon Moss, <jon.moss@audata.co.uk>
     * @return String
     */
    public function getClassification()
    {
        $returnValue = null;
        if (isset($this->Parent)){
			$returnValue = $this->Parent->getClassification() . " - " . $this->Value;
        }else{
			$returnValue = $this->Value;
		}
        return $returnValue;
    }

} /* end of class Classification */

?>