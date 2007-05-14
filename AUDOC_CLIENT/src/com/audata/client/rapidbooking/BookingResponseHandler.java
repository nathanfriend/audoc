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
package com.audata.client.rapidbooking;

import com.audata.client.Language;
import com.audata.client.json.BaseRequestCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;

/**
 * @author jonm
 *
 */
public class BookingResponseHandler extends BaseRequestCallback {

    private static final Language LANG = (Language) GWT.create(Language.class);
    
    private RapidBookingDialog parent;
	
    public BookingResponseHandler(RapidBookingDialog parent){
	this.parent = parent;
    }
    
    /* (non-Javadoc)
     * @see com.google.gwt.http.client.RequestCallback#onResponseReceived(com.google.gwt.http.client.Request, com.google.gwt.http.client.Response)
     */
    public void onResponseReceived(Request request, Response response) {
	JSONObject jObj = this.parseJSON(response);
	if(jObj != null){
		JSONObject record = jObj.get("result").isObject();
			this.addRecord(record);
	}
    }
    
    private void addRecord(JSONObject record){
	String recnum = record.get("RecordNumber").isString().stringValue();
	String cot = record.get("CheckedOutTo").isString().stringValue();
	String action = "";
	if(cot == ""){
	    action = LANG.checked_in_Text();
	}else{
	    action = LANG.checked_out_Text() + " " +cot;
	}
	this.parent.addProcess(recnum, action);
    }

}
