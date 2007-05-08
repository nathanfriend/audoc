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
 * Trays work like favourites lists, sotring pointers to records for quick
 * reference at a later date
 * @package    AuDoc.DataObjects
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */
class Tray
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
     * @orm char(512)
     *
     * @access public
     * @var String
     */
    public $Description = null;

    /**
     * @orm has one User
     *
     * @access public
     * @var User
     */
    public $Owner = null;

    /**
     * @orm has many Record
     *
     * @access public
     * @var Record
     */
    public $Records = null;

    // --- OPERATIONS ---

    /**
     * Short description of method toArray
     *
     * @access public
     * @author firstname and lastname of author, <author@example.org>
     * @return void
     */
    public function toArray()
    {
        $ret = array();
		$ret['uuid'] = $this->uuid;
		$ret['Name'] = $this->Name;
		$ret['Description'] = $this->Description;
		if(isset($this->Owner)){
			$ret['Owner'] = $this->Owner;
		}
		//Records returned by seperate function
		return $ret;
    }

    /**
     * Short description of method __construct
     *
     * @access public
     * @author firstname and lastname of author, <author@example.org>
     * @return void
     */
    public function __construct()
    {
        parent::__construct();
    }

} /* end of class Tray */

?>