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
package com.audata.client;

import org.gwtwidgets.client.util.Location;
import org.gwtwidgets.client.util.WindowUtils;
import org.gwtwidgets.client.wrap.Effect;

import com.audata.client.admin.AdminPanel;
import com.audata.client.authentication.LoginDialog;
import com.audata.client.authentication.LogoutListener;
import com.audata.client.checkout.CheckoutPanel;
import com.audata.client.classification.ClassBrowser;
import com.audata.client.json.JSONCall;
import com.audata.client.newRecord.NewRecord;
import com.audata.client.rapidbooking.RapidBookingDialog;
import com.audata.client.reporting.ReportPanel;
import com.audata.client.search.QuickSearchPanel;
import com.audata.client.search.SavedSearchPanel;
import com.audata.client.search.SearchPanel;
import com.audata.client.state.SecLoader;
import com.audata.client.state.State;
import com.audata.client.trays.TrayPanel;
import com.audata.client.widgets.CaptionButton;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The AuDoc class is the bootstrap code for the
 * AuDoc client user interface. 
 */
public class AuDoc implements EntryPoint {

	private static final Language LANG = (Language) GWT.create(Language.class);
	public static final int SECTION_HOME = 0;

	public static final int SECTION_SEARCH = 1;

	public static final int SECTION_NEW = 2;

	public static final int SECTION_REPORT = 3;

	public static final int SECTION_ADMIN = 4;
	
	public static final int SECTION_RAPID = 5;
	
	public static final int STACK_ALL = 0;
	public static final int STACK_CLASSIFICATION = 1;
	public static final int STACK_SAVEDSEARCHES = 2;
	public static final int STACK_TRAYS = 3;
	public static final int STACK_CHECKOUTS = 4;

	public static JSONCall jsonCall;
	
	private static AuDoc instance;

	private LoginDialog ld;
	
	private QuickSearchPanel quicksearch;

	private TrayPanel tp;
	
	private SavedSearchPanel ss;

	private ClassBrowser bp;

	private CheckoutPanel cp;

	private CaptionButton adminButton;

	private ScrollPanel main;

	// private VerticalPanel main;

	public static State state;

	/**
	 * A JSNI method to return config items from the
	 * configuration javascript file
	 * @param name The name of the config item wanted
	 * @return String
	 */
	public static native String getConfigItem(String name)/*-{
		if($wnd.configItems[name]){
			return $wnd.configItems[name];
		}else{
			return "";
		}	   
	 }-*/;

	/**
	 * Returns a reference to the current instance of AuDoc
	 * @return The instance of AuDoc currently running
	 */
	public static AuDoc getInstance(){
		return AuDoc.instance;
	}
	
	/**
	 * This is the entry point method. and is responsible for building the UI
	 */
	public void onModuleLoad() {
		//set static variable to instance of this object
		if(AuDoc.instance == null){
			AuDoc.instance = this;
		}
		
		//Set window title
		Window.setTitle(LANG.site_title_Text());
		
		//get timeout value
		String tout = AuDoc.getConfigItem("timeout");
		int timeout;
		if(tout.equals("")){
			timeout = 500000;
		}else{
			timeout = Integer.parseInt(tout);
		}
		//get server url
		String url = AuDoc.getConfigItem("url");
		if(url.equals("")){
			url = this.getURL();
		}
		//create JSONCall object
		AuDoc.jsonCall = new JSONCall(url, timeout);
		//create state object
		AuDoc.state = new State();

		//Build UI
		RootPanel.get("logo").add(new Image("images/title/Logo.jpg"));
		RootPanel.get("menu").add(this.buildMenu());
		RootPanel.get("stack").add(this.buildStack());
		this.quicksearch = new QuickSearchPanel(this);
		RootPanel.get("quickSearch").add(this.quicksearch);
		this.main = new ScrollPanel();
		this.main.setHeight("100%");
		this.main.addStyleName("audoc-main");
		RootPanel.get("main").add(this.main);
		this.main.add(new HomePanel());

		//build login dialog and reset app to starting state
		this.ld = new LoginDialog(this);
		this.resetApp();
	}

