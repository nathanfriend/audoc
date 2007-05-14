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

import java.util.ArrayList;

import com.audata.client.AuDoc;
import com.audata.client.feedback.ResponseListener;
import com.audata.client.json.NoOpResponseHandler;
import com.audata.client.json.UpdateResponseHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;

/**
 * @author jonm
 *
 */
public class DeleteCallback implements ResponseListener {

	private ArrayList records;
	private RecordListPanel parent;
	
	public DeleteCallback(RecordListPanel parent, ArrayList records){
		this.parent = parent;
		this.records = records;
	}
	
	/* (non-Javadoc)
	 * @see com.audata.client.feedback.ResponseListener#onResponse(boolean)
	 */
	public void onResponse(boolean response) {
		if(response){
			for(int i=0;i<this.records.size();i++){
				String uuid = (String)this.records.get(i);
				JSONArray params = new JSONArray();
				params.set(0, new JSONString(uuid));
				if(i == (this.records.size() - 1)){
					AuDoc.jsonCall.asyncPost2("recman.delRecord", params, new UpdateResponseHandler(this.parent));
				}else{
					AuDoc.jsonCall.asyncPost2("recman.delRecord", params, new NoOpResponseHandler());
				}
				
			}
		}
	}

}
