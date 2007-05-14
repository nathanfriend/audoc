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

import java.util.ArrayList;
import java.util.HashMap;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.util.FieldTypes;
import com.audata.client.widgets.Field;
import com.audata.client.widgets.UploadPanel;
import com.audata.client.widgets.Wizard;
import com.audata.client.widgets.WizardPage;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Metadata extends WizardPage {
	
	private static final Language LANG = (Language) GWT.create(Language.class);
	private VerticalPanel main;
	private ArrayList fields;
	private ArrayList textboxes;
	private UploadPanel upload;
	private String rTypeName;

	public Metadata(){
		
		
		
		this.main = new VerticalPanel();
		this.main.setSpacing(4);
		this.fields = new ArrayList();
		this.textboxes = new ArrayList();
		this.initWidget(this.main);
	}
	
	private void addFields(){
		this.fields.add(new Field(LANG.rec_num_Text(), FieldTypes.TYPE_RECNUM, false));
		this.fields.add(new Field(LANG.classification_Text(), FieldTypes.TYPE_CLASS, false));
		this.fields.add(new Field(LANG.title_Text(), FieldTypes.TYPE_STRING, false));
		this.fields.add(new Field(LANG.date_created_Text(), FieldTypes.TYPE_DATE, false));
		this.fields.add(new Field(LANG.owner_Text(), FieldTypes.TYPE_STRING, false));
		this.fields.add(new Field(LANG.author_Text(), FieldTypes.TYPE_STRING, false));
		this.fields.add(new Field(LANG.notes_Text(), FieldTypes.TYPE_NOTES, false));
	}
	
	public boolean isValid() {
		boolean ret = true;
		
		for(int i=0;i<this.fields.size();i++){
			Field f = (Field)this.fields.get(i);
			if((f.name.equals(LANG.rec_num_Text())) || (f.name.equals(LANG.classification_Text()))){
				if (f.getText().equals("")){
					ret = false;
				}
			}
		}
		return ret;
	}

	public HashMap getValues() {
		HashMap ret = new HashMap();
		HashMap fields = new HashMap();
		HashMap udfs = new HashMap();
		for(int i=0;i<this.fields.size();i++){
			Field f = (Field)this.fields.get(i);
			if(f != null){
				if(f.isUDF){
					udfs.put(f.name, f.getText());
				}else{
					fields.put(f.name, f.getText());
				}
			}
		}
		ret.put("fields", fields);
		ret.put("udfs", udfs);
		ret.put("upload", this.upload);
		ret.put("filename", this.upload.getText());
		return ret;
	}

	public void onShow(Wizard parent) {
		this.upload = new UploadPanel();
		String url = AuDoc.jsonCall.getURL();
		this.upload.setAction(url + "docIO.php");
		this.upload.setEncoding(FormPanel.ENCODING_MULTIPART);
	    this.upload.setMethod(FormPanel.METHOD_POST);
		
		this.fields.clear();
		this.addFields();
		HashMap v = parent.getValues();
		ArrayList a = (ArrayList)v.get("udfields");
		this.rTypeName = (String)v.get("typename");
		this.fields.addAll(a);
		this.paintFields();
	}
	
	private void paintFields(){
		this.main.clear();
		//Display Record Type
		Label type = new Label(this.rTypeName);
		type.setWidth("150px");
		type.addStyleName("audoc-sectionSubTitle");
		this.main.add(type);
		
		//Add Fields
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
				n.setText(LANG.req_field_marker_Text());
				n.removeStyleName("audoc-note");
				n.addStyleName("audoc-required");
			}
			hp.add(l);
			hp.add(w);
			hp.add(n);
			this.main.add(hp);
		}
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(5);
		
		Label l = new Label(LANG.document_Text());
		l.setWidth("150px");
		l.addStyleName("audoc-label");
		l.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
		hp.add(l);
		hp.add(this.upload);
		this.main.add(hp);
		
		Label req = new Label();
		req.setText(LANG.req_field_Text());
		req.addStyleName("audoc-required");
		this.main.add(req);
	}

	public void reset(){
		this.main.clear();
		this.fields.clear();
		this.textboxes.clear();
	}
	
	public void submit(String uuid){
		this.upload.submit(uuid);
	}
}
