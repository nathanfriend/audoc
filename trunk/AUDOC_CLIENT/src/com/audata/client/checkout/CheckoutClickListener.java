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

import com.audata.client.record.RecordPropertiesDialog;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class CheckoutClickListener implements ClickListener {

	private CheckoutPanel parent;
	private String uuid;
	private String recType;
	private String cot;
	
	public CheckoutClickListener(CheckoutPanel parent, String uuid, String recType, String cot){
		this.parent = parent;
		this.uuid = uuid;
		this.recType = recType;
		this.cot = cot;
	}
	
	public void onClick(Widget sender) {
		new RecordPropertiesDialog(this.parent, this.recType, this.uuid, this.cot);
	}

}
