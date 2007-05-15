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
package com.audata.client.json;

import com.audata.client.Language;
import com.audata.client.feedback.SimpleDialog;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

public abstract class BaseRequestCallback implements RequestCallback {
    
    	private static final Language LANG = (Language) GWT.create(Language.class);

	public void onError(Request request, Throwable exception) {
		if (exception instanceof RequestTimeoutException) {
			SimpleDialog.displayDialog(SimpleDialog.TYPE_ERROR, LANG.timeout_Text(), LANG.timeout_message_Text());
          } else {
        	  SimpleDialog.displayDialog(SimpleDialog.TYPE_ERROR, LANG.error_Text(), exception.getMessage());
          }
	}
	
	protected JSONObject parseJSON(Response response){
		JSONObject jObj = null;
		String responseText = response.getText();
		if (responseText.startsWith("{") && responseText.endsWith("}")){
			try {
				jObj = (JSONObject)JSONParser.parse(responseText);
				
				if(jObj.get("error").toString() != "null"){
					//Window.alert(response.get("error").isString().stringValue());
					SimpleDialog.displayDialog(SimpleDialog.TYPE_ERROR,LANG.error_Text(), jObj.get("error").isString().stringValue());
					jObj = null;
				}
			} catch (JSONException e) {
				SimpleDialog.displayDialog(SimpleDialog.TYPE_ERROR, LANG.error_Text(), e.getMessage());
				jObj = null;
			}		
		}else{
			SimpleDialog.displayDialog(SimpleDialog.TYPE_ERROR, LANG.error_Text(), LANG.malformed_Text() + " \n" + responseText);
			response = null;
		}
		return jObj;
	}

}
