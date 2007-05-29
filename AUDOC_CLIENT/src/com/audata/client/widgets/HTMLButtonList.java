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

import java.util.ArrayList;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author jonm
 * template should contain items to be replaced as follows:
 *  "<span><1></span>
 */
public class HTMLButtonList extends Composite implements ClickListener{
	
	private ArrayList items;
	private ArrayList clickListeners;
	private ArrayList selected;
	private boolean isMultiSelect;
	private String icon;
	private String template;
	private ScrollPanel panel;
	private VerticalPanel current;
	
	public HTMLButtonList(String iconSrc, String template, boolean isMultiSelect){
		this.items = new ArrayList();
		this.selected = new ArrayList();
		this.clickListeners = new ArrayList();
		this.icon = iconSrc;
		this.template = template;
		this.isMultiSelect = isMultiSelect;
		this.panel = new ScrollPanel();
		this.panel.addStyleName("selectableButtonList");
		this.panel.setWidth("100%");
		this.initWidget(this.panel);
	}
	
	private HTML formatCaption(String[] caption){
		String cap = this.template;
		for(int i=0; i<caption.length;i++){
			String regex = "#" + i;
			cap = cap.replaceAll(regex, caption[i]);
		}
		return new HTML(cap);
	}
	
	public HTMLButton addItem(String[] caption){
		return this.addItem(caption, null, null);
	}

	public HTMLButton addItem(String[] caption, String imgSrc){
		return this.addItem(caption, imgSrc, null);
	}
	
	public HTMLButton addItem(String[] caption, String imgSrc, Object custom){
		HTML cap = this.formatCaption(caption);
		HTMLButton sb;
		if(imgSrc != null){
			sb = new HTMLButton(imgSrc, cap);
		}else{
			sb = new HTMLButton(this.icon, cap);
		}
		if(custom != null){
			sb.custom = custom;
		}
		sb.addClickListener(this);
		this.items.add(sb);
		//int size = this.panel.getOffsetWidth() - 15;
		//sb.setWidth(size + "px");
		this.selected.add(new Boolean(false));
		this.paint();
		return sb;
	}
	
	public HTMLButton getItem(int index){
		return (HTMLButton)this.items.get(index);
	}
	
	public ArrayList getItems(){
		return this.items;
	}
	
	public void addClickListener(ClickListener cl){
		this.clickListeners.add(cl);
	}
	
	public void removeClickListener(ClickListener cl){
		this.clickListeners.remove(cl);
	}
	
	public void clear(){
		this.items.clear();
		this.selected.clear();
		this.paint();
	}
	
	public void removeItem(int index){
		this.items.remove(index);
		this.selected.remove(index);
		this.paint();
	}
	
	public ArrayList getSelected(){
		ArrayList ret = new ArrayList();
		for(int i=0; i<this.selected.size();i++){
			Boolean isSelected = (Boolean)this.selected.get(i);
			if(isSelected.booleanValue()){
				ret.add(this.items.get(i));
			}
		}
		return ret;
	}
	
	public void setSelected(int index, boolean isSelected){
		this.selected.set(index, new Boolean(isSelected));
	}
	
	private void paint(){
		this.current = new VerticalPanel();
		this.current.setWidth("100%");
		this.current.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
		for(int i=0; i<this.items.size();i++){
			this.current.add((HTMLButton)this.items.get(i));
		}
		this.panel.clear();
		this.panel.add(this.current);
	}
	
	public void onClick(Widget sender){
		HTMLButton hb = (HTMLButton)sender;
		int pos = this.items.indexOf(sender);
		Boolean isSelected = (Boolean)this.selected.get(pos);
		if(isSelected.booleanValue()){
			hb.removeStyleName("htmlButton-selected");
			this.selected.set(pos, new Boolean(false));
		}else{
			hb.addStyleName("htmlButton-selected");
			this.selected.set(pos, new Boolean(true));
		}
		if(!this.isMultiSelect){
			for(int i=0;i<this.selected.size();i++){
				if(i != pos){
					HTMLButton h = (HTMLButton)this.items.get(i);
					h.removeStyleName("htmlButton-selected");
					this.selected.set(i,new Boolean(false));
				}
			}
		}
		for(int i=0;i<this.clickListeners.size();i++){
			ClickListener cl = (ClickListener)this.clickListeners.get(i);
			cl.onClick(sender);
		}
	}
	
	public int indexOf(Object obj){
		return this.items.indexOf(obj);
	}
	
	public Panel getRecordListPanel(){
		return this.current;
	}
	
	public void ensureVisible(UIObject item){
	    this.panel.ensureVisible(item);
	}
}
