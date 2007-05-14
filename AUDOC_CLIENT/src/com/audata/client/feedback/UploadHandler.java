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
package com.audata.client.feedback;

import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;

public class UploadHandler implements FormHandler {
	
	private UploadDialog dia;
	private UploadListener up;
	
	public UploadHandler(UploadListener up){
		this.up = up;
	}

	public void onSubmit(FormSubmitEvent event) {
		this.dia = new UploadDialog();
		this.dia.show();
		this.dia.setPopupPosition(10,10);
	}

	public void onSubmitComplete(FormSubmitCompleteEvent event) {
		this.dia.hide();
		if(this.up != null){
			this.up.onUploadComplete();
		}
	}
}
