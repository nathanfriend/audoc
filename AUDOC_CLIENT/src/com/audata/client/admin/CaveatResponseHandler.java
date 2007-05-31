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
 * Response Handler for Caveat arrays
 * @author jonm
 *
 */
public class CaveatResponseHandler extends BaseRequestCallback {

	private SecurityPanel parent;
	/**
	 * Public contructor
	 * @param parent
	 */
	public CaveatResponseHandler(SecurityPanel parent){
		this.parent = parent;
	}
	
	/**
	 * Called when a response is received
	 * Parses response for errors and passes
	 * the response to addCaveats
	 */
	public void onResponseReceived(Request request, Response response) {
		JSONObject jObj = this.parseJSON(response);
		if(jObj != null){
			JSONArray caveats = jObj.get("result").isArray();
				this.addCaveats(caveats);
		}
	}
	/**
	 * Extract data from response array and
	 * add the Caveat data to the parent form
	 * @param caveats JSONArray of Caveat object
	 */
	private void addCaveats(JSONArray caveats){
		if(caveats != null){
			for(int i=0;i<caveats.size();i++){
				JSONObject cav = (JSONObject)caveats.get(i);
				String name = cav.get("Name").isString().stringValue();
				String uuid = cav.get("uuid").isString().stringValue();
				this.parent.addCaveat(name, uuid);
			}
		}
	}

}
