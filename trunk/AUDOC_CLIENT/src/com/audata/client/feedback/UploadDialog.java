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

import org.gwtwidgets.client.wrap.Callback;
import org.gwtwidgets.client.wrap.Effect;
import org.gwtwidgets.client.wrap.EffectOption;

import com.audata.client.Language;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;

public class UploadDialog extends DialogBox implements Callback {

	private static final Language LANG = (Language) GWT.create(Language.class);
	
	public UploadDialog(){
		setText(LANG.uploading_Text());
		Image uploader = new Image("images/custom/upload.gif");
		this.setWidget(uploader);
	}
	
	/**
	 * Overide parent show method to add effects
	 * and dialog centering
	 */
	public void show(){
		this.setVisible(false);
		super.show();
		Effect.appear(this);
		int left = 20;
		int top = 20;
		this.setPopupPosition(left,top);
	}
	
	/**
	 * Overide parent hide method to add
	 * effects
	 */
	public void hide(){
		Effect.fade(this, new EffectOption[]{new EffectOption("afterFinish", this)});
		//Effect.dropOut(this, new EffectOption[]{new EffectOption("afterFinish", this)});
	}
	
	/**
	 * Called when hide effect are complete
	 * to finish hide process
	 */
	public void execute(){
		super.hide();
	}
}
