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

public class ReportHandler extends BaseRequestCallback {

	private ReportingPanel parent;
	
	/**
	 * public constructor takes a ReportingPanel
	 * for updates
	 * @param parent ReportingPanel to update
	 */
	public ReportHandler(ReportingPanel parent){
		this.parent = parent;
		
	}
	
	/**
	 * Called when a response is received
	 * Parses the response and updates parent
	 */
	public void onResponseReceived(Request request, Response response) {
		JSONObject rObj = this.parseJSON(response);
		if(rObj != null){
			JSONArray reports = rObj.get("result").isArray();
			if(reports != null){
				for(int i=0; i<reports.size();i++){
					JSONObject report = reports.get(i).isObject();
					String uuid = report.get("uuid").isString().stringValue();
					String title = report.get("Name").isString().stringValue();
					String template = report.get("Template").isString().stringValue();
					String criteria = report.get("SearchString").isString().stringValue();
					this.parent.addReport(uuid, title, template, criteria);
					
				}
			}
			
		}

	}

}
