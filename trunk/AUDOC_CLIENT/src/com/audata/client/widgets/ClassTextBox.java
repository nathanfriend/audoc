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
package com.audata.client.widgets;

import com.audata.client.classification.PopUpClassBrowser;
import com.google.gwt.user.client.ui.TextBox;

public class ClassTextBox extends TextBox{

	private PopUpClassBrowser cb;
	
	public ClassTextBox(){
		this.cb = new PopUpClassBrowser(this, "");
	}
	
	public String getText(){
		return cb.getUUID();
	}
	
	public String getValue(){
		return super.getText();
	}
	
	public void setValues(String name, String uuid){
		this.cb.setValues(name, uuid);
	}

	public void showBrowser(int left, int top){
		this.cb.show(left, top);
	}
}
