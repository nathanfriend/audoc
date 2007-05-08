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
 * Saves search criteria so they can be used again
 * @package    AuDoc.DataObjects
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */
class SavedSearch
    extends absAudocBase
{
    // --- ATTRIBUTES ---

    /**
     * @orm char(128)
     *
     * @access public
     * @var String
     */
    public $Title = null;

    /**
     * @orm char(512)
     *
     * @access public
     * @var String
     */
    public $Description = null;

    /**
     * @orm char(512)
     *
     * @access public
     * @var String
     */
    public $Criteria = null;

    /**
     * @orm has one User
     *
     * @access public
     * @var User
     */
    public $Owner = null;

    // --- OPERATIONS ---

    /**
     * Returns the properties of the saved
     * search as an array
     *
     * @access public
     * @return array
     */
    public function toArray()
    {
        $ret = array();
		$ret['uuid'] = $this->uuid;
		$ret['Description'] = $this->Description;
		$ret['Title'] = $this->Title;
		$ret['Owner'] = $this->Owner;
		$ret['Criteria'] = $this->Criteria;
		return $ret;
    }

    /**
     * Calls the parent constructor to 
     * obtain a uuid
     *
     * @access public
     * @return void
     */
    public function __construct()
    {
        parent::__construct();
    }

} /* end of class SavedSearch */

?>