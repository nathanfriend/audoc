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
package com.audata.client.classification;

import com.audata.client.Language;
import com.audata.client.util.TreeNodeType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PopUpClassBrowser extends DialogBox implements TreeListener, ClickListener{

	private static final Language LANG = (Language) GWT.create(Language.class);
	private ClassBrowser cp;
	private TextBox cName;
	private String uuid;
	private Button cancelBut;
	
	public PopUpClassBrowser(TextBox cName, String uuid){
		VerticalPanel main = new VerticalPanel();
		main.setSpacing(5);
		this.setText(LANG.class_chooser_Text());
		this.cName = cName;
		this.uuid = uuid;
		this.cp = new ClassBrowser("200px", "200px");
		this.cp.onUpdate();
		this.cp.classes.addTreeListener(this);
		main.add(this.cp);
		
		this.cancelBut = new Button(LANG.cancel_Text());
		this.cancelBut.addClickListener(this);
		main.add(this.cancelBut);
		this.setWidget(main);
		
	}
	
	public void onTreeItemSelected(TreeItem item){
		TreeNodeType nt = (TreeNodeType)item.getUserObject();
		if(!nt.hasChildren){
			this.setValues(item.getText(), nt.uuid);
			this.hide();
		}
	}
	
	public void onTreeItemStateChanged(TreeItem item){
		
	}
	
	
	public void show(int left, int top){
		this.setPopupPosition(left, top);
		super.show();
	}
	
	public void onClick(Widget sender){
		if(sender == this.cancelBut){
			this.hide();
		}
	}
	
	public String getUUID(){
		return this.uuid;
	}
	
	public void setValues(String name, String uuid){
		this.uuid = uuid;
		this.cName.setText(name);
	}
}
