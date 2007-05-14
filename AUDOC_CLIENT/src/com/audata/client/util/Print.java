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
package com.audata.client.util;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.UIObject;

public class Print {

    public static native void it() /*-{
        $wnd.print();
    }-*/;

    public static native void it(String html) /*-{
        var frame = $doc.getElementById('__printingFrame');
        if (!frame) {
            $wnd.alert("Error: Can't find printing frame.");
            return;
        }
        frame = frame.contentWindow;
        var doc = frame.document;
        doc.open();
        doc.write(html);
        doc.close();
        frame.focus();
        frame.print();
    }-*/;

    public static void it(UIObject obj) {
        it("", obj.getElement().toString());
    }

    public static void it(Element element) {
        it("", element.toString());
    }

    public static void it(String style, String it) {
    	it("<html><header>"+style+"</header><body>"+it+"</body></html>"); 
    }

    public static void it(String style, UIObject obj) {
        it(style, obj.getElement().toString());
    }

    public static void it(String style, Element element) {
        it(style, element.toString());
    } 
}