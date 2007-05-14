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

import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.VerticalPanel;


public class UploadPanel extends FormPanel {

	private FileUpload file;
	private Hidden recid;
	
	public UploadPanel(){
		VerticalPanel main = new VerticalPanel();
		
		this.file = new FileUpload();
		this.file.setName("document");
		
		this.recid = new Hidden();
		this.recid.setName("recid");
		
		Hidden method = new Hidden();
		method.setName("method");
		method.setValue("put");
		
		main.add(this.recid);
		main.add(method);
		main.add(this.file);
		this.add(main);
	}
	
	public void submit(String uuid){
		this.recid.setValue(uuid);
		super.submit();
	}
	
	public String getText(){
		return this.file.getFilename();
	}
}
