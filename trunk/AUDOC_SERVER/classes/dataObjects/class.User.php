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
 * Holds the details of a user of the system
 * @package    AuDoc.DataObjects
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   
 * @version    $Id: $
 */
class User
    extends absAudocBase
{
    // --- ATTRIBUTES ---

    /**
     * @orm char(128)
     *
     * @access public
     * @var String
     */
    public $Forename = null;

    /**
     * @orm char(128)
     *
     * @access public
     * @var String
     */
    public $Surname = null;

    /**
     * @orm char(128) index(idx_username)
     *
     * @access public
     * @var String
     */
    public $UserName = null;

    /**
     * @orm char(128) index(idx_password)
     *
     * @access public
     * @var String
     */
    public $Password = null;

    /**
     * @orm bool
     *
     * @access public
     * @var boolean
     */
    public $isAdmin = false;

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
		$ret['Forename'] = $this->Forename;
		$ret['Surname'] = $this->Surname;
		$ret['UserName'] = $this->UserName;
		if(isset($this->SecLevel)){
			$ret['SecLevel'] = $this->SecLevel->Name;
		}
		$ret['isAdmin'] = $this->isAdmin;
		$ret['Caveats'] = array();
		foreach($this->Caveats as $caveat){
			$ret['Caveats'][] = $caveat->toArray();
		}
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

} /* end of class User */

?>