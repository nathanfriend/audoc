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
 * Listeners are objects that are called when certain
 * actions occur within the AuDoc system
 * @package    AuDoc.DataObjects
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */
class Listeners
    extends absAudocBase
{
    // --- ATTRIBUTES ---

    /**
     * @orm has one Module
     *
     * @access public
     * @var Module
     */
    public $Module = null;

    /**
     * @orm integer
     *
     * @access public
     * @var int
     */
    public $Type = 0;

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
		$ret['Type'] = $this->Type;
		$ret['Module'] = $this->Module->toArray();
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

} /* end of class Listeners */

?>