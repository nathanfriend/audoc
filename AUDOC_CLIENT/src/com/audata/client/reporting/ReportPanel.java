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
package com.audata.client.reporting;

import java.util.HashMap;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.widgets.HTMLButton;
import com.audata.client.widgets.HTMLButtonList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ReportPanel extends VerticalPanel implements ClickListener{

	private static final Language LANG = (Language) GWT.create(Language.class);
	private HTMLButtonList reports;
	private Button run;
	private String uuid;

	public ReportPanel(){
		this.setSpacing(5);
		Label title = new Label(LANG.reports_Text());
		title.addStyleName("audoc-sectionTitle");
		this.add(title);
		
		String template = "<span class=\"audoc-report-title\">#0</span><br/><span class=\"audoc-report-template\">#1</span>";
		this.reports = new HTMLButtonList("images/48x48/reports.gif", template, false);
		this.reports.addClickListener(this);
		this.reports.addStyleName("audoc-udfs");
		this.reports.setPixelSize(300, 300);
		this.add(this.reports);
		
		this.run = new Button(LANG.run_report_Text());
		this.run.addClickListener(this);
		this.add(this.run);
		this.getReports();
	}
	
	public void onClick(Widget sender){
		if(sender ==  this.run){
			if(!this.uuid.equals("")){
				JSONArray params = new JSONArray();
				params.set(0, new JSONString(this.uuid));
				AuDoc.jsonCall.asyncPost2("reporting.genReport", params, new ReportGenHandler());
			}
			return;
		}
		//user clicked on a udf item
		if(this.reports.indexOf(sender) != -1){
			HTMLButton hb = (HTMLButton)sender;
			HashMap custom = (HashMap)hb.custom;
			this.uuid = (String)custom.get("uuid");
		}
	}
	
	public void getReports(){
		AuDoc.jsonCall.asyncPost2("reporting.getReports", new JSONArray(), new ReportHandler(this));
	}
	
	public void addReport(String uuid, String title, String template){
		HashMap custom = new HashMap();
		custom.put("uuid", uuid);
		custom.put("title", title);
		custom.put("template", template);
		String[] caption = new String[2];
		caption[0] = title;
		caption[1] = template;
		this.reports.addItem(caption, null, custom);
	}
}
