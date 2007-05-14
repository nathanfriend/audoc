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

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;

/**
 *  
 * @author jonm
 *
 */
public class UpdateResponseHandler extends BaseRequestCallback{
	
	private UpdateListener listener;
	
	public UpdateResponseHandler(UpdateListener listener){
		this.listener = listener;
	}

	public void onResponseReceived(Request request, Response response) {
		//just check its ok
		this.parseJSON(response);
		this.listener.onUpdate();
	}

}