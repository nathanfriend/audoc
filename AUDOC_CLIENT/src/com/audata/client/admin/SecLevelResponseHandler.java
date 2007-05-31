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

/**
 * A response handler for security levels
 * @author jonm
 *
 */
public class SecLevelResponseHandler extends BaseRequestCallback{

	private SecurityPanel parent;
	
	/**
	 * Public constructor
	 * @param parent
	 */
	public SecLevelResponseHandler(SecurityPanel parent){
		this.parent = parent;
	}
	
	/**
	 * Called when the response is received.
	 * Parse the response and passes the 
	 * results on to addLevels()
	 */
	public void onResponseReceived(Request request, Response response) {
		JSONObject jObj = this.parseJSON(response);
		if(jObj != null){
			JSONArray levels = (JSONArray)jObj.get("result").isArray();
				this.addLevels(levels);
		}
	}
	/**
	 * Extracts data from the response and 
	 * add the security levels to the parent form
	 * @param levels JSONArray of of SecLevel objects
	 */
	private void addLevels(JSONArray levels){
		if(levels != null){
			for(int i=0;i<levels.size();i++){
				JSONObject seclevel = (JSONObject)levels.get(i);
				String name = seclevel.get("Name").isString().stringValue();
				int level = (int)seclevel.get("Level").isNumber().getValue();
				String uuid = seclevel.get("uuid").isString().stringValue();
				this.parent.addSecLevel(name, level, uuid);
			}
		}
	}

}
