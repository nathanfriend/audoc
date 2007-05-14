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
package com.audata.client.record;

import com.audata.client.json.BaseRequestCallback;
import com.audata.client.util.FieldTypes;
import com.audata.client.widgets.Field;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

public class RecTypeCallback extends BaseRequestCallback {

	private Properties parent;
	
	public RecTypeCallback(Properties parent){
		this.parent = parent;
	}
	
	public void onResponseReceived(Request request, Response response) {
		JSONObject rObj = this.parseJSON(response);
		if(rObj != null){
			JSONObject rType = rObj.get("result").isObject();
			if((rType != null) && (rType.containsKey("UDFs"))){
				JSONArray udfs = rType.get("UDFs").isArray();
				Field[] ret = new Field[udfs.size()];
				for(int i=0;i<udfs.size();i++){
					JSONObject u = udfs.get(i).isObject();
					String name = u.get("Name").isString().stringValue();
					int type = (int)u.get("Type").isNumber().getValue();
					String kwh = null;
					if(type == FieldTypes.TYPE_KEYWORD){
						kwh = u.get("KeywordHierarchy").isString().stringValue();
					}
					ret[i] = new Field(name, type, true, kwh);
				}
				this.parent.addUdfs(ret);
			}
		}
		this.parent.paint();
	}

}
