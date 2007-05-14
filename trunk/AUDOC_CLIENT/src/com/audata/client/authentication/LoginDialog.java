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
package com.audata.client.authentication;



import java.util.Map;

import org.gwtwidgets.client.util.Location;
import org.gwtwidgets.client.util.WindowUtils;
import org.gwtwidgets.client.wrap.Callback;
import org.gwtwidgets.client.wrap.Effect;
import org.gwtwidgets.client.wrap.EffectOption;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoginDialog extends DialogBox implements ClickListener, Callback{
	
	private static final Language CONSTANTS = (Language) GWT.create(Language.class);
	
	private AuDoc parent;
	private TextBox username;
	private PasswordTextBox password;
	private Button loginButton;
	private ListBox languages;
	
	public LoginDialog(AuDoc parent){
		this.parent = parent;
		setText(CONSTANTS.welcome_Text());

	    // Create a DockPanel to contain the 'about' label and the 'OK' button.
	    DockPanel outer = new DockPanel();
	    outer.setSpacing(4);

	    outer.add(new Image("images/48x48/security.gif"), DockPanel.WEST);
	    
	    VerticalPanel formPanel = new VerticalPanel();
	    formPanel.setSpacing(1);
	    
	    HorizontalPanel userPanel = new HorizontalPanel();
	    this.username = new TextBox();
	    Label l = new Label(CONSTANTS.username_Text());
	    l.addStyleName("audoc-label");
	    l.setWidth("85px");
	    userPanel.add(l);
	    userPanel.add(this.username);
	    formPanel.add(userPanel);
	    
	    HorizontalPanel passPanel = new HorizontalPanel();
	    this.password = new PasswordTextBox();
	    l = new Label(CONSTANTS.password_Text());
	    l.addStyleName("audoc-label"); 
	    l.setWidth("85px");
	    passPanel.add(l);
	    passPanel.add(this.password);
	    formPanel.add(passPanel);
	    
	    HorizontalPanel langPanel = new HorizontalPanel();
	    l = new Label(CONSTANTS.lang_Text());
	    l.addStyleName("audoc-label");
	    l.setWidth("85px");
	    langPanel.add(l);
	    formPanel.add(langPanel);

	    this.languages = new ListBox();
	    this.populateLocales();
	    this.languages.setWidth("146px");
	    
	    langPanel.add(this.languages);
	    
	    
	    this.languages.addChangeListener(new ChangeListener() {
	    	public void onChange(Widget sender) {
	    		//refreshes the browser in the selected locale
	    		Location loc = WindowUtils.getLocation();
	    		String path = loc.getProtocol() + "//" + loc.getHost() + loc.getPath();
	    		String locale = languages.getValue(languages.getSelectedIndex());
	    		Window.open(path + "?locale=" + locale, "_self", "");
	    	}
	    });
	    
	    this.loginButton = new Button(CONSTANTS.login_Text(), this); 
	    formPanel.add(loginButton);
	    formPanel.setCellHorizontalAlignment(this.loginButton, HasAlignment.ALIGN_RIGHT);
	    outer.add(formPanel, DockPanel.SOUTH);

	    
	    HTML text = new HTML(CONSTANTS.message_Text());
	    text.setStyleName("audoc-LoginDialogText");
	    outer.add(text, DockPanel.CENTER);

	    // Add a bit of spacing and margin to the dock to keep the components from
	    // being placed too closely together.
	    outer.setSpacing(8);
	    this.setWidget(outer);
	}

	/**
	 * Calls the login function
	 */
	public void onClick(Widget sender) {
		String method = "Users.authUser";
		JSONArray params = new JSONArray();
		params.set(0, new JSONString(this.username.getText()));
		params.set(1, new JSONString(this.password.getText()));
		AuDoc.jsonCall.asyncPost2(method, params, new LoginResponseHandler(this.parent, this));
		this.password.setText("");
	}
	
	/**
	 * Handles users pressing ENTER to submit the form
	 */
	public boolean onKeyPressPreview(char key, int modifiers){
		if (key == KeyboardListener.KEY_ENTER){
			onClick(this.loginButton);
			return false;
		}else{
			return true;
		}
		
	}
	
	/**
	 * Overide parent show to integrate effects
	 */
	public void show(){
		this.setVisible(false);
		super.show();
		Effect.appear(this);
		int left = (Window.getClientWidth() / 2) - 150;
		int top = (Window.getClientHeight() / 2) - 50;
		this.setPopupPosition(left,top);
	}
	
	public void hide(){
		Effect.fade(this, new EffectOption[]{new EffectOption("afterFinish", this)});
		//Effect.dropOut(this, new EffectOption[]{new EffectOption("afterFinish", this)});
	}
	
	public void execute(){
		super.hide();
	}
	
	private void populateLocales(){
		String localesStr = AuDoc.getConfigItem("locales");
	    if(!localesStr.equals("")){
		    String[] locales = localesStr.split(";");
		    for(int i=0;i<locales.length;i++){
		    	String localeStr = locales[i];
		    	String[] locale = localeStr.split(":");
		    	if(locale.length == 2){
		    		this.languages.addItem(locale[0], locale[1]);
		    	}
		    }
		    Location loc = WindowUtils.getLocation();
		    Map params = loc.getParameterMap();
		    String curLocale = "";
		    if(params.containsKey("locale")){
		    	curLocale = (String)params.get("locale");
		    }else{
		    	curLocale = "en";
		    }
		    for(int i=0;i<this.languages.getItemCount();i++){
	    		if(this.languages.getValue(i).equals(curLocale)){
	    			this.languages.setItemSelected(i, true);
	    		}
	    	}
	    }else{
	    	this.languages.addItem("English", "en");
	    }
	}
}
