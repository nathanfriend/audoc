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
 * iListener Interface
 *
 * The interface that must be implemented by all 
 * Listeners of AuDoc
 *
 * @package    AuDoc.MicroCore
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited
 * @version    $Id: $
 */
interface iListener
{
    // --- OPERATIONS ---

    /**
     * Executed on an Event.
     *
     * @access public
     * @param Integer $event The event type raised
     * @param String $obType The name of the class effected
     * @param Integer $obId The id of the object effected
     */
    public function onEvent($event, $obType, $obId);
}
/* end of interface iListener */

?>
