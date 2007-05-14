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
package com.audata.client.newRecord;

import java.util.ArrayList;
import java.util.HashMap;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.widgets.HTMLButton;
import com.audata.client.widgets.HTMLButtonList;
import com.audata.client.widgets.Wizard;
import com.audata.client.widgets.WizardPage;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TypeChooser extends WizardPage implements ClickListener{
	
	private static final Language LANG = (Language) GWT.create(Language.class);
	private VerticalPanel main;
	private HTMLButtonList types;
	private ArrayList uuids;
	
	private HashMap values;
	
	public TypeChooser(){
		this.uuids = new ArrayList();
		this.values = new HashMap();
		this.main = new VerticalPanel();
		String template = 	"<p><span class=\"audoc-rectype-title\">#0</span><br/>" +
							"<span class=\"audoc-rectype-notes\">#1</span></p>"; 
		this.types = new HTMLButtonList("images/48x48/rectypes.gif", template, false);
		this.types.addClickListener(this);
		this.types.addStyleName("htmlButtonList");
		this.types.setPixelSize(200,250);
		
		Label l = new Label(LANG.select_rec_type_Text());
		this.main.add(l);
		this.main.add(this.types);
		this.initWidget(this.main);
		this.onShow(null);
	}

	public boolean isValid() {
		if(this.values.size() > 0){
			return true;
		}else{
			return false;
		}
	}

	public HashMap getValues() {
		return this.values;
	}

	public void onShow(Wizard parent) {
		this.types.clear();
		this.values.clear();
		this.uuids.clear();
		AuDoc.jsonCall.asyncPost2("recman.getRecTypes", new JSONArray(), new RecTypeResponseHandler(this));
	}
	
	public void onClick(Widget sender){
		if(this.types.indexOf(sender) != -1){
			this.values.clear();
			HTMLButton selected = (HTMLButton)sender;
			HashMap custom = (HashMap)selected.custom;
			this.values.put("typename", custom.get("name"));
			this.values.put("typeuuid", custom.get("uuid"));
			this.values.put("udfields", custom.get("udfs"));
		}
	}
	
	public void addRecType(String uuid, String name, String desc, ArrayList udfs){
		boolean isNew = true;

		for(int i=0;i<this.uuids.size();i++){
			String u = (String)this.uuids.get(i);
			if(u.equals(name)){
				isNew = false;
				break;
			}
		}
		if(isNew){
			this.uuids.add(name);
			HashMap custom = new HashMap();
			custom.put("name", name);
			custom.put("uuid", uuid);
			custom.put("description", desc);
			custom.put("udfs", udfs);
			String[] caption = new String[2];
			caption[0] = name;
			caption[1] = desc;
			this.types.addItem(caption,null, custom);
		}
	}
	
	public void reset(){
		this.types.clear();
		this.values.clear();
		this.uuids.clear();
	}

}
