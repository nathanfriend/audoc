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

import java.util.ArrayList;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author jonm
 *
 */
public class HTMLButton extends Composite implements ClickListener{

	private ArrayList clickListeners;
	private Image icon;
	private HTML caption;
	private HorizontalPanel panel;
	
	public Object custom;
	
	public HTMLButton(String iconSrc, HTML caption){
		this.clickListeners = new ArrayList();
		HorizontalPanel panel = new HorizontalPanel();
		panel.setVerticalAlignment(HasAlignment.ALIGN_TOP);
		this.panel = panel;
		this.panel.setWidth("100%");
		this.panel.addStyleName("htmlButton");
		
		this.icon = new Image(iconSrc);
		this.icon.addStyleName("htmlButton-icon");
		
		this.caption = caption;
		this.caption.setWidth("100%");
		
		this.caption.addStyleName("htmlButton-caption");
		

		this.panel.add(this.icon);
		this.panel.add(this.caption);
		this.panel.setSpacing(5);
		this.panel.setCellWidth(this.icon,"48px");
		
		this.caption.addClickListener(this);
		this.icon.addClickListener(this);
		this.initWidget(this.panel);
	}
	
	public void setSpacing(int spacing){
		this.panel.setSpacing(spacing);
	}

	
	public void addClickListener(ClickListener cl){
		this.clickListeners.add(cl);
	}
	
	public void removeClickListener(ClickListener cl){
		this.clickListeners.remove(cl);
	}
	
	public void onClick(Widget sender){
		for(int i=0;i<this.clickListeners.size();i++){
			ClickListener cl = (ClickListener)this.clickListeners.get(i);
			cl.onClick(this);
		}
	}
}
