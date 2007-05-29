/* +----------------------------------------------------------------------+
 * | AUDOC_CLIENT                                                      |
 * +----------------------------------------------------------------------+
 * | Copyright (c) 2007 Audata Ltd                                     |
 * +----------------------------------------------------------------------+
 * | This source file is subject to version 2 of the Gnu Public License,  |
 * | that is bundled with this package in the file License.txt, and is    |
 * | available at through the world-wide-web at                           |
 * | http://www.gnu.org/licenses/gpl.txt.                                 |
 * | If you did not receive a copy of the GPL license and are unable to   |
 * | obtain it through the world-wide-web, please send a note to          |
 * | support@audata.co.uk so we can mail you a copy immediately.          |
 * +----------------------------------------------------------------------+
 * | Authors: jonm													  |
 * +----------------------------------------------------------------------+
 */
package com.audata.client.record;

import com.audata.client.json.BaseRequestCallback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;

/**
 * @author jonm
 *
 */
public class CountHandler extends BaseRequestCallback {

    private RecordListPanel parent;
    
    
    public CountHandler(RecordListPanel parent){
	this.parent = parent;
    }
    
    /* (non-Javadoc)
     * @see com.google.gwt.http.client.RequestCallback#onResponseReceived(com.google.gwt.http.client.Request, com.google.gwt.http.client.Response)
     */
    public void onResponseReceived(Request request, Response response) {
	JSONObject oResponse = this.parseJSON(response);
	if(oResponse != null){
	    Double result = new Double(oResponse.get("result").isNumber().getValue());
	    this.parent.setCount(result.intValue());
	}
	
    }

}
