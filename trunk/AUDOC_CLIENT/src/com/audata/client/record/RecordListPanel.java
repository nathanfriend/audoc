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
package com.audata.client.record;

import java.util.ArrayList;
import java.util.HashMap;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.json.UpdateListener;
import com.audata.client.widgets.CaptionButton;
import com.audata.client.widgets.HTMLButtonList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RecordListPanel extends FocusPanel implements UpdateListener, KeyboardListener{

	private static final Language LANG = (Language) GWT.create(Language.class);
	private VerticalPanel main;
	private HTMLButtonList rList;
	private JSONArray params;
	private String method;
	private String subtitle;
	private String uuid;
	private int count;
	
	public RecordListPanel(String subtitle, JSONArray records, String method, JSONArray params){
		this(subtitle, records, method, params, null, null);
	}
	
	public RecordListPanel(String subtitle, JSONArray records, String method, JSONArray params, String criteria){
		this(subtitle, records, method, params, null, criteria);
	}
	
	public RecordListPanel(String subtitle, JSONArray records, String method, JSONArray params, String uuid, String criteria){
		this.main = new VerticalPanel();
		this.count = records.size();
		this.subtitle = subtitle;
		this.method = method;
		this.params = params;
		this.uuid = uuid;
		this.setSize("100%", "100%");
		this.main.setSize("100%", "100%");
		this.main.setSpacing(4);
		
		HorizontalPanel title = new HorizontalPanel();
		title.setSpacing(4);
		title.setWidth("100%");
		Label l = new Label(LANG.records_Text());
		l.addStyleName("audoc-sectionTitle");
		title.add(l);
		title.setCellHorizontalAlignment(l, HasAlignment.ALIGN_LEFT);
		Label s = new Label(this.subtitle + "\n "+ LANG.rec_count_Text() +": " + this.count);
		s.addStyleName("audoc-sectionSubTitle");
		title.add(s);
		title.setCellHorizontalAlignment(s, HasAlignment.ALIGN_RIGHT);
		this.main.add(title);
		
		if(criteria != null){
			Label critLabel = new Label(LANG.criteria_Text()+": [" + criteria + "]");
			critLabel.setWidth("100%");
			critLabel.addStyleName("audoc-criteria");
			this.main.add(critLabel);
		}
		
		HorizontalPanel hp = new HorizontalPanel();
		this.main.add(hp);
		
		
		
		
		hp.setVerticalAlignment(HasAlignment.ALIGN_TOP);
		hp.setSize("100%", "100%");
		hp.setSpacing(4);
	
		VerticalPanel vp = new VerticalPanel();
		//vp.setSpacing(4);
		vp.setSize("100%","100%");
		vp.add(this.buildMenu());
		
		String template = "<span class=\"audoc-record-title\">#0 [#1]</span><br/>" +
							"<span class=\"audoc-record-class\">#2</span><br/>" +
							"<span class=\"audoc-record-cot\">"+LANG.with_Text()+": #3<span>";
		this.rList = new HTMLButtonList("images/48x48/rectypes.gif", template, true);
		this.rList.addStyleName("audoc-recList");
		vp.add(this.rList);
		this.rList.setSize("100%", "100%");
		vp.setCellHeight(this.rList, "100%");
		hp.add(vp);
		this.addRecords(records);
		
		Panel cPanel = this.buildCommands();
		
		hp.add(cPanel);
		cPanel.setWidth("100%");
		//hp.setCellWidth(this.rList, "70%");
		this.add(main);
		this.addKeyboardListener(this);
	}
	
	protected void addRecords(JSONArray records){
		for(int i=0;i<records.size();i++){
			JSONObject rObj = records.get(i).isObject();
			HashMap custom = new HashMap();
			String uuid = rObj.get("uuid").isString().stringValue();
			String title = rObj.get("Title").isString().stringValue();
			String cls = rObj.get("ClassPath").isString().stringValue();
			String cot = "";
			if(rObj.containsKey("CheckedOutTo")){
				cot = rObj.get("CheckedOutTo").isString().stringValue();
			}
			String date = rObj.get("DateCreated").isString().stringValue();
			String recNum = rObj.get("RecordNumber").isString().stringValue();
			String recType = rObj.get("RecordType").isString().stringValue();
			Double documents = new Double(rObj.get("Documents").isNumber().getValue());
			date = date.substring(0,10);
			custom.put("uuid", uuid);
			custom.put("Title", title);
			custom.put("class", cls);
			custom.put("DateCreated", date);
			custom.put("CheckedOutTo", cot);
			custom.put("RecordNumber", recNum);
			custom.put("RecordType", recType);
			custom.put("Documents", documents);
			String[] cap = new String[4];
			cap[0] = title;
			cap[1] = recNum;
			cap[2] = cls;
			if(!cot.equals("")){
				cap[3] = cot;
			}else{
				cap[3] = LANG.no_one_Text();
			}
			this.rList.addItem(cap, null, custom);
		}
	}
	
	/**
	 * Returns a list of the selected items
	 * @return Arraylist of selected items
	 */
	public ArrayList getSelected(){
		return this.rList.getSelected();
	}
	
	private Panel buildMenu(){
		HorizontalPanel menu = new HorizontalPanel();
		menu.setSpacing(4);
		menu.addStyleName("audoc-commandPanel");
		//menu.setWidth("100%");
		menu.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
		
		//add Refresh button
		//CaptionButton refreshButton = new CaptionButton("images/16x16/refresh.gif", "Refresh", CaptionButton.CAPTION_EAST);
		CaptionButton refreshButton = new CaptionButton();
		refreshButton.setCaptionText(LANG.refresh_Text());
		refreshButton.setImageUrl("images/16x16/refresh.gif");
		refreshButton.addClickListener(new CommandClickListener(this, CommandClickListener.COMMAND_REFRESH));
		refreshButton.setTitle(LANG.refresh_title_Text());
		menu.add(refreshButton);
		
		//add print button
		//CaptionButton printButton = new CaptionButton("images/16x16/print.gif", "Print", CaptionButton.CAPTION_EAST);
		CaptionButton printButton = new CaptionButton();
		printButton.setCaptionText(LANG.print_Text());
		printButton.setImageUrl("images/16x16/print.gif");
		printButton.addClickListener(new CommandClickListener(this, CommandClickListener.COMMAND_PRINT));
		printButton.setTitle(LANG.print_title_Text());
		menu.add(printButton);
		return menu;
	}
	
	private Panel buildCommands(){
		VerticalPanel cPanel = new VerticalPanel();
		cPanel.setSpacing(4);
		cPanel.addStyleName("audoc-commandPanel");
		//cPanel.setWidth("100%");
		cPanel.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
		cPanel.setVerticalAlignment(HasAlignment.ALIGN_TOP);
		Label cLabel = new Label(LANG.commands_Text());
		cLabel.addStyleName("audoc-subTitle");
		cPanel.add(cLabel);
		cPanel.setCellHorizontalAlignment(cLabel, HasAlignment.ALIGN_LEFT);
		
		//CaptionButton propButton = new CaptionButton("images/48x48/props.gif", "Properties", CaptionButton.CAPTION_EAST);
		CaptionButton propButton = new CaptionButton();
		propButton.setImageUrl("images/48x48/props.gif");
		propButton.setCaptionText(LANG.props_Text());
		propButton.addClickListener(new CommandClickListener(this, CommandClickListener.COMMAND_PROPERTIES));
		propButton.setTitle(LANG.props_title_Text());
		//propButton.setWidth("100%");
		cPanel.add(propButton);
		
		//CaptionButton viewButton = new CaptionButton("images/48x48/generic.gif", "View", CaptionButton.CAPTION_EAST);
		CaptionButton viewButton = new CaptionButton();
		viewButton.setImageUrl("images/48x48/generic.gif");
		viewButton.setCaptionText(LANG.view_Text());
		viewButton.addClickListener(new CommandClickListener(this, CommandClickListener.COMMAND_VIEW));
		viewButton.setTitle(LANG.view_title_Text());
		//viewButton.setWidth("100%");
		cPanel.add(viewButton);
		
		//CaptionButton cotButton = new CaptionButton("images/48x48/checkout.gif", "Checkout", CaptionButton.CAPTION_EAST);
		CaptionButton cotButton = new CaptionButton();
		cotButton.setImageUrl("images/48x48/checkout.gif");
		cotButton.setCaptionText(LANG.checkout_Text());
		cotButton.addClickListener(new CommandClickListener(this, CommandClickListener.COMMAND_CHECKOUT));
		cotButton.setTitle(LANG.checkout_title_Text());
		//cotButton.setWidth("100%");
		cPanel.add(cotButton);
	
		CaptionButton trayButton = new CaptionButton();
		trayButton.setImageUrl("images/48x48/tray.gif");
		if(this.method.equals("trays.getItems")){
			//trayButton = new CaptionButton("images/48x48/tray.gif", "Remove from Tray", CaptionButton.CAPTION_EAST);
			trayButton.setCaptionText(LANG.remove_from_tray_Text());
			trayButton.setTitle(LANG.remove_from_tray_title_Text());
			trayButton.addClickListener(new CommandClickListener(this, CommandClickListener.COMMMAND_RTRAY, this.uuid));
		}else{
			//trayButton = new CaptionButton("images/48x48/tray.gif", "Add to Tray", CaptionButton.CAPTION_EAST);
			trayButton.setCaptionText(LANG.add_to_tray_Text());
			trayButton.setTitle(LANG.add_to_tray_title_Text());
			trayButton.addClickListener(new CommandClickListener(this, CommandClickListener.COMMAND_TRAY));
		}
		//trayButton.setWidth("100%");
		cPanel.add(trayButton);
		
		if (AuDoc.state.getItem("isAdmin") == "true") {
			Label aLabel = new Label(LANG.admin_commands_Text());
			aLabel.addStyleName("audoc-subTitle");
			cPanel.add(aLabel);
			cPanel.setCellHorizontalAlignment(aLabel, HasAlignment.ALIGN_LEFT);
			
			//CaptionButton amendButton = new CaptionButton("images/48x48/props.gif", "Change Record Number", CaptionButton.CAPTION_EAST);
			CaptionButton amendButton = new CaptionButton();
			amendButton.setImageUrl("images/48x48/props.gif");
			amendButton.setCaptionText(LANG.change_rec_num_Text());
			amendButton.addClickListener(new CommandClickListener(this, CommandClickListener.COMMAND_AMEND));
			amendButton.setTitle(LANG.change_rec_num_title_Text());
			//amendButton.setWidth("100%");
			cPanel.add(amendButton);
			
			//CaptionButton checkinButton = new CaptionButton("images/48x48/error.gif", "Undo Checkout", CaptionButton.CAPTION_EAST);
			CaptionButton checkinButton = new CaptionButton();
			checkinButton.setImageUrl("images/48x48/error.gif");
			checkinButton.setCaptionText(LANG.undo_checkout_Text());
			checkinButton.addClickListener(new CommandClickListener(this, CommandClickListener.COMMAND_CHECKIN));
			checkinButton.setTitle(LANG.check_in_msg_Text());
			//checkinButton.setWidth("100%");
			cPanel.add(checkinButton);
			
			//CaptionButton delButton = new CaptionButton("images/48x48/logout.gif", "Delete", CaptionButton.CAPTION_EAST);
			CaptionButton delButton = new CaptionButton();
			delButton.setImageUrl("images/48x48/logout.gif");
			delButton.setCaptionText(LANG.del_rec_Text());
			delButton.addClickListener(new CommandClickListener(this, CommandClickListener.COMMAND_DEL));
			delButton.setTitle(LANG.del_rec_title_Text());
			//delButton.setWidth("100%");
			cPanel.add(delButton);			
		}
		return cPanel;
	}
	
	public void onUpdate(){
		this.rList.clear();
		AuDoc.jsonCall.asyncPost2(this.method, this.params, new RefreshCallback(this));
	}
	
	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		String newChar = Character.toString(keyCode);
		if(modifiers == KeyboardListener.MODIFIER_SHIFT && newChar.equalsIgnoreCase("r")){
			this.onUpdate();
		}
	}

	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
		
	}

	public void onKeyUp(Widget sender, char keyCode, int modifiers) {
	}
	
	public Panel getRecordList(){
		return this.rList.getRecordListPanel();
	}
}
