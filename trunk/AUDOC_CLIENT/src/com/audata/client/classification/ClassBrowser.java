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

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.util.TreeNodeType;
import com.audata.client.widgets.CaptionButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ClassBrowser extends VerticalPanel implements TreeListener{

	private static final Language LANG = (Language) GWT.create(Language.class);
	public Tree classes;
	private TreeItem selected;
	private Label loading;
	private ScrollPanel sp;
	private AuDoc audoc;

	public ClassBrowser(String width, String height){
		this(null, width, height);
	}
	
	public ClassBrowser(AuDoc audoc, String width, String height){
		this.audoc = audoc;
		this.setSize(width, height);
		this.sp = new ScrollPanel();
		
		this.classes = new Tree();
		this.classes.setImageBase("images/16x16/");
		this.classes.addTreeListener(this);
		this.classes.addStyleName("audoc-tree");
		//this.classes.setSize("1000px", "1000px");
		this.selected = null;
		this.sp.setSize(width, height);
		
		this.add(this.sp);
	}
	
	public void onUpdate(){
		this.loading = new Label(LANG.no_classes_Text());
		this.loading.setWidth("100%");
		this.loading.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		this.loading.addStyleName("audoc-classLabel");
		this.sp.setWidget(this.loading);
		this.selected = null;
		this.classes.clear();
		this.getClasses(null);
	}
	
	public void onTreeItemSelected(TreeItem item){
		this.selected = item;
		TreeNodeType nt = (TreeNodeType)item.getUserObject();
		if(nt.hasChildren){
			this.getClasses(nt.uuid);
		}else{
			if(this.audoc != null){
				this.getRecords(nt.uuid, nt.name);
			}
		}
	}
	
	public void onTreeItemStateChanged(TreeItem item){
		
	}
	
	public void addClass(String name, String uuid, TreeItem parent, boolean hasChildren){
		//this.sp.setWidget(this.classes);
		CaptionButton i = new CaptionButton();
		i.setCaptionText(name);
		if(hasChildren){
			//i = new CaptionButton("images/16x16/treeclass.gif", name, CaptionButton.CAPTION_EAST);
			i.setImageUrl("images/16x16/treeclass.gif");
			
		}else{
			i.setImageUrl("images/16x16/treeclassb.gif");
		}
		i.setCaptionStyleName("tree-text");
		TreeItem c = new TreeItem(i);
		c.setUserObject(new TreeNodeType("class", uuid, name, hasChildren));
		if(parent == null){
			this.classes.addItem(c);
		}else{
			parent.addItem(c);
			parent.setState(true);
		}
	}
	
	public void addClass(String name, String uuid, boolean hasChildren){
		this.addClass(name, uuid, null, hasChildren);
	}
	
	private void getClasses(String uuid){
		JSONArray params = new JSONArray();
		String method = "";
		if(uuid == null){
			//get toplevel classes
			method = "recMan.getTopLevel";
		}else{
			//get child classes
			params.set(0,new JSONString(uuid));
			method = "recMan.getChildren";
		}
		AuDoc.jsonCall.asyncPost2(method , params, new ClassResponseHandler(this, this.selected));
	}
	
	private void getRecords(String uuid, String name){
		String method = "search.rawsearch";
		String query = "FROM Record WHERE Classification.uuid = '" + uuid +"'";
		JSONArray params = new JSONArray();
		params.set(0, new JSONString(query));
		params.set(1, new JSONNumber(0));
		params.set(2, JSONBoolean.getInstance(false));
		AuDoc.jsonCall.asyncPost2(method, params, new RecordResponseHandler(this.audoc, query, name));
	}
	
	public void show(){
		this.sp.setWidget(this.classes);
	}
	
	public void onLogout(){
		this.sp.setWidget(this.loading);
	}
}
