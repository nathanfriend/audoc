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
import com.audata.client.json.UpdateListener;
import com.audata.client.json.UpdateResponseHandler;
import com.audata.client.state.SecLoader;
import com.audata.client.widgets.HTMLButtonList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Admin panel for Security Levels and Caveats
 * @author jonm
 *
 */
public class SecurityPanel extends HorizontalPanel implements ClickListener,
		UpdateListener {

	private static final Language LANG = (Language) GWT.create(Language.class);
	
	private HTMLButtonList levelList;

	private HTMLButtonList caveatList;

	private String levUUID = "";

	private TextBox cavName;

	private String cavUUID = "";

	private Button newCavButton;

	private Button saveCavButton;

	private Button delCavButton;

	private TextBox levelNameBox;

	private TextBox levelBox;

	private Button newLevButton;

	private Button saveLevButton;

	private Button delLevButton;

	/**
	 * Public Constructor
	 *
	 */
	public SecurityPanel() {

		this.setWidth("100%");
		this.setHeight("100%");
		this.setSpacing(20);
		this.add(this.buildLevelPanel());
		this.add(this.buildCaveatPanel());

		this.getCaveats();
		this.getLevels();
	}

	/**
	 * Creates SecLevel panel
	 * @return VerticalPanel containing SecLevel admin features
	 */
	private VerticalPanel buildLevelPanel() {
		VerticalPanel lp = new VerticalPanel();
		lp.setSpacing(5);
		lp.addStyleName("audoc-group");

		Label levelTitle = new Label(LANG.security_levels_Text());
		levelTitle.addStyleName("audoc-sectionTitle");
		String levelCaption = "<span class=\"secLevel-title\">#1</span><br/>" +
								"<span class=\"secLevel-level\">"+ LANG.level_Text() +": #0</span>";
		this.levelList = new HTMLButtonList("images/48x48/security.gif",
				levelCaption, false);
		this.levelList.addStyleName("audoc-levels");
		this.levelList.addClickListener(new SecLevelClickListener(this));
		this.levelList.setPixelSize(250,200);

		lp.add(levelTitle);
		lp.add(this.levelList);

		HorizontalPanel namePanel = new HorizontalPanel();
		Label nameLabel = new Label(LANG.name_Text());
		nameLabel.setWidth("100px");
		nameLabel.addStyleName("audoc-label");
		this.levelNameBox = new TextBox();
		namePanel.add(nameLabel);
		namePanel.add(this.levelNameBox);
		lp.add(namePanel);

		HorizontalPanel levelPanel = new HorizontalPanel();
		Label levelLabel = new Label(LANG.level_Text());
		levelLabel.setWidth("100px");
		levelLabel.addStyleName("audoc-label");
		this.levelBox = new TextBox();
		levelPanel.add(levelLabel);
		levelPanel.add(this.levelBox);
		lp.add(levelPanel);

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);

		this.newLevButton = new Button(LANG.new_Text());
		this.newLevButton.addClickListener(this);

		this.saveLevButton = new Button(LANG.save_Text());
		this.saveLevButton.addClickListener(this);

		this.delLevButton = new Button(LANG.delete_Text());
		this.delLevButton.addClickListener(this);

		buttonPanel.add(this.newLevButton);
		buttonPanel.add(this.saveLevButton);
		buttonPanel.add(this.delLevButton);

		lp.add(buttonPanel);
		return lp;
	}

	/**
	 * Creates the Caveat admin panel
	 * @return VerticalPanel containing Caveat functions
	 */
	private VerticalPanel buildCaveatPanel() {
		VerticalPanel cp = new VerticalPanel();
		cp.setSpacing(5);
		cp.addStyleName("audoc-group");

		Label caveatTitle = new Label(LANG.security_caveats_Text());
		caveatTitle.addStyleName("audoc-sectionTitle");
		String cavCaption  = "<span class=\"caveat-title\">#0</span>";
		this.caveatList = new HTMLButtonList("images/48x48/security.gif", cavCaption,  false);
		this.caveatList.addStyleName("audoc-caveats");
		this.caveatList.addClickListener(new CaveatClickListener(this));
		this.caveatList.setPixelSize(250, 200);

		cp.add(caveatTitle);
		cp.add(this.caveatList);

		HorizontalPanel namePanel = new HorizontalPanel();
		Label nameLabel = new Label(LANG.name_Text());
		nameLabel.setWidth("100px");
		nameLabel.addStyleName("audoc-label");
		this.cavName = new TextBox();
		namePanel.add(nameLabel);
		namePanel.add(this.cavName);

		cp.add(namePanel);

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);

		this.newCavButton = new Button(LANG.new_Text());
		this.newCavButton.addClickListener(this);

		this.saveCavButton = new Button(LANG.save_Text());
		this.saveCavButton.addClickListener(this);

		this.delCavButton = new Button(LANG.delete_Text());
		this.delCavButton.addClickListener(this);

		buttonPanel.add(this.newCavButton);
		buttonPanel.add(this.saveCavButton);
		buttonPanel.add(this.delCavButton);
		cp.add(buttonPanel);
		return cp;
	}

	/**
	 * Get the list of existing Caveat object
	 *
	 */
	private void getCaveats() {
		AuDoc.jsonCall.asyncPost2("Security.getCaveats", new JSONArray(), new CaveatResponseHandler(this));
	}

	/**
	 * Get the list of existing SecLevel object
	 *
	 */
	private void getLevels() {
		AuDoc.jsonCall.asyncPost2("Security.getSecLevels", new JSONArray(), new SecLevelResponseHandler(this));
	}

	/**
	 * Adds a Caveat to the list
	 * @param name The Caveat's name
	 * @param uuid The Caveat's uuid
	 */
	public void addCaveat(String name, String uuid) {
		String[] cap = new String[1];
		cap[0] = name;
		HashMap custom = new HashMap();
		custom.put("uuid", uuid);
		custom.put("name", name);
		this.caveatList.addItem(cap, null, custom);
		
		this.cavName.setText("");
		this.cavUUID = "";
	}

	/**
	 * Add a SecLevel to the list
	 * @param name The SecLevel's name
	 * @param level The level of the SecLevel
	 * @param uuid The SecLevels uuid
	 */
	public void addSecLevel(String name, int level, String uuid) {
		String[] cap = new String[2];
		cap[0] = Integer.toString(level);
		cap[1] = name;
		
		HashMap custom = new HashMap();
		custom.put("uuid", uuid);
		custom.put("level", new Integer(level));
		custom.put("name", name);
		
		this.levelList.addItem(cap, null, custom);
		
		this.levelBox.setText("");
		this.levelNameBox.setText("");
		this.levUUID = "";
	}

	/**
	 * Set the values of the Caveat form
	 * @param name Caveat's name
	 * @param uuid Caveat's uuid
	 */
	public void setCaveatForm(String name, String uuid) {
		this.cavName.setText(name);
		this.cavUUID = uuid;
	}

	/**
	 * Sets the values of the SecLevel form
	 * @param name SecLevel's name
	 * @param level SecLevel's level
	 * @param uuid SecLevel's uuid
	 */
	public void setLevelForm(String name, int level, String uuid) {
		this.levelNameBox.setText(name);
		this.levelBox.setText(Integer.toString(level));
		this.levUUID = uuid;
	}

	/**
	 * Called when one of the forms buttons is pressed
	 */
	public void onClick(Widget sender) {
		if (sender == this.newCavButton) {
			this.cavName.setText("");
			this.cavUUID = "";
			return;
		}

		if (sender == this.saveCavButton) {
			String method = null;
			JSONArray params = null;
			// check name is not empty!!
			if (!this.cavName.getText().equals("")) {
				if (this.cavUUID == "") {
					// new cav to save
					method = "Security.addCaveat";
					params = new JSONArray();
					params.set(0, new JSONString(this.cavName.getText()));
				} else {
					// mod existing cav
					method = "Security.modCaveat";
					params = new JSONArray();
					params.set(0, new JSONString(this.cavUUID));
					params.set(1, new JSONString(this.cavName.getText()));
				}
				//String msg = AuDoc.jsonCall.getJSONMsg(method, params);
				AuDoc.jsonCall.asyncPost2(method, params, new UpdateResponseHandler(this));
			}
			return;
		}

		if (sender == this.newLevButton) {
			this.levelBox.setText("");
			this.levelNameBox.setText("");
			this.levUUID = "";
			return;
		}

		if (sender == this.saveLevButton) {
			if (!this.levelNameBox.getText().equals("")) {
				String method = null;
				JSONArray params = null;
				if (this.levUUID == "") {
					// new level to save
					method = "Security.addSecLevel";
					params = new JSONArray();
					params.set(0, new JSONString(this.levelNameBox.getText()));
					double level = Double.parseDouble(this.levelBox.getText());
					params.set(1, new JSONNumber(level));
				} else {
					// mod existing level
					method = "Security.modSecLevel";
					params = new JSONArray();
					params.set(0, new JSONString(this.levUUID));
					params.set(1, new JSONString(this.levelNameBox.getText()));
					// todo: number not showing up!
					double level = Double.parseDouble(this.levelBox.getText());
					params.set(2, new JSONNumber(level));
				}
				//String msg = AuDoc.jsonCall.getJSONMsg(method, params);
				AuDoc.jsonCall.asyncPost2(method, params, new UpdateResponseHandler(this));
			}
			return;
		}

		if (sender == this.delLevButton) {
			if (this.levUUID != "") {
				JSONArray params = new JSONArray();
				params.set(0, new JSONString(this.levUUID));
				AuDoc.jsonCall.asyncPost2("Security.delSecLevel", params, new UpdateResponseHandler(this));
			}
			return;
		}

		if (sender == this.delCavButton) {
			if (this.cavUUID != "") {
				JSONArray params = new JSONArray();
				params.set(0, new JSONString(this.cavUUID));
				AuDoc.jsonCall.asyncPost2("Security.delCaveat", params, new UpdateResponseHandler(this));
			}
			return;
		}

	}

	/**
	 * Called when the form needs updating
	 */
	public void onUpdate() {
		this.caveatList.clear();
		this.levelList.clear();

		SecLoader.cacheCaveats();
		SecLoader.cacheSecLevels();
		this.getCaveats();
		this.getLevels();
	}
}
