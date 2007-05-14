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
package com.audata.client.newRecord;

import java.util.HashMap;
import java.util.Iterator;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.feedback.SimpleDialog;
import com.audata.client.feedback.UploadHandler;
import com.audata.client.widgets.UploadPanel;
import com.audata.client.widgets.Wizard;
import com.audata.client.widgets.WizardListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class NewRecordListener implements WizardListener {

	private static final Language LANG = (Language) GWT.create(Language.class);
	private Wizard wizard;
	private NewRecord n;
	private UploadHandler upHandler;
	
	public NewRecordListener(NewRecord n){
		this.n = n ;
	}
	
	public void onFinish(Wizard wizard, HashMap values) {
		this.wizard = wizard;
		this.upHandler = new UploadHandler(this.n);
		HashMap fields = (HashMap)values.get("fields");
			
		//$title, $classID, $datec, $owner, $author, $notes, $rTypeID, $recNum, $UDFs
		JSONArray params = new JSONArray();
		try{
			params.set(0, new JSONString((String)fields.get(LANG.title_Text())));
			params.set(1,new JSONString((String)fields.get(LANG.classification_Text())));
			params.set(2, new JSONString((String)fields.get(LANG.date_created_Text())));
			params.set(3,new JSONString((String)fields.get(LANG.owner_Text())));
			params.set(4,new JSONString((String)fields.get(LANG.author_Text())));
			params.set(5,new JSONString((String)fields.get(LANG.notes_Text())));
			params.set(6,new JSONString((String)values.get("typeuuid")));
			params.set(7,new JSONString((String)fields.get(LANG.rec_num_Text())));
			
			HashMap udfs = (HashMap)values.get("udfs");
			params.set(8,this.formatUDFs(udfs));
			
			String filename = (String)values.get("filename");
			UploadPanel upload = (UploadPanel)values.get("upload");
			upload.addFormHandler(this.upHandler);
			this.newRecord(params, filename, upload);
			
		}catch(Exception ex){
			SimpleDialog.displayDialog(SimpleDialog.TYPE_ERROR, LANG.error_Text(), LANG.new_rec_error_Text());
		}
		
		
		
	}
	
	private void newRecord(JSONArray params, String filename, UploadPanel upload){
		AuDoc.jsonCall.asyncPost2("recman.addRecord", params, new NewRecordResponseHandler(this.wizard, filename, upload));
	}
	
	private JSONObject formatUDFs(HashMap udfs){
		JSONObject ret = new JSONObject();
		Iterator i = udfs.keySet().iterator();
		while(i.hasNext()){
			try{
				String name = (String)i.next();
				String value = (String)udfs.get(name);
				ret.put(name, new JSONString(value));
			}catch(Exception e){}
		}
		return ret;
	}

}
