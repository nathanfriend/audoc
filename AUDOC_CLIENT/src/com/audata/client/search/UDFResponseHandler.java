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

import com.audata.client.json.BaseRequestCallback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

public class UDFResponseHandler extends BaseRequestCallback {

	SearchPanel parent;
	
	public UDFResponseHandler(SearchPanel parent){
		this.parent = parent;
	}
	public void onResponseReceived(Request request, Response response) {
		JSONObject jObj = this.parseJSON(response);
		if(jObj != null){
			JSONArray udfs = jObj.get("result").isArray();
			if(udfs != null){
				for(int i=0;i<udfs.size();i++){
					JSONObject u = udfs.get(i).isObject();
					if(u != null){
						String name = u.get("Name").isString().stringValue();
						int type = new Double(u.get("Type").isNumber().getValue()).intValue();
						String kwh = null;
						if(u.containsKey("KeywordHierarchy")){
							kwh = u.get("KeywordHierarchy").isString().stringValue();
						}
						this.parent.addField(name, name, type, true, kwh);
					}
				}
			}
		}
	}

}
