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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.json.UpdateListener;
import com.audata.client.json.UpdateResponseHandler;
import com.audata.client.widgets.HTMLButton;
import com.audata.client.widgets.HTMLButtonList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
/**
 * Record Type admin panel
 * @author jonm
 *
 */
public class RecTypePanel extends VerticalPanel implements UpdateListener,
		ClickListener{

	private static final Language LANG = (Language) GWT.create(Language.class);
	private HTMLButtonList recTypes;

	private TextBox nameBox;

	private TextArea descBox;

	private ListBox udfsBox;

	private Button newBut;

	private Button saveBut;

	private Button delBut;

	private String selectedUUID;

	/**
	 * Constructor for the PecTypePanel.
	 * Builds the UI and gets the content of the
	 * list.
	 */
	public RecTypePanel() {

		this.selectedUUID = null;

		this.setSize("100%", "100%");
		this.setSpacing(5);
		Label title = new Label(LANG.admin_rectypes_Text());
		title.addStyleName("audoc-sectionTitle");
		this.add(title);

		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(5);
		String template = 	"<p><span class=\"audoc-rectype-title\">#0</span><br/>" +
							"<span class=\"audoc-rectype-notes\">#1</span></p>"; 
		this.recTypes = new HTMLButtonList("images/48x48/rectypes.gif", template, false);
		this.recTypes.addStyleName("audoc-recTypes");
		this.recTypes.addClickListener(this);
		this.recTypes.setPixelSize(300,300);
		
		hp.add(this.recTypes);

		VerticalPanel form = new VerticalPanel();
		form.setSpacing(5);
		form.addStyleName("audoc-form");

		HorizontalPanel namePanel = new HorizontalPanel();
		Label nameLabel = new Label(LANG.name_Text());
		nameLabel.setWidth("100px");
		namePanel.add(nameLabel);
		this.nameBox = new TextBox();
		namePanel.add(this.nameBox);
		form.add(namePanel);

		HorizontalPanel descPanel = new HorizontalPanel();
		Label descLabel = new Label(LANG.description_Text());
		descLabel.setWidth("100px");
		descPanel.add(descLabel);
		this.descBox = new TextArea();
		descPanel.add(this.descBox);
		form.add(descPanel);

		HorizontalPanel udfPanel = new HorizontalPanel();
		Label udfLabel = new Label(LANG.udf_Text());
		udfLabel.setWidth("100px");
		udfPanel.add(udfLabel);
		this.udfsBox = new ListBox();
		this.udfsBox.setVisibleItemCount(5);
		this.udfsBox.setMultipleSelect(true);
		udfPanel.add(this.udfsBox);
		form.add(udfPanel);

		HorizontalPanel butPanel = new HorizontalPanel();
		butPanel.setSpacing(5);

		this.newBut = new Button(LANG.new_Text());
		this.newBut.addClickListener(this);
		butPanel.add(this.newBut);

		this.saveBut = new Button(LANG.save_Text());
		this.saveBut.addClickListener(this);
		butPanel.add(this.saveBut);

		this.delBut = new Button(LANG.delete_Text());
		this.delBut.addClickListener(this);
		butPanel.add(this.delBut);

		form.add(butPanel);

		hp.add(form);

		this.add(hp);
		this.getRecTypes();
		this.getUDFs();
	}

	/**
	 * Called when an UpdateResponseHandler
	 * received a response.
	 * Clears the content of the current lists
	 * and grabs the content to re-fill them
	 */
	public void onUpdate() {
		this.nameBox.setText("");
		this.descBox.setText("");
		this.recTypes.clear();
		this.selectedUUID = null;
		this.udfsBox.clear();
		this.getRecTypes();
		this.getUDFs();
	}

	/**
	 * Handles the click events for save, new
	 * delete buttons and items in the recType List
	 */
	public void onClick(Widget sender) {
		//new button click handler
		if (sender == this.newBut) {
			this.selectedUUID = null;
			this.nameBox.setText("");
			this.descBox.setText("");
			for (int i = 0; i < this.udfsBox.getItemCount(); i++) {
				this.udfsBox.setItemSelected(i, false);
			}
			this.udfsBox.setItemSelected(0, true);
			return;
		}

		//delete button click handler
		if (sender == this.delBut) {
			if (this.selectedUUID != null) {
				// remove it
				JSONArray params = new JSONArray();
				params.set(0, new JSONString(this.selectedUUID));
				AuDoc.jsonCall.asyncPost2("recman.delRecType",params, new UpdateResponseHandler(this));
			} 
			return;
		}

		//save button click handler
		if (sender == this.saveBut) {
			String method = "";
			JSONArray params = new JSONArray();
			if (this.selectedUUID != null) {
				// mod recType
				method = "recman.modRecType";
				params.set(0, new JSONString(this.selectedUUID));
				params.set(1, new JSONString(this.nameBox.getText()));
				params.set(2, new JSONString(this.descBox.getText()));
				JSONArray udfs = new JSONArray();
				int c = 0;
				for (int i = 0; i < this.udfsBox.getItemCount(); i++) {
					if (this.udfsBox.isItemSelected(i)) {
						String u = this.udfsBox.getItemText(i);
						if(u != ""){
							udfs.set(c, new JSONString(u));
							c++;
						}
					}

				}
				params.set(3, udfs);
			} else {
				// new recType
				method = "recman.addRecType";
				params.set(0, new JSONString(this.nameBox.getText()));
				params.set(1, new JSONString(this.descBox.getText()));
				JSONArray udfs = new JSONArray();
				int c = 0;
				for (int i = 0; i < this.udfsBox.getItemCount(); i++) {
					if (this.udfsBox.isItemSelected(i)) {
						if (!this.udfsBox.getItemText(i).equals("")) {
							udfs.set(c, new JSONString(this.udfsBox
									.getItemText(i)));
							c++;
						}
					}

				}
				params.set(2, udfs);
			}
			AuDoc.jsonCall.asyncPost2(method, params, new UpdateResponseHandler(this)); 
			return;
		}

		// user clicked a item in the rectype list
		if(this.recTypes.indexOf(sender) != -1){
			HTMLButton hb = (HTMLButton)sender;
			this.onChange(hb);
			return;
		}
	}

	/**
	 * Called when the user clicks on a rectype
	 * @param sender The Record type button clicked on
	 */
	private void onChange(HTMLButton sender){
		HashMap custom = (HashMap)sender.custom;
		String uuid = (String)custom.get("uuid");
		this.selectedUUID = uuid;
		String name = (String)custom.get("name");
		String desc = (String)custom.get("description");
		ArrayList udfs = (ArrayList)custom.get("udfs");
		this.nameBox.setText(name);
		this.descBox.setText(desc);
		for (int i = 0; i < this.udfsBox.getItemCount(); i++) {
			String item = this.udfsBox.getItemText(i);
			this.udfsBox.setItemSelected(i, false);
			Iterator it = udfs.iterator();
			while (it.hasNext()) {
				String sItem = (String) it.next();
				if (item.equals(sItem)) {
					this.udfsBox.setItemSelected(i, true);
				} 
			}

		}
	}
	
	/**
	 * Populates the RecType List
	 */
	private void getRecTypes() {
		//Clear list and inform user
		this.recTypes.clear();
		//Get record types
		String method = "recman.getRecTypes";
		JSONArray params = new JSONArray();
		AuDoc.jsonCall.asyncPost2(method, params, new RecTypeHandler(this));
	}

	/**
	 * Adds a new recType to the list 
	 * @param uuid The record types uuid
	 * @param name The record types name
	 * @param description The record types description
	 * @param udfs An ArrayList of the udfs associated with the recType
	 */
	public void addRecType(String uuid, String name, String description, ArrayList udfs) {
		HashMap custom = new HashMap();
		custom.put("uuid", uuid);
		custom.put("name", name);
		custom.put("description", description);
		custom.put("udfs", udfs);
		String[] caption = new String[2];
		caption[0] = name;
		caption[1] = description;
		this.recTypes.addItem(caption, null, custom);
	}
	
	/**
	 * gets the contents of the udf list
	 */
	private void getUDFs() {
		//Clear list and inform users
		this.udfsBox.clear();
		this.udfsBox.setEnabled(false);
		
		//get UDFs
		String method = "recman.getUDFs";
		JSONArray params = new JSONArray();
		AuDoc.jsonCall.asyncPost2(method, params, new RecTypeUDFHandler(this));
	}

	/**
	 * Adds udfs to the select list
	 * @param names An array of udf names
	 */
	public void addUDFs(String[] names) {
		this.udfsBox.clear();
		this.udfsBox.setEnabled(true);
		this.udfsBox.addItem("");
		for (int i = 0; i < names.length; i++) {
			this.udfsBox.addItem(names[i]);
		}
	}
}
