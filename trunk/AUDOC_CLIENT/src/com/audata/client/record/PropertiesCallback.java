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

import java.util.HashMap;

import com.audata.client.json.BaseRequestCallback;
import com.audata.client.util.FieldTypes;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

public class PropertiesCallback extends BaseRequestCallback {

	private Properties parent;
	
	public PropertiesCallback(Properties parent){
		this.parent = parent;
	}
	
	public void onResponseReceived(Request request, Response response) {
		HashMap record = new HashMap();
		JSONObject rObj = this.parseJSON(response);
		if(rObj != null){
			JSONObject rec = rObj.get("result").isObject();
			if(rec != null){
				//standard string fields
				record.put("Record Number", rec.get("RecordNumber").isString().stringValue());
				record.put("Classification", rec.get("Classification").isString().stringValue());
				String cp = rec.get("ClassPath").isString().stringValue();
				int divPos = cp.lastIndexOf(" - ");
				if(divPos != -1){
					cp = cp.substring(divPos+3);
				}
				record.put("ClassPath", cp);
				
				record.put("Title", rec.get("Title").isString().stringValue());
				String dc = rec.get("DateCreated").isString().stringValue();
				int pos = dc.indexOf("T");
				if(pos != -1){
					dc = dc.substring(0, pos);
				}
				record.put("Date Created", dc);
				record.put("Owner", rec.get("Owner").isString().stringValue());
				record.put("Author", rec.get("Author").isString().stringValue());
				record.put("Notes", rec.get("Notes").isString().stringValue());
				
				//udf fields
				JSONArray udfs = rec.get("UDFs").isArray();
				for(int i=0;i<udfs.size();i++){
					JSONObject udf = udfs.get(i).isObject();
					int type = (int)udf.get("Type").isNumber().getValue();
					String name = udf.get("Name").isString().stringValue();
					String value = "";
					switch(type){
					case(FieldTypes.TYPE_INT):
						int in = (int)udf.get("Value").isNumber().getValue();
						value = Integer.toString(in);
						break;
					case(FieldTypes.TYPE_DEC):
						double d = udf.get("Value").isNumber().getValue();
						value = Double.toString(d);
						break;
					case(FieldTypes.TYPE_DATE):
						value = udf.get("Value").isString().stringValue();
						int b = value.indexOf("T");
						if(b != -1){
							value = value.substring(0, b);
						}
						break;
					case(FieldTypes.TYPE_STRING):
						value = udf.get("Value").isString().stringValue();
						break;
					case(FieldTypes.TYPE_KEYWORD):
						JSONObject kv = udf.get("Value").isObject();
						if(kv != null){
							value = kv.get("uuid").isString().stringValue();
							record.put(name+"Text", kv.get("Path").isString().stringValue());
						}else{
							value = "";
							record.put(name+"Text", "");
						}
					}
					record.put(name, value);
				}
				this.parent.setRecord(record);
			}
		}

	}

}
