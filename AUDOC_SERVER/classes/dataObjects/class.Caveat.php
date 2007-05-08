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
 * Represents a security Caveat
 * @package    AuDoc.DataObjects
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   
 * @version    $Id: $
 */


class Caveat
    extends absAudocBase
{
    // --- ATTRIBUTES ---

    /**
     * @orm char(128) index(idx_cavName)
     *
     * @access public
     * @var String
     */
    public $Name = null;

    // --- OPERATIONS ---

    /**
     * Returns the properties of the object 
     * as an array
     *
     * @access public
     * @return associative array of object properties 
     */
    public function toArray()
    {
        $ret = array();
		$ret['uuid'] = $this->uuid;
		$ret['Name'] = $this->Name;
		return $ret;
    }

    /**
     * Object Constructor
     *
     * @access public
     * @return void
     */
    public function __construct()
    {
        parent::__construct();
    }

} /* end of class Caveat */

?>