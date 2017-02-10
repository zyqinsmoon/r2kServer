package com.apabi.r2k.common.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;

public class PropertiesUtil {
	private static PropertiesConfiguration config = null;
	static {
		try {
//			config = new PropertiesConfiguration("properties/config.properties");
			config = new PropertiesConfiguration();
			config.setEncoding("UTF-8");
			config.load(PropertiesUtil.class.getClassLoader().getResource("properties/config.properties").toString());
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 根据key得到value的值
	 * 
	 * @throws IOException
	 */
	public static String getValue(String key) throws IOException {
		return config.getString(key);
	}

	public static String getSystemProperty(String key) throws Exception{
		return System.getProperty(key);
	}
	
	public static String getRootPath() throws Exception{
		String dir = getSystemProperty("catalina.home");
		if(StringUtils.isNotBlank(dir)){
			return dir + "/webapps";
		}
		return null;
	}
	
	public static String get(final String key) {
		return config.getString(key);
	}

	public static String get(final String key, final String defaultValue) {
		return config.getString(key, defaultValue);
	}

	public static boolean getBoolean(final String key) {
		return config.getBoolean(key);
	}

	public static boolean getBoolean(final String key, final boolean defaultValue) {
		return config.getBoolean(key, defaultValue);
	}

	public static int getInt(final String key) {
		return config.getInt(key);
	}

	public static int getInt(final String key, final int defaultValue) {
		return config.getInt(key, defaultValue);
	}

	public static double getDouble(final String key) {
		return config.getDouble(key);
	}

	public static double getDouble(final String key, final double defaultValue) {
		return config.getDouble(key, defaultValue);
	}

	public static long getLong(final String key) {
		return config.getLong(key);
	}

	public static long getLong(final String key, final long defaultValue) {
		return config.getLong(key, defaultValue);
	}

	public static List<String> getList(final String key) {
		return config.getList(key);
	}

	public static List<String> getList(final String key, final List<String> defaultValue) {
		return config.getList(key, defaultValue);
	}

	public static String[] getStringArray(final String key) {
		return config.getStringArray(key);
	}
	
	public static void saveConfig(String key, Object value){
		try {
			config.addProperty(key, value);
			config.save(PropertiesUtil.class.getClassLoader().getResource("properties/config.properties").toString());
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static Iterator<String> getKeys(String prefix){
		return config.getKeys(prefix);
	}
	
 	public static void main(String[] args) throws Exception {

		System.out.println(PropertiesUtil.get("url.base.r2k"));
	}

}
