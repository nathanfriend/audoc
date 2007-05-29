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
import com.audata.client.feedback.SimpleDialog;
import com.audata.client.json.Entry;
import com.audata.client.json.NoOpResponseHandler;
import com.audata.client.json.URLEncoder;
import com.audata.client.json.UpdateResponseHandler;
import com.audata.client.trays.TrayDialog;
import com.audata.client.util.Print;
import com.audata.client.widgets.HTMLButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class CommandClickListener implements ClickListener {

	private static final Language LANG = (Language) GWT.create(Language.class);
	public static final int COMMAND_PROPERTIES = 0;
	public static final int COMMAND_CHECKOUT = 1;
	public static final int COMMAND_TRAY = 2;
	public static final int COMMAND_DEL = 3;
	public static final int COMMAND_AMEND = 4;
	public static final int COMMMAND_RTRAY = 5;
	public static final int COMMAND_VIEW = 6;
	public static final int COMMAND_CHECKIN = 7;
	public static final int COMMAND_REFRESH = 8;
	public static final int COMMAND_PRINT = 9;
	
	private RecordListPanel parent;
	private int command;
	private String uuid;
	private ArrayList selected;
	
	public CommandClickListener(RecordListPanel parent, int command){
		this(parent, command, null);
	}
	
	public CommandClickListener(RecordListPanel parent, int command, String uuid){
		this.parent = parent;
		this.command = command;
		this.uuid = uuid;
	}
	
	public void onClick(Widget sender) {
		this.selected = this.parent.getSelected();
		switch(this.command){
			case (CommandClickListener.COMMAND_PROPERTIES):
				this.onProperties();
			break;
			case (CommandClickListener.COMMAND_CHECKOUT):
				this.onCheckOut();
			break;
			case(CommandClickListener.COMMAND_TRAY):
				this.onTray();
			break;
			case(CommandClickListener.COMMAND_AMEND):
				this.onAmend();
			break;
			case(CommandClickListener.COMMAND_DEL):
				this.onDelete();
			break;
			case(CommandClickListener.COMMMAND_RTRAY):
				this.onRTray();
			break;
			case(CommandClickListener.COMMAND_VIEW):
				this.onView();
			break;
			case(CommandClickListener.COMMAND_CHECKIN):
				this.onCheckin();
			break;
			case(CommandClickListener.COMMAND_REFRESH):
				this.onRefresh();
			break;
			case(CommandClickListener.COMMAND_PRINT):
				this.onPrint();
			break;
		}
	}
	
	private void onProperties(){
		if(this.selected.size() != 0){
			HTMLButton selected = (HTMLButton)this.selected.get(0);
			HashMap props = (HashMap)selected.custom;
			String record = (String)props.get("uuid");
			String rType = (String)props.get("RecordType");
			String cot = (String)props.get("CheckedOutTo");		
			new RecordPropertiesDialog(this.parent, rType, record, cot);
			
			
		}
	}
	
	/**
	 * checkes out the record to the user
	 * and then retrieves the document itself
	 */
	private void onCheckOut(){
		if(this.selected.size() != 0){
			HTMLButton selected = (HTMLButton)this.selected.get(0);
			HashMap props = (HashMap)selected.custom;
			String uuid = (String)props.get("uuid");
			Double documents = (Double)props.get("Documents");
			String cot = (String)props.get("CheckedOutTo");
			if (cot.length() > 0){
				SimpleDialog.displayDialog(SimpleDialog.TYPE_MESSAGE, LANG.cannot_checkout_Text(), LANG.cannot_checkout_msg_Text() + " " + cot);
			}else{ 
				JSONArray params = new JSONArray();
				params.set(0, new JSONString(uuid));
				if(documents.intValue() > 0){
					AuDoc.jsonCall.asyncPost2("recman.checkout", params, new CheckoutCallback(this.parent, uuid));
				}else{
					AuDoc.jsonCall.asyncPost2("recman.checkout", params, new UpdateResponseHandler(this.parent));
					AuDoc.getInstance().updateStack(AuDoc.STACK_CHECKOUTS);
				}
			}
		}
	}
	
	private void onTray(){
		if(this.selected.size() != 0){
			ArrayList records = new ArrayList();
			for(int i=0; i<this.selected.size();i++){
				HTMLButton s = (HTMLButton)this.selected.get(i);
				HashMap props = (HashMap)s.custom;
				records.add(props.get("uuid"));
			}
			TrayDialog td = new TrayDialog(records);
			td.show();
		}
	}
	
	private void onDelete(){
		if(this.selected.size() != 0){
			ArrayList records = new ArrayList();
			for(int i=0; i<this.selected.size();i++){
				HTMLButton s = (HTMLButton)this.selected.get(i);
				HashMap props = (HashMap)s.custom;
				String uuid = (String)props.get("uuid");
				records.add(uuid);
			}
			SimpleDialog.displayDialog(SimpleDialog.TYPE_QUERY, LANG.confirm_del_Text(), LANG.confirm_del_msg_Text(), new DeleteCallback(this.parent, records));
		}
	}
	
	private void onAmend(){
		if(this.selected.size() != 0){
			HTMLButton selected = (HTMLButton)this.selected.get(0);
			HashMap props = (HashMap)selected.custom;
			String uuid = (String)props.get("uuid");
			String recNumber = (String)props.get("RecordNumber");
			if (recNumber != ""){
				RecNumberDialog r = new RecNumberDialog(uuid, recNumber, this.parent);
				r.show();
			}
		}
	}
	
	private void onRTray(){
		if(this.selected.size() != 0){
			for(int i=0; i<this.selected.size();i++){
				HTMLButton s = (HTMLButton)this.selected.get(i);
				HashMap props = (HashMap)s.custom;
				//records.add(props.get("uuid"));
				JSONArray params = new JSONArray();
				params.set(0, new JSONString(this.uuid));
				params.set(1, new JSONString((String)props.get("uuid")));
				if(i == (this.selected.size()-1)){
					AuDoc.jsonCall.asyncPost2("trays.remItem", params, new UpdateResponseHandler(this.parent));
				}else{
					AuDoc.jsonCall.asyncPost2("trays.remItem", params, new NoOpResponseHandler());
				}
			}
		}
	}
	
	private void onView(){
		if(this.selected.size() != 0){
			HTMLButton selected = (HTMLButton)this.selected.get(0);
			HashMap props = (HashMap)selected.custom;
			Double documents = (Double)props.get("Documents");
			if(documents.intValue() != 0 ){
				String uuid = (String)props.get("uuid");
				String url = AuDoc.jsonCall.getURL() + "docIO.php";
				Entry[] params = new Entry[2];
				params[0] = new Entry("method", "view");
				params[1] = new Entry("id", uuid);
				String encParams = URLEncoder.buildQueryString(params);
				this.view(url + "?" + encParams);
			}else{
				SimpleDialog.displayDialog(SimpleDialog.TYPE_MESSAGE, LANG.no_electronic_Text(), LANG.no_electronic_msg_Text());
			}
		}
	}
	
	private void onCheckin(){
		if(this.selected.size() != 0){
			ArrayList records = new ArrayList();
			for(int i=0; i<this.selected.size();i++){
				HTMLButton s = (HTMLButton)this.selected.get(i);
				HashMap props = (HashMap)s.custom;
				String uuid = (String)props.get("uuid");
				records.add(uuid);
			}
			SimpleDialog.displayDialog(SimpleDialog.TYPE_QUERY, LANG.confirm_checkin_Text(), LANG.confirm_checkin_msg_Text(), new CheckinCallback(this.parent, records));
		}
	}
	
	private void onRefresh(){
		this.parent.onUpdate();
	}
	
	private void onPrint(){
		Print.it(this.parent.getRecordList());
	}
	
	/**
	 * Needed to open a file directly
	 * @param url
	 */
	public native void download(String url) /*-{
	  $wnd.location.href = url; 
	}-*/;
		
	/**
	 * Needed to open a file directly
	 * @param url
	 */
	public native void view(String url) /*-{
	  $wnd.open(url, '_blank');  
	}-*/;
}
