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
package com.audata.client.Keyword;

import java.util.HashMap;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.widgets.CaptionButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.VerticalPanel;

public class KeywordBrowser extends VerticalPanel implements TreeListener{

	private static final Language LANG = (Language) GWT.create(Language.class);
	public String hierarchy;
	public Tree keywords;
	private TreeItem selected;
	private Label loading;
	private ScrollPanel sp;

	public KeywordBrowser(String width, String height){
		this.setSize(width, height);
		this.sp = new ScrollPanel();
		
		this.keywords = new Tree();
		this.keywords.setImageBase("images/16x16/");
		this.keywords.addTreeListener(this);
		this.keywords.addStyleName("audoc-tree");
		//this.classes.setSize("1000px", "1000px");
		this.selected = null;
		this.sp.setSize(width, height);
		
		this.add(this.sp);
	}
	
	public void init(String hierarchy){
		this.hierarchy = hierarchy;
		this.loading = new Label(LANG.loading_Text());
		this.sp.setWidget(this.loading);
		this.selected = null;
		this.keywords.clear();
		this.getKeywords(null);
	}
	
	public void onTreeItemSelected(TreeItem item){
		this.selected = item;
		HashMap nt = (HashMap)item.getUserObject();
		Boolean hasChildren = (Boolean)nt.get("hasChildren"); 
		if(hasChildren.booleanValue()){
			this.getKeywords((String)nt.get("uuid"));
		}
	}
	
	public void onTreeItemStateChanged(TreeItem item){
		
	}
	
	public void addClass(String name, String uuid, TreeItem parent, boolean hasChildren, String path){
		this.sp.setWidget(this.keywords);
		CaptionButton i = new CaptionButton();
		i.setCaptionText(name);
		if(hasChildren){
			i.setImageUrl("images/16x16/treeclass.gif");
		}else{
			i.setImageUrl("images/16x16/treeclassb.gif");
		}
		i.setCaptionStyleName("tree-text");
		TreeItem c = new TreeItem(i);
		HashMap uObj = new HashMap();
		uObj.put("uuid", uuid);
		uObj.put("name", name);
		uObj.put("path", path);
		uObj.put("hasChildren", new Boolean(hasChildren));
		//c.setUserObject(new TreeNodeType("class", uuid, name, hasChildren));
		c.setUserObject(uObj);
		if(parent == null){
			this.keywords.addItem(c);
		}else{
			parent.addItem(c);
			parent.setState(true);
		}
	}
	
	public void addClass(String name, String uuid, boolean hasChildren, String path){
		this.addClass(name, uuid, null, hasChildren, path);
	}
	
	private void getKeywords(String uuid){
		JSONArray params = new JSONArray();
		String method = "";
		if(uuid == null){
			//get toplevel classes
			params.set(0, new JSONString(this.hierarchy));
			method = "recman.getKWTopLevel";
		}else{
			//get child classes
			params.set(0,new JSONString(uuid));
			method = "recman.getKWChildren";
		}
		AuDoc.jsonCall.asyncPost2(method , params, new KWResponseHandler(this, this.selected));
	}
	
	public void finishedLoading(){
		this.loading.setText("");
	}
}
