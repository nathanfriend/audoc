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
 * Base data object class from which all other data objects
 * descend.  Provides a uuid - universally unique identifier
 * @abstract
 * @package    AuDoc.DataObjects
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */

abstract class absAudocBase
{
    // --- ATTRIBUTES ---

    /**
     * @orm char(32) unique(idx_uuid)
     *
     * @access public
     * @var char
     */
    public $uuid = '';

    // --- OPERATIONS ---

    /**
     * Constructor - Generates the objects
     * uuid
     *
     * @access public
     * @author firstname and lastname of author, <author@example.org>
     * @return void
     */
    public function __construct()
    {
        $this->uuid = md5(uniqid(rand(), true));
    }

    /**
     * Should be overridden by all descending classes
     *
     * @access public
     * @return boolean
     */
    public function toArray()
    {
        $returnValue = null;
        return $returnValue;
    }

} /* end of abstract class absAudocBase */

?>