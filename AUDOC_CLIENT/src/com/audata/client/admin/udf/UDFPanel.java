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
package com.audata.client.admin.udf;

import java.util.HashMap;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.feedback.SimpleDialog;
import com.audata.client.json.UpdateListener;
import com.audata.client.json.UpdateResponseHandler;
import com.audata.client.util.FieldTypes;
import com.audata.client.widgets.HTMLButton;
import com.audata.client.widgets.HTMLButtonList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UDFPanel extends VerticalPanel implements UpdateListener, ClickListener, ChangeListener{

	
	
	private static final Language LANG = (Language) GWT.create(Language.class);
	private HTMLButtonList userDFs;
	private TextBox name;
	private ListBox types;
	private ListBox keywords;
	private Panel kwp;
	private String selectedUUID = null; 
	
	private Button addBut;
	private Button saveBut;
	private Button delBut;
	
	public UDFPanel(){
		
		this.setSize("100%", "100%");
		
		//section title
		Label l = new Label(LANG.udf_Text());
		l.addStyleName("audoc-sectionTitle");
		this.add(l);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(5);
		
		//add UDF list
		String template = "<span class=\"audoc-udf-title\">#0</span><br/><span class=\"audoc-udf-type\">#1</span>"; 
		this.userDFs = new HTMLButtonList("images/48x48/udf.gif", template, false);
		this.userDFs.addClickListener(this);
		this.userDFs.addStyleName("audoc-udfs");
		this.userDFs.setPixelSize(200, 300);
		hp.add(this.userDFs);
		
		VerticalPanel form = new VerticalPanel();
		form.addStyleName("audoc-form");
		
		//name
		HorizontalPanel namePanel = new HorizontalPanel();
		Label nameLabel = new Label(LANG.name_Text());
		nameLabel.setWidth("100px");
		namePanel.add(nameLabel);
		
		this.name = new TextBox();
		namePanel.add(this.name);
		form.add(namePanel);
		
		//type
		HorizontalPanel typePanel = new HorizontalPanel();
		Label typeLabel = new Label(LANG.type_Text());
		typeLabel.setWidth("100px");
		typePanel.add(typeLabel);
		
		this.types = new ListBox();
		this.types.addChangeListener(this);
		this.popTypes();
		typePanel.add(this.types);
		form.add(typePanel);
		
		//Keyword
		this.kwp = new HorizontalPanel();
		this.kwp.setVisible(false);
		Label kwl = new Label(LANG.keywords_Text());
		kwl.setWidth("100px");
		this.kwp.add(kwl);
		this.keywords = new ListBox();
		this.keywords.setVisibleItemCount(1);
		this.kwp.add(this.keywords);
		form.add(this.kwp);
		//buttons
		HorizontalPanel butPanel = new HorizontalPanel();
		butPanel.setSpacing(5);
		
		this.addBut = new Button(LANG.new_Text());
		this.addBut.addClickListener(this);
		butPanel.add(this.addBut);
		
		this.saveBut = new Button(LANG.save_Text());
		this.saveBut.addClickListener(this);
		butPanel.add(this.saveBut);
		
		this.delBut = new Button(LANG.delete_Text());
		this.delBut.addClickListener(this);
		butPanel.add(this.delBut);
		
		form.add(butPanel);
		
		hp.add(form);
		
		//Add hp to panel
		this.add(hp);
		
		//populate lists
		this.getUDFs();
		this.getKWHs();
	}
	
	public void onUpdate(){
		this.userDFs.clear();
		this.getUDFs();
		this.name.setText("");
		this.types.setItemSelected(0, true);
		this.types.setEnabled(false);
	}
	
	private void getUDFs(){
		JSONArray params = new JSONArray();
		AuDoc.jsonCall.asyncPost2("recman.getUDFs", params, new UDFResponseHandler(this));
	}
 
	public void addUDF(String uuid, String name, int type, String kwh){
		String[] caption = new String[2];
		caption[0] = name;
		switch(type){
			case(FieldTypes.TYPE_INT):
				caption[1] = LANG.type_integer_Text();
				break;
			case(FieldTypes.TYPE_DEC):
				caption[1] = LANG.type_decimal_Text();
				break;
			case(FieldTypes.TYPE_DATE):
				caption[1] = LANG.type_date_Text();
				break;
			case(FieldTypes.TYPE_STRING):
				caption[1] = LANG.type_string_Text();
				break;
			case(FieldTypes.TYPE_KEYWORD):
				caption[1] = LANG.type_keyword_Text();
				break;
			default:
				caption[1] = LANG.type_unknown_Text();
		}
		HashMap custom = new HashMap();
		custom.put("uuid", uuid);
		custom.put("name", name);
		custom.put("type", new Integer(type));
		custom.put("kwh", kwh);
		
		
		this.userDFs.addItem(caption, null,  custom);
	}
	/*
	public void updateForm(String uuid, String name, int type, String kwh){
		this.selectedUUID = uuid;
		this.name.setText(name);
		this.types.setItemSelected(type, true);
		this.types.setEnabled(false);
		if(type == FieldTypes.TYPE_KEYWORD){
			if(kwh != null){
				for(int i=0;i<this.keywords.getItemCount();i++){
					String u = this.keywords.getValue(i);
					if(u.equals(kwh)){
						this.keywords.setItemSelected(i, true);
					}
				}
			}
			this.kwp.setVisible(true);
			this.keywords.setEnabled(false);

		}
	}
*/
	/*
	private void paintUDFs(){
		this.userDFs.clear();
		Iterator it  = this.udfs.keySet().iterator();
		while(it.hasNext()){
			Object key = it.next();
			String name = (String)this.udfs.get(key);
			this.userDFs.addItem(name);
		}
	}
	*/
	private void popTypes(){
		this.types.addItem(LANG.type_integer_Text());
		this.types.addItem(LANG.type_decimal_Text());
		this.types.addItem(LANG.type_date_Text());
		this.types.addItem(LANG.type_string_Text());
		this.types.addItem(LANG.type_keyword_Text());
	}
	
	public void onClick(Widget sender){
		
		//setup to add a new UDF
		if(sender == this.addBut){
			this.name.setText("");
			this.types.setItemSelected(0, true);
			this.types.setEnabled(true);
			this.selectedUUID = null;
			return;
		}
		
		//delete selected UDF
		if(sender == this.delBut){
			if(this.selectedUUID != null){
				JSONArray params = new JSONArray();
				params.set(0,new JSONString(this.selectedUUID));
				this.selectedUUID = null;
				AuDoc.jsonCall.asyncPost2("recman.delUDF", params, new UpdateResponseHandler(this));
			}else{
				SimpleDialog.displayDialog(SimpleDialog.TYPE_MESSAGE, "Warning","You must select a User Defined Field before you can delete it");
			}
			this.kwp.setVisible(false);
			return;
		}
		
		//Save the UDFDef
		if(sender == this.saveBut){
			JSONArray params = new JSONArray();
			String method = "";
			if(this.name.getText().equals("")){
				SimpleDialog.displayDialog(SimpleDialog.TYPE_MESSAGE,"Unable to Save", "You must supply a name for the User Defined Field");
			}else{
				if(this.selectedUUID != null){
					//update
					method = "recman.modUDF";
					params.set(0,new JSONString(this.selectedUUID));
					params.set(1,new JSONString(this.name.getText()));
				}else{
					//new
					method = "recman.addUDF";
					params.set(0,new JSONString(this.name.getText()));
					params.set(1,new JSONNumber(this.types.getSelectedIndex()));
					if(this.types.getSelectedIndex() == FieldTypes.TYPE_KEYWORD){
						params.set(2, new JSONString(this.keywords.getValue(this.keywords.getSelectedIndex())));
					}
				}
				AuDoc.jsonCall.asyncPost2(method, params, new UpdateResponseHandler(this));
			}
			this.kwp.setVisible(false);
			return;
		}
		
		//user clicked on a udf item
		if(this.userDFs.indexOf(sender) != -1){
			HTMLButton hb = (HTMLButton)sender;
			this.onChange(hb);
		}
		
	}
	
	private void onChange(HTMLButton sender){
		HashMap custom = (HashMap)sender.custom;
		this.selectedUUID = (String)custom.get("uuid");
		this.name.setText((String)custom.get("name"));
		Integer typeObj = (Integer)custom.get("type");
		this.types.setItemSelected(typeObj.intValue(), true);
		this.types.setEnabled(false);
		if(typeObj.intValue() == FieldTypes.TYPE_KEYWORD){
			String kwh = (String)custom.get("kwh");
			if(kwh != null){
				for(int i=0;i<this.keywords.getItemCount();i++){
					String u = this.keywords.getValue(i);
					if(u.equals(kwh)){
						this.keywords.setItemSelected(i, true);
					}
				}
			}
			this.kwp.setVisible(true);
			this.keywords.setEnabled(false);
		}else{
			this.kwp.setVisible(false);
		}
	}
	
	public void onChange(Widget sender){
		if(sender == this.types){
			String type = this.types.getItemText(this.types.getSelectedIndex());
			if(type.equals(LANG.type_keyword_Text())){
				this.kwp.setVisible(true);
			}else{
				this.kwp.setVisible(false);
			}
		}
	}
	
	private void getKWHs(){
		AuDoc.jsonCall.asyncPost2("recman.getKWHs", new JSONArray(), new KWHResponseHandler(this));
	}
	
	public void addKWH(String name, String uuid){
		this.keywords.addItem(name, uuid);
	}
}
