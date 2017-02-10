package com.apabi.r2k.common.base;

import cn.org.rapid_framework.util.DateConvertUtils;


/**
 * @author badqiu
 */
public class BaseEntity implements java.io.Serializable {

	private static final long serialVersionUID = -7200095849148417467L;

	protected static final String DATE_FORMAT = "yyyy-MM-dd";
	
	protected static final String TIME_FORMAT = "HH:mm:ss";
	
	protected static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	protected static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
	public String date2String(final java.util.Date date, final String dateFormat) {
		return DateConvertUtils.format(date, dateFormat);
	}

	public <T extends java.util.Date> T string2Date(
			final String dateString, final String dateFormat,
			final Class<T> targetResultType) {
		return DateConvertUtils.parse(dateString, dateFormat, targetResultType);
	}
	
}
