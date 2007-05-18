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

import com.google.gwt.i18n.client.Constants;

/**
 * The interface to the internationalisation
 * system. Provides methods for all externalised string 
 * @author jonm
 *
 */
public interface Language extends Constants {
	String home_Text();
	String app_name_Text();
	String welcome_Text();
	String username_Text();
	String password_Text();
	String login_Text();
	String message_Text();
	String admin_user_Text();
	String admin_user_title_Text();
	String admin_security_Text();
	String admin_security_title_Text();
	String classification_Text();
	String admin_classification_title_Text();
	String admin_keyword_Text();
	String admin_keyword_title_Text();
	String admin_udf_Text();
	String admin_udf_title_Text();
	String admin_rectypes_Text();
	String admin_rectypes_title_Text();
	String reports_Text();
	String admin_reports_title_Text();
	String admin_language_Text();
	String admin_language_title_Text();
	String admin_modules_Text();
	String admin_modules_title_Text();
	String not_implemented_Text();
	String not_implemented_msg_Text();
	String password_reenter_Text();
	String set_password_Text();
	String cancel_Text();
	String uploading_Text();
	String site_title_Text();
	String lang_Text();
	String homepage_Text();
	String home_title_Text();
	String search_Text();
	String search_title_Text();
	String newrec_Text();
	String newrec_title_Text();
	String report_Text();
	String report_title_Text();
	String admin_Text();
	String admin_title_Text();
	String browse_Text();
	String saved_searches_Text();
	String trays_Text();
	String checkouts_Text();
	String name_Text();
	String retention_Text();
	String security_level_Text();
	String security_caveat_Text();
	String security_levels_Text();
	String security_caveats_Text();
	String save_Text();
	String delete_Text();
	String new_Text();
	String new_child_Text();
	String new_keyword_hierarchy_Text();
	String keyword_hierarchy_Text();
	String choose_hierarchy_Text();
	String description_Text();
	String udf_Text();
	String title_Text();
	String template_Text();
	String criteria_Text();
	String level_Text();
	String type_Text();
	String keywords_Text();
	String type_integer_Text();
	String type_date_Text();
	String type_decimal_Text();
	String type_string_Text();
	String type_keyword_Text();
	String type_unknown_Text();
	String edit_user_Text();
	String new_user_Text();
	String forename_Text();
	String surname_Text();
	String is_admin_Text();
	String invalid_password_Text();
	String invalid_password_msg_Text();
	String warning_Text();
	String user_msg_Text();
	String edit_Text();
	String confirm_Text();
	String del_user_msg_Text();
	String check_in_msg_Text();
	String check_in_Text();
	String check_in_version_Text();
	String ok_Text();
	String no_rec_checkout_msg_Text();
	String no_classes_Text();
	String class_chooser_Text();
	String loading_Text();
	String rec_num_Text();
	String date_created_Text();
	String owner_Text();
	String author_Text();
	String notes_Text();
	String int_Text();
	String dec_Text();
	String date_format_Text();
	String req_field_marker_Text();
	String req_field_Text();
	String document_Text();
	String error_Text();
	String new_rec_error_Text();
	String new_record_Text();
	String select_rec_type_Text();
	String record_props_Text();
	String check_in_new_doc_Text();
	String change_rec_num_Text();
	String records_Text();
	String rec_count_Text();
	String with_Text();
	String no_one_Text();
	String refresh_Text();
	String refresh_title_Text();
	String print_Text();
	String print_title_Text();
	String commands_Text();
	String props_Text();
	String props_title_Text();
	String view_Text();
	String view_title_Text();
	String checkout_Text();
	String checkout_title_Text();
	String add_to_tray_Text();
	String add_to_tray_title_Text();
	String remove_from_tray_Text();
	String remove_from_tray_title_Text();
	String admin_commands_Text();
	String change_rec_num_title_Text();
	String undo_checkout_Text();
	String del_rec_Text();
	String del_rec_title_Text(); 
	String cannot_checkout_Text();
	String cannot_checkout_msg_Text();
	String confirm_del_Text();
	String confirm_del_msg_Text();
	String no_electronic_Text();
	String no_electronic_msg_Text();
	String confirm_checkin_Text();
	String confirm_checkin_msg_Text();
	String close_Text();
	String run_report_Text();
	String quick_search_Text();
	String save_search_Text();
	String no_saved_searches_Text();
	String field_Text();
	String and_Text();
	String or_Text();
	String add_Text();
	String clear_Text();
	String dates_Text();
	String ints_Text();
	String decs_Text();
	String strings_Text();
	String date_reg_Text();
	String last_mod_Text();
	String review_date_Text();
	String search_results_Text();
	String add_to_tray_error_Text();
	String no_trays_Text();
	String previous_Text();
	String next_Text();
	String finish_Text();
	String wizard_invalid_Text();
	String rapid_title_Text();
	String user_Text();
	String checked_out_Text();
	String checked_in_Text();
	String checked_out_date_Text();
	String timeout_Text();
	String timeout_message_Text();
	String malformed_Text();
}
