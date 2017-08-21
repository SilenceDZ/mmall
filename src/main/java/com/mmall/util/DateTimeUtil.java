package com.mmall.util;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeUtil {
	//joda-time
	//str->Date
	//Date->str
	
	public static final String STANDARD_FORMAT="yyyy-MM-DD HH:mm:ss";
	
	/**
	 *Title:strToDate
	 *Description:字符串转日期
	 *@param dateTimeStr
	 *@param formatStr
	 *@return
	 *Throws
	 */
	public static Date strToDate(String dateTimeStr,String formatStr){
		DateTimeFormatter dateTimeFormatter=DateTimeFormat.forPattern(formatStr);
		DateTime dateTime =dateTimeFormatter.parseDateTime(dateTimeStr);
		return dateTime.toDate();
	}
	
	/**
	 *Title:dateToStr
	 *Description:日期装换成字符串
	 *@param date
	 *@param format
	 *@return
	 *Throws
	 */
	public static String dateToStr(Date date,String format){
		if(date==null){
			return StringUtils.EMPTY;
		}
		DateTime dateTime=new DateTime(date);
		return dateTime.toString(format);
	}
	 
	
	public static Date strToDate(String dateTimeStr){
		DateTimeFormatter dateTimeFormatter=DateTimeFormat.forPattern(STANDARD_FORMAT);
		DateTime dateTime =dateTimeFormatter.parseDateTime(dateTimeStr);
		return dateTime.toDate();
	}
	
	public static String dateToStr(Date date){
		if(date==null){
			return StringUtils.EMPTY;
		}
		DateTime dateTime=new DateTime(date);
		return dateTime.toString(STANDARD_FORMAT);
	}
}
