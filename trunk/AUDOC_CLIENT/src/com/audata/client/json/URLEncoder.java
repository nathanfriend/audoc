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
package com.audata.client.json;

import com.google.gwt.http.client.URL;

public class URLEncoder {

	  public static String buildQueryString(Entry[] queryEntries) {
	    StringBuffer sb = new StringBuffer();

	    for (int i = 0, n = queryEntries.length; i < n; ++i) {
	      Entry queryEntry = queryEntries[i];

	      if (i > 0) {
	        sb.append("&");
	      }

	      // encode the characters in the name
	      String encodedName = URL.encodeComponent(queryEntry.getName());
	      sb.append(encodedName);
	      
	      sb.append("=");
	    
	      // encode the characters in the value
	      String encodedValue = URL.encodeComponent(queryEntry.getValue());
	      sb.append(encodedValue);
	    }
	    
	    return sb.toString();
	  }
}
