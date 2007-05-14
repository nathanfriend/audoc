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
package com.audata.client.widgets;

import com.audata.client.util.FieldTypes;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Field implements IValidated{

	
	
	public String name;
	public int type;
	public boolean isUDF;
	public String kwh;
	private Widget field;
	
	public Field(String name, int type, boolean isUDF){
		this(name, type, isUDF, null);
	}
	
	public Field(String name, int type, boolean isUDF, String kwh){
		this.name = name;
		this.type = type;
		this.isUDF = isUDF;
		if(kwh != null){
			this.kwh = kwh;
		}
		switch(this.type){
		case(FieldTypes.TYPE_CLASS):
			this.field = new ClassField();
			break;
		case(FieldTypes.TYPE_DATE):
			this.field = new ValidatedTextBox(ValidatedTextBox.PATTERN_DATE);
			break;
		case(FieldTypes.TYPE_DEC):
			this.field = new ValidatedTextBox(ValidatedTextBox.PATTERN_FLOAT);
			break;
		case(FieldTypes.TYPE_INT):
			this.field = new NumericTextBox();
			break;
		case(FieldTypes.TYPE_NOTES):
			this.field = new TextArea();
			break;
		case(FieldTypes.TYPE_STRING):
			this.field = new ValidatedTextBox(ValidatedTextBox.PATTERN_ANY);
			break;
		case(FieldTypes.TYPE_KEYWORD):
			this.field = new KeywordField(this.kwh, this.name);
			break;
		case(FieldTypes.TYPE_RECNUM):
			this.field = new ValidatedTextBox(ValidatedTextBox.PATTERN_ANY);
		}
	}
	
	public String getText(){
		String ret = "";
		switch(this.type){
		case(FieldTypes.TYPE_CLASS):
			ClassField w = (ClassField)this.field;
			ret = w.getTextBox().getText();
			break;
		case(FieldTypes.TYPE_KEYWORD):
			KeywordField k = (KeywordField)this.field;
			ret = k.getTextBox().getText();
			break;
		case(FieldTypes.TYPE_DATE):
		case(FieldTypes.TYPE_DEC):
		case(FieldTypes.TYPE_INT):
		case(FieldTypes.TYPE_STRING):
		case(FieldTypes.TYPE_RECNUM):
			TextBox t = (TextBox)this.field;
			ret = t.getText();
			break;
		case(FieldTypes.TYPE_NOTES):
			TextArea ta = (TextArea)this.field;
			ret = ta.getText();
			break;
		}
		return ret;
	}
	
	public void setReadOnly(){
		switch(this.type){
		case(FieldTypes.TYPE_CLASS):
			//cannot be readonly
			break;
		case(FieldTypes.TYPE_DATE):
		case(FieldTypes.TYPE_DEC):
		case(FieldTypes.TYPE_INT):
		case(FieldTypes.TYPE_STRING):
		case(FieldTypes.TYPE_KEYWORD):
		case(FieldTypes.TYPE_RECNUM):
			TextBox t = (TextBox)this.field;
			t.setEnabled(false);
			t.addStyleName("audoc-disabled");
			break;
		case(FieldTypes.TYPE_NOTES):
			TextArea ta = (TextArea)this.field;
			ta.setEnabled(false);
			break;
		}
	}
	
	public void setText(String value){
		switch(this.type){
		case(FieldTypes.TYPE_CLASS):
			//needs uuid as well, use setValue
			break;
		case(FieldTypes.TYPE_KEYWORD):
			//needs uuid as well, use setValue;
			break;
		case(FieldTypes.TYPE_DATE):
		case(FieldTypes.TYPE_DEC):
		case(FieldTypes.TYPE_INT):
		case(FieldTypes.TYPE_STRING):
		case(FieldTypes.TYPE_RECNUM):
			TextBox t = (TextBox)this.field;
			t.setText(value);
			break;
		case(FieldTypes.TYPE_NOTES):
			TextArea ta = (TextArea)this.field;
			ta.setText(value);
			break;
		}
	}
	
	/**
	 * Special adaption of setText for Classification fields
	 * @param name
	 * @param value
	 */
	public void setValue(String name, String value){
		switch(this.type){
		case FieldTypes.TYPE_CLASS:
			ClassField cf = (ClassField)this.field;
			ClassTextBox ctb = (ClassTextBox)cf.getTextBox();
			ctb.setValues(name, value);
			break;
		case FieldTypes.TYPE_KEYWORD:
			KeywordField kf = (KeywordField)this.field;
			KeywordTextBox ktb = (KeywordTextBox)kf.getTextBox();
			ktb.setValues(name, value);
		}
	
	}
	
	public String getValue(){
		String ret = "";
		switch(this.type){
		case FieldTypes.TYPE_KEYWORD:
			KeywordField kf = (KeywordField)this.field;
			KeywordTextBox ktb = (KeywordTextBox)kf.getTextBox();
			ret = ktb.getValue();
			break;
		case FieldTypes.TYPE_CLASS:
			ClassField cf = (ClassField)this.field;
			ClassTextBox ctb = (ClassTextBox)cf.getTextBox();
			ctb.getValue();
			break;
		}
		return ret;
	}
	
	public Widget getField(){
		return this.field;
	}
	
	public boolean isValid(){
		if(this.type == FieldTypes.TYPE_NOTES){
			return true;
		}
		IValidated v = (IValidated)this.field;
		return v.isValid();
	}
	
}
