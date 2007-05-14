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

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.DockPanel.DockLayoutConstant;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;

public class CaptionButton extends Composite {

	private DockPanel main;
	private HTML caption;
	private Image image;
	private Object userObject;
	
	public CaptionButton() {
		main = new DockPanel();
		initWidget(main);
		main.setSpacing(4);
		main.setStyleName("captionButton");

		image = new Image();
		main.add(image, DockPanel.WEST);
		main.setCellHorizontalAlignment(image, HasHorizontalAlignment.ALIGN_LEFT);
		main.setCellVerticalAlignment(image, HasVerticalAlignment.ALIGN_MIDDLE);
		image.setStyleName("captionButton-Icon");
		image.setUrl("images/48x48/admin.gif");

		caption = new HTML("New Label");
		main.add(caption, DockPanel.EAST);
		main.setCellHorizontalAlignment(caption, HasHorizontalAlignment.ALIGN_LEFT);
		main.setCellVerticalAlignment(caption, HasVerticalAlignment.ALIGN_MIDDLE);
		caption.setStyleName("captionButton-caption");
		//setWidth("100%");
	}
	
	public void setOrientation(DockLayoutConstant layout){
		DockLayoutConstant capPos = layout;
		DockLayoutConstant imgPos = DockPanel.WEST;
		HorizontalAlignmentConstant capHorizAlign = HasHorizontalAlignment.ALIGN_CENTER;
		VerticalAlignmentConstant capVertAlign = HasVerticalAlignment.ALIGN_MIDDLE;
		HorizontalAlignmentConstant imgHorizAlign = HasHorizontalAlignment.ALIGN_CENTER;
		VerticalAlignmentConstant imgVertAlign = HasVerticalAlignment.ALIGN_MIDDLE;
		if(layout == DockPanel.EAST){
			imgPos = DockPanel.WEST;
			capHorizAlign = HasHorizontalAlignment.ALIGN_LEFT;
			imgHorizAlign = HasHorizontalAlignment.ALIGN_LEFT;
		}
		if(layout == DockPanel.NORTH){
			imgPos = DockPanel.SOUTH;
			capVertAlign = HasVerticalAlignment.ALIGN_BOTTOM;
			imgVertAlign = HasVerticalAlignment.ALIGN_BOTTOM;
		}
		if(layout == DockPanel.WEST){
			imgPos = DockPanel.EAST;
			capHorizAlign = HasHorizontalAlignment.ALIGN_LEFT;
			imgHorizAlign = HasHorizontalAlignment.ALIGN_LEFT;
		}
		if(layout == DockPanel.SOUTH){
			imgPos = DockPanel.NORTH;
			capVertAlign = HasVerticalAlignment.ALIGN_BOTTOM;
			imgVertAlign = HasVerticalAlignment.ALIGN_BOTTOM;
		}
		main.clear();
		main.add(caption, capPos);
		main.setCellHorizontalAlignment(caption, capHorizAlign);
		main.setCellVerticalAlignment(caption, capVertAlign);
		main.add(image,imgPos);
		main.setCellHorizontalAlignment(image, imgHorizAlign);
		main.setCellVerticalAlignment(image, imgVertAlign);
	}
	
	public String getCaptionText() {
		return caption.getHTML();
	}
	public void setCaptionText(String text) {
		caption.setHTML(text);
	}
	public String getImageUrl() {
		return image.getUrl();
	}
	public void setImageUrl(String url) {
		image.setUrl(url);
	}
	
	public void setWidth(String width){
		super.setWidth(width);
		caption.setWidth("100%");
	}
	public String getTitle() {
		return main.getTitle();
	}
	public void setTitle(String title) {
		main.setTitle(title);
	}
	
	public void addClickListener(ClickListener listener){
		image.addClickListener(listener);
		caption.addClickListener(listener);
	}
	
	public void setCaptionStyleName(String style){
		this.caption.setStyleName(style);
	}
	
	public void addCaptionStyleName(String style){
		this.caption.addStyleName(style);
	}
	
	public void removeCaptionStyleName(String style){
		this.caption.removeStyleName(style);
	}

	public void setUserObject(Object userObject){
		this.userObject = userObject;
	}
	
	public Object getUserObject(){
		return this.userObject;
	}
}
