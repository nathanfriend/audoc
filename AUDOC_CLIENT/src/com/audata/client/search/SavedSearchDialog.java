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
package com.audata.client.search;

import java.util.ArrayList;

import org.gwtwidgets.client.wrap.Callback;
import org.gwtwidgets.client.wrap.Effect;
import org.gwtwidgets.client.wrap.EffectOption;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.feedback.SimpleDialog;
import com.audata.client.json.NoOpResponseHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SavedSearchDialog extends DialogBox  implements ClickListener, Callback {

	private static final Language LANG = (Language) GWT.create(Language.class);
	private TextBox title;
	private TextArea description;
	private Button ok;
	private Button cancel;
	private ArrayList criteria;
	
	
	public SavedSearchDialog(ArrayList criteria){
		this.criteria = criteria;
		this.setText(LANG.save_search_Text());
		VerticalPanel main = new VerticalPanel();
		main.setSpacing(4);
		
		HorizontalPanel titlePanel = new HorizontalPanel();
		Label tLabel = new Label(LANG.title_Text());
		titlePanel.add(tLabel);
		this.title = new TextBox();
		titlePanel.add(this.title);
		titlePanel.setCellWidth(tLabel, "100px");
		main.add(titlePanel);
		
		HorizontalPanel descPanel = new HorizontalPanel();
		Label dLabel = new Label(LANG.description_Text());
		descPanel.add(dLabel);
		this.description = new TextArea();
		descPanel.add(this.description);
		descPanel.setCellWidth(dLabel, "100px");
		main.add(descPanel);
		
		HorizontalPanel butPanel = new HorizontalPanel();
		butPanel.setSpacing(4);
		this.ok = new Button(LANG.save_Text());
		this.ok.addClickListener(this);
		butPanel.add(this.ok);
		this.cancel = new Button(LANG.cancel_Text());
		this.cancel.addClickListener(this);
		butPanel.add(this.cancel);
		main.add(butPanel);
		
		this.setWidget(main);
	}
	
	public void onClick(Widget sender){
		if(sender == this.cancel){
			this.hide();
			return;
		}
		
		if(sender == this.ok){
			if((this.title.getText() != "")){
				JSONArray criteria = new JSONArray();
				for(int i=0;i<this.criteria.size();i++){
					Criteria c = (Criteria)this.criteria.get(i);
					String sterms = c.getSearchString();
					if(i==0){
						int pos = sterms.indexOf(" ");
						sterms = sterms.substring(pos+1);
					}
					criteria.set(i, new JSONString(sterms));
				}
				JSONArray params = new JSONArray();
				params.set(0, new JSONString(this.title.getText()));
				params.set(1, new JSONString(this.description.getText()));
				params.set(2, criteria);
				
				AuDoc.jsonCall.asyncPost2("search.savesearch", params, new NoOpResponseHandler());
				AuDoc.getInstance().updateStack(AuDoc.STACK_SAVEDSEARCHES);
				this.hide();
			}else{
				SimpleDialog.displayDialog(SimpleDialog.TYPE_ERROR, "Error", "Your saved search must have a title");
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
