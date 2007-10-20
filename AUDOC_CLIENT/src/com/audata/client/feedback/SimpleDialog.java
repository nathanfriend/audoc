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

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SimpleDialog extends DialogBox implements ClickListener, Callback{

	public static final int TYPE_MESSAGE = 0;
	public static final int TYPE_ERROR = 1;
	public static final int TYPE_QUERY = 2;
	
	private Button close;
	private Button ok;
	private Button cancel;
	private int type;
	private ResponseListener listener;
	
	public static void displayDialog(int type, String title, String message, ResponseListener listener){
		SimpleDialog d = new SimpleDialog(type, title, message, listener);
		d.setVisible(false); //prevents the dialog from displaying prematurely
		d.show();
		Effect.slideDown(d);		
	}
	
	public static void displayDialog(int type, String title, String message){
		SimpleDialog d = new SimpleDialog(type, title, message, null);
		//d.setVisible(false); //prevents the dialog from displaying prematurely
		d.show();
		Effect.slideDown(d);		
	}
	
	private SimpleDialog(int type, String title, String message, ResponseListener listener){
		this.listener = listener;
		this.type = type;
		this.setText(title);
		this.addStyleName("audoc-simpleDialog");
		
		DockPanel main = new DockPanel();
		
	    main.setSpacing(4);
	    HorizontalPanel butPanel = new HorizontalPanel();
	    butPanel.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
	    butPanel.setSpacing(4);
	    switch(type){
	    case SimpleDialog.TYPE_ERROR:
	    	main.add(new Image("images/48x48/error.gif"), DockPanel.WEST);
	    	this.close = new Button("Close");
		    this.close.addClickListener(this);
		    butPanel.add(this.close);
	    	break;
	    case SimpleDialog.TYPE_MESSAGE:
	    	main.add(new Image("images/48x48/udf.gif"), DockPanel.WEST);
	    	this.close = new Button("Close");
		    this.close.addClickListener(this);
		    butPanel.add(this.close);
	    	break;
	    case SimpleDialog.TYPE_QUERY:
	    	main.add(new Image("images/48x48/help.gif"), DockPanel.WEST);
	    	this.ok = new Button("Ok");
	    	this.ok.addClickListener(this);
	    	this.cancel = new Button("Cancel");
	    	this.cancel.addClickListener(this);
	    	butPanel.add(this.ok);
	    	butPanel.add(this.cancel);
	    	break;
	    }
	    VerticalPanel p = new VerticalPanel();
	    p.setSpacing(15);
	    p.add(new Label(message));
	    p.add(butPanel);
	    p.setCellHorizontalAlignment(butPanel, HasAlignment.ALIGN_RIGHT);
	    main.add(p, DockPanel.EAST);
	    this.setWidget(main);
		this.setPopupPosition(0, 0);
	}
	
	public void onClick(Widget sender){
		if(sender == this.close){
			Effect.slideUp(this, new EffectOption[]{new EffectOption("afterFinish", this)});
			return;
		}
		if(sender == this.cancel){
			if(this.listener != null){
				this.listener.onResponse(false);
			}
			Effect.slideUp(this, new EffectOption[]{new EffectOption("afterFinish", this)});
		}
		if(sender == this.ok){
			if(this.listener != null){
				this.listener.onResponse(true);
			}
			Effect.slideUp(this, new EffectOption[]{new EffectOption("afterFinish", this)});
		}
		
	}
	
	/**
	 * Handles users pressing ENTER to submit the form
	 */
	public boolean onKeyPressPreview(char key, int modifiers){
		if ((key == KeyboardListener.KEY_ENTER) && (this.type == SimpleDialog.TYPE_ERROR || this.type == SimpleDialog.TYPE_MESSAGE)){
			onClick(this.close);
		}
		return true;		
	}
	
	public void execute(){
		this.hide();
	}
}
