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

import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class NumericTextBox extends TextBox implements KeyboardListener, IValidated {

	public NumericTextBox() {
		this.addKeyboardListener(this);
		this.addStyleName("audoc-valid");
	}

	public void onKeyPress(Widget sender, char keyCode, int modifiers) {

	}

	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
		String newChar = Character.toString(keyCode);
		switch(keyCode){
		case(KeyboardListener.KEY_BACKSPACE):
		case(KeyboardListener.KEY_DELETE):
		case(KeyboardListener.KEY_DOWN):
		case(KeyboardListener.KEY_END):
		case(KeyboardListener.KEY_ENTER):
		case(KeyboardListener.KEY_ESCAPE):
		case(KeyboardListener.KEY_HOME):
		case(KeyboardListener.KEY_LEFT):
		case(KeyboardListener.KEY_PAGEUP):
		case(KeyboardListener.KEY_PAGEDOWN):
		case(KeyboardListener.KEY_RIGHT):
		case(KeyboardListener.KEY_SHIFT):
		case(KeyboardListener.KEY_TAB):
		case(KeyboardListener.KEY_UP):
		break;
		default:
			try {
				new Double(newChar);
			} catch (NumberFormatException nfe) {
				this.cancelKey();
			} 
		}
	}

	public void onKeyUp(Widget sender, char keyCode, int modifiers) {

	}
	
	public boolean isValid(){
		if (this.getText().length() != 0){
			return true;
		}else{
			return false;
		}
	}
}
