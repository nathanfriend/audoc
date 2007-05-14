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
package com.audata.client.widgets;

import java.util.HashMap;

import com.audata.client.Language;
import com.audata.client.feedback.SimpleDialog;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Wizard extends Composite implements ClickListener{

	private static final Language LANG = (Language) GWT.create(Language.class);
	private VerticalPanel main;
	private HorizontalPanel control;
	private DeckPanel pages;
	private HashMap values;
	private Button next;
	private Button prev;
	private Button finish;
	
	private WizardListener listener;
	
	public Wizard(String title, WizardListener listener){
		this.listener = listener;
		this.values = new HashMap();
		this.main = new VerticalPanel();
		this.main.setHeight("100%");
		this.main.setWidth("100%");
		this.main.setSpacing(5);
		Label titleLabel = new Label(title);
		titleLabel.addStyleName("audoc-sectionTitle");
		this.main.add(titleLabel);
		
		this.pages = new DeckPanel();
		this.pages.setSize("100%", "100%");
		this.main.add(this.pages);
		
		this.control = new HorizontalPanel();
		this.control.setSpacing(5);
		this.control.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
		this.control.setVerticalAlignment(HasAlignment.ALIGN_BOTTOM);
		
		this.prev = new Button(LANG.previous_Text());
		this.prev.addClickListener(this);
		this.prev.setVisible(false);
		
		this.next = new Button(LANG.next_Text());
		this.next.addClickListener(this);
		
		this.finish = new Button(LANG.finish_Text());
		this.finish.addClickListener(this);
		this.finish.setVisible(false);
		
		this.control.add(this.prev);
		this.control.add(this.next);
		this.control.add(this.finish);
		this.main.add(this.control);
		
		this.initWidget(this.main);
		this.setSize("100%", "100%");
	}
	
	public void onClick(Widget sender){
		if(sender == this.next){
			this.nextPage();
			return;
		}
		if(sender == this.prev){
			this.prevPage();
			return;
		}
		if(sender == this.finish){
			this.onFinish();
			return;
		}
	}
	
	private void nextPage(){
		WizardPage curPage = (WizardPage)this.pages.getWidget(this.pages.getVisibleWidget());
		WizardPage nextPage = (WizardPage)this.pages.getWidget(this.pages.getVisibleWidget()+1);
		if(curPage.isValid()){
			this.values.putAll(curPage.getValues());
			int next = this.pages.getVisibleWidget() + 1;
			int count = this.pages.getWidgetCount();
			if(next > 0){
				this.prev.setVisible(true);
			}
			if(next == count - 1){
				this.next.setVisible(false);
				this.finish.setVisible(true);
			}
			nextPage.onShow(this);
			this.pages.showWidget(next);
		}else{
			SimpleDialog.displayDialog(SimpleDialog.TYPE_MESSAGE, LANG.warning_Text(), LANG.wizard_invalid_Text());
		}
	}
	
	private void prevPage(){
		int prev = this.pages.getVisibleWidget() - 1;
		WizardPage prevPage = (WizardPage)this.pages.getWidget(prev);
		if(prev == 0){
			this.prev.setVisible(false);
		}
		this.next.setVisible(true);
		this.finish.setVisible(false);
		prevPage.onShow(this);
		this.pages.showWidget(prev);
	}
	
	public void addPage(WizardPage page){
		this.pages.add(page);
		this.pages.showWidget(0);
	}
	
	private void onFinish(){
		WizardPage curPage = (WizardPage)this.pages.getWidget(this.pages.getVisibleWidget());
		if(curPage.isValid()){
			this.values.clear();
			for(int i=0;i<this.pages.getWidgetCount();i++){
				WizardPage page = (WizardPage)this.pages.getWidget(i);
				this.values.putAll(page.getValues());
			}
			this.listener.onFinish(this, this.values);
		}else{
			SimpleDialog.displayDialog(SimpleDialog.TYPE_MESSAGE, LANG.warning_Text(), LANG.wizard_invalid_Text());
		}
		
	}
	
	public HashMap getValues(){
		return this.values;
	}
	
	public void reset(){
		this.values = new HashMap();
		for(int i=0;i<this.pages.getWidgetCount();i++){
			WizardPage p = (WizardPage)this.pages.getWidget(i);
			p.reset();
		}
		this.pages.showWidget(0);
		WizardPage cPage = (WizardPage)this.pages.getWidget(0);
		cPage.onShow(this);
		
		this.next.setVisible(true);
		this.prev.setVisible(false);
		this.finish.setVisible(false);
	}
}

