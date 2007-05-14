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

public class Criteria {

	private String fieldName;
	private String disName;
	private boolean isUDF;
	private String value1;
	private String disValue1;
	private String value2;
	private String disValue2;
	private String operator;
	
	public Criteria(String fieldName, String disName, boolean isUDF, String value1, String disValue1, String value2, String disValue2, String operator){
		this.fieldName = fieldName;
		this.disName = disName;
		this.isUDF = isUDF;
		this.value1 = value1;
		this.disValue1 = disValue1;
		this.value2 = value2;
		this.disValue2 = disValue2;
		this.operator = operator;
	}
	
	public Criteria(String fieldName, String disName, boolean isUDF, String value1, String disValue1, String operator){
		this(fieldName, disName, isUDF, value1, disValue1, null, null, operator);
	}
	
	public String getDisplayString(){
		String ret = "";
		if(this.value2 == null){
			//range criteria
			ret = this.operator + " " + this.disName +" : "+ this.disValue1;
		}else{
			//single value
			ret = this.operator + " " + this.disName +" : "+ this.disValue1 +" - "+ this.disValue2;
		}
		ret = ret.trim();
		return ret;
	}
	
	public String getSearchString(){
		String ret = "";
		String udf = "";
		if(this.isUDF){
			udf = "(udf)";
		}
		if(this.value2 == null){
			//range criteria
			ret = this.operator + " " + udf + this.fieldName +" : "+ this.value1;
		}else{
			//single value
			ret = this.operator + " " + udf + this.fieldName +" : "+ this.value1 +" - "+ this.value2;
		}
		ret = ret.trim();
		return ret;
	}
}