	/**
	 * Builds the menu bar panel
	 * @return HorizontalPanel containing the menu bar
	 */
	private HorizontalPanel buildMenu() {
		HorizontalPanel menu = new HorizontalPanel();
		menu.addStyleName("menu");
		menu.setWidth("100%");
		menu.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
		menu.setVerticalAlignment(HasAlignment.ALIGN_BOTTOM);
		menu.setSpacing(4);

		CaptionButton cb = new CaptionButton();
		cb.setImageUrl("images/48x48/home.gif");
		cb.setCaptionText(LANG.home_Text());
		cb.setOrientation(DockPanel.SOUTH);
		cb.addClickListener(new MenuClickListener(this, AuDoc.SECTION_HOME));
		cb.setTitle(LANG.home_title_Text());
		menu.add(cb);

		CaptionButton cb_1 = new CaptionButton();
		cb_1.setImageUrl("images/48x48/search.gif");
		cb_1.setCaptionText(LANG.search_Text());
		cb_1.setOrientation(DockPanel.SOUTH);
		cb_1.addClickListener(new MenuClickListener(this, AuDoc.SECTION_SEARCH));
		cb_1.setTitle(LANG.search_title_Text());
		menu.add(cb_1);

		CaptionButton cb_2 = new CaptionButton();
		cb_2.addCaptionStyleName("nowrap");
		cb_2.setOrientation(DockPanel.SOUTH);
		cb_2.setImageUrl("images/48x48/newrec.gif");
		cb_2.setCaptionText(LANG.newrec_Text());
		cb_2.addClickListener(new MenuClickListener(this, AuDoc.SECTION_NEW));
		cb_2.setTitle(LANG.newrec_title_Text());
		menu.add(cb_2);

		CaptionButton cb_3 = new CaptionButton();
		cb_3.setImageUrl("images/48x48/reports.gif");
		cb_3.setCaptionText(LANG.report_Text());
		cb_3.setOrientation(DockPanel.SOUTH);
		cb_3.addClickListener(new MenuClickListener(this, AuDoc.SECTION_REPORT));
		cb_3.setTitle(LANG.report_title_Text());
		menu.add(cb_3);
		
		CaptionButton cb_4 = new CaptionButton();
		cb_4.setImageUrl("images/48x48/checkout.gif");
		cb_4.setCaptionText(LANG.rapid_title_Text());
		cb_4.setOrientation(DockPanel.SOUTH);
		cb_4.addClickListener(new MenuClickListener(this, AuDoc.SECTION_RAPID));
		cb_4.setTitle(LANG.rapid_title_Text());
		menu.add(cb_4);

		this.adminButton = new CaptionButton();
		this.adminButton.setImageUrl("images/48x48/admin.gif");
		this.adminButton.setCaptionText(LANG.admin_Text());
		this.adminButton.setOrientation(DockPanel.SOUTH);
		this.adminButton.setVisible(false);
		this.adminButton.addClickListener(new MenuClickListener(this,
				AuDoc.SECTION_ADMIN));
		this.adminButton.setTitle(LANG.admin_title_Text());
		menu.add(this.adminButton);
		menu.setCellWidth(this.adminButton, "100%");
		menu.setCellHorizontalAlignment(this.adminButton,
				HasAlignment.ALIGN_RIGHT);

		return menu;
	}

	/**
	 * Builds the stackpanel
	 * @return StackPanel containing the stack
	 */
	private StackPanel buildStack() {
		StackPanel panel = new StackPanel();
		panel.setWidth("180px");
		panel.setHeight("100%");
		this.bp = new ClassBrowser(this, "180px", "100%");
		panel.add(this.bp, LANG.browse_Text());

		this.ss = new SavedSearchPanel(this);
		panel.add(this.ss, LANG.saved_searches_Text());

		this.tp = new TrayPanel(this);
		panel.add(this.tp, LANG.trays_Text());

		this.cp = new CheckoutPanel(this);
		panel.add(this.cp, LANG.checkouts_Text());
		return panel;
	}

