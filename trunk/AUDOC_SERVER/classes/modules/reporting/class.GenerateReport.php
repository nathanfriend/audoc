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
 * This file contains the class for the generating reports
 *
 * @package    AuDoc.Modules
 * @author	   Jonathan Moss <jon.moss@audata.co.uk>
 * @copyright  2007 Audata Limited   

 */
class GenerateReport {
 	
 	private $title;
 	private $records;
 	private $template;
 	private $text;
 	
 	public function __construct($title, $template){
 		$this->title = $title;
 		$this->template = implode(" ", file($template));
 	}
 	
 	public function generate($records){
 		$this->records = $records;
		$begin = stripos($this->template, '%start-records%');
		$end = stripos($this->template, '%end-records%');

		
		$header = substr($this->template, 0, $begin);
		$this->doNonRecord($header);
		$recSection = substr($this->template, $begin+15, $end - ($begin+15));
		$this->doRecord($recSection);
		
		$footer = substr($this->template, $end+13);
		$this->doNonRecord($footer);
		return $this->text;		
 	}
 	
 	private function doRecord($temp){
		foreach($this->records as $record){
 			$fields = $record->toArray();
 			$line = $temp;
 			foreach($fields as $field => $value){
 				if(!is_array($value)){
 					
 					$pattern = "{%$field%}";
 					$line = preg_replace($pattern, $value, $line);
 				}
 			}
 			$this->text .= $line;
 		}
 	}
 	
 	private function doNonRecord($temp){
 			$temp = preg_replace("{%username%}", $_SESSION['Username'], $temp);
 			$temp = preg_replace("{%date%}", date('l dS \of F Y'), $temp);
 			$temp = preg_replace("{%title%}", $this->title, $temp);
 			$this->text .= $temp;
 	}
 	
 }
?>
