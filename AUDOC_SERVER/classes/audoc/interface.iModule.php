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
 * iModule Interface
 *
 * The interface that must be implemented by all 
 * modules of AuDoc
 *
 * @package    AuDoc.MicroCore
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited
 * @version    $Id: $
 */
interface iModule
{
    // --- OPERATIONS ---

    /**
     * Executes a request.
     *
     * @access public
     * @param String $method The name of the method to be called
     * @param Array $params An array of additional parameters
     * @return mixed Various return types
     */
    public function execute($method, $params);

    /**
     * Called by the MicroCore to give access to the MicroCore
     * to the Module.
     *
     * @access public
     * @param MicroCore $parent The MicroCore object
     */	
	public function setParent(MicroCore $parent);
	
	/**
	 * Should return true if the specified method is
	 * permitted for the current user state
	 * 
	 * @param String $method The method to be tested
	 * @param bool $state one of the MicroCore PERM_* states
	 * @return bool True if method is permitted
	 */
	public function isPermitted($method, $state);
	
} /* end of interface iModule */

?>
