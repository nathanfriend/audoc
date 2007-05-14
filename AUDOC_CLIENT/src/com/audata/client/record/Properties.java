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
import java.util.HashMap;
import java.util.Iterator;

import org.gwtwidgets.client.wrap.Callback;
import org.gwtwidgets.client.wrap.Effect;
import org.gwtwidgets.client.wrap.EffectOption;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.feedback.UploadHandler;
import com.audata.client.feedback.UploadListener;
import com.audata.client.json.UpdateListener;
import com.audata.client.json.UpdateResponseHandler;
import com.audata.client.util.FieldTypes;
import com.audata.client.widgets.Field;
import com.audata.client.widgets.UploadPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Properties extends DialogBox implements Callback, ClickListener, UploadListener{

	private static final Language LANG = (Language) GWT.create(Language.class);
	private ArrayList fields;
	private UploadPanel upload;
	private String rType;
	private String record;
	private String cot;
	private VerticalPanel main;
	private HorizontalPanel butPanel;
	private Button ok;
	private Button cancel;
	private UpdateListener parent;
	
	public Properties(UpdateListener parent, String rType, String record, String CheckedOutTo){
		this.parent = parent;
		this.setText(LANG.record_props_Text());
		this.main = new VerticalPanel();
		this.rType = rType;
		this.record = record;
		this.cot = CheckedOutTo;
		this.fields = new ArrayList();
		
		this.butPanel = new HorizontalPanel();
		this.butPanel.setSpacing(4);
		this.ok = new Button(LANG.save_Text());
		this.ok.addClickListener(this);
		this.butPanel.add(this.ok);
		
		this.cancel = new Button(LANG.cancel_Text());
		this.cancel.addClickListener(this);
		this.butPanel.add(this.cancel);
		
		//prepare upload panel
		this.upload = new UploadPanel();
		String url = AuDoc.jsonCall.getURL();
		this.upload.setAction(url + "docIO.php");
		this.upload.setEncoding(FormPanel.ENCODING_MULTIPART);
	    this.upload.setMethod(FormPanel.METHOD_POST);
		
		//load default fields
		this.addFields();
		this.setWidget(this.main);
	}
	
	private void addFields(){
		this.fields.add(new Field(LANG.rec_num_Text(), FieldTypes.TYPE_RECNUM, false));
		this.fields.add(new Field(LANG.classification_Text(), FieldTypes.TYPE_CLASS, false));
		this.fields.add(new Field(LANG.title_Text(), FieldTypes.TYPE_STRING, false));
		this.fields.add(new Field(LANG.date_created_Text(), FieldTypes.TYPE_DATE, false));
		this.fields.add(new Field(LANG.owner_Text(), FieldTypes.TYPE_STRING, false));
		this.fields.add(new Field(LANG.author_Text(), FieldTypes.TYPE_STRING, false));
		this.fields.add(new Field(LANG.notes_Text(), FieldTypes.TYPE_NOTES, false));
		//obtain udfs for given record type
		JSONArray params = new JSONArray();
		params.set(0, new JSONString(this.rType));
		AuDoc.jsonCall.asyncPost2("recman.getRecType", params, new RecTypeCallback(this));
	}
	
	/**
	 * Add the udfs for this record type
	 * @param udfs An array of Fields to add to field list
	 */
	public void addUdfs(Field[] udfs){
		for(int i=0;i<udfs.length;i++){
			this.fields.add(udfs[i]);
		}
	}
	/**
	 * Called when all the udfs have been added
	 *
	 */
	public void paint(){
		this.main.clear();
		for(int i=0; i<this.fields.size();i++){
			Field f = (Field)this.fields.get(i);
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(5);
			
			Label l = new Label(f.name);
			l.setWidth("150px");
			l.addStyleName("audoc-label");
			l.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
			Widget w = null;
			w = f.getField();
			Label n = new Label("");
			n.addStyleName("audoc-note");
			switch(f.type){
			case FieldTypes.TYPE_INT:
				n.setText(LANG.int_Text());
				break;
			case FieldTypes.TYPE_DEC:
				n.setText(LANG.dec_Text());
				break;
			case FieldTypes.TYPE_DATE:
				n.setText(LANG.date_format_Text());
				break;
			case FieldTypes.TYPE_STRING:
				break;
			case FieldTypes.TYPE_KEYWORD:
				break;
			case FieldTypes.TYPE_CLASS:
				n.setText(LANG.req_field_marker_Text());
				n.removeStyleName("audoc-note");
				n.addStyleName("audoc-required");
				break;
			case FieldTypes.TYPE_NOTES:
				break;
			case FieldTypes.TYPE_RECNUM:
				f.setReadOnly();
			}
			hp.add(l);
			hp.add(w);
			hp.add(n);
			this.main.add(hp);
		}
		if((this.cot.equals(AuDoc.state.getItem("username"))) || (this.cot.equals(""))){
			Label newRev = new Label(LANG.check_in_new_doc_Text());
			newRev.addStyleName("audoc-subTitle");
			this.main.add(newRev);
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(5);
			
			Label l = new Label(LANG.document_Text());
			l.setWidth("150px");
			l.addStyleName("audoc-label");
			l.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
			hp.add(l);
			hp.add(this.upload);
			this.main.add(hp);
		}
		this.main.add(this.butPanel);
		this.getRecord();
		this.show();
	}
	
	/**
	 * Overide parent show method to add effects
	 * and dialog centering
	 */
	public void show(){
		this.setVisible(false);
		super.show();
		Effect.appear(this);
		int left = 20;
		int top = 20;
		this.setPopupPosition(left,top);
	}
	
	/**
	 * Overide parent hide method to add
	 * effects
	 */
	public void hide(){
		Effect.fade(this, new EffectOption[]{new EffectOption("afterFinish", this)});
	}
	
	/**
	 * Called when hide effect are complete
	 * to finish hide process
	 */
	public void execute(){
		super.hide();
	}
	
	public void onClick(Widget sender){
		if(sender == this.cancel){
			this.hide();
			return;
		}
		if(sender == this.ok){
			HashMap fields = new HashMap();
			HashMap udfs = new HashMap();
			for(int i=0;i<this.fields.size();i++){
				Field f = (Field)this.fields.get(i);
				if(f.isUDF){
					udfs.put(f.name, f.getText());
				}else{
					fields.put(f.name, f.getText());
				}
			}			
			this.saveRecord(fields, udfs);
			return;
		}
	}
	
	private void getRecord(){
		JSONArray params = new JSONArray();
		params.set(0, new JSONString(this.record));
		AuDoc.jsonCall.asyncPost2("recman.getRecord", params, new PropertiesCallback(this));
	}
	
	public void setRecord(HashMap record){
		for(int i=0;i<this.fields.size();i++){
			Field f = (Field)this.fields.get(i);
			String name = f.name;
			//handle udfs (nice and easy)
			if(f.isUDF){
				switch(f.type){
				case FieldTypes.TYPE_KEYWORD:
					//Keyword
					String kname = (String)record.get(name+"Text");
					String ku = (String)record.get(name);
					if((kname != null) && (ku != null)){
						if((!kname.equals("")) && (!ku.equals(""))){
							f.setValue(kname, ku);
						}
					}
					break;
				default:
					String value = (String)record.get(name);
					f.setText(value);
					break;
				}
			}else{
				switch(f.type){
				case FieldTypes.TYPE_CLASS:
					//Do thing differently
					String cname = (String)record.get("ClassPath");
					String value = (String)record.get("Classification");
					f.setValue(cname, value);
					break;
				default:
//					we must be dealing with a standard field
					String v = (String)record.get(name);
					f.setText(v);
				}
			}
		}
	}
	
	/**
	 * $uuid, $title, $classID, $datec, $owner, $author, $notes, $recNum, $UDFs
	 * @param fields
	 * @param udfs
	 */
	private void saveRecord(HashMap fields, HashMap udfs){
		JSONArray params = new JSONArray();
		params.set(0, new JSONString(this.record));
		params.set(1, new JSONString((String)fields.get(LANG.title_Text())));
		params.set(2,new JSONString((String)fields.get(LANG.classification_Text())));
		params.set(3, new JSONString((String)fields.get(LANG.date_created_Text())));
		params.set(4,new JSONString((String)fields.get(LANG.owner_Text())));
		params.set(5,new JSONString((String)fields.get(LANG.author_Text())));
		params.set(6,new JSONString((String)fields.get(LANG.notes_Text())));
		params.set(7,new JSONString((String)fields.get(LANG.rec_num_Text())));
		
		JSONObject u = new JSONObject();
		Iterator i = udfs.keySet().iterator();
		while(i.hasNext()){
			try{
				String name = (String)i.next();
				String value = (String)udfs.get(name);
				u.put(name, new JSONString(value));
			}catch(Exception e){}
		}
		
		params.set(8, u);
		AuDoc.jsonCall.asyncPost2("recman.modRecord", params, new PropertiesSavedCallback(this));
	}
	
	public void saveDoc(){
		if(this.cot.equals(AuDoc.state.getItem("username")) || this.cot == ""){
			JSONArray params = new JSONArray();
			params.set(0, new JSONString(this.record));
			AuDoc.jsonCall.asyncPost2("recman.checkin", params, new UpdateResponseHandler(this.parent));
			this.upload.addFormHandler(new UploadHandler(this));
			this.upload.submit(this.record);
		}
		//is this the part causing the issue??
		this.hide();
	}
	
	public void onUploadComplete(){
		//this.hide();
	}
}
