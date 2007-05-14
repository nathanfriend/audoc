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

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class KeywordField extends Composite implements ClickListener, IValidated {

	private KeywordTextBox tb;
	private Button select;
	private String hierarchy;
	
	public KeywordField(String hierarchy, String title){
		this.hierarchy = hierarchy;
		HorizontalPanel main = new HorizontalPanel();
		main.setSpacing(2);
		
		this.tb = new KeywordTextBox(this.hierarchy, title);
		this.tb.setEnabled(false);
		this.select = new Button("...");
		this.select.addClickListener(this);
		main.add(this.tb);
		main.add(this.select);
		
		this.initWidget(main);
	}
	
	public void onClick(Widget sender) {
		int left = sender.getAbsoluteLeft();
		int top = sender.getAbsoluteTop();
		this.tb.showBrowser(left, top);
	}
	
	public TextBox getTextBox(){
		return this.tb;
	}
	
	public boolean isValid(){
		if(this.tb.getText().length() != 0){
			return true;
		}else{
			return false;
		}
	}
}
