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

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.stack.ItemDeleteClickListener;
import com.audata.client.widgets.CaptionButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author jonm
 *
 */
public class SavedSearchPanel extends VerticalPanel{
	
	private static final Language LANG = (Language) GWT.create(Language.class);
	private ArrayList searches;
	private AuDoc parent;
	
	public SavedSearchPanel(AuDoc parent){
		this.parent = parent;
		this.setWidth("100%");	
		Label l = new Label(LANG.no_saved_searches_Text());
		l.addStyleName("audoc-searchLabel");
		l.setWidth("100%");
		l.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		this.add(l);
		
		this.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		this.setWidth("100%");
		
		this.searches = new ArrayList();
		this.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
	}
	
	public void onUpdate(){
		this.clear();
		this.searches.clear();
		this.getSavedSearches();
	}
	
	public void onLogout(){
		this.clear();
		this.searches.clear();
		Label l = new Label(LANG.no_saved_searches_Text());
		l.addStyleName("audoc-searchLabel");
		this.add(l);
	}
	
	private void getSavedSearches(){
		JSONArray params = new JSONArray();
		params.set(0,new JSONString((String)AuDoc.state.getItem("my_uuid")));
		AuDoc.jsonCall.asyncPost2("Search.getsavedsearches", params, new SSearchCallbackListener(this));
	}
	
	public void addSavedSearch(String uuid, String name, String desc){
		HorizontalPanel sp = new HorizontalPanel();
		sp.setSpacing(2);
		sp.setWidth("100%");
		//CaptionButton cp = new CaptionButton("images/16x16/tray.gif", name, CaptionButton.CAPTION_EAST);
		CaptionButton cp = new CaptionButton();
		cp.setImageUrl("images/16x16/tray.gif");
		cp.setCaptionText(name);
		cp.setTitle(desc);
		cp.setCaptionStyleName("tree-text");
		cp.addClickListener(new SavedSearchClickListener(this.parent, uuid));
		
		Image dButton = new Image("images/16x16/logout.gif");
		dButton.setTitle(LANG.delete_Text() + " " + name);
		dButton.addClickListener(new ItemDeleteClickListener(AuDoc.STACK_SAVEDSEARCHES, uuid));
		dButton.addStyleName("hand");
		sp.add(cp);
		sp.add(dButton);
		this.searches.add(sp);
	}
	
	public void show(){
		if(this.searches.size() > 0){
			this.clear();
			for (int i=0; i< this.searches.size() ;i++){
				HorizontalPanel hp = (HorizontalPanel)this.searches.get(i);
				this.add(hp);
			}
		}
	}

}
