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
 * The absUDF class represents the parent of all
 * User defined fields
 *
 * @package    AuDoc.DataObjects
 * @author     Jon Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */
abstract class absUDF
    extends absAudocBase
{
    // --- ATTRIBUTES ---

    /**
     * @orm integer
     *
     * @access public
     * @var int
     */
    public $Type = 0;

    /**
     * Short description of attribute Name
     * @orm char(128)
     * @access public
     * @var String
     */
    public $Name = null;

    /**
     * Short description of attribute Value
     *
     * @access public
     * @var String
     * @orm char(64)
     */
    public $Value = null;

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
		if($this->Type != UDFMan::TYPE_KEYWORD){
			if($this->Type != UDFMan::TYPE_DATETIME){
				$ret['Value'] = $this->Value;
			}else{
				$ret['Value'] = date("c", $this->Value);
			}
		}else{
			$ret['Value'] = $this->Value->toArray();
		}
		$ret['Type'] = $this->Type;
		return $ret;
    }

} /* end of abstract class absUDF */

?>