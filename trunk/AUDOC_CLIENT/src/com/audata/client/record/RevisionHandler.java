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
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * @author jonm
 *
 */
public class RevisionHandler extends BaseRequestCallback {

    private Revisions parent;
    
    public RevisionHandler(Revisions parent){
	this.parent = parent;
    }
    
    /* (non-Javadoc)
     * @see com.google.gwt.http.client.RequestCallback#onResponseReceived(com.google.gwt.http.client.Request, com.google.gwt.http.client.Response)
     */
    public void onResponseReceived(Request request, Response response) {
	// TODO Auto-generated method stub
	JSONObject rObj = this.parseJSON(response);
	if(rObj != null){
	    JSONArray docs = rObj.get("result").isArray();
	    if(docs != null){
		this.extractRevision(docs);
	    }
	}
    }
    
    private void extractRevision(JSONArray docs){
	for(int i=0;i<docs.size();i++){
	   JSONObject doc = docs.get(i).isObject();
	   String uuid = doc.get("uuid").isString().stringValue();
	   int revision = (int)doc.get("Revision").isNumber().getValue();
	   String name = doc.get("Name").isString().stringValue();
	   this.parent.addRevision(uuid, revision, name);
	}
    }

}
