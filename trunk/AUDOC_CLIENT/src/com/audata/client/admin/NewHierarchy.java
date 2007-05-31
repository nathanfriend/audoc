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

import org.gwtwidgets.client.wrap.Callback;
import org.gwtwidgets.client.wrap.Effect;
import org.gwtwidgets.client.wrap.EffectOption;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.feedback.SimpleDialog;
import com.audata.client.json.UpdateResponseHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A New Keyword Hierarchy Dialog
 * @author jonm
 *
 */
public class NewHierarchy extends DialogBox implements Callback, ClickListener{

	private static final Language LANG = (Language) GWT.create(Language.class);
	private KeywordPanel parent;
	
	private TextBox name;
	private Button ok;
	private Button cancel;
	
	/**
	 * Public Constructor which take the parent admin panel
	 * so that it can be updated when a new Hierarchy has
	 * been added
	 * @param parent The parent KeywordPanel
	 */
	public NewHierarchy(KeywordPanel parent){
		this.parent = parent;
		
		this.setText(LANG.new_keyword_hierarchy_Text());
		VerticalPanel main = new VerticalPanel();
		main.setSpacing(4);
		
		HorizontalPanel fields = new HorizontalPanel();
		fields.setSpacing(4);
		Label l = new Label(LANG.name_Text());
		this.name = new TextBox();
		this.name.setWidth("150px");
		fields.add(l);
		fields.add(this.name);
		main.add(fields);
		
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.setSpacing(4);
		this.ok = new Button(LANG.save_Text());
		this.ok.addClickListener(this);
		this.cancel = new Button(LANG.cancel_Text());
		this.cancel.addClickListener(this);
		buttons.add(this.ok);
		buttons.add(this.cancel);
		main.add(buttons);
		this.setWidget(main);
	}
	
	/**
	 * Called when one of the buttons is clicked
	 * @param sender The widget that was clicked
	 */
	public void onClick(Widget sender){
		if(sender == this.cancel){
			this.hide();
			return;
		}
		
		if(sender == this.ok){
			if(this.name.getText().equals("")){
				SimpleDialog.displayDialog(SimpleDialog.TYPE_ERROR, "Error", "You must specify a name for the new keyword hierarchy");
			}else{
				JSONArray params = new JSONArray();
				params.set(0, new JSONString(this.name.getText()));
				AuDoc.jsonCall.asyncPost2("recman.addKWH", params, new UpdateResponseHandler(this.parent));
				this.hide();
			}
		}
	}
	
	/**
	 * Overide parent show() to integrate effects
	 */
	public void show(){
		this.setVisible(false);
		super.show();
		Effect.appear(this);
		int left = (Window.getClientWidth() / 2) - 150;
		int top = (Window.getClientHeight() / 2) - 50;
		this.setPopupPosition(left,top);
	}
	
	/**
	 * Override parent hide() to integrate effects
	 */
	public void hide(){
		Effect.fade(this, new EffectOption[]{new EffectOption("afterFinish", this)});
	}
	
	/**
	 * called by effect to finally close the Dialog
	 */
	public void execute(){
		super.hide();
	}
}
