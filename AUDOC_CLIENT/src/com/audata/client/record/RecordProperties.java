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

import java.util.ArrayList;
import java.util.HashMap;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.feedback.UploadHandler;
import com.audata.client.util.FieldTypes;
import com.audata.client.widgets.Field;
import com.audata.client.widgets.UploadPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author jonm
 *
 */
public class RecordProperties extends Composite {

    private static final Language LANG = (Language) GWT.create(Language.class);
    
    private UploadPanel upload;
    private String cot;
    private VerticalPanel main;
    private Label title;
    private RecordPropertiesDialog parent;
    
    public RecordProperties(RecordPropertiesDialog parent, String cot){
	
	this.parent = parent;
	this.cot = cot;
	
	this.main = new VerticalPanel();
	this.main.setSpacing(3);
	this.title = new Label(LANG.record_props_Text());
	this.title.addStyleName("audoc-subsection");
	
	//prepare upload panel
	this.upload = new UploadPanel();
	String url = AuDoc.jsonCall.getURL();
	this.upload.setAction(url + "docIO.php");
	this.upload.setEncoding(FormPanel.ENCODING_MULTIPART);
	this.upload.setMethod(FormPanel.METHOD_POST);
	
	//load default fields
	this.initWidget(this.main);
}


/**
 * Called when all the udfs have been added
 *
 */
public void paint(){
	this.main.clear();
	ArrayList fields = this.parent.getFields();
	for(int i=0; i<fields.size();i++){
		Field f = (Field)fields.get(i);
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
}






public void setRecord(HashMap record){
    	ArrayList fields = this.parent.getFields(); 
	for(int i=0;i<fields.size();i++){
		Field f = (Field)fields.get(i);
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
//				we must be dealing with a standard field
				String v = (String)record.get(name);
				f.setText(v);
			}
		}
	}
}

protected void submit(UploadHandler handler, String record){
    	this.upload.addFormHandler(handler);
	this.upload.submit(record);
}
}
