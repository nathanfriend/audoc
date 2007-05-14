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
package com.audata.client.admin.users;

import java.util.ArrayList;

import com.audata.client.json.BaseRequestCallback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

public class UserResponseHandler extends BaseRequestCallback {

	private UserPanel parent;
	
	public UserResponseHandler(UserPanel parent){
		this.parent = parent;
	}
	
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
				
				String uuid = "";
				if(user.containsKey("uuid")){
					uuid = user.get("uuid").isString().stringValue();
				}
				
				String uname = "";
				if(user.containsKey("UserName")){
					uname = user.get("UserName").isString().stringValue();
				}
				
				boolean isAdmin = false;
				if(user.containsKey("isAdmin")){
					isAdmin = user.get("isAdmin").isBoolean().booleanValue();
				}

				String secLevel = "";
				if(user.containsKey("SecLevel")){
					secLevel = user.get("SecLevel").isString().stringValue();
				}
				
				ArrayList caveats = new ArrayList();
				if(user.containsKey("Caveats")){
					JSONArray cavs = user.get("Caveats").isArray();
					for(int j=0;j<cavs.size();j++){
						JSONObject cav = cavs.get(j).isObject();
						String c = cav.get("Name").isString().stringValue();
						caveats.add(c);
					}
				}
				this.parent.addUser(uuid, fname, sname, uname, isAdmin, secLevel, caveats);
			}
		}
	}

}
