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
package com.audata.client.record;

import com.audata.client.AuDoc;
import com.audata.client.json.BaseRequestCallback;
import com.audata.client.json.Entry;
import com.audata.client.json.URLEncoder;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;

public class CheckoutCallback extends BaseRequestCallback {

	private String uuid;
	private RecordListPanel parent;
	
	public CheckoutCallback(RecordListPanel parent, String uuid){
		this.parent = parent;
		this.uuid = uuid;
	}
	
	public void onResponseReceived(Request request, Response response) {
		JSONObject jObj = this.parseJSON(response);
		if(jObj != null){
			AuDoc.getInstance().updateStack(AuDoc.STACK_CHECKOUTS);
			//start the download
			String url = AuDoc.jsonCall.getURL() + "docIO.php";
			Entry[] params = new Entry[2];
			params[0] = new Entry("method", "get");
			params[1] = new Entry("id", this.uuid);
			String encParams = URLEncoder.buildQueryString(params);
			this.download(url + "?" + encParams);
			this.parent.onUpdate();
		}
	}

	/**
	 * Needed to open a file directly
	 * @param url
	 */
	public native void download(String url) /*-{
	  $wnd.location.href = url; 
	}-*/;
	
}
