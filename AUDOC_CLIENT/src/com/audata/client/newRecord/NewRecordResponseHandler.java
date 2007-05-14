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
package com.audata.client.newRecord;

import com.audata.client.json.BaseRequestCallback;
import com.audata.client.widgets.UploadPanel;
import com.audata.client.widgets.Wizard;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;

public class NewRecordResponseHandler extends BaseRequestCallback {

	private String filename;
	private UploadPanel upload;
	private Wizard wizard;
	
	public NewRecordResponseHandler(Wizard wizard, String filename, UploadPanel upload){
		this.filename = filename;
		this.upload = upload;
		this.wizard = wizard;
	}
	
	public void onResponseReceived(Request request, Response response) {
		JSONObject jObj = this.parseJSON(response);
		if(jObj.containsKey("result")){
			JSONObject result = jObj.get("result").isObject();
			if(result != null){
				String uuid = result.get("uuid").isString().stringValue();
				if(this.filename.equals("")){
//					no upload -> go straight to form
					this.wizard.reset();
				}else{
//					upload the file
					this.upload.submit(uuid);
				}
			}
		}
	}

}
