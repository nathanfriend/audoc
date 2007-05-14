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
package com.audata.client.checkout;

import com.audata.client.json.BaseRequestCallback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

public class CheckoutCallback extends BaseRequestCallback{

	private CheckoutPanel parent;
	
	public CheckoutCallback(CheckoutPanel parent){
		this.parent = parent;
	}
	
	public void onResponseReceived(Request request, Response response){
		JSONObject jObj = this.parseJSON(response);
		if(jObj != null){
			this.addRecords(jObj);
		}
	}
	
	public void addRecords(JSONObject obj){
		JSONArray records = (JSONArray)obj.get("result");
		if (records != null){
			for(int i=0;i<records.size();i++){
				JSONObject rec = (JSONObject)records.get(i);
				String title = rec.get("Title").isString().stringValue();
				String uuid = rec.get("uuid").isString().stringValue();
				String recType = rec.get("RecordType").isString().stringValue();
				String cot = rec.get("CheckedOutTo").isString().stringValue();
				this.parent.addRecord(uuid, title, recType, cot);
			}
			this.parent.paint();
		}
	}
}
