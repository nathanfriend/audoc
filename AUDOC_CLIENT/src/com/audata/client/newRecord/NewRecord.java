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
package com.audata.client.newRecord;

import com.audata.client.Language;
import com.audata.client.feedback.UploadListener;
import com.audata.client.widgets.Wizard;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NewRecord extends VerticalPanel implements UploadListener{

	private static final Language LANG = (Language) GWT.create(Language.class);
	private Wizard wizard;
	
	public NewRecord(){
		this.wizard = new Wizard(LANG.new_record_Text(), new NewRecordListener(this));
		this.wizard.addPage(new TypeChooser());
		this.wizard.addPage(new Metadata());
		//this.wizard.addPage(new FileChooser());
		this.add(this.wizard);
	}
	
	public void onUploadComplete(){
		this.wizard.reset();
	}
}
