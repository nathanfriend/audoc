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
import com.audata.client.widgets.CaptionButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;

/**
 * The AuDoc administration panel widget
 * @author jonm
 *
 */
public class AdminPanel extends Composite {

	private static final Language LANG = (Language) GWT.create(Language.class);
	
	/**
	 * Public constructor
	 *
	 */
	public AdminPanel() {

		final Grid grid = new Grid();
		initWidget(grid);
		grid.setCellSpacing(4);
		grid.resize(3, 3);

		final CaptionButton capButton = new CaptionButton();
		capButton.setWidth("100%");
		grid.setWidget(0, 0, capButton);
		capButton.setImageUrl("images/48x48/users.gif");
		capButton.setStyleName("block-background");
		capButton.setTitle(LANG.admin_user_title_Text());
		capButton.setCaptionText(LANG.admin_user_Text());
		capButton.addClickListener(new AdminPanelClickListener(AdminPanelClickListener.PANEL_USER));

		final CaptionButton capButton_1 = new CaptionButton();
		capButton_1.setWidth("100%");
		grid.setWidget(0, 1, capButton_1);
		capButton_1.setTitle(LANG.admin_security_title_Text());
		capButton_1.setStyleName("block-background");
		capButton_1.setImageUrl("images/48x48/security.gif");
		capButton_1.setCaptionText(LANG.admin_security_Text());
		capButton_1.addClickListener(new AdminPanelClickListener(AdminPanelClickListener.PANEL_SECURITY));

		final CaptionButton capButton_2 = new CaptionButton();
		capButton_2.setWidth("100%");
		grid.setWidget(0, 2, capButton_2);
		capButton_2.setStyleName("block-background");
		capButton_2.setTitle(LANG.admin_classification_title_Text());
		capButton_2.setCaptionText(LANG.classification_Text());
		capButton_2.setImageUrl("images/48x48/class.gif");
		capButton_2.addClickListener(new AdminPanelClickListener(AdminPanelClickListener.PANEL_CLASS));
		
		final CaptionButton capButton_3 = new CaptionButton();
		capButton_3.setWidth("100%");
		grid.setWidget(1, 0, capButton_3);
		capButton_3.setTitle(LANG.admin_keyword_title_Text());
		capButton_3.setImageUrl("images/48x48/keyword.gif");
		capButton_3.setStyleName("block-background");
		capButton_3.setCaptionText(LANG.admin_keyword_Text());
		capButton_3.addClickListener(new AdminPanelClickListener(AdminPanelClickListener.PANEL_KEYWORD));

		final CaptionButton capButton_4 = new CaptionButton();
		capButton_4.setWidth("100%");
		grid.setWidget(1, 1, capButton_4);
		capButton_4.setTitle(LANG.admin_udf_title_Text());
		capButton_4.setImageUrl("images/48x48/udf.gif");
		capButton_4.setStyleName("block-background");
		capButton_4.setCaptionText(LANG.admin_udf_Text());
		capButton_4.addClickListener(new AdminPanelClickListener(AdminPanelClickListener.PANEL_UDF));

		final CaptionButton capButton_5 = new CaptionButton();
		capButton_5.setWidth("100%");
		grid.setWidget(1, 2, capButton_5);
		capButton_5.setImageUrl("images/48x48/rectypes.gif");
		capButton_5.setTitle(LANG.admin_rectypes_title_Text());
		capButton_5.setStyleName("block-background");
		capButton_5.setCaptionText(LANG.admin_rectypes_Text());
		capButton_5.addClickListener(new AdminPanelClickListener(AdminPanelClickListener.PANEL_RECTYPE));

		final CaptionButton capButton_6 = new CaptionButton();
		capButton_6.setWidth("100%");
		grid.setWidget(2, 0, capButton_6);
		capButton_6.setTitle(LANG.admin_reports_title_Text());
		capButton_6.setStyleName("block-background");
		capButton_6.setImageUrl("images/48x48/reports.gif");
		capButton_6.setCaptionText(LANG.reports_Text());
		capButton_6.addClickListener(new AdminPanelClickListener(AdminPanelClickListener.PANEL_REPORTS));

		final CaptionButton capButton_7 = new CaptionButton();
		capButton_7.setWidth("100%");
		grid.setWidget(2, 1, capButton_7);
		capButton_7.setTitle(LANG.admin_language_title_Text());
		capButton_7.setStyleName("block-background");
		capButton_7.setImageUrl("images/48x48/lang.gif");
		capButton_7.setCaptionText(LANG.admin_language_Text());
		capButton_7.addClickListener(new AdminPanelClickListener(AdminPanelClickListener.PANEL_LANG));
		
		final CaptionButton capButton_8 = new CaptionButton();
		capButton_8.setWidth("100%");
		grid.setWidget(2, 2, capButton_8);
		capButton_8.setStyleName("block-background");
		capButton_8.setTitle(LANG.admin_modules_title_Text());
		capButton_8.setImageUrl("images/48x48/modules.gif");
		capButton_8.setCaptionText(LANG.admin_modules_Text());
		capButton_8.addClickListener(new AdminPanelClickListener(AdminPanelClickListener.PANEL_MODULES));
	}

}
