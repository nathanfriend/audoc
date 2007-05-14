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

import java.util.ArrayList;

import com.audata.client.json.BaseRequestCallback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * A JSON Request Handler for dealing with returned Record
 * Types
 * @author jonm
 *
 */
public class RecTypeHandler extends BaseRequestCallback {

	private RecTypePanel parent;
	
	/**
	 * Public constructor
	 * @param parent parent panel so it can be updated on completion
	 */
	public RecTypeHandler(RecTypePanel parent){
		this.parent = parent;
	}
	
	/**
	 * Called when response it received.
	 * Parses response and passes JSONObject on
	 */
	public void onResponseReceived(Request request, Response response) {
		JSONObject jObj = this.parseJSON(response);
		if (jObj != null) {
			JSONArray types = jObj.get("result").isArray();
			if(types != null){
				this.addRecTypes(types);
			}		
		}
	}
	
	/**
	 * Extracts data from JSONObject and added
	 * it to parent
	 * @param types A JSON array of Record Types
	 */
	private void addRecTypes(JSONArray types){
		for(int i=0;i<types.size();i++){
			JSONObject rtype = types.get(i).isObject();
			if(rtype != null){
				String name = "";
				if(rtype.containsKey("Name")){
					name = rtype.get("Name").isString().stringValue();
				}
				
				String uuid = "";
				if(rtype.containsKey("uuid")){
					uuid = rtype.get("uuid").isString().stringValue();
				}
				
				String desc = "";
				if(rtype.containsKey("Description")){
					desc = rtype.get("Description").isString().stringValue();
				}
				
				ArrayList udfs = new ArrayList();
				if(rtype.containsKey("UDFs")){
					JSONArray us = rtype.get("UDFs").isArray();
					if(us != null){
						for(int j=0;j<us.size();j++){
							JSONObject u = us.get(j).isObject();
							if(u != null){
								if(u.containsKey("Name")){
									udfs.add(u.get("Name").isString().stringValue());
								}
							}
						}
					}
				}
				
				this.parent.addRecType(uuid, name, desc, udfs);
			}
		}
	}
}
