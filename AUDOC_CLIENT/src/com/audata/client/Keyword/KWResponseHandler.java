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
package com.audata.client.Keyword;

import com.audata.client.json.BaseRequestCallback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.TreeItem;

public class KWResponseHandler extends BaseRequestCallback {

	private KeywordBrowser browser;
	private TreeItem parent;
	
	public KWResponseHandler(KeywordBrowser browser, TreeItem parent){
		this.browser = browser;
		this.parent = parent;
		
	}
	public void onResponseReceived(Request request, Response response) {
		this.browser.setHeight("100%");
		JSONObject jObj = this.parseJSON(response);
		if(jObj != null){
			JSONArray classes = jObj.get("result").isArray();
			if(classes != null){
				this.addClasses(classes);
			}
		}
	}
		
	private void addClasses(JSONArray classes){
		if(this.parent != null){
			this.parent.removeItems();
		}
		if(classes.size() == 0){
			this.browser.finishedLoading();
			return;
		}
		for(int i=0;i<classes.size();i++){
			JSONObject c = classes.get(i).isObject();
			String uuid = c.get("uuid").isString().stringValue();
			String name = c.get("Value").isString().stringValue();
			String path = c.get("Path").isString().stringValue();
			boolean hasChildren = c.get("HasChildren").isBoolean().booleanValue();
			if(this.parent != null){
				this.browser.addClass(name, uuid, this.parent, hasChildren, path);
			}else{
				this.browser.addClass(name, uuid, hasChildren, path);
			}
		}
	}

}
