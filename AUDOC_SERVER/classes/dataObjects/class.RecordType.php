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
 * Record types define what kinds of metadata are associated with
 * a specific group of records
 * @package    AuDoc.DataObjects
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */
class RecordType
    extends absAudocBase
{
    // --- ATTRIBUTES ---

    /**
     * @orm char(512)
     *
     * @access public
     * @var String
     */
    public $Description = null;

    /**
     * @orm has many UDFDefinition
     *
     * @access public
     * @var UDFDefinition
     */
    public $UDFs = array();

    /**
     * @orm char(128) index(idx_rTypeName)
     *
     * @access public
     * @var String
     */
    public $Name = null;

    // --- OPERATIONS ---

    /**
     * Returns the record types properties
     * as an array
     *
     * @access public
     * @return array
     */
    public function toArray()
    {
        $ret = array();
		$ret['uuid'] = $this->uuid;
		$ret['Name'] = $this->Name;
		$ret['Description'] = $this->Description;
		$ret['UDFs'] = array();
		foreach($this->UDFs as $udfDef){
			$ret['UDFs'][] = $udfDef->toArray();
		}
		return $ret;
    }

    /**
     * Calls the parent constructor to 
     * obtain an uuid
     *
     * @access public
     * @return void
     */
    public function __construct()
    {
        parent::__construct();
    }

} /* end of class RecordType */

?>