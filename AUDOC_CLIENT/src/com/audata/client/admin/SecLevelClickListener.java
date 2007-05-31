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
package com.audata.client.admin;

import java.util.HashMap;

import com.audata.client.widgets.HTMLButton;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
/**
 * ClickListener for the Security Level list on the security
 * admin panel
 * @author jonm
 *
 */
public class SecLevelClickListener implements ClickListener {
	
	private SecurityPanel parent;
	
	/**
	 * Public contructor
	 * @param parent
	 */
	public SecLevelClickListener(SecurityPanel parent){
		this.parent = parent;
	}

	/**
	 * Called when a security level is clicked on 
	 * the SecLevel list. Updates the Security Level
	 * form
	 */
	public void onClick(Widget sender) {
		HTMLButton widget = (HTMLButton)sender;
		HashMap custom = (HashMap)widget.custom;
		String name = (String)custom.get("name");
		Integer level = (Integer)custom.get("level");
		String uuid = (String)custom.get("uuid");
		this.parent.setLevelForm(name, level.intValue(), uuid);
	}

}
