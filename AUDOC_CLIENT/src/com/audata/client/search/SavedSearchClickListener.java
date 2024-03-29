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
package com.audata.client.search;

import com.audata.client.AuDoc;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class SavedSearchClickListener implements ClickListener {

	private String uuid;
	private AuDoc audoc;
	
	public SavedSearchClickListener(AuDoc audoc, String uuid){
		this.audoc = audoc;
		this.uuid = uuid;
	}
	
	public void onClick(Widget sender) {
		JSONArray params = new JSONArray();
		params.set(0, new JSONString(this.uuid));
		params.set(1, new JSONNumber(0));
		params.set(2, JSONBoolean.getInstance(false));
		AuDoc.jsonCall.asyncPost2("search.runsavedsearch", params, new SearchResponseHandler(this.audoc, SearchResponseHandler.TYPE_SAVED, params));
	}

}
