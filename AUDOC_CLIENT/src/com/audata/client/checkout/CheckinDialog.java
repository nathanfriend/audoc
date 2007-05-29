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
package com.audata.client.checkout;

import org.gwtwidgets.client.wrap.Callback;
import org.gwtwidgets.client.wrap.Effect;
import org.gwtwidgets.client.wrap.EffectOption;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.feedback.UploadHandler;
import com.audata.client.feedback.UploadListener;
import com.audata.client.json.UpdateListener;
import com.audata.client.json.UpdateResponseHandler;
import com.audata.client.widgets.UploadPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CheckinDialog extends DialogBox implements ClickListener, Callback, UploadListener,  UpdateListener {

	private static final Language LANG = (Language) GWT.create(Language.class);
	private String uuid;
	private String name;
	private Button ok;
	private Button cancel;
	private RadioButton undo;
	private RadioButton checkin;
	private UploadPanel upload;
	
	public CheckinDialog(String uuid, String name){
		this.uuid = uuid;
		this.name = name;
		this.setText(LANG.check_in_msg_Text() + ": " + this.name);
		
		VerticalPanel main = new VerticalPanel();
		main.setSize("100%", "100%");
		main.setSpacing(3);
		main.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
		
		this.undo = new RadioButton("ActionGroup", LANG.check_in_Text());
		this.checkin = new RadioButton("ActionGroup", LANG.check_in_version_Text());
		this.undo.setChecked(true);
		this.undo.addClickListener(this);
		this.checkin.addClickListener(this);
		main.add(this.undo);
		main.add(this.checkin);
	
		
		this.upload = new UploadPanel();
		String url = AuDoc.jsonCall.getURL();
		this.upload.setAction(url + "docIO.php");
		this.upload.setEncoding(FormPanel.ENCODING_MULTIPART);
	    this.upload.setMethod(FormPanel.METHOD_POST);
		this.upload.setVisible(false);
		main.add(upload);
		
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.setSpacing(3);
		this.ok = new Button(LANG.ok_Text());
		this.ok.addClickListener(this);
		buttons.add(this.ok);
		this.cancel = new Button(LANG.cancel_Text());
		this.cancel.addClickListener(this);
		buttons.add(this.cancel);
		main.add(buttons);
		main.setCellHorizontalAlignment(buttons, HasAlignment.ALIGN_RIGHT);
		
		this.setWidget(main);
	}
	
	public void onClick(Widget sender) {
		if(sender == this.undo){
			this.upload.setVisible(false);
			return;
		}
		if(sender == this.checkin){
			this.upload.setVisible(true);
			return;
		}
		if(sender == this.cancel){
			this.hide();
			return;
		}
		if(sender == this.ok){
			this.checkin();
			return;
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
		int top = (Window.getClientHeight() / 2) - 150;
		this.setPopupPosition(left,top);
	}
	
	/**
	 * Overide parent hide to integrate effects
	 */
	public void hide(){
		Effect.fade(this, new EffectOption[]{new EffectOption("afterFinish", this)});
	}
	
	public void execute() {
		super.hide();
	}
	
	private void checkin(){
		JSONArray params = new JSONArray();
		params.set(0, new JSONString(this.uuid));
		AuDoc.jsonCall.asyncPost2("recman.checkin", params, new UpdateResponseHandler(this));
	}
	
	private void saveDoc(){
		this.upload.addFormHandler(new UploadHandler(this));
		this.upload.submit(this.uuid);
	}
	
	public void onUpdate(){
	    	AuDoc.getInstance().updateStack(AuDoc.STACK_CHECKOUTS);
		if(this.undo.isChecked()){
			this.hide();
		}else{
			this.saveDoc();
		}
	}
	
	public void onUploadComplete(){
		this.hide();
	}

}
