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
package com.audata.client.reporting;

import org.gwtwidgets.client.wrap.Callback;
import org.gwtwidgets.client.wrap.Effect;
import org.gwtwidgets.client.wrap.EffectOption;

import com.audata.client.Language;
import com.audata.client.util.Print;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ReportDialog extends DialogBox implements ClickListener, Callback{

	private static final Language LANG = (Language) GWT.create(Language.class);
	private HTML report;
	private Button print;
	private Button close;
	private VerticalPanel main;
	
	public ReportDialog(String report){
		this.setText(LANG.report_Text());
		this.setSize("600px", "400px");
		this.main = new VerticalPanel();
		this.main.setSize("100%", "100%");
		this.main.setSpacing(5);
		ScrollPanel sp = new ScrollPanel();
		sp.setStyleName("audoc-report");
		this.report = new HTML(report);
		this.report.addStyleName("audoc-report-content");
		sp.add(this.report);
		sp.setWidth("586px");
		sp.setHeight("300px");
		
		this.main.add(sp);
		
		HorizontalPanel butPanel = new HorizontalPanel();
		butPanel.setSpacing(5);
		this.print = new Button(LANG.print_Text());
		this.print.addClickListener(this);
		this.close = new Button(LANG.close_Text());
		this.close.addClickListener(this);
		butPanel.add(this.print);
		butPanel.add(this.close);
		this.main.add(butPanel);
		
		this.setWidget(this.main);
	}
	
	public void onClick(Widget sender){
		if(sender ==  this.close){
			this.hide();
		}
		
		if(sender == this.print){
			//print!
			Print.it(this.report);
			this.hide();
		}
	}
	
	/**
	 * Overide parent show to integrate effects
	 */
	public void show(){
		this.setVisible(false);
		super.show();
		Effect.appear(this);
		int left = 50;
		int top = 50;
		this.setPopupPosition(left,top);
	}
	
	public void hide(){
		Effect.fade(this, new EffectOption[]{new EffectOption("afterFinish", this)});
	}
	
	public void execute(){
		super.hide();
	}
}
