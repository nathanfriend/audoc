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

import org.gwtwidgets.client.wrap.Callback;
import org.gwtwidgets.client.wrap.Effect;
import org.gwtwidgets.client.wrap.EffectOption;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.feedback.SimpleDialog;
import com.audata.client.json.UpdateListener;
import com.audata.client.json.UpdateResponseHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RecNumberDialog extends DialogBox implements ClickListener, Callback {

	private static final Language LANG = (Language) GWT.create(Language.class);
	private Button ok;
	private Button cancel;
	private TextBox numBox;
	
	private String uuid;
	private UpdateListener listener;
	
	public RecNumberDialog(String uuid, String recNumber, UpdateListener listener){
		this.setText(LANG.change_rec_num_Text());
		this.listener = listener;
		this.uuid = uuid;
		
		VerticalPanel main = new VerticalPanel();
		
		HorizontalPanel fields = new HorizontalPanel();
		fields.setSpacing(3);
		Label l = new Label(LANG.rec_num_Text());
		fields.add(l);
		this.numBox = new TextBox();
		this.numBox.setText(recNumber);
		fields.add(this.numBox);
		main.add(fields);
		
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.setSpacing(3);
		this.ok = new Button(LANG.save_Text());
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
		if(sender == this.cancel){
			this.hide();
			return;
		}
		
		if(sender ==  this.ok){
			if(this.numBox.getText() != ""){
				JSONArray params = new JSONArray();
				params.set(0, new JSONString(this.uuid));
				params.set(1, new JSONString(this.numBox.getText()));
				AuDoc.jsonCall.asyncPost2("recman.modRecordNumber", params, new UpdateResponseHandler(this.listener));
				this.hide();
			}else{
				SimpleDialog.displayDialog(SimpleDialog.TYPE_ERROR, "Error", "You must specify a new record number");
				return;
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
	
	/**
	 * Handles users pressing ENTER to submit the form
	 */
	public boolean onKeyPressPreview(char key, int modifiers){
		if (key == KeyboardListener.KEY_ENTER){
			onClick(this.ok);
		}
		return true;		
	}

}
