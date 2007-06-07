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

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class QuickSearchPanel extends HorizontalPanel implements FocusListener, KeyboardListener, ClickListener{

	private static final Language LANG = (Language) GWT.create(Language.class);
	private static String qsMsg = LANG.quick_search_Text();
	
	private TextBox criteria;
	private Image search;
	private AuDoc audoc;
	
	public QuickSearchPanel(AuDoc audoc){
		this.audoc = audoc;
		this.setSpacing(2);
		this.criteria = new TextBox();
		this.criteria.setText(QuickSearchPanel.qsMsg);
		this.criteria.addStyleName("audoc-quickSearch");
		//this.criteria.setWidth("134px");
		this.criteria.addFocusListener(this);
		this.criteria.addKeyboardListener(this);
		this.add(this.criteria);
		
		this.search = new Image("images/16x16/search.gif");
		this.search.addClickListener(this);
		this.search.setTitle(LANG.search_Text());
		this.search.addStyleName("hand");
		this.add(this.search);
	}
	
	public void onFocus(Widget w){
		if(this.criteria.getText().equals(QuickSearchPanel.qsMsg)){
			this.criteria.setText("");
		}
	}
	
	public void onLostFocus(Widget w){
		if (this.criteria.getText().equals("")){
			this.criteria.setText(QuickSearchPanel.qsMsg);
		}
	}
	
	public void onKeyPress(Widget sender, char key, int modifiers){
		if (key == KeyboardListener.KEY_ENTER){
			this.onClick(this.search);
		}
	}
	
	public void onKeyDown(Widget sender, char key, int modifiers){
	}
	
	public void onKeyUp(Widget sender, char key, int modifiers){
	}
	
	public void onClick(Widget sender){
		if(sender == this.search){
			JSONArray params = new JSONArray();
			params.set(0, new JSONString(this.criteria.getText()));
			params.set(1, new JSONNumber(0));
			params.set(2, JSONBoolean.getInstance(false));
			AuDoc.jsonCall.asyncPost2("search.quicksearch", params, new SearchResponseHandler(this.audoc, SearchResponseHandler.TYPE_QUICK, params));
		}
	}
	
	public void onLogout(){
		this.criteria.setText(QuickSearchPanel.qsMsg);
	}
}
