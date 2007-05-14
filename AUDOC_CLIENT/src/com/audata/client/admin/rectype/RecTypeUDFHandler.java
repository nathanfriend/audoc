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
package com.audata.client.admin.rectype;

import com.audata.client.json.BaseRequestCallback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * Handles responses of UDF definitions for
 * the Record Type admin panel
 * @author jonm
 *
 */
public class RecTypeUDFHandler extends BaseRequestCallback {

	RecTypePanel parent;
	/**
	 * Public constructor
	 * @param parent The parent RecType panel so it can be updated
	 */
	public RecTypeUDFHandler(RecTypePanel parent){
		this.parent = parent;
	}
	
	/**
	 * Called when a response is received
	 * Parses the response and hands on the results
	 */
	public void onResponseReceived(Request request, Response response) {
		JSONObject jObj = this.parseJSON(response);
		if (jObj != null) {
			JSONArray udfs = jObj.get("result").isArray();
				if(udfs != null){
					this.addUDFs(udfs);
				}

		}
	}
	
	/**
	 * Extract data from the parsed response and add
	 * it to the parent panel
	 * @param udfs An JSONArray of UDFDefinition
	 */
	private void addUDFs(JSONArray udfs){
		String[] names = new String[udfs.size()];
		for(int i=0;i<udfs.size();i++){
			JSONObject udf = udfs.get(i).isObject();
			if(udf != null){
				String name = "";
				if(udf.containsKey("Name")){
					name = udf.get("Name").isString().stringValue();
				}
				names[i] = name; 
			}
		}
		this.parent.addUDFs(names);
	}

}
