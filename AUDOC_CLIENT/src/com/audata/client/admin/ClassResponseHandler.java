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

import java.util.ArrayList;

import com.audata.client.json.BaseRequestCallback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * A response handler class for the Classification
 * admin panel
 * @author jonm
 *
 */
public class ClassResponseHandler extends BaseRequestCallback {

	private ClassificationPanel parent;
	
	/**
	 * Constructor that take the parent classification admin panel
	 * @param parent The ClassificationPanel that needs update on method return 
	 */
	public ClassResponseHandler(ClassificationPanel parent){
		this.parent = parent;
	}
	
	/**
	 * Called when a response is received. Parses the 
	 * response to check for validity then passes
	 * the results back to the ClassificationPanel
	 */
	public void onResponseReceived(Request request, Response response) {
		JSONObject jObj = this.parseJSON(response);
		if(jObj != null){
			JSONObject classification = jObj.get("result").isObject();
			if(classification != null){
				this.updateForm(classification);
			}
		}

	}
	
	/**
	 * Extracts the data from the response and updates
	 * the ClassificationPanel
	 * @param classification The classification object returned from the server
	 */
	private void updateForm(JSONObject classification){
		/*
		String uuid = "";
		if(classification.get("uuid") != null){
			uuid = classification.get("uuid").isString().stringValue();
		}
		*/
		
		String name = "";
		if(classification.get("Value") != null){
			name = classification.get("Value").isString().stringValue();
		}
		
		double retention = 0;
		if(classification.get("Retention") != null){
			retention = classification.get("Retention").isNumber().getValue();
		}
		
		JSONObject secLevelObj = null;
		String secLevel = "";
		if(classification.get("SecLevel") != null){
			secLevelObj = classification.get("SecLevel").isObject();
			String sname = secLevelObj.get("Name").isString().stringValue();
			double slevel = secLevelObj.get("Level").isNumber().getValue();
			secLevel = Double.toString(slevel);
			int pos = secLevel.indexOf(".");
			if(pos > 0){
				secLevel = secLevel.substring(0,pos);
			}
			secLevel = "[" + secLevel + "] " + sname;
		}
		
		ArrayList caveats = new ArrayList();
		if (classification.get("Caveats") != null){
			JSONArray c = null;
			c = classification.get("Caveats").isArray();
			for(int i=0;i<c.size();i++){
				JSONObject caveat = c.get(i).isObject();
				if(caveat.get("Name") != null){
					caveats.add(caveat.get("Name").isString().stringValue());
				}
			}
		}
		
		String parentUUID = "";
		if(classification.get("Parent") != null){
			parentUUID = classification.get("Parent").isString().stringValue();
		}
		
		this.parent.setForm(name, retention, secLevel, caveats, parentUUID);
	}

}
