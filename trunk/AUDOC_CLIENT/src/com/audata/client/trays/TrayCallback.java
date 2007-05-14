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
package com.audata.client.trays;

import com.audata.client.json.BaseRequestCallback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * @author jonm
 *
 */
public class TrayCallback extends BaseRequestCallback{

	ITrayCollection parent;
	
	public TrayCallback(ITrayCollection parent){
		this.parent = parent;
	}
	/** 
	 * @see com.google.gwt.http.client.RequestCallback#onResponseReceived(com.google.gwt.http.client.Request, com.google.gwt.http.client.Response)
	 **/
	public void onResponseReceived(Request request, Response response) {
		JSONObject jObj = this.parseJSON(response);
		if(jObj != null){
			this.addTrays(jObj);
		}
	}
	
	private void addTrays(JSONObject obj){
		JSONArray trays = (JSONArray)obj.get("result");
		if (trays != null){
			for(int i=0;i<trays.size();i++){
				JSONObject tray = (JSONObject)trays.get(i);
				String uuid = tray.get("uuid").isString().stringValue();
				String name = tray.get("Name").isString().stringValue();
				String desc = tray.get("Description").isString().stringValue();
				this.parent.addTray(uuid, name, desc);
			}
		}
	}

}
