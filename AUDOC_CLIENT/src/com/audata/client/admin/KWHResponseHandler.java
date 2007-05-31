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

public class KWHResponseHandler extends BaseRequestCallback {

	private UDFPanel parent;
	
	public KWHResponseHandler(UDFPanel parent){
		this.parent = parent;
	}
	
	public void onResponseReceived(Request request, Response response) {
		JSONObject rObj = this.parseJSON(response);
		if(rObj != null){
			JSONArray res = rObj.get("result").isArray();
			if (res != null){
				for(int i=0;i<res.size();i++){
					JSONObject h = res.get(i).isObject();
					String name = h.get("Name").isString().stringValue();
					String uuid = h.get("uuid").isString().stringValue();
					this.parent.addKWH(name, uuid);
				}
			}
		}

	}

}