	/**
	 * Called when a user logs in
	 */
	public void onLogin() {

		SecLoader.cacheCaveats();
		SecLoader.cacheSecLevels();

		HorizontalPanel panel = new HorizontalPanel();
		panel.setSpacing(2);
		panel.setWidth("150px");
		panel.add(new Image("images/16x16/users.gif"));
		Label name = new Label(AuDoc.state.getItem("surname") + ", "
				+ AuDoc.state.getItem("forename"));
		name.setWidth("116px");
		name.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		name.addStyleName("audoc-userNote");
		panel.add(name);
		Image logout = new Image("images/16x16/logout.gif");
		logout.addStyleName("audoc-logoutButton");
		logout.addClickListener(new LogoutListener(this));
		panel.add(logout);
		RootPanel.get("user").add(panel);

		if (AuDoc.state.getItem("isAdmin") == "true") {
			this.adminButton.setVisible(true);
		}

		this.bp.onUpdate();
		this.tp.onUpdate();
		this.cp.onUpdate();
		this.ss.onUpdate();
	}

	/**
	 * Adds a message to the debug panel
	 * @param msg the message to add
	 */
	public void setDebug(String msg) {
		RootPanel.get("Debug").add(new Label(msg));
	}

	/**
	 * Set the application back to it's starting
	 * state
	 */
	public void resetApp() {
		AuDoc.state.clearState();
		switchMain(AuDoc.SECTION_HOME);
		RootPanel.get("user").clear();

		this.adminButton.setVisible(false);
		this.quicksearch.onLogout();
		
		this.bp.onLogout();
		this.tp.onLogout();
		this.cp.onLogout();
		this.ss.onLogout();

		this.ld.show();
		}

	/**
	 * Switches the main section to the specified
	 * content type
	 * @param section An integer used to determine the section type to use. Use AuDoc.SECTION_* to specify section
	 */
	public void switchMain(int section) {
		Widget p = null;
		try {
			this.main.clear();
		} catch (Exception e) {
			Window.alert(e.getMessage());
		}
		switch (section) {
		case (AuDoc.SECTION_ADMIN):
			p = new AdminPanel();
			break;
		case (AuDoc.SECTION_HOME):
			p = new HomePanel();
			break;
		case (AuDoc.SECTION_NEW):
			//p = new RecordPanel("a");
			p = new NewRecord();
			break;
		case (AuDoc.SECTION_REPORT):
			p = new ReportPanel();
			break;
		case (AuDoc.SECTION_SEARCH):
			p = new SearchPanel(this);
			break;
		default:
			p = new HomePanel();
			break;
		}
		p.setVisible(false);
		this.main.add(p);
		Effect.appear(p);
		if(section == AuDoc.SECTION_RAPID){
		    RapidBookingDialog rbd = new RapidBookingDialog();
		    rbd.show();
		}
	}

	/**
	 * Sets the main section to the Widget provided
	 * @param content The Widget object to display
	 */
	public void setMain(Widget content){
		this.main.clear();
		this.main.setWidget(content);
	}
	
	/**
	 * Update one or all of the sections in the stack
	 * 
	 * @param stack Use AuDoc.STACK_* to specify the stack item(s) to update
	 */
	public void updateStack(int stack){
		switch(stack){
		case AuDoc.STACK_CHECKOUTS:
			this.cp.onUpdate();
			break;
		case AuDoc.STACK_CLASSIFICATION:
			this.bp.onUpdate();
			break;
		case AuDoc.STACK_SAVEDSEARCHES:
			this.ss.onUpdate();
			break;
		case AuDoc.STACK_TRAYS:
			this.tp.onUpdate();
			break;
		case AuDoc.STACK_ALL:
		default:
			this.bp.onUpdate();
			this.tp.onUpdate();
			this.ss.onUpdate();
			this.cp.onUpdate();
			break;
		}	
	}
	
	/**
	 * Returns the Server URL if installed along side the
	 * the client. Only used if server URL not speficied in
	 * config.js
	 * @return String The server URL 
	 */
	private String getURL(){
	    	String folder = LANG.app_name_Text().toUpperCase() + "_SERVER/";
		Location location = WindowUtils.getLocation();
		String ret = "http://" + location.getHost() + "/" + folder;
		return ret;
	}
}
