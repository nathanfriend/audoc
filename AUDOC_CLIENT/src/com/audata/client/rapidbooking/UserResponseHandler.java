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
package com.audata.client.rapidbooking;

import com.audata.client.json.BaseRequestCallback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * @author jonm
 *
 */
public class UserResponseHandler extends BaseRequestCallback {

    
    private RapidBookingDialog parent;
    
    public UserResponseHandler(RapidBookingDialog parent){
	this.parent = parent;
    }
    
    /* (non-Javadoc)
     * @see com.google.gwt.http.client.RequestCallback#onResponseReceived(com.google.gwt.http.client.Request, com.google.gwt.http.client.Response)
     */
    public void onResponseReceived(Request request, Response response) {
	JSONObject jObj = this.parseJSON(response);
	if(jObj != null){
		JSONArray users = jObj.get("result").isArray();
			this.addUsers(users);
	}
    }
    
    private void addUsers(JSONArray users){
	if(users != null){
		for(int i=0;i<users.size();i++){
			JSONObject user = (JSONObject)users.get(i);
			
			String fname = "";
			if(user.containsKey("Forename")){
				fname = user.get("Forename").isString().stringValue();
			}
			
			String sname = "";
			if(user.containsKey("Surname")){
				sname = user.get("Surname").isString().stringValue();
			}
			
			String uname = "";
			if(user.containsKey("UserName")){
				uname = user.get("UserName").isString().stringValue();
			}
			
			this.parent.addUser(uname, fname, sname);
		}
	}
    }

}
