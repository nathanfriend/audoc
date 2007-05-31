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
package com.audata.client.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.gwtwidgets.client.wrap.Callback;
import org.gwtwidgets.client.wrap.Effect;
import org.gwtwidgets.client.wrap.EffectOption;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.feedback.SimpleDialog;
import com.audata.client.json.UpdateResponseHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserDialog extends DialogBox implements ClickListener, Callback{
	
	private static final Language LANG = (Language) GWT.create(Language.class);
	private UserPanel parent;
	
	private String uuid;
	private TextBox forename;
	private TextBox surname;
	private TextBox username;
	private PasswordTextBox pass1;
	private PasswordTextBox pass2;
	private CheckBox isAdmin;
	private ListBox secLevel;
	private ListBox caveats;
	
	private Button saveBut;
	private Button cancelBut;
	
	public UserDialog(UserPanel parent){
		this(parent, "", null);
	}
	
	public UserDialog(UserPanel parent, String uuid, HashMap details){
		this.parent = parent;
		this.uuid = uuid;
		if (this.uuid != ""){
			this.setText(LANG.edit_user_Text());
			//call details
		}else{
			this.setText(LANG.new_user_Text());
		}
		//build empty
		VerticalPanel main = new VerticalPanel();
		
		HorizontalPanel fname = new HorizontalPanel();
		fname.setSpacing(5);
		Label flabel = new Label(LANG.forename_Text());
		flabel.setWidth("150px");
		fname.add(flabel);
		this.forename = new TextBox();
		fname.add(this.forename);
		main.add(fname);
		
		HorizontalPanel sname = new HorizontalPanel();
		sname.setSpacing(5);
		Label slabel = new Label(LANG.surname_Text());
		slabel.setWidth("150px");
		sname.add(slabel);
		this.surname = new TextBox();
		sname.add(this.surname);
		main.add(sname);
		
		HorizontalPanel uname = new HorizontalPanel();
		uname.setSpacing(5);
		Label ulabel = new Label(LANG.username_Text());
		ulabel.setWidth("150px");
		uname.add(ulabel);
		this.username = new TextBox();
		uname.add(this.username);
		main.add(uname);
		if (this.uuid.equals("")){
			HorizontalPanel p1 = new HorizontalPanel();
			p1.setSpacing(5);
			Label p1label = new Label(LANG.password_Text());
			p1label.setWidth("150px");
			p1.add(p1label);
			this.pass1 = new PasswordTextBox();
			p1.add(this.pass1);
			main.add(p1);
			
			HorizontalPanel p2 = new HorizontalPanel();
			p2.setSpacing(5);
			Label p2label = new Label(LANG.password_reenter_Text());
			p2label.setWidth("150px");
			p2.add(p2label);
			this.pass2 = new PasswordTextBox();
			p2.add(this.pass2);
			main.add(p2);
		}
		
		HorizontalPanel adminPanel = new HorizontalPanel();
		adminPanel.setSpacing(5);
		Label adminlabel = new Label(LANG.is_admin_Text());
		adminlabel.setWidth("150px");
		adminPanel.add(adminlabel);
		this.isAdmin = new CheckBox();
		adminPanel.add(this.isAdmin);
		main.add(adminPanel);
		
		HorizontalPanel levPanel = new HorizontalPanel();
		levPanel.setSpacing(5);
		Label levlabel = new Label(LANG.security_level_Text());
		levlabel.setWidth("150px");
		levPanel.add(levlabel);
		this.secLevel = new ListBox();
		levPanel.add(this.secLevel);
		main.add(levPanel);
		
		HorizontalPanel cavPanel = new HorizontalPanel();
		cavPanel.setSpacing(5);
		Label cavlabel = new Label(LANG.security_caveats_Text());
		cavlabel.setWidth("150px");
		cavPanel.add(cavlabel);
		this.caveats = new ListBox();
		this.caveats.setMultipleSelect(true);
		this.caveats.setVisibleItemCount(5);
		cavPanel.add(this.caveats);
		main.add(cavPanel);
		
		HorizontalPanel butPanel = new HorizontalPanel();
		butPanel.setSpacing(5);
		this.saveBut = new Button(LANG.save_Text());
		this.saveBut.addClickListener(this);
		butPanel.add(this.saveBut);
		this.cancelBut = new Button(LANG.cancel_Text());
		this.cancelBut.addClickListener(this);
		butPanel.add(this.cancelBut);
		main.add(butPanel);
		
		this.popLists();
		
		this.setWidget(main);
		//this.show();
		int left = (Window.getClientWidth() - this.getOffsetWidth())/2;
		int top = (Window.getClientHeight() - this.getOffsetHeight())/2;
		this.setPopupPosition(left,top);
		
		if(details != null){
			this.setValues(details);
		}
	}
	
	public void onClick(Widget sender){
		if (sender == this.cancelBut){
			Effect.fade(this, new EffectOption[]{new EffectOption("afterFinish", this)});
			return;
		}
		
		if(sender == this.saveBut){
			if(this.uuid.equals("")){
				//new user
				this.newUser();
			}else{
				//modify user
				this.modUser();
			}
			Effect.fade(this, new EffectOption[]{new EffectOption("afterFinish", this)});
		}	
	}
	
	private void newUser(){
		JSONArray params = new JSONArray();
		if(!this.pass1.getText().equals(this.pass2.getText())){
			SimpleDialog.displayDialog(SimpleDialog.TYPE_ERROR, LANG.invalid_password_Text(), LANG.invalid_password_msg_Text());
		}else{
			if (this.surname.getText().equals("") || this.username.equals("")){
				SimpleDialog.displayDialog(SimpleDialog.TYPE_MESSAGE, LANG.warning_Text(), LANG.user_msg_Text());
			}
			params.set(0,new JSONString(this.forename.getText()));
			params.set(1,new JSONString(this.surname.getText()));
			params.set(2,new JSONString(this.username.getText()));
			params.set(3,new JSONString(this.pass1.getText()));
			params.set(4, JSONBoolean.getInstance(this.isAdmin.isChecked()));
			
			String secLevel = this.secLevel.getItemText(this.secLevel.getSelectedIndex());
			if(secLevel != ""){
				int pos = secLevel.indexOf("]") + 2;
				secLevel = secLevel.substring(pos, secLevel.length());
			}
			params.set(5,new JSONString(secLevel));
			JSONArray cavs = new JSONArray();
			int ccount = 0;
			for(int i=0;i<this.caveats.getItemCount();i++){
				if(this.caveats.isItemSelected(i) && this.caveats.getItemText(i) != ""){
					cavs.set(ccount, new JSONString(this.caveats.getItemText(i)));
					ccount++;
				}
			}
			params.set(6,cavs);
			AuDoc.jsonCall.asyncPost2("Users.addUser", params, new UpdateResponseHandler(this.parent));
		}
	}
	
	private void modUser(){
		JSONArray params = new JSONArray();
		params.set(0, new JSONString(this.uuid));
		params.set(1,new JSONString(this.forename.getText()));
		params.set(2,new JSONString(this.surname.getText()));
		params.set(3,new JSONString(this.username.getText()));
		params.set(4, JSONBoolean.getInstance(this.isAdmin.isChecked()));
		
		String secLevel = this.secLevel.getItemText(this.secLevel.getSelectedIndex());
		if (secLevel != ""){
			int pos = secLevel.indexOf("]") + 2;
			secLevel = secLevel.substring(pos, secLevel.length());
		}
		params.set(5,new JSONString(secLevel));
		
		JSONArray cavs = new JSONArray();
		int ccount = 0;
		for(int i=0;i<this.caveats.getItemCount();i++){
			if(this.caveats.isItemSelected(i) && this.caveats.getItemText(i) != ""){
				JSONString item = new JSONString(this.caveats.getItemText(i));
				cavs.set(ccount, item);
				ccount++;
			}
			
		}
		params.set(6,cavs);
		AuDoc.jsonCall.asyncPost2("Users.modUser", params, new UpdateResponseHandler(this.parent));
	}
	
	private void popLists(){
		this.caveats.addItem("");
		Object c = AuDoc.state.getItem("Caveats");
		if (c != null){
			HashMap caveats = (HashMap)c;
			ArrayList cavs = new ArrayList(caveats.values());
			Collections.sort(cavs);
			for(int i=0;i<cavs.size();i++){
				this.caveats.addItem((String)cavs.get(i));
			}
		}
		
		this.secLevel.addItem("");
		Object l = AuDoc.state.getItem("SecLevels");
		if (l != null){
			HashMap levels = (HashMap)l;
			ArrayList levs = new ArrayList(levels.values());
			Collections.sort(levs);
			for(int i=0;i<levs.size();i++){
				this.secLevel.addItem((String)levs.get(i));
			}
		}
	}
	
	public void setValues(HashMap details){
		this.forename.setText((String)details.get("forename"));
		this.surname.setText((String)details.get("surname"));
		this.username.setText((String)details.get("username"));
		Boolean isAdmin = (Boolean)details.get("isAdmin");
		this.isAdmin.setChecked(isAdmin.booleanValue());
		String secLevel = (String)details.get("seclevel");
		if (!secLevel.equals("")){
			for(int i=0;i<this.secLevel.getItemCount();i++){
				String item = this.secLevel.getItemText(i);
				item = item.trim();
				if(item.endsWith(secLevel.trim())){
					this.secLevel.setItemSelected(i,true);
				}else{
					this.secLevel.setItemSelected(i, false);
				}
			}
		}else{
			this.secLevel.setItemSelected(0,true);
		}
		ArrayList caveats = (ArrayList)details.get("caveats");
		for(int i=0;i<this.caveats.getItemCount();i++){
			this.caveats.setItemSelected(i, false);
			String item = this.caveats.getItemText(i);
			for(int j=0;j<caveats.size();j++){
				if(item.equals((String)caveats.get(j))){
					this.caveats.setItemSelected(i, true);
				}
			}
		}
	}
	
	public void execute(){
		this.hide();
	}
}