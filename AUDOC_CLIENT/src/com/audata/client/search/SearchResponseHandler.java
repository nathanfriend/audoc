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
package com.audata.client.search;

import java.util.ArrayList;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.json.BaseRequestCallback;
import com.audata.client.record.RecordListPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

public class SearchResponseHandler extends BaseRequestCallback {

	private static final Language LANG = (Language) GWT.create(Language.class);
	public static final int TYPE_QUICK = 0;
	public static final int TYPE_FULL = 1;
	public static final int TYPE_SAVED = 2;
	
	private int method;
	private JSONArray params;
	private AuDoc audoc;
	
	public SearchResponseHandler(AuDoc audoc, int method, JSONArray params){
		this.audoc = audoc;
		this.method = method;
		this.params = params;
	}
	
	public void onResponseReceived(Request request, Response response) {
		
		JSONObject jObj = this.parseJSON(response);
		if(jObj != null){
			JSONArray records = jObj.get("result").isArray();
			RecordListPanel content = null;
			switch(this.method){
			case SearchResponseHandler.TYPE_QUICK:	
				content = new RecordListPanel(LANG.search_results_Text(), records, "search.quicksearch", this.params, null, getCriteria());
				break;
			case SearchResponseHandler.TYPE_FULL:
				content = new RecordListPanel(LANG.search_results_Text(), records, "search.complexsearch", this.params, null, getCriteria());
				break;
			case SearchResponseHandler.TYPE_SAVED:
				content = new RecordListPanel(LANG.search_results_Text(), records, "search.runsavedsearch", this.params);
			}
			this.audoc.setMain(content);
		}
		
		
	}
	
	private String getCriteria(){
		String ret = "";
		switch(this.method){
		case SearchResponseHandler.TYPE_QUICK:
			ret = params.get(0).isString().stringValue();
			break;
		case SearchResponseHandler.TYPE_FULL:
			ArrayList searchTerms = new ArrayList();
			if(AuDoc.state.containsKey("Search")){
				searchTerms = (ArrayList)AuDoc.state.getItem("Search");
			}else{
				searchTerms = new ArrayList();
			}
			for(int i=0;i<searchTerms.size();i++){
				Criteria c = (Criteria)searchTerms.get(i);
				String sterms = c.getDisplayString();
				if(i==0){
					int pos = sterms.indexOf(" ");
					sterms = sterms.substring(pos+1);
				}
				ret = ret + sterms + " ";
			}
			ret = ret.trim();
			break;
		default:
			ret = "";
		}
		
		return ret;
	}

}
