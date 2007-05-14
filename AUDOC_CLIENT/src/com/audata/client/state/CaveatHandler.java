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
package com.audata.client.state;

import java.util.HashMap;

import com.audata.client.AuDoc;
import com.audata.client.json.BaseRequestCallback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

public class CaveatHandler extends BaseRequestCallback {

	
	public CaveatHandler(){
	}
	
	public void onResponseReceived(Request request, Response response) {
		JSONObject jObj = this.parseJSON(response);
		if(jObj != null){
			JSONArray caveats = jObj.get("result").isArray();
			if (caveats != null){
				this.addCaveats(caveats);
			}
		}
	}
	
	private void addCaveats(JSONArray caveats){
		HashMap caveatMap = new HashMap();
		for(int i=0;i<caveats.size();i++){
			JSONObject caveat = (JSONObject)caveats.get(i);
			String name = caveat.get("Name").isString().stringValue();
			String uuid = caveat.get("uuid").isString().stringValue();
			caveatMap.put(uuid, name);
		}
		AuDoc.state.setItem("Caveats", caveatMap);
	}

}
