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
package com.audata.client.authentication;

import com.audata.client.AuDoc;
import com.audata.client.json.NoOpResponseHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class LogoutListener implements ClickListener {

	private AuDoc parent;
	
	public LogoutListener(AuDoc parent){
		this.parent = parent;
	}
	
	public void onClick(Widget sender) {
		JSONArray params = new JSONArray();
		AuDoc.jsonCall.asyncPost2("Users.logout", params, new NoOpResponseHandler());
		this.parent.resetApp();
	}

}
