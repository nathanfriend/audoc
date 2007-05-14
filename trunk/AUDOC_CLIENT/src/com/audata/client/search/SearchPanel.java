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
package com.audata.client.search;

import java.util.ArrayList;
import java.util.HashMap;

import com.audata.client.AuDoc;
import com.audata.client.Language;
import com.audata.client.util.FieldTypes;
import com.audata.client.widgets.CaptionButton;
import com.audata.client.widgets.Field;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SearchPanel extends VerticalPanel implements TreeListener,
		ClickListener {
	private static final Language LANG = (Language) GWT.create(Language.class);
	private AuDoc audoc;
	
	private Tree fieldsTree;

	private TreeItem textFields;

	private TreeItem decimalFields;

	private TreeItem integerFields;

	private TreeItem dateFields;

	private TreeItem keywordFields;

	private Label fieldName;

	//private Widget value;

	private Field field1;
	private Field field2;

	private RadioButton and;

	private RadioButton or;

	private Button add;

	private Button clear;

	private Button search;
	
	private Button save;

	private ListBox criteria;

	private HorizontalPanel valuePanel;

	private ArrayList searchTerms;
	
	private HashMap currentField;

	public SearchPanel(AuDoc audoc){
		this(audoc, null);
	}
	
	public SearchPanel(AuDoc audoc, ArrayList criteria) {
		this.audoc = audoc;
		if(criteria != null){
			this.searchTerms = criteria;
		}else{
			if(AuDoc.state.containsKey("Search")){
				this.searchTerms = (ArrayList)AuDoc.state.getItem("Search");
			}else{
				this.searchTerms = new ArrayList();
			}
		}
		this.setSize("100%", "100%");
		this.setSpacing(4);
		Label title = new Label(LANG.search_Text());
		title.addStyleName("audoc-sectionTitle");
		this.add(title);

		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(4);
		hp.setSize("100%", "100%");

		this.fieldsTree = new Tree();
		this.fieldsTree.addTreeListener(this);
		//this.fieldsTree.setSize("100%", "100%");
		this.fieldsTree.setHeight("90%");
		this.fieldsTree.addStyleName("audoc-fieldTree");
		hp.add(this.fieldsTree);
		this.buildSections();
		this.getUDFs();
		this.addStdFields();

		VerticalPanel form = new VerticalPanel();
		form.addStyleName("audoc-searchForm");
		//form.setSize("100%","100%");
		form.setWidth("250px");
		form.setSpacing(4);
		HorizontalPanel fieldRow = new HorizontalPanel();
		fieldRow.setSpacing(4);
		Label fieldLabel = new Label(LANG.field_Text());
		this.fieldName = new Label();
		this.fieldName.addStyleName("bold");

		fieldRow.add(fieldLabel);
		fieldRow.add(this.fieldName);
		fieldRow.setCellWidth(fieldLabel, "100px");
		fieldRow.setCellHorizontalAlignment(fieldLabel, HasAlignment.ALIGN_LEFT);
		form.add(fieldRow);

		
		
		this.valuePanel = new HorizontalPanel();
		this.valuePanel.setSpacing(4);
		Label valueLabel = new Label(LANG.criteria_Text());
		TextBox value = new TextBox();
		this.valuePanel.add(valueLabel);
		this.valuePanel.add(value);
		this.valuePanel.setCellWidth(valueLabel, "100px");
		this.valuePanel.setCellHorizontalAlignment(valueLabel, HasAlignment.ALIGN_LEFT);
		form.add(this.valuePanel);

		HorizontalPanel andOr = new HorizontalPanel();
		andOr.setSpacing(4);
		this.and = new RadioButton("andOr", LANG.and_Text());
		this.and.setChecked(true);
		this.or = new RadioButton("andOr", LANG.or_Text());
		andOr.add(this.and);
		andOr.add(this.or);
		form.add(andOr);

		HorizontalPanel buttons = new HorizontalPanel();
		buttons.setSpacing(4);
		this.add = new Button(LANG.add_Text());
		this.add.addClickListener(this);
		buttons.add(this.add);
		this.clear = new Button(LANG.clear_Text());
		this.clear.addClickListener(this);
		buttons.add(this.clear);

		form.add(buttons);

		this.criteria = new ListBox();
		this.criteria.setVisibleItemCount(10);
		this.criteria.setWidth("100%");
		form.add(this.criteria);

		HorizontalPanel buttons2 =new HorizontalPanel();
		buttons2.setSpacing(4);
		this.search = new Button(LANG.search_Text());
		this.search.addClickListener(this);
		buttons2.add(this.search);
		this.save = new Button(LANG.save_Text());
		this.save.addClickListener(this);
		buttons2.add(this.save);
		form.add(buttons2);
		
		hp.add(form);

		hp.setCellHeight(this.fieldsTree, "100%");
		this.add(hp);
		this.paintCriteria();
	}

	private void buildSections() {
		CaptionButton cp = new CaptionButton();
		cp.setImageUrl("images/16x16/field.gif");
		cp.setCaptionText(LANG.dates_Text());
		this.dateFields = new TreeItem(cp);
		this.dateFields.setUserObject(new Integer(FieldTypes.TYPE_DATE));
		this.fieldsTree.addItem(this.dateFields);

		CaptionButton cp_1 = new CaptionButton();
		cp_1.setImageUrl("images/16x16/field.gif");
		cp_1.setCaptionText(LANG.decs_Text());
		this.decimalFields = new TreeItem(cp_1);
		this.decimalFields.setUserObject(new Integer(FieldTypes.TYPE_DEC));
		this.fieldsTree.addItem(this.decimalFields);

		CaptionButton cp_2 = new CaptionButton();
		cp_2.setImageUrl("images/16x16/field.gif");
		cp_2.setCaptionText(LANG.ints_Text());
		this.integerFields = new TreeItem(cp_2);
		this.integerFields.setUserObject(new Integer(FieldTypes.TYPE_INT));
		this.fieldsTree.addItem(this.integerFields);

		CaptionButton cp_3 = new CaptionButton();
		cp_3.setImageUrl("images/16x16/field.gif");
		cp_3.setCaptionText(LANG.keywords_Text());
		this.keywordFields = new TreeItem(cp_3);
		this.keywordFields.setUserObject(new Integer(FieldTypes.TYPE_KEYWORD));
		this.fieldsTree.addItem(this.keywordFields);
		
		CaptionButton cp_4 = new CaptionButton();
		cp_4.setImageUrl("images/16x16/field.gif");
		cp_4.setCaptionText(LANG.strings_Text());
		this.textFields = new TreeItem(cp_4);
		this.textFields.setUserObject(new Integer(FieldTypes.TYPE_STRING));
		this.fieldsTree.addItem(this.textFields);
	}

	private void addStdFields() {
		this.addField(LANG.title_Text(), "Title", FieldTypes.TYPE_STRING, false, null);
		this.addField(LANG.rec_num_Text(), "RecordNumber", FieldTypes.TYPE_STRING, false, null);
		this.addField(LANG.date_created_Text(), "DateCreated", FieldTypes.TYPE_DATE, false, null);
		this.addField(LANG.date_reg_Text(), "DateRegistered", FieldTypes.TYPE_DATE, false, null);
		this.addField(LANG.last_mod_Text(), "LastModified", FieldTypes.TYPE_DATE, false, null);
		this.addField(LANG.review_date_Text(), "DateReview", FieldTypes.TYPE_DATE, false, null);
		this.addField(LANG.owner_Text(), "Owner", FieldTypes.TYPE_STRING, false, null);
		this.addField(LANG.author_Text(), "Author", FieldTypes.TYPE_STRING, false, null);
		this.addField(LANG.notes_Text(), "Notes", FieldTypes.TYPE_STRING, false, null);
	}

	public void addField(String name, String fieldName, int type, boolean isUDF, String kwh) {
		TreeItem ti = new TreeItem(name);
		HashMap user = new HashMap();
		user.put("FieldName", fieldName);
		user.put("Type", new Integer(type));
		user.put("isUDF", new Boolean(isUDF));
		user.put("KeywordHierarchy", kwh);
		ti.setUserObject(user);
		switch (type) {
		case FieldTypes.TYPE_DATE:
			this.dateFields.addItem(ti);
			break;
		case FieldTypes.TYPE_DEC:
			this.decimalFields.addItem(ti);
			break;
		case FieldTypes.TYPE_INT:
			this.integerFields.addItem(ti);
			break;
		case FieldTypes.TYPE_KEYWORD:
			this.keywordFields.addItem(ti);
			break;
		case FieldTypes.TYPE_STRING:
			this.textFields.addItem(ti);
			break;
		}
	}

	private void getUDFs() {
		AuDoc.jsonCall.asyncPost2("recman.getUDFs", new JSONArray(),
				new UDFResponseHandler(this));
	}

	public void onTreeItemSelected(TreeItem ti) {
		if (ti.getChildCount() < 1) {
			// we have a field selected
			this.fieldName.setText(ti.getText());
			HashMap user = (HashMap)ti.getUserObject();
			String caption = (String)user.get("FieldName");
			Integer type = (Integer)user.get("Type");
			Boolean isUDF = (Boolean)user.get("isUDF");
			String kwh = (String)user.get("KeywordHierarchy");
			this.setField(caption, type.intValue(), isUDF.booleanValue(), kwh);
			this.currentField = (HashMap)ti.getUserObject();
		} else {
			if (ti.getState()) {
				// this.closeAll();
				ti.setState(false);
			} else {
				this.closeAll();
				ti.setState(true);
			}
		}
	}

	private void setField(String caption, int type, boolean isUDF, String kwh) {

		switch(type){
			case FieldTypes.TYPE_DATE:
			case FieldTypes.TYPE_DEC:
			case FieldTypes.TYPE_INT:
				this.field1 = new Field(caption, type, isUDF);
				this.field2 = new Field(caption, type, isUDF);
				break;
			case FieldTypes.TYPE_KEYWORD:
				this.field1 = new Field(caption, type, true, kwh);
				this.field2 = null;
				break;
			case FieldTypes.TYPE_STRING:
				this.field1 = new Field(caption, type, isUDF);
				this.field2 = null;
				break;
		}
		Widget value1 = this.field1.getField();
		Widget value2 = null;
		if(this.field2 != null){
			value2 = this.field2.getField();
		}
		this.valuePanel.clear();
		Label l = new Label(LANG.criteria_Text() + ":");
		this.valuePanel.add(l);
		this.valuePanel.add(value1);
		if(value2 != null){
			this.valuePanel.add(new Label(" - "));
			this.valuePanel.add(value2);
		}
	}

	private void closeAll() {
		this.dateFields.setState(false);
		this.decimalFields.setState(false);
		this.integerFields.setState(false);
		this.keywordFields.setState(false);
		this.textFields.setState(false);
	}

	public void onTreeItemStateChanged(TreeItem ti) {
		// do nothing
	}

	public void onClick(Widget sender) {
		/*
		 * When the add button is pressed
		 */	
		if(sender == this.add){
			if(this.field1 != null && this.field1.isValid()){
				String disName = this.fieldName.getText();
				String fieldName = (String)this.currentField.get("FieldName");
				Boolean isUDF = (Boolean)this.currentField.get("isUDF");
				String operator = "";
				if (this.and.isChecked()) {
					operator = "and";
				} else {
					operator = "or";
				}
				
				String value1 = "";
				String disValue1 = "";
				if (this.field1.type == FieldTypes.TYPE_KEYWORD) {
					disValue1 = this.field1.getValue();
					value1 = this.field1.getText();
				} else {
					if(this.field1.type == FieldTypes.TYPE_STRING){
						disValue1 = this.field1.getText();
						value1 = this.field1.getText();
						//value1.replaceAll("\\*", "%");
						if((value1.indexOf("%") < 0) && (value1.indexOf("*") < 0)){
							value1 = "%" + value1 + "%";
						}
					}else{
						disValue1 = this.field1.getText();
						value1 = this.field1.getText();
					}
				}
				if(this.field2 != null && this.field2.getText().length() != 0 && this.field2.isValid()){
					String value2 = this.field2.getText();
					String disValue2 = this.field2.getText();
					Criteria crit = new Criteria(fieldName, disName, isUDF.booleanValue(), value1, disValue1, value2, disValue2, operator);
					this.searchTerms.add(crit);
				}else{
					Criteria crit = new Criteria(fieldName, disName, isUDF.booleanValue(), value1, disValue1, operator);
					this.searchTerms.add(crit);
				}
			
			}
			this.paintCriteria();
		}

		/*
		 * When the clear button is pressed
		 */
		if (sender == this.clear) {
			this.criteria.clear();
			this.searchTerms.clear();
			return;
		}
		
		/*
		 * When the search button is pressed
		 */
		if (sender == this.search) {
			if(this.searchTerms.size() > 0){
				JSONArray criteria = new JSONArray();
				for(int i=0;i<this.searchTerms.size();i++){
					Criteria c = (Criteria)this.searchTerms.get(i);
					String sterms = c.getSearchString();
					if(i==0){
						int pos = sterms.indexOf(" ");
						sterms = sterms.substring(pos+1);
					}
					criteria.set(i, new JSONString(sterms));
				}
				JSONArray params = new JSONArray();
				params.set(0, criteria);
				
				AuDoc.jsonCall.asyncPost2("search.complexsearch", params, new SearchResponseHandler(this.audoc, SearchResponseHandler.TYPE_FULL, params));
				AuDoc.state.setItem("Search", this.searchTerms);
			}
		}
		
		/*
		 * When the save button is pressed
		 */
		if(sender == this.save){
			//open a save search dialog
			if(this.searchTerms.size() > 0){
				SavedSearchDialog s = new SavedSearchDialog(this.searchTerms);
				s.show();	
			}
			return;
		}
	}
	
	private void paintCriteria(){
		this.criteria.clear();
		for(int i=0;i<this.searchTerms.size();i++){
			Criteria c = (Criteria)this.searchTerms.get(i);
			String sterms = c.getDisplayString();
			if(i==0){
				int pos = sterms.indexOf(" ");
				sterms = sterms.substring(pos+1);
			}
			this.criteria.addItem(sterms);
		}
	}
}
