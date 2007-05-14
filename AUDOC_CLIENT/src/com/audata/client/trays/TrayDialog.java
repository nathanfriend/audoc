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
package com.audata.client.trays;

import java.util.ArrayList;

import org.gwtwidgets.client.wrap.Callback;
import org.gwtwidgets.client.wrap.Effect;
import org.gwtwidgets.client.wrap.EffectOption;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.feedback.SimpleDialog;
import com.audata.client.json.NoOpResponseHandler;
import com.audata.client.json.UpdateListener;
import com.audata.client.json.UpdateResponseHandler;
import com.audata.client.widgets.HTMLButton;
import com.audata.client.widgets.HTMLButtonList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TrayDialog extends DialogBox implements ClickListener, Callback, UpdateListener, ITrayCollection{
	
	private static final Language LANG = (Language) GWT.create(Language.class);
	HTMLButtonList trays;
	TextBox newText;
	TextBox descript;
	Button addBut;
	Button cancel;
	Button newBut;
	ArrayList records;
	
	public TrayDialog(ArrayList records){
		this.setText(LANG.add_to_tray_Text());
		this.records = records;
		String template = "<span class=\"traylist-title\">#0</span>";
		this.trays = new HTMLButtonList("images/48x48/tray.gif", template, false);
		this.trays.addStyleName("audoc-trays");
		this.addBut = new Button(LANG.add_Text());
		this.addBut.addClickListener(this);
		this.cancel = new Button(LANG.cancel_Text());
		this.cancel.addClickListener(this);
		this.newText = new TextBox();
		this.newBut = new Button(LANG.new_Text());
		this.newBut.addClickListener(this);
		this.getTrays();
		VerticalPanel main = new VerticalPanel();
		main.setSpacing(3);
		
		main.add(this.trays);
		
		HorizontalPanel p = new HorizontalPanel();
		p.add(new Label("New tray:"));
		p.setSpacing(3);
		p.add(this.newText);
		p.add(this.newBut);
		p.setCellHorizontalAlignment(this.addBut, HasAlignment.ALIGN_RIGHT);
		main.add(p);
		
		HorizontalPanel p2 = new HorizontalPanel();
		p2.setSpacing(3);
		p2.add(this.addBut);
		p2.add(this.cancel);
		main.add(p2);
		main.setCellHorizontalAlignment(p2, HasAlignment.ALIGN_RIGHT);
		this.setWidget(main);
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
	
	/**
	 * queries the server for a list of trays
	 */
	private void getTrays(){
		JSONArray params = new JSONArray();
		params.set(0,new JSONString((String)AuDoc.state.getItem("my_uuid")));
		AuDoc.jsonCall.asyncPost2("Trays.getTrays", params, new TrayCallback(this));
	}
	
	/**
	 * Adds a trays to the tray list
	 * @param name The name of the tray
	 * @param uuid The tray's uuid
	 */
	public void addTray(String uuid, String name, String description){
		String caption[] = new String[1];
		caption[0] = name;
		this.trays.addItem(caption, null, uuid);
	}
	
	public void onClick(Widget sender){
		if(sender == this.cancel){
			this.hide();
			return;
		}
		
		if(sender == this.addBut){
			if(this.trays.getSelected().size() != 0){
				HTMLButton selected = (HTMLButton)this.trays.getSelected().get(0);
				String uuidT = (String)selected.custom;
				for(int i=0; i<this.records.size();i++){
					String uuidR = (String)this.records.get(i);
					this.addItem(uuidT, uuidR);
				}
				this.hide();
			}else{
				SimpleDialog.displayDialog(SimpleDialog.TYPE_ERROR, LANG.error_Text(), LANG.add_to_tray_error_Text());
			}
		}
		
		if((sender == this.newBut) && (this.newText.getText() != "")){
			this.createTray();
		}
	}
	
	/**
	 * Callback method for onHide
	 */
	public void execute(){
		super.hide();
	}
	
	private void createTray(){
		JSONArray params = new JSONArray();
		params.set(0, new JSONString(this.newText.getText()));
		params.set(1, new JSONString(""));
		AuDoc.jsonCall.asyncPost2("Trays.addTray", params, new UpdateResponseHandler(this));
		AuDoc.getInstance().updateStack(AuDoc.STACK_TRAYS);
	}
	
	public void onUpdate(){
		this.trays.clear();
		this.getTrays();
	}
	
	private void addItem(String tray, String record){
		JSONArray params = new JSONArray();
		params.set(0, new JSONString(tray));
		params.set(1, new JSONString(record));
		AuDoc.jsonCall.asyncPost2("Trays.addItem", params, new NoOpResponseHandler());
	}
}
