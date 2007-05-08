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
 * Stores details about a module within the Audoc system
 * along with the name of the modules entry class
 * @package    AuDoc.DataObjects
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   
 * @version    $Id: $
 */
class Module
    extends absAudocBase
{
    // --- ATTRIBUTES ---

    /**
     * @orm char(128) index(idx_modName)
     *
     * @access public
     * @var String
     */
    public $Name = null;

    /**
     * @orm float
     *
     * @access public
     * @var float
     */
    public $Version = 0.0;

    /**
     * @orm char(512)
     *
     * @access public
     * @var String
     */
    public $Description = null;

    /**
     * @orm char(128)
     *
     * @access public
     * @var String
     */
    public $EntryClass = null;

    /**
     * @orm composed_of one Document
     *
     * @access public
     * @var Document
     */
    public $Contents = null;

    // --- OPERATIONS ---

    /**
     * returns the modules properties as an array
     *
     * @access public
     * @return void
     */
    public function toArray()
    {
        $ret = array();
		$ret['uuid'] = $this->uuid;
		$ret['Name'] = $this->Name;
		$ret['Description'] = $this->Description;
		$ret['Version'] = $this->Version;
		//Don't really think the entry class should ever be required by the client for security reasons
		//$ret['EntryClass'] = $this->EntryClass;
		return $ret;
    }

    /**
     * Calls parent constructor to set uuid field
     *
     * @access public
     * @return void
     */
    public function __construct()
    {
        parent::__construct();
    }

} /* end of class Module */

?>