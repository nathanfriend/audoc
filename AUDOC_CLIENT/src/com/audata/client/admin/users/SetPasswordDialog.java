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

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.feedback.SimpleDialog;
import com.audata.client.json.NoOpResponseHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SetPasswordDialog extends DialogBox implements ClickListener{

	private static final Language LANG = (Language) GWT.create(Language.class);
	private String userUUID;
	
	private Button setButton;
	private Button cancelButton;
	
	private PasswordTextBox pass1;
	private PasswordTextBox pass2;
	
	public SetPasswordDialog(UserPanel parent){
		this.setText(LANG.set_password_Text());
		
		DockPanel outer = new DockPanel();
	    outer.setSpacing(4);

	    outer.add(new Image("images/48x48/security.gif"), DockPanel.WEST);
	    
	    VerticalPanel formPanel = new VerticalPanel();
	    
	    
	    HorizontalPanel pass1Panel = new HorizontalPanel();
	    Label l1 = new Label(LANG.password_Text());
	    l1.addStyleName("audoc-label");
	    l1.setWidth("120px");
	    
	    pass1Panel.add(l1);
	    this.pass1 = new PasswordTextBox();
	    pass1Panel.add(this.pass1);
	    formPanel.add(pass1Panel);
	    
	    HorizontalPanel pass2Panel = new HorizontalPanel();
	    
	    Label l2 = new Label(LANG.password_reenter_Text());
	    l2.addStyleName("audoc-label");
	    l2.setWidth("120px");
	    pass2Panel.add(l2);
	    this.pass2 = new PasswordTextBox();
	    pass2Panel.add(this.pass2);
	    formPanel.add(pass2Panel);
	    
	    HorizontalPanel buttonPanel = new HorizontalPanel();
	    buttonPanel.setSpacing(5);
	    
	    this.setButton = new Button(LANG.set_password_Text());
	    this.setButton.addClickListener(this);
	    buttonPanel.add(this.setButton);
	    
	    this.cancelButton = new Button(LANG.cancel_Text());
	    this.cancelButton.addClickListener(this);
	    buttonPanel.add(this.cancelButton);
	    
	    formPanel.add(buttonPanel);
	    formPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
	    
	    outer.add(formPanel, DockPanel.SOUTH);
	    formPanel.setSpacing(4);
	    outer.setSpacing(8);
	    setWidget(outer);
	}
	
	public void show(String uuid){
		this.userUUID = uuid;
		super.show();
	}
	
	public void hide(){
		this.userUUID = null;
		super.hide();
	}
	
	public void onClick(Widget sender){
		if(sender == this.cancelButton){
			//close dialog
			this.hide();
			pass1.setText("");
			pass2.setText("");
			return;
		}
		if (sender == this.setButton){
			//update password
			if (pass1.getText() != ""){
				if (pass1.getText().equals(pass2.getText())){
					JSONArray params = new JSONArray();
					params.set(0, new JSONString(this.userUUID));
					params.set(1, new JSONString(pass1.getText()));
					AuDoc.jsonCall.asyncPost2("Users.setPassword", params, new NoOpResponseHandler());
					this.hide();
				}else{
					SimpleDialog.displayDialog(SimpleDialog.TYPE_ERROR, "Invalid Passwords", "The passwords entered are not the same");
				}
				pass1.setText("");
				pass2.setText("");
			}
			return;
		}
	}
	
	/**
	 * Handles users pressing ENTER to submit the form
	 */
	public boolean onKeyPressPreview(char key, int modifiers){
		if (key == KeyboardListener.KEY_ENTER){
			onClick(this.setButton);
		}
			return true;		
	}
}
