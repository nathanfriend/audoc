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

import com.audata.client.feedback.SimpleDialog;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.json.client.JSONArray;


public class JSONCall {

	private static int count;
	private String url;
	private int timeout;
	
	public JSONCall(String url, int timeout){
		JSONCall.count = 0;
		this.url = url;
		this.timeout = timeout;
	}
	
	public String getJSONMsg(String method, JSONArray params){
		String msg = "";
		JSONCall.count++;
		int id = JSONCall.count;
		
		msg = "{\"method\":\"" +
				method +
				"\",\"id\":\"" +
				id +
				"\",\"params\":" +
				params.toString() +
				"}";
		return msg;
	}
	
	public String getURL(){
		return this.url;
	}
	
	public Request asyncPost2(String method, JSONArray params, RequestCallback handler){
		String msg = this.getJSONMsg(method, params);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, this.url + "WebService_JSON.php");
		Request request = null;
	    try {
	      builder.setTimeoutMillis(this.timeout);
	      request = builder.sendRequest(msg, handler);
	    } catch (RequestException e) {
	    	SimpleDialog.displayDialog(SimpleDialog.TYPE_ERROR, "Error", "Unable to send request to server\n" + e.getMessage());
	    }
	    return request;
	}
	/*
	public long parseDate(String date){
		long ret = 0;
		String[] dStrings = date.split("-");
		int year = Integer.parseInt(dStrings[0]);
		int month = Integer.parseInt(dStrings[1]);
		int day = Integer.parseInt(dStrings[2]);

		Date d = new Date(year-1900, month-1, day);
		
		ret = d.getTime();
		return ret;
	}
	*/
	
}
