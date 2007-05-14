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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A simple widget that represents the home page of the AuDoc system
 * 
 * @author jonm
 */
public class HomePanel extends Composite {

    private static final Language LANG = (Language) GWT.create(Language.class);

    /**
     * Public contructor. Builds the HomePanel
     */
    public HomePanel() {

	final VerticalPanel verticalPanel = new VerticalPanel();
	initWidget(verticalPanel);
	verticalPanel.setSize("100%", "100%");

	final Image image = new Image();
	verticalPanel.add(image);
	verticalPanel.setCellVerticalAlignment(image,
		HasVerticalAlignment.ALIGN_MIDDLE);
	verticalPanel.setCellHorizontalAlignment(image,
		HasHorizontalAlignment.ALIGN_CENTER);
	image.setUrl("images/title/welcome.jpg");

	final HTML html = new HTML(LANG.homepage_Text());
	html.setWidth("100%");
	verticalPanel.add(html);
	html.setStyleName("audoc-welcomeText");
	setSize("100%", "100%");
    }

}
