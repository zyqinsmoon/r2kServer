package com.apabi.r2k.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

	/**
	 * 检查字符串source中是否含有pattern模式的子串
	 */
	public static boolean match(String source, String pattern){
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source);
		return m.find();
	}
}
