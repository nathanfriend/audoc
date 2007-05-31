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
import com.audata.client.widgets.HTMLButton;
import com.audata.client.widgets.HTMLButtonList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Admin panel for the Reporting system
 * @author jonm
 *
 */
public class ReportingPanel extends VerticalPanel implements ClickListener, UpdateListener{

	private static final Language LANG = (Language) GWT.create(Language.class);
	private HTMLButtonList reports;
	private Button saveButton;
	private Button newButton;
	private Button delButton;
	private TextBox title;
	private TextArea criteria;
	private TextBox template;
	private String uuid;
	
	/**
	 * public contrcutor
	 */
	public ReportingPanel(){
		this.uuid = "";
		this.setSize("100%", "100%");
		
		//section title
		Label l = new Label(LANG.reports_Text());
		l.addStyleName("audoc-sectionTitle");
		this.add(l);
		
		HorizontalPanel main = new HorizontalPanel();
		main.setSpacing(5);
		
		//Reports list
		String rTemplate = "<span class=\"audoc-report-title\">#0</span><br/><span class=\"audoc-report-template\">#1</span><br/><span class=\"audoc-report-criteria\">#2</span>";
		this.reports = new HTMLButtonList("images/48x48/reports.gif", rTemplate, false);
		this.reports.addClickListener(this);
		this.reports.addStyleName("audoc-udfs");
		this.reports.setPixelSize(300, 300);
		main.add(this.reports);
		
		//Form
		VerticalPanel formPanel = new VerticalPanel();
		formPanel.setSpacing(4);
		
		HorizontalPanel tPanel = new HorizontalPanel();
		Label titleLabel = new Label(LANG.title_Text());
		titleLabel.setWidth("100px");
		tPanel.add(titleLabel);
		this.title = new TextBox();
		tPanel.add(this.title);		
		formPanel.add(tPanel);
		
		HorizontalPanel tempPanel = new HorizontalPanel();
		Label tempLabel = new Label(LANG.template_Text());
		tempLabel.setWidth("100px");
		tempPanel.add(tempLabel);
		this.template = new TextBox();
		tempPanel.add(this.template);		
		formPanel.add(tempPanel);
		
		HorizontalPanel cPanel = new HorizontalPanel();
		Label cLabel = new Label(LANG.criteria_Text());
		cLabel.setWidth("100px");
		cPanel.add(cLabel);
		this.criteria = new TextArea();
		this.criteria.setVisibleLines(5);
		cPanel.add(this.criteria);		
		formPanel.add(cPanel);
		
		HorizontalPanel bPanel = new HorizontalPanel();
		bPanel.setSpacing(5);
		this.newButton = new Button(LANG.new_Text());
		this.newButton.addClickListener(this);
		bPanel.add(this.newButton);
		this.saveButton = new Button(LANG.save_Text());
		this.saveButton.addClickListener(this);
		bPanel.add(this.saveButton);
		this.delButton = new Button(LANG.delete_Text());
		this.delButton.addClickListener(this);
		bPanel.add(this.delButton);
		formPanel.add(bPanel);
		
		main.add(formPanel);
		this.add(main);
		this.onUpdate();
	}
	
	/** 
	 * Called when one of the buttons is clicked
	 * 
	 */
	public void onClick(Widget sender){
	    	//clears the form and state variables
		if(sender == this.newButton){
			this.uuid = "";
			this.criteria.setText("");
			this.template.setText("");
			this.title.setText("");
			return;
		}
		
		// saves the report
		if(sender == this.saveButton){
			JSONArray params = new JSONArray();
			String method = "";
			if(this.uuid.equals("")){
				//new report
				method = "reporting.addReport";
				params.set(0, new JSONString(this.title.getText()));
				params.set(1, new JSONString(this.template.getText()));
				params.set(2, new JSONString(this.criteria.getText()));
			}else{
				//update
				method = "reporting.modReport";
				params.set(0, new JSONString(this.uuid));
				params.set(1, new JSONString(this.title.getText()));
				params.set(2, new JSONString(this.template.getText()));
				params.set(3, new JSONString(this.criteria.getText()));
			}
			AuDoc.jsonCall.asyncPost2(method, params, new UpdateResponseHandler(this));
			this.uuid = "";
			this.criteria.setText("");
			this.template.setText("");
			this.title.setText("");
			return;
		}
		
		//deletes the selected report
		if(sender == this.delButton){
			if(!this.uuid.equals("")){
				JSONArray params = new JSONArray();
				params.set(0, new JSONString(this.uuid));
				AuDoc.jsonCall.asyncPost2("reporting.delReport", params, new UpdateResponseHandler(this));
				this.uuid = "";
				this.criteria.setText("");
				this.template.setText("");
				this.title.setText("");
			}
			return;
		}
		
//		user clicked on a udf item
		if(this.reports.indexOf(sender) != -1){
			HTMLButton hb = (HTMLButton)sender;
			this.onChange(hb);
		}
	}
	
	/**
	 * Called by ReportHandler when an update occurs
	 */
	public void onUpdate(){
		this.reports.clear();
		AuDoc.jsonCall.asyncPost2("reporting.getReports", new JSONArray(), new ReportHandler(this));
	}
	
	/**
	 * Add a report to the list
	 * @param uuid The reports uuid
	 * @param title The title of the report
	 * @param template The report template
	 * @param criteria The search criteria of the report
	 */
	public void addReport(String uuid, String title, String template, String criteria){
		HashMap custom = new HashMap();
		custom.put("uuid", uuid);
		custom.put("title", title);
		custom.put("template", template);
		custom.put("criteria", criteria);
		String[] caption = new String[3];
		caption[0] = title;
		caption[1] = template;
		caption[2] = criteria;
		this.reports.addItem(caption, null, custom);
	}
	
	/**
	 * Called when an item is selected from the reports
	 * list
	 * @param hb
	 */
	public void onChange(HTMLButton hb){
		HashMap custom = (HashMap)hb.custom;
		this.uuid = (String)custom.get("uuid");
		this.title.setText((String)custom.get("title"));
		this.template.setText((String)custom.get("template"));
		this.criteria.setText((String)custom.get("criteria"));
	}
}
