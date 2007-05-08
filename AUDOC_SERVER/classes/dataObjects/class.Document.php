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
 * Stores the metadata about an electronic document held within the system
 * @package    AuDoc.DataObjects
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */


class Document
    extends absAudocBase
{
    // --- ATTRIBUTES ---

    /**
     * @orm char(128)
     *
     * @access public
     * @var String
     */
    public $Name = null;

    /**
     * @orm char(10)
     *
     * @access public
     * @var String
     */
    public $Extension = null;

    /**
     * @orm integer(6)
     *
     * @access public
     * @var int
     */
    public $Revision = 0;

    /**
     * @orm char(256)
     *
     * @access public
     * @var String
     */
    public $Folder = null;

    // --- OPERATIONS ---

    /**
     * Returns the objects properties as an associative array
     *
     * @access public
     * @author Jon Moss, <jon.moss@audata.co.uk>
     * @return void
     */
    public function toArray()
    {
        $ret = array();
		$ret['uuid'] = $this->uuid;
		$ret['Name'] = $this->Name;
		$ret['Folder'] = $this->Folder;
		$ret['Extension'] = $this->Extension;
		$ret['Revision'] = $this->Revision;
		return $ret;
    }

    /**
     * Object Constructor
     *
     * @access public
     * @author firstname and lastname of author, <author@example.org>
     * @return void
     */
    public function __construct()
    {
        parent::__construct();
    }

} /* end of class Document */

?>