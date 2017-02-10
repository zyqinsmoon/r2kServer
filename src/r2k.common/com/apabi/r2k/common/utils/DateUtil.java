package com.apabi.r2k.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static String SOLR_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	public static String formatDate(Date date) {
		return formatDate(date, null);
	}

	public static String formatDate(Date date, String format) {
		if (format == null) {
			format = DEFAULT_DATE_FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String currentDate(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = new Date();
		return sdf.format(date);
	}

	public static int PeriodRelation(Date oldStartDate, Date oldEndDate,
			Date newStartDate, Date newEndDate) {
		if (oldEndDate.after(newStartDate) && oldStartDate.before(newEndDate)) {
			return 1;// 1.old new部分重合
		} else if (oldStartDate.before(newEndDate)
				&& oldEndDate.after(newStartDate)) {
			return 2;// 2.new old部分重合
		} else if (oldEndDate.equals(newStartDate)) {
			return 3;// 3.old连接new
		} else if (oldStartDate.equals(newEndDate)) {
			return 4;// 4.new连接old
		} else if (oldEndDate.before(newStartDate)
				|| oldStartDate.after(newEndDate)) {
			return 5;// 5.不重合
		} else if (oldEndDate.before(newEndDate)
				&& oldStartDate.after(newStartDate)) {
			return 6;// 6.new包含old
		} else {
			return 7;// 7.old包含new
		}
	}

	public static String twoDateRelation(Date startDate, Date endDate) {
		Date now = new Date();
		// 开始时间大于结束时间
		if (startDate.after(endDate)) {
			return "error1";
		}
		// 开始时间小于当前时间
		if (startDate.before(now)) {
			return "error2";
		}
		return "right";
	}
	
	public static Date getDateFromString(String time, String format){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date date = sdf.parse(time);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date getDateFromString(String time){
		return getDateFromString(time,DEFAULT_DATE_FORMAT);
	}
	
	//比较两个日期是否为同一天
	public static boolean isSameDay(Date date1, Date date2){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strDate1 = sdf.format(date1.getTime());
		String strDate2 = sdf.format(date2.getTime());
		if(strDate1.equals(strDate2)){
			return true;
		}
		return false;
	}
	
	//判断是否为今天
	public static boolean isToday(Date date){
		return isSameDay(date, new Date());
	}
	
	/**
	 * 获取指定天数后的日期
	 */
	public static Date getDateAfterDays(Date date, int days){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + days);
		return calendar.getTime();
	}
	
	/**
	 * 获取指定分钟数之前的时间
	 */
	public static Date getDateBeforeMinutes(Date date, int minutes){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - minutes);
		return calendar.getTime();
	}
	
	public static void main(String[] args) {
//		System.out.println(isToday(getDateFromString("2014/07/03", "yyyy/MM/dd")));
		Date date = getDateBeforeMinutes(new Date(), 10);
		System.out.println(formatDate(date));
	}
}
