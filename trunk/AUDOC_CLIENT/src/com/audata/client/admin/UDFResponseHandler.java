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
package com.audata.client.admin;

import com.audata.client.json.BaseRequestCallback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

public class UDFResponseHandler extends BaseRequestCallback {

	UDFPanel parent;

	public UDFResponseHandler(UDFPanel parent) {
		this.parent = parent;
	}

	public void onResponseReceived(Request request, Response response) {
		JSONObject jObj = this.parseJSON(response);
		if (jObj != null) {
			JSONArray udfs = jObj.get("result").isArray();
			if(udfs != null){
				this.update(udfs);
			}
		}

	}

	private void update(JSONArray udfs) {
		for (int i = 0; i < udfs.size(); i++) {
			JSONObject udf = udfs.get(i).isObject();

			String uuid = "";
			if (udf.containsKey("uuid")) {
				uuid = udf.get("uuid").isString().stringValue();
			}

			String name = "";
			if (udf.containsKey("Name")) {
				name = udf.get("Name").isString().stringValue();
			}
			
			int type = 0;
			if(udf.containsKey("Type")){
				type = (int)udf.get("Type").isNumber().getValue();

			}
			String kwh = null;
			if(udf.containsKey("KeywordHierarchy")){
				kwh = udf.get("KeywordHierarchy").isString().stringValue();
			}

			this.parent.addUDF(uuid, name, type, kwh);
		}
	}

}
