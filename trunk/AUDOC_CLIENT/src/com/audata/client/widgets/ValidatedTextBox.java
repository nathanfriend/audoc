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

import java.util.Date;

import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ValidatedTextBox extends TextBox implements KeyboardListener, IValidated {

	public static String PATTERN_DATE = "(^[0-9]{4}-[0-9]{2}-[0-9]{2}$)|(^[-+][0-9]+ (week[s]?|month[s]?|year[s]?)$)|(^now$)|(^today$)";
	public static String PATTERN_ANY = ".*";
	public static String PATTERN_FLOAT = "[-+]?([0-9]*.)?[0-9]+";
	
	private String pattern;
	private boolean isValid;
	
	public ValidatedTextBox(String pattern) {
		this.isValid = false;
		this.pattern = pattern;
		this.addKeyboardListener(this);
		this.addStyleName("audoc-invalid");
	}

	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		//auto file todays date
		if(this.pattern == ValidatedTextBox.PATTERN_DATE){
			String newChar = Character.toString(keyCode);
			if(newChar.equalsIgnoreCase("t") && modifiers == KeyboardListener.MODIFIER_SHIFT){
				this.cancelKey();
				Date d = new Date();
				String year = Integer.toString(d.getYear() + 1900);
				String month = Integer.toString(d.getMonth() + 1);
				if(month.length() == 1){
					month = "0" + month;
				}
				String day = Integer.toString(d.getDate());
				if(day.length() == 1){
					day = "0" + day;
				}
				this.setText(year + "-" + month + "-" + day);
			}
		}
	}

	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
		
	}

	public void onKeyUp(Widget sender, char keyCode, int modifiers) {
		String text = this.getText();
		if (text.matches(this.pattern)){
			this.removeStyleName("audoc-invalid");
			this.addStyleName("audoc-valid");
			this.isValid = true;
		}else{
			this.removeStyleName("audoc-valid");
			this.addStyleName("audoc-invalid");
			this.isValid = false;
		}
	}
	
	public void setText(String text){
		super.setText(text);
		if (text.matches(this.pattern)){
			this.removeStyleName("audoc-invalid");
			this.addStyleName("audoc-valid");
		}else{
			this.removeStyleName("audoc-valid");
			this.addStyleName("audoc-invalid");
		}
	}
	
	public boolean isValid(){
		return this.isValid;
	}
}
