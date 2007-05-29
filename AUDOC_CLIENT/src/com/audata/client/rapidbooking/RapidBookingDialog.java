/* +----------------------------------------------------------------------+
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
package com.audata.client.rapidbooking;



import org.gwtwidgets.client.wrap.Callback;
import org.gwtwidgets.client.wrap.Effect;
import org.gwtwidgets.client.wrap.EffectOption;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.widgets.HTMLButton;
import com.audata.client.widgets.HTMLButtonList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RapidBookingDialog extends DialogBox implements ClickListener, Callback{
	
	private static final Language CONSTANTS = (Language) GWT.create(Language.class);
	
	private RadioButton checkin;
	private RadioButton checkout;
	private ListBox users;
	private HTMLButtonList processed;
	private TextBox recnum;
	private Button okButton;
	private Button closeButton;
	private HorizontalPanel userPanel;
	
	public RapidBookingDialog(){
	    setText(CONSTANTS.rapid_title_Text());
	    DockPanel outer = new DockPanel();
	    outer.setSpacing(4);

	    outer.add(new Image("images/48x48/checkout.gif"), DockPanel.WEST);

	    VerticalPanel form = new VerticalPanel();
	    form.setSpacing(4);
	    
	    this.checkin = new RadioButton("ActionGroup", CONSTANTS.check_in_Text());
	    this.checkin.addClickListener(this);
	    this.checkin.setChecked(true);
	    this.checkin.addStyleName("audoc-label");
	    form.add(this.checkin);
	    this.checkout = new RadioButton("ActionGroup", CONSTANTS.checkout_Text());
	    this.checkout.addClickListener(this);
	    this.checkout.addStyleName("audoc-label");
	    form.add(this.checkout);
	    
	    this.userPanel = new HorizontalPanel();
	    Label l = new Label(CONSTANTS.user_Text());
	    l.addStyleName("audoc-label");
	    userPanel.add(l);
	    this.users = new ListBox();
	    userPanel.add(this.users);
	    userPanel.setCellWidth(l, "100px");
	    this.userPanel.setVisible(false);
	    form.add(this.userPanel);
	    
	    HorizontalPanel recnumPanel = new HorizontalPanel();
	    Label r = new Label(CONSTANTS.rec_num_Text());
	    r.addStyleName("audoc-label");
	    recnumPanel.add(r);
	    this.recnum = new TextBox();
	    recnumPanel.add(this.recnum);
	    recnumPanel.setCellWidth(r, "100px");
	    form.add(recnumPanel);
	    
	    HorizontalPanel butPanel = new HorizontalPanel();
	    butPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	    butPanel.setSpacing(4);
	    this.closeButton = new Button(CONSTANTS.close_Text());
	    this.closeButton.addClickListener(this);
	    butPanel.add(this.closeButton);
	    
	    this.okButton = new Button(CONSTANTS.ok_Text());
	    this.okButton.addClickListener(this);
	    butPanel.add(this.okButton);
//	    butPanel.setWidth("100%");
	    form.add(butPanel);
	    
	    String template = "<span class=\"rapid_processed\">#0 #1</span>";
	    this.processed = new HTMLButtonList("images/16x16/treerec.gif", template, false);
	    this.processed.setWidth("100%");
	    this.processed.setHeight("75px");
	    form.add(this.processed);
	    
	    
	    outer.add(form, DockPanel.NORTH);
	    if(AuDoc.state.getItem("isAdmin") == "true"){
		this.getUsers();
	    }else{
		String username = (String)AuDoc.state.getItem("username");
		String forename = (String)AuDoc.state.getItem("forename");
		String surname = (String)AuDoc.state.getItem("surname");
		this.users.addItem(surname + ", " + forename, username);
	    }
	    this.setWidget(outer);
	}


	public boolean onKeyPressPreview(char key, int modifiers){
		if (key == KeyboardListener.KEY_ENTER){
			onClick(this.okButton);
			return false;
		}else{
			return true;
		}
	}
	
	public void onClick(Widget sender){
	    if(sender == this.checkin){
		this.userPanel.setVisible(false);
		return;
	    }
	    
	    if(sender == this.checkout){
		this.userPanel.setVisible(true);
		return;
	    }
	    
	    if(sender == this.okButton){
		this.process();
		return;
	    }
	    
	    if(sender == this.closeButton){
		this.hide();
		return;
	    }
	}
	
	public void getUsers(){
	    String method = "Users.getUsers";
	    JSONArray params = new JSONArray();
	    AuDoc.jsonCall.asyncPost2(method, params, new UserResponseHandler(this));
	}
	
	public void addUser(String username, String forename, String surname){
	    this.users.addItem(surname + ", " + forename, username);
	}
	
	public void addProcess(String recNum, String action){
	    String[] caption = new String[2];
	    caption[0] = recNum;
	    caption[1] = action;
	    HTMLButton but = this.processed.addItem(caption);
	    this.processed.ensureVisible(but);
	}
	
	public void process(){
	    String recnum = this.recnum.getText();
	    if(recnum == ""){
		return;
	    }else{
		this.recnum.setText("");
		if(this.checkin.isChecked()){
		    //this is a checkin action
		    String method = "reccentre.checkIn";
		    JSONArray params = new JSONArray();
		    params.set(0, new JSONString(recnum));
		    AuDoc.jsonCall.asyncPost2(method, params, new BookingResponseHandler(this));
		}else{
		    //this is a checkout action
		    String user = this.users.getValue(this.users.getSelectedIndex());
		    if(user == ""){
			return;
		    }else{
			String method = "reccentre.checkOut";
			    JSONArray params = new JSONArray();
			    params.set(0, new JSONString(recnum));
			    params.set(1, new JSONString(user));
			    AuDoc.jsonCall.asyncPost2(method, params, new BookingResponseHandler(this));
		    }
		}
		
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
}
