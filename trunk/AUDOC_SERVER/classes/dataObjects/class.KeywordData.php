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
 * Represents a single keyword with a keyword hierarchy
 * @package    AuDoc.DataObjects
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */


class KeywordData
    extends absAudocBase
{
    // --- ATTRIBUTES ---

    /**
     * @orm char(128) index(idx_keyValue)
     *
     * @access public
     * @var String
     */
    public $Value = null;

    /**
     * @orm has one KeywordHierarchy
     *
     * @access public
     * @var KeywordHierarchy
     */
    public $Hierarchy = null;

    /**
     * @orm has one KeywordData inverse(Children)
     *
     * @access public
     * @var KeywordData
     */
    public $Parent = null;

    /**
     * @orm composed_of many KeywordData inverse(Parent)
     *
     * @access public
     * @var KeywordData
     */
    public $Children = null;

    // --- OPERATIONS ---

    /**
     * Object constructor
     *
     * @access public
     * @return void
     */
    public function __construct()
    {
        parent::__construct();
    }

    /**
     * Returns object properties as an associative array
     *
     * @access public
     * @return associative array
     */
    public function toArray()
    {
        $ret = array();
		$ret['uuid'] = $this->uuid;
		$ret['Value'] = $this->Value;
		if(isset($this->Parent)){
			$ret['Parent'] = $this->Parent->uuid;
		}else{
			$ret['Parent'] = null;
		}
		$ret['Hierarchy'] = $this->Hierarchy->uuid;
		if (count($this->Children) > 0){
			$ret['HasChildren'] = true;
		}else{
			$ret['HasChildren'] = false;
		}
		$ret['Path'] = $this->getHierarchy();
		return $ret;
    }
    
    public function getHierarchy(){
    	$returnValue = null;
        if (isset($this->Parent)){
			$returnValue = $this->Parent->getHierarchy() . " - " . $this->Value;
        }else{
			$returnValue = $this->Value;
		}
        return $returnValue;
    }

} /* end of class KeywordData */

?>