/* +----------------------------------------------------------------------+
 * | AUDOC_CLIENT                                                      |
 * +----------------------------------------------------------------------+
 * | Copyright (c) 2007 Audata Ltd                                     |
 * +----------------------------------------------------------------------+
 * | This source file is subject to version 2 of the Gnu Public License,  |
 * | that is bundled with this package in the file License.txt, and is    |
 * | available at through the world-wide-web at                           |
 * | http://www.gnu.org/licenses/gpl.txt.                                 |
 * | If you did not receive a copy of the GPL license and are unable to   |
 * | obtain it through the world-wide-web, please send a note to          |
 * | support@audata.co.uk so we can mail you a copy immediately.          |
 * +----------------------------------------------------------------------+
 * | Authors: Jonathan Moss													  |
 * +----------------------------------------------------------------------+
 */
package com.audata.client.record;

import java.util.HashMap;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.widgets.HTMLButtonList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author jonm
 *
 */
public class Revisions extends Composite {

    private String recuuid;
    private HTMLButtonList revs;
    private static final Language LANG = (Language) GWT.create(Language.class);
    
    public Revisions(String recuuid){
	this.recuuid = recuuid;
	VerticalPanel main = new VerticalPanel();
	main.setSize("100%", "100%");
	main.setSpacing(6);
	String template = "<span class=\"audoc-revision\">" + LANG.version_Text() + ": #0 - #1</span>";
	this.revs = new HTMLButtonList("images/16x16/generic.gif", template, false);
	this.revs.setSize("250px", "350px");
	this.revs.addStyleName("audoc-revisions");
	main.add(this.revs);
	this.getRevs();
	this.initWidget(main);
    }
    
    
    
    private void getRevs(){
	JSONArray params = new JSONArray();
	params.set(0, new JSONString(this.recuuid));
	AuDoc.jsonCall.asyncPost2("docstore.getRevisions", params, new RevisionHandler(this));
    }
    
    public void addRevision(String uuid, int revision, String name){
	String[] caption = new String[2];
	caption[0] = Integer.toString(revision);
	caption[1] = name;
	HashMap custom = new HashMap();
	custom.put("uuid", uuid);
	custom.put("name", name);
	custom.put("revision", new Integer(revision));
	this.revs.addItem(caption, null, custom);
    }
    
}
