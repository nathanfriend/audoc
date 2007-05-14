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

import java.util.ArrayList;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.json.UpdateListener;
import com.audata.client.widgets.CaptionButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CheckoutPanel extends VerticalPanel implements UpdateListener{

	private static final Language LANG = (Language) GWT.create(Language.class);
	private ArrayList records;
	
	
	public CheckoutPanel(AuDoc parent){
		this.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		this.setWidth("100%");
		this.records = new ArrayList();
		Label l = new Label(LANG.no_rec_checkout_msg_Text());
		l.addStyleName("audoc-treeLabel");
		this.add(l);
	}
	
	public void onLogout(){
		this.records.clear();
		this.clear();
		Label l = new Label(LANG.no_rec_checkout_msg_Text());
		l.addStyleName("audoc-treeLabel");
		this.add(l);
	}
	
	public void getCheckouts(){
		AuDoc.jsonCall.asyncPost2("Recman.getCheckedOut", new JSONArray(), new CheckoutCallback(this));
	}
	
	public void paint(){
		this.clear();
		if(this.records.size() > 0){
			for(int i=0; i<this.records.size();i++){
				this.add((HorizontalPanel)this.records.get(i));
			}
		}else{
			Label l = new Label(LANG.no_rec_checkout_msg_Text());
			l.addStyleName("audoc-treeLabel");
			this.add(l);
		}
	}
	
	public void onUpdate(){
		this.records.clear();
		this.clear();
		this.getCheckouts();
	}
	
	public void addRecord(String uuid, String title, String recordType, String cot){
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.setSpacing(3);
		//CaptionButton button = new CaptionButton("images/16x16/treerec.gif",title, CaptionButton.CAPTION_EAST);
		CaptionButton button = new CaptionButton();
		button.setImageUrl("images/16x16/treerec.gif");
		button.setCaptionText(title);
		
		button.addClickListener(new CheckoutClickListener(this, uuid, recordType, cot));
		
		Image cButton = new Image("images/16x16/checkouts.gif");
		cButton.addClickListener(new CheckinClickListener(uuid, title));
		cButton.setTitle(LANG.check_in_Text() + " " + title);
		cButton.addStyleName("hand");
		
		hp.add(button);
		hp.add(cButton);
		hp.setCellVerticalAlignment(cButton, HasAlignment.ALIGN_MIDDLE);
		this.records.add(hp);
	}
}
