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
public class TrayPanel extends VerticalPanel implements ITrayCollection{
	
	private static final Language LANG = (Language) GWT.create(Language.class);
	private ArrayList trays;
	private AuDoc parent;
	
	public TrayPanel(AuDoc parent){
		this.parent = parent;
		this.setWidth("100%");	
		Label l = new Label(LANG.no_trays_Text());
		l.addStyleName("audoc-trayLabel");
		l.setWidth("100%");
		l.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		this.add(l);
		
		this.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		this.setWidth("100%");
		
		this.trays = new ArrayList();
		this.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
	}
	
	public void onUpdate(){
		this.clear();
		this.trays.clear();
		this.getTrays();
	}
	
	public void onLogout(){
		this.clear();
		this.trays.clear();
		Label l = new Label(LANG.no_trays_Text());
		l.addStyleName("audoc-trayLabel");
		this.add(l);
	}
	
	private void getTrays(){
		JSONArray params = new JSONArray();
		params.set(0,new JSONString((String)AuDoc.state.getItem("my_uuid")));
		AuDoc.jsonCall.asyncPost2("Trays.getTrays", params, new TrayCallback(this));
	}
	
	public void addTray(String uuid, String name, String description){
		HorizontalPanel tp = new HorizontalPanel();
		tp.setSpacing(2);
		tp.setWidth("100%");
		//CaptionButton cp = new CaptionButton("images/16x16/tray.gif", name, CaptionButton.CAPTION_EAST);
		CaptionButton cp = new CaptionButton();
		cp.setImageUrl("images/16x16/tray.gif");
		cp.setCaptionText(name);
		cp.setCaptionStyleName("audoc-trayLabel");
		cp.setTitle(description);
		cp.addClickListener(new TrayClickListener(this.parent, name, uuid));
		Image dButton = new Image("images/16x16/logout.gif");
		dButton.setTitle("delete " + name);
		dButton.addClickListener(new ItemDeleteClickListener(AuDoc.STACK_TRAYS, uuid));
		dButton.addStyleName("hand");
		
		tp.add(cp);
		tp.add(dButton);
		tp.setCellVerticalAlignment(dButton, HasAlignment.ALIGN_MIDDLE);
		this.trays.add(tp);
		this.show();
	}
	
	private void show(){
		if(this.trays.size() > 0){
			this.clear();
			for (int i=0; i< this.trays.size() ;i++){
				HorizontalPanel t = (HorizontalPanel)trays.get(i);
				this.add(t);
			}
		}
	}
}
