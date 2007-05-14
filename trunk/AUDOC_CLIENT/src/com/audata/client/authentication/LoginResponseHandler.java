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
package com.audata.client.authentication;

import com.audata.client.AuDoc;
import com.audata.client.json.BaseRequestCallback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.DialogBox;

public class LoginResponseHandler extends BaseRequestCallback{

	private DialogBox dlg;
	private AuDoc parent;
	
	public LoginResponseHandler(AuDoc parent, DialogBox dlg){
		this.dlg = dlg;
		this.parent = parent;
	}
	
	public void onResponseReceived(Request request, Response response) {
		JSONObject jObj = this.parseJSON(response);
		if(jObj != null){
			this.dlg.hide();
			setUserState(jObj);
			this.parent.onLogin();
		}
	}
	
	private void setUserState(JSONObject response){
		JSONObject result = (JSONObject)response.get("result");
		AuDoc.state.setItem("forename",result.get("Forename").isString().stringValue());
		AuDoc.state.setItem("surname",result.get("Surname").isString().stringValue());
		AuDoc.state.setItem("username",result.get("UserName").isString().stringValue());		
		AuDoc.state.setItem("isAdmin",Boolean.toString(result.get("isAdmin").isBoolean().booleanValue()));
		AuDoc.state.setItem("my_uuid", result.get("uuid").isString().stringValue());		
	}
}
