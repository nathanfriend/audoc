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
package com.audata.client.admin.classification;

import java.util.ArrayList;
import java.util.HashMap;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.classification.ClassBrowser;
import com.audata.client.feedback.SimpleDialog;
import com.audata.client.json.UpdateListener;
import com.audata.client.json.UpdateResponseHandler;
import com.audata.client.util.TreeNodeType;
import com.audata.client.widgets.NumericTextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Button;
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
 * Classification administration panel
 * @author jonm
 *
 */
public class ClassificationPanel extends VerticalPanel implements TreeListener,
		ClickListener, UpdateListener {

	private static final Language LANG = (Language) GWT.create(Language.class);
	
	private ClassBrowser classBrowser;

	private String selectedUUID = null;

	private String selectParentID = null;

	private boolean isNew = true;

	private TextBox name;

	private NumericTextBox retention;

	private ListBox secLevel;

	private ListBox caveats;

	private Button save;

	private Button delete;

	private Button newTop;

	private Button newChild;

	/**
	 * Public constructor of Classification
	 * Admin panel
	 *
	 */
	public ClassificationPanel() {
		this.setSpacing(5);
		this.setSize("100%", "100%");

		// add section title
		Label l = new Label(LANG.classification_Text());
		l.addStyleName("audoc-sectionTitle");
		this.add(l);

		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(10);
		hp.setSize("100%", "100%");
		
		
		// add classification browser
		this.classBrowser = new ClassBrowser("100%", "100%");
		this.classBrowser.onUpdate();
		this.classBrowser.classes.addTreeListener(this);
		this.classBrowser.setStyleName("audoc-browser");
		this.classBrowser.setSize("100%", "100%");
		hp.add(this.classBrowser);
		

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

		// retention field
		HorizontalPanel retPanel = new HorizontalPanel();
		retPanel.setSpacing(5);
		Label r = new Label(LANG.retention_Text());
		r.setWidth("100px");
		retPanel.add(r);
		this.retention = new NumericTextBox();
		retPanel.add(this.retention);
		vp.add(retPanel);

		// security level field
		HorizontalPanel secPanel = new HorizontalPanel();
		secPanel.setSpacing(5);
		Label s = new Label(LANG.security_level_Text());
		s.setWidth("100px");
		secPanel.add(s);
		this.secLevel = new ListBox();
		secPanel.add(this.secLevel);
		vp.add(secPanel);

		// caveats field
		HorizontalPanel cavPanel = new HorizontalPanel();
		cavPanel.setSpacing(5);
		Label c = new Label(LANG.security_caveat_Text());
		c.setWidth("100px");
		cavPanel.add(c);
		this.caveats = new ListBox();
		this.caveats.setMultipleSelect(true);
		this.caveats.setVisibleItemCount(5);
		cavPanel.add(this.caveats);
		vp.add(cavPanel);

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
		hp.setCellWidth(this.classBrowser, "100%");
		
		this.add(hp);
		this.popLists();
	}

	/**
	 * Called when a TreeItem in the classification
	 * hierarchy is selected. Attempts to load any child classes
	 */
	public void onTreeItemSelected(TreeItem item) {
		TreeNodeType nt = (TreeNodeType) item.getUserObject();
		this.selectedUUID = nt.uuid;
		this.isNew = false;
		JSONArray params = new JSONArray();
		params.set(0, new JSONString(this.selectedUUID));
		AuDoc.jsonCall.asyncPost2("Recman.getClass", params, new ClassResponseHandler(this));
	}
	
	/**
	 * Called when a TreeItem is opened or cloased
	 */
	public void onTreeItemStateChanged(TreeItem item) {
	}

	/**
	 * Populates the caveats and security level lists
	 *
	 */
	private void popLists() {
		this.caveats.addItem("");
		Object c = AuDoc.state.getItem("Caveats");
		if (c != null) {
			HashMap caveats = (HashMap) c;
			ArrayList cavs = new ArrayList(caveats.values());
			for (int i = 0; i < cavs.size(); i++) {
				this.caveats.addItem((String) cavs.get(i));
			}
		}

		this.secLevel.addItem("");
		Object l = AuDoc.state.getItem("SecLevels");
		if (l != null) {
			HashMap levels = (HashMap) l;
			ArrayList levs = new ArrayList(levels.values());
			for (int i = 0; i < levs.size(); i++) {
				this.secLevel.addItem((String) levs.get(i));
			}
		}
	}

	/**
	 * When the properties of a class is returned, this
	 * method is called to add the properties to the form
	 * @param name The name of the classification item
	 * @param retention the retention in years
	 * @param secLevel The security level of this class
	 * @param caveats The caveats associated with this class
	 * @param parentUUID The UUID of the parent class
	 */
	public void setForm(String name, double retention, String secLevel,
			ArrayList caveats, String parentUUID) {
		this.selectParentID = parentUUID;
		this.name.setText(name);
		String ret = Double.toString(retention);
		int pos = ret.indexOf(".");
		if (pos > 0) {
			ret = ret.substring(0, pos);
		}
		this.retention.setText(ret);

		// select security level
		for (int i = 0; i < this.secLevel.getItemCount(); i++) {
			String s = this.secLevel.getItemText(i);
			if (s.trim().equals(secLevel.trim())) {
				this.secLevel.setItemSelected(i, true);
			}
		}

		// select active caveats
		for (int i = 0; i < this.caveats.getItemCount(); i++) {
			String item = this.caveats.getItemText(i);
			this.caveats.setItemSelected(i, false);
			for (int j = 0; j < caveats.size(); j++) {
				if (item.equals((String) caveats.get(j))) {
					this.caveats.setItemSelected(i, true);
				}
			}
		}
	}

	/**
	 * Called when one of the buttons is clicked
	 * @param sender The widget calling this method
	 */
	public void onClick(Widget sender) {
		if (sender == this.newTop) {
			// clear correctly selected parent
			this.selectedUUID = null;
			this.selectParentID = null;
			this.isNew = true;

			// reset form
			this.name.setText("");
			this.retention.setText("");
			this.secLevel.setItemSelected(0, true);
			this.caveats.setItemSelected(0, true);
			for (int i = 1; i < this.caveats.getItemCount(); i++) {
				this.caveats.setItemSelected(i, false);
			}
			return;
		}

		if (sender == this.newChild) {
			this.isNew = true;
			// reset form
			this.name.setText("");
			return;
		}

		if (sender == this.save) {
			this.saveClass();
		}

		if (sender == this.delete) {
			if (this.selectedUUID != null) {
				JSONArray params = new JSONArray();
				params.set(0, new JSONString(this.selectedUUID));

				AuDoc.jsonCall.asyncPost2("recman.delClass", params, new UpdateResponseHandler(this));
				return;
			}
		}
	}

	/**
	 * Called when an async update response is received.
	 * Refreshed the class tree
	 */
	public void onUpdate() {
		this.classBrowser.classes.removeItems();
		this.classBrowser.onUpdate();
		// clear correctly selected parent
		this.selectedUUID = null;
		this.isNew = true;

		// reset form
		this.name.setText("");
		this.retention.setText("");
		this.secLevel.setItemSelected(0, true);
		this.caveats.setItemSelected(0, true);
		for (int i = 1; i < this.caveats.getItemCount(); i++) {
			this.caveats.setItemSelected(i, false);
		}
	}

	/**
	 * Saves the class currently being
	 * edited
	 *
	 */
	private void saveClass() {
		String method = "";
		JSONArray params = new JSONArray();
		if (this.isNew) {
			// add class
			method = "recman.addClass";

			params = new JSONArray();
			params.set(0, new JSONString(this.name.getText()));
			double ret;
			try{
				ret = Double.parseDouble(this.retention.getText());
			}catch(Exception e){
				ret = 0;
			}
			if (this.selectedUUID != null) {
				params.set(1, new JSONString(this.selectedUUID));
			} else {
				params.set(1, JSONNull.getInstance());
			}

			params.set(2, new JSONNumber(ret));
			int secLevelPos = this.secLevel.getSelectedIndex();
			String sec = this.secLevel.getItemText(secLevelPos);
			if(sec.equals("")){
				SimpleDialog.displayDialog(SimpleDialog.TYPE_ERROR, "Error", "You must select a security level");
				return;
			}
			int secpos = sec.indexOf("]");
			sec = sec.substring(secpos + 2, sec.length());
			params.set(3, new JSONString(sec));
			JSONArray cavs = new JSONArray();
			int c = 0;
			for (int i = 0; i < this.caveats.getItemCount(); i++) {
				if (this.caveats.isItemSelected(i)
						&& !this.caveats.getItemText(i).equals("")) {
					cavs.set(c, new JSONString(this.caveats.getItemText(i)));
					c++;
				}
			}
			params.set(4, cavs);
		} else {
			// mod class
			method = "recman.modClass";
			params = new JSONArray();
			params.set(0, new JSONString(this.selectedUUID));
			params.set(1, new JSONString(this.name.getText()));
			double ret;
			try{
				ret = Double.parseDouble(this.retention.getText());
			}catch(Exception e){
				ret = 0;
			}
			params.set(2, new JSONString(this.selectParentID));
			params.set(3, new JSONNumber(ret));
			int secLevelPos = this.secLevel.getSelectedIndex();
			String sec = this.secLevel.getItemText(secLevelPos);
			int secpos = sec.indexOf("]") + 2;
			sec = sec.substring(secpos, sec.length());
			params.set(4, new JSONString(sec));
			JSONArray cavs = new JSONArray();
			int c = 0;
			for (int i = 0; i < this.caveats.getItemCount(); i++) {
				if (this.caveats.isItemSelected(i)
						&& this.caveats.getItemText(i) != "") {
					cavs.set(c, new JSONString(this.caveats.getItemText(i)));
					c++;
				}
			}
			params.set(5, cavs);
		}
		AuDoc.jsonCall.asyncPost2(method, params, new UpdateResponseHandler(this));
		return;
	}
}
