/*
 * +----------------------------------------------------------------------+
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
 */
package com.audata.client.state;

import java.util.HashMap;


public class State {

		private HashMap state;
		
		public State(){
			this.state = new HashMap();
		}
		
		public Object getItem(String key){
			if(this.state.containsKey(key)){
				return this.state.get(key);
			}else{
				return null;
			}
		}
		
		public boolean containsKey(String key){
			return this.state.containsKey(key);
		}
		
		public void setItem(String key, Object value){
			this.state.put(key, value);
		}
		
		public void clearState(){
			this.state.clear();
		}
}
