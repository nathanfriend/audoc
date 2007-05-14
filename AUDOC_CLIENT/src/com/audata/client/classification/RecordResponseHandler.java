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
package com.audata.client.classification;

import com.audata.client.AuDoc;
import com.audata.client.json.BaseRequestCallback;
import com.audata.client.record.RecordListPanel;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class RecordResponseHandler extends BaseRequestCallback {

	private AuDoc audoc;
	private String search;
	private String name;
	
	public RecordResponseHandler(AuDoc audoc, String search, String name){
		this.search = search;
		this.audoc = audoc;
		this.name = name;
	}
	
	public void onResponseReceived(Request request, Response response) {
		JSONObject res = this.parseJSON(response);
		if(res != null){
			JSONArray records = res.get("result").isArray();
			JSONArray params = new JSONArray();
			params.set(0, new JSONString(this.search));
			RecordListPanel content = new RecordListPanel("Classification: " + this.name, records, "search.rawsearch", params);
			this.audoc.setMain(content);
		}
	}
}
