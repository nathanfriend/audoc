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

import java.util.HashMap;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.Keyword.KeywordBrowser;
import com.audata.client.json.UpdateListener;
import com.audata.client.json.UpdateResponseHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Keyword Hierarchy Admin panel
 * @author jonm
 *
 */
public class KeywordPanel extends VerticalPanel implements TreeListener,
		ClickListener, UpdateListener, ChangeListener {

	private static final Language LANG = (Language) GWT.create(Language.class);
	private KeywordBrowser keywordBrowser;

	private String selectedUUID = null;

	private boolean isNew = true;

	private TextBox name;


	private Button save;

	private Button delete;

	private Button newTop;

	private Button newChild;
	
	private Button newHierarchy;
	
	private Button delHierarchy;
	
	private ListBox hierarchies;

	/**
	 * Public Constructor
	 *
	 */
	public KeywordPanel() {
		this.setSpacing(5);
		this.setSize("100%", "100%");

		// add section title
		Label l = new Label(LANG.admin_keyword_Text());
		l.addStyleName("audoc-sectionTitle");
		this.add(l);

		HorizontalPanel kwhs = new HorizontalPanel();
		kwhs.setSpacing(5);
		Label kl = new Label(LANG.keyword_hierarchy_Text());
		kwhs.add(kl);
		
		this.hierarchies = new ListBox();
		this.hierarchies.setWidth("150px");
		this.hierarchies.setVisibleItemCount(1);
		this.hierarchies.addChangeListener(this);
		kwhs.add(this.hierarchies);
		
		this.newHierarchy = new Button(LANG.new_Text());
		this.newHierarchy.addClickListener(this);
		kwhs.add(this.newHierarchy);
		
		this.delHierarchy = new Button(LANG.delete_Text());
		this.delHierarchy.addClickListener(this);
		kwhs.add(this.delHierarchy);
		
		this.add(kwhs);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(10);
		hp.setSize("100%", "100%");
		
		
		// add classification browser

		this.keywordBrowser = new KeywordBrowser("100%", "100%");
		this.keywordBrowser.keywords.addTreeListener(this);
		this.keywordBrowser.setStyleName("audoc-browser");
		this.keywordBrowser.setSize("100%", "100%");
		hp.add(this.keywordBrowser);
		hp.setCellWidth(this.keywordBrowser, "50%");
		hp.setCellHeight(this.keywordBrowser, "300px");

		// form
		VerticalPanel vp = new VerticalPanel();
		vp.addStyleName("audoc-form");
		vp.setSpacing(5);

		// name field
		HorizontalPanel namePanel = new HorizontalPanel();
		namePanel.setSpacing(5);
		Label n = new Label(LANG.name_Text());
		n.setWidth("100px");
		namePanel.add(n);
		this.name = new TextBox();
		namePanel.add(this.name);
		vp.add(namePanel);

		// Buttons
		HorizontalPanel butPanel = new HorizontalPanel();
		butPanel.setSpacing(5);

		this.save = new Button(LANG.save_Text());
		this.save.addClickListener(this);
		butPanel.add(this.save);

		this.delete = new Button(LANG.delete_Text());
		this.delete.addClickListener(this);
		butPanel.add(this.delete);

		this.newTop = new Button(LANG.new_Text());
		this.newTop.addClickListener(this);
		butPanel.add(this.newTop);

		this.newChild = new Button(LANG.new_child_Text());
		this.newChild.addClickListener(this);
		butPanel.add(this.newChild);

		vp.add(butPanel);

		// add it all up!
		hp.add(vp);
		
		this.add(hp);
		this.getHierarchies();
	}

	/**
	 * Called when a TreeItem is selected.
	 * Attempts to obtain child levels
	 */
	public void onTreeItemSelected(TreeItem item) {
		HashMap nt = (HashMap) item.getUserObject();
		
		this.selectedUUID = (String)nt.get("uuid");
		this.isNew = false;
		this.name.setText((String)nt.get("name"));
	}

	/**
	 * Called when a TreeItem is expanded or contracted
	 */
	public void onTreeItemStateChanged(TreeItem item) {
	}

	/**
	 * Called when one of the buttons is
	 * clicked
	 * @param sender The Widget clicked 
	 */
	public void onClick(Widget sender) {
		if (sender == this.newTop) {
			// clear correctly selected parent
			this.selectedUUID = null;
			this.isNew = true;
			// reset form
			this.name.setText("");
			return;
		}

		if (sender == this.newChild) {
			this.isNew = true;
			// reset form
			this.name.setText("");
			return;
		}

		if (sender == this.save) {
			this.saveKeyword();
			return;
		}

		if (sender == this.delete) {
			if (this.selectedUUID != null) {
				JSONArray params = new JSONArray();
				params.set(0, new JSONString(this.selectedUUID));

				AuDoc.jsonCall.asyncPost2("recman.delKWData", params, new UpdateResponseHandler(this));
				return;
			}
		}
		
		if(sender == this.newHierarchy){
			this.isNew = false;
			NewHierarchy nh = new NewHierarchy(this);
			nh.show();
		}
		
		if(sender == this.delHierarchy){
			int index = this.hierarchies.getSelectedIndex();
			String value = this.hierarchies.getValue(index);
			if(!value.equals("")){
				this.isNew = false;
				JSONArray params = new JSONArray();
				params.set(0, new JSONString(value));
				AuDoc.jsonCall.asyncPost2("recman.delKWH", params, new UpdateResponseHandler(this));
			}
		}
	}

	/**
	 * Called when an Update Response is received
	 */
	public void onUpdate() {
		if(this.isNew){
			int index = this.hierarchies.getSelectedIndex();
			this.keywordBrowser.init(this.hierarchies.getValue(index));
			this.isNew = false;
		}else{
			this.getHierarchies();
		}
		// clear correctly selected parent
		this.selectedUUID = null;
		this.isNew = true;

		// reset form
		this.name.setText("");
	}

	/**
	 * Saves a keyword
	 *
	 */
	private void saveKeyword() {
		String method = "";
		int index = this.hierarchies.getSelectedIndex();
		String hierarchy = this.hierarchies.getValue(index);
		JSONArray params = new JSONArray();
		if(this.isNew){
			method = "recman.addKWData";
			if(this.selectedUUID == null){
				//new top level
				params.set(0, new JSONString(hierarchy));
				params.set(1, JSONNull.getInstance());
				params.set(2, new JSONString(this.name.getText()));
			}else{
				//new child
				params.set(0, new JSONString(hierarchy));
				params.set(1, new JSONString(this.selectedUUID));
				params.set(2, new JSONString(this.name.getText()));
			}
		}else{
			method = "recman.modKWData";
			//mod existing
			params.set(0, new JSONString(this.selectedUUID));
			params.set(1, new JSONString(this.name.getText()));
		}
		if(!method.equals("")){
			this.isNew = true; // forces update of hierarchy
			AuDoc.jsonCall.asyncPost2(method, params, new UpdateResponseHandler(this));
		}
		return;
	}
	
	/**
	 * Sends a request to receive a list of all existing
	 * Keyword Hierarchies
	 *
	 */
	private void getHierarchies(){
		this.hierarchies.clear();
		this.hierarchies.addItem(LANG.choose_hierarchy_Text(), "");
		AuDoc.jsonCall.asyncPost2("recman.getKWHs", new JSONArray(), new HierarchyResponseHandler(this));
	}
	
	/**
	 * Adds a hierarchy to the list
	 * @param uuid The uuid of the Keyword Hierarchy
	 * @param name The name of the Keyword Hierarchy
	 */
	public void addHierarchy(String uuid, String name){
		this.hierarchies.addItem(name, uuid);
	}
	
	/**
	 * Called when a different keyword hierarch is selected
	 */
	public void onChange(Widget sender){
		if(sender == this.hierarchies){
			int index = this.hierarchies.getSelectedIndex();
			String value = this.hierarchies.getValue(index);
			if(!value.equals("")){
				this.keywordBrowser.init(value);
			}
		}
	}
}
