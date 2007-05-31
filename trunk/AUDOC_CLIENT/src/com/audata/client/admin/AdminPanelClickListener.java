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

import com.audata.client.Language;
import com.audata.client.AuDoc;
import com.audata.client.feedback.SimpleDialog;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ClickListener class for the AdminPanel widget
 * @author jonm
 *
 */
public class AdminPanelClickListener implements ClickListener {

	public static final int PANEL_USER = 0;
	public static final int PANEL_SECURITY = 1;
	public static final int PANEL_CLASS = 2;
	public static final int PANEL_UDF = 3;
	public static final int PANEL_KEYWORD = 4;
	public static final int PANEL_RECTYPE = 5;
	public static final int PANEL_REPORTS = 6;
	public static final int PANEL_LANG = 7;
	public static final int PANEL_MODULES = 8;
	
	private int panel;
	
	private static final Language LANG = (Language) GWT.create(Language.class);
	
	/**
	 * Constructor taking an int representing which admin
	 * section should be loaded
	 * @param panel An integer representing which admin section should be loaded @see AdminPanel
	 */
	public AdminPanelClickListener(int panel){
		this.panel = panel;
	}
	
	/**
	 * Called when the widget bound to this ClickListener is clicked
	 * Loads the appropriate admin section
	 */
	public void onClick(Widget sender) {
		Panel p = null;
		switch(this.panel){
		case(AdminPanelClickListener.PANEL_SECURITY):
			p = new SecurityPanel();
			break;
		case(AdminPanelClickListener.PANEL_USER):
			p = new UserPanel();
			break;
		case(AdminPanelClickListener.PANEL_CLASS):
			p = new ClassificationPanel();
			break;
		case(AdminPanelClickListener.PANEL_UDF):
			p = new UDFPanel(); 
			break;
		case(AdminPanelClickListener.PANEL_RECTYPE):
			p = new RecTypePanel();
			break;
		case(AdminPanelClickListener.PANEL_MODULES):
			p = null;
			break;
		case(AdminPanelClickListener.PANEL_LANG):
			p = null;
			break;
		case(AdminPanelClickListener.PANEL_KEYWORD):
			p = new KeywordPanel();
			break;
		case(AdminPanelClickListener.PANEL_REPORTS):
			p = new ReportingPanel();
			break;
		}
	
		if(p != null){
			AuDoc a = AuDoc.getInstance();
			a.setMain(p);
			//this.parent.clear();
			//this.parent.add(p);
		}else{
			SimpleDialog.displayDialog(SimpleDialog.TYPE_MESSAGE, LANG.not_implemented_Text(),LANG.not_implemented_msg_Text());
		}
	}
}
