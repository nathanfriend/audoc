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
package com.audata.client.admin.users;

import java.util.ArrayList;
import java.util.HashMap;

import org.gwtwidgets.client.wrap.Effect;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.feedback.ResponseListener;
import com.audata.client.feedback.SimpleDialog;
import com.audata.client.json.UpdateListener;
import com.audata.client.json.UpdateResponseHandler;
import com.audata.client.widgets.HTMLButtonList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserPanel extends VerticalPanel implements UpdateListener, ClickListener, ResponseListener{

	private static final Language LANG = (Language) GWT.create(Language.class);
	private Button newUser;
	private Button editUser;
	private Button delUser;
	private Button setPassword;
	private HTMLButtonList usersBox;
	//private HashMap users;
	
	private SetPasswordDialog dialog;
	
	private String userUUID = "";
	private HashMap custom = null;
	
	
	public UserPanel(){
		
		this.dialog = new SetPasswordDialog(this);
		this.setSpacing(5);
		
		Label title = new Label(LANG.admin_user_Text());
		title.addStyleName("audoc-sectionTitle");
		this.add(title);
		
		String caption = "<span class=\"user-name\">#0 #1</span><br/>" +
		"<span class=\"user-username\">#2</span>";
		this.usersBox =  new HTMLButtonList("images/48x48/users.gif", caption, false);
		this.usersBox.addStyleName("audoc-users");
		this.usersBox.setPixelSize(300,250);
		this.usersBox.addClickListener(new UserClickListener(this));
		
		this.add(this.usersBox);
		
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.setSpacing(5);
		this.newUser = new Button(LANG.new_Text());
		this.newUser.addClickListener(this);
		
		this.editUser = new Button(LANG.edit_Text());
		this.editUser.addClickListener(this);
		
		this.delUser = new Button(LANG.edit_Text());
		this.delUser.addClickListener(this);
		
		this.setPassword = new Button(LANG.set_password_Text());
		this.setPassword.addClickListener(this);
		
		buttons.add(this.newUser);
		buttons.add(this.editUser);
		buttons.add(this.delUser);
		buttons.add(this.setPassword);
		
		this.add(buttons);
		this.getUsers();
	}

	public void onUpdate(){
		this.usersBox.clear();
		this.getUsers();
	}
	
	public void getUsers(){
		
		AuDoc.jsonCall.asyncPost2("Users.getUsers", new JSONArray(), new UserResponseHandler(this));
	}
	
	public void addUser(String uuid, String fname, String sname, String uname, boolean isAdmin, String secLevel, ArrayList caveats){
		HashMap custom = new HashMap();
		custom.put("uuid", uuid);
		custom.put("forename", fname);
		custom.put("surname", sname);
		custom.put("username", uname);
		custom.put("isAdmin", new Boolean(isAdmin));
		custom.put("seclevel", secLevel);
		custom.put("caveats", caveats);
		String[] cap = new String[3];
		cap[0] = fname;
		cap[1] = sname;
		cap[2] = uname;
		this.usersBox.addItem(cap, null, custom);
	}
	
	public void onClick(Widget sender){
		if (sender == this.setPassword){
			if(this.userUUID != ""){
				this.dialog.show(this.userUUID);
				this.dialog.setVisible(false);
				int left = (Window.getClientWidth() / 2) - 150;
				int top = (Window.getClientHeight() / 2) - 50;
				this.dialog.setPopupPosition(left,top);
				Effect.appear(this.dialog);
			}
			return;
		}
		
		if (sender == this.newUser){
			UserDialog dialog = new UserDialog(this);
			dialog.setVisible(false);
			dialog.show();
			int left = (Window.getClientWidth() / 2) - 150;
			int top = (Window.getClientHeight() / 2) - 150;
			dialog.setPopupPosition(left,top);
			Effect.appear(dialog);
			return;
		}
		
		if (sender == this.editUser){
			if(this.userUUID != ""){
				UserDialog dialog = new UserDialog(this, this.userUUID, this.custom);
				dialog.setVisible(false);
				dialog.show();
				int left = (Window.getClientWidth() / 2) - 150;
				int top = (Window.getClientHeight() / 2) - 150;
				dialog.setPopupPosition(left,top);
				Effect.appear(dialog);
			}
			return;
		}
		
		if(sender == this.delUser){
			if(this.userUUID != ""){
				SimpleDialog.displayDialog(SimpleDialog.TYPE_QUERY, LANG.confirm_Text(), LANG.del_user_msg_Text(), this);
			}
		}
	}
	
	public void setUser(String uuid, HashMap custom){
		this.userUUID = uuid;
		this.custom = custom;
	}
	
	/**
	 * Called when the user respondse to a delete query
	 */
	public void onResponse(boolean response){
		if(response){
//			delete it
			JSONArray params = new JSONArray();
			params.set(0, new JSONString(this.userUUID));
			AuDoc.jsonCall.asyncPost2("Users.remUser", params, new UpdateResponseHandler(this));
		}
	}
}
