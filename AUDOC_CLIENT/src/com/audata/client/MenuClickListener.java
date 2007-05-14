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
package com.audata.client;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * A ClickListener for the AuDoc menu
 * @author jonm
 *
 */
public class MenuClickListener implements ClickListener {

	private int section;
	private AuDoc parent;
	
	/**
	 * Constructor for the ClickListener specifying the parent object and the
	 * desired section 
	 * @param parent A link back to the current instance of AuDoc
	 * @param section An integer representing which section the click listener should load @see AuDoc
	 */
	public MenuClickListener(AuDoc parent, int section){
		this.section = section;
		this.parent = parent;
	}
	
	/**
	 * Called when a user clicks on the bound widget
	 * @param sender The widget that sent the request
	 */
	public void onClick(Widget sender) {
		parent.switchMain(this.section);
	}
}
