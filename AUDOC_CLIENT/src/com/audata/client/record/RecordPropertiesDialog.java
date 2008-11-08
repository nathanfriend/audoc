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
 * | Authors: jonm													  |
 * +----------------------------------------------------------------------+
 */
package com.audata.client.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.gwtwidgets.client.wrap.Callback;
import org.gwtwidgets.client.wrap.Effect;
import org.gwtwidgets.client.wrap.EffectOption;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.feedback.UploadHandler;
import com.audata.client.feedback.UploadListener;
import com.audata.client.json.UpdateListener;
import com.audata.client.json.UpdateResponseHandler;
import com.audata.client.util.FieldTypes;
import com.audata.client.widgets.Field;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author jonm
 * 
 */
public class RecordPropertiesDialog extends DialogBox implements TabListener,
	Callback, UploadListener, ClickListener {

    private static final Language LANG = (Language) GWT.create(Language.class);

    private String recorduuid;

    private String recordType;

    private String checkedOutTo;

    private UpdateListener parent;

    private Revisions revisions;

    private RecordProperties properties;

    private VerticalPanel main;

    private Button okButton;

    private Button cancelButton;

    private ArrayList fields;

    public RecordPropertiesDialog(UpdateListener parent, String rType,
	    String record, String checkedOutTo) {
	this.parent = parent;
	this.recorduuid = record;
	this.recordType = rType;
	this.checkedOutTo = checkedOutTo;

	this.fields = new ArrayList();
	this.properties = new RecordProperties(this, this.checkedOutTo);
	this.revisions = new Revisions(this.recorduuid);
	this.main = new VerticalPanel();

	TabBar tabs = new TabBar();
	tabs.addTab(LANG.props_Text());
	tabs.addTab("Revisions");
	tabs.addTabListener(this);
	tabs.selectTab(0);
	this.main.add(tabs);

	this.main.add(this.properties);
	this.main.add(this.revisions);
	this.revisions.setVisible(false);

	HorizontalPanel buttonPanel = new HorizontalPanel();
	buttonPanel.setSpacing(4);

	this.okButton = new Button(LANG.ok_Text());
	this.okButton.addClickListener(this);
	buttonPanel.add(this.okButton);
	this.cancelButton = new Button(LANG.cancel_Text());
	this.cancelButton.addClickListener(this);
	buttonPanel.add(this.cancelButton);
	this.main.add(buttonPanel);
	this.addFields();
	this.getFields();
	this.getRecord();
	this.setWidget(this.main);
    }

    public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
	this.clear();
	switch (tabIndex) {
	case 0:
	    this.revisions.setVisible(false);
	    this.properties.setVisible(true);
	    break;
	case 1:
	    int height = this.properties.getOffsetHeight();
	    int width = this.properties.getOffsetWidth();
	    this.revisions.setVisible(true);
	    this.revisions.setPixelSize(width, height);
	    this.properties.setVisible(false);
	    break;
	}

    }

    public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
	return true;
    }

    /**
         * Overide parent show method to add effects and dialog centering
         */
    public void show() {
	this.setVisible(true);
	super.show();
	//Effect.appear(this);
	int left = 20;
	int top = 20;
	this.setPopupPosition(left, top);
    }

    /**
         * Overide parent hide method to add effects
         */
    public void hide() {
	Effect.fade(this, new EffectOption[] { new EffectOption("afterFinish",
		this) });
    }

    /**
         * Called when hide effect are complete to finish hide process
         */
    public void execute() {
	super.hide();
    }

    private void getRecord() {
	JSONArray params = new JSONArray();
	params.set(0, new JSONString(this.recorduuid));
	AuDoc.jsonCall.asyncPost2("recman.getRecord", params,
		new PropertiesCallback(this));
    }

    private void addFields() {
	this.fields.add(new Field(LANG.rec_num_Text(), FieldTypes.TYPE_RECNUM,
		false));
	this.fields.add(new Field(LANG.classification_Text(),
		FieldTypes.TYPE_CLASS, false));
	this.fields.add(new Field(LANG.title_Text(), FieldTypes.TYPE_STRING,
		false));
	this.fields.add(new Field(LANG.date_created_Text(),
		FieldTypes.TYPE_DATE, false));
	this.fields.add(new Field(LANG.owner_Text(), FieldTypes.TYPE_STRING,
		false));
	this.fields.add(new Field(LANG.author_Text(), FieldTypes.TYPE_STRING,
		false));
	this.fields.add(new Field(LANG.notes_Text(), FieldTypes.TYPE_NOTES,
		false));
	// obtain udfs for given record type
	JSONArray params = new JSONArray();
	params.set(0, new JSONString(this.recordType));
	AuDoc.jsonCall.asyncPost2("recman.getRecType", params,
		new RecTypeCallback(this));
    }

    /**
         * Add the udfs for this record type
         * 
         * @param udfs
         *                An array of Fields to add to field list
         */
    public void addUdfs(Field[] udfs) {
	for (int i = 0; i < udfs.length; i++) {
	    this.fields.add(udfs[i]);
	}
    }

    public void paint() {
	this.properties.paint();
	this.show();
    }

    protected ArrayList getFields() {
	return this.fields;
    }

    public void setRecord(HashMap props) {
	this.properties.setRecord(props);
	this.properties.paint();
    }

    /**
         * $uuid, $title, $classID, $datec, $owner, $author, $notes, $recNum,
         * $UDFs
         * 
         * @param fields
         * @param udfs
         */
    private void saveRecord(HashMap fields, HashMap udfs) {
	JSONArray params = new JSONArray();
	params.set(0, new JSONString(this.recorduuid));
	params.set(1, new JSONString((String) fields.get(LANG.title_Text())));
	params.set(2, new JSONString((String) fields.get(LANG
		.classification_Text())));
	params.set(3, new JSONString((String) fields.get(LANG
		.date_created_Text())));
	params.set(4, new JSONString((String) fields.get(LANG.owner_Text())));
	params.set(5, new JSONString((String) fields.get(LANG.author_Text())));
	params.set(6, new JSONString((String) fields.get(LANG.notes_Text())));
	params.set(7, new JSONString((String) fields.get(LANG.rec_num_Text())));

	JSONObject u = new JSONObject();
	Iterator i = udfs.keySet().iterator();
	while (i.hasNext()) {
	    try {
		String name = (String) i.next();
		String value = (String) udfs.get(name);
		u.put(name, new JSONString(value));
	    } catch (Exception e) {
	    }
	}

	params.set(8, u);
	AuDoc.jsonCall.asyncPost2("recman.modRecord", params,
		new PropertiesSavedCallback(this));
    }

    public void saveDoc() {
	if (this.checkedOutTo.equals(AuDoc.state.getItem("username"))
		|| this.checkedOutTo == "") {
	    JSONArray params = new JSONArray();
	    params.set(0, new JSONString(this.recorduuid));
	    AuDoc.jsonCall.asyncPost2("recman.checkin", params,
		    new UpdateResponseHandler(this.parent));
	    this.properties.submit(new UploadHandler(this), this.recorduuid);

	}
	// is this the part causing the issue??
	// this.hide();
    }

    public void onUploadComplete() {
	this.hide();
    }

    public void onClick(Widget sender) {
	if (sender == this.cancelButton) {
	    this.hide();
	    return;
	}
	if (sender == this.okButton) {
	    HashMap fields = new HashMap();
	    HashMap udfs = new HashMap();
	    for (int i = 0; i < this.fields.size(); i++) {
		Field f = (Field) this.fields.get(i);
		if (f.isUDF) {
		    udfs.put(f.name, f.getText());
		} else {
		    fields.put(f.name, f.getText());
		}
	    }
	    this.saveRecord(fields, udfs);
	    return;
	}
    }
}
