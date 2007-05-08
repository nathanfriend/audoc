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
 * Defines the settings for a report
 * @package    AuDoc.DataObjects
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */
class Report
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
    public $SearchString = null;

    /**
     * The document name for the report template
     * @orm char(32)
     *
     * @access public
     * @var String
     */
    public $Template = null;

    /**
     * The HTML generated when the report is run
     *
     * @access public
     * @var String
     */
    public $Output = null;

    // --- OPERATIONS ---

    /**
     * Returns the report's properties
     * as an array
     *
     * @access public
     * @return array
     */
    public function toArray()
    {
        $ret = array();
		$ret['uuid'] = $this->uuid;
		$ret['Name'] = $this->Name;
		$ret['SearchString'] = $this->SearchString;
		$ret['Template'] = $this->Template;
		$ret['Output'] = $this->Output;
		return $ret;
    }

    /**
     * Calls the parent constructor to
     * obtain an uuid
     *
     * @access public
     * @author firstname and lastname of author, <author@example.org>
     * @return void
     */
    public function __construct()
    {
        parent::__construct();
    }

} /* end of class Report */

?>