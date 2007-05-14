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
package com.audata.client.stack;

import com.audata.client.AuDoc;
import com.audata.client.feedback.ResponseListener;
import com.audata.client.feedback.SimpleDialog;
import com.audata.client.json.NoOpResponseHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class ItemDeleteClickListener implements ClickListener, ResponseListener {

	private String uuid;
	private String method;
	private int stack;
	
	public ItemDeleteClickListener(int stack, String uuid){
		this.stack = stack;
		switch(this.stack){
		case AuDoc.STACK_SAVEDSEARCHES:
			this.method = "search.delsavedsearch";
			break;
		case AuDoc.STACK_TRAYS:
			this.method = "trays.delTray";
			break;
		}
		this.uuid = uuid;
	}
	
	public void onClick(Widget sender) {
		switch(this.stack){
		case AuDoc.STACK_SAVEDSEARCHES:
			SimpleDialog.displayDialog(SimpleDialog.TYPE_QUERY, "Confirmation", "Are you sure you want to delete this saved search?", this);
			break;
		case AuDoc.STACK_TRAYS:
			SimpleDialog.displayDialog(SimpleDialog.TYPE_QUERY, "Confirmation", "Are you sure you want to delete this tray?", this);
			break;
		}
	}
	
	public void onResponse(boolean response){
		if(response){
			AuDoc.getInstance().switchMain(AuDoc.SECTION_HOME);
			JSONArray params = new JSONArray();
			params.set(0, new JSONString(this.uuid));
			AuDoc.jsonCall.asyncPost2(this.method, params, new NoOpResponseHandler());
			AuDoc.getInstance().updateStack(this.stack);
		}
	}

}
