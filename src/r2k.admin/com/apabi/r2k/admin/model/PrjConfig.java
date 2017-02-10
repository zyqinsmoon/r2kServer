package com.apabi.r2k.admin.model;


import java.util.Date;
import java.util.Map;

import com.apabi.r2k.common.utils.CollectionUtils;

public class PrjConfig {
	
	//alias
	public static final String TABLE_ALIAS = "PrjConfig";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_ORG_ID = "orgId";
	public static final String ALIAS_DEVICE_TYPE = "deviceType";
	public static final String ALIAS_DEVICE_ID = "deviceId";
	public static final String ALIAS_CONFIG_KEY = "配置项字段";
	public static final String ALIAS_VALUE = "配置项值";
	public static final String ALIAS_CRT_DATE = "crtDate";
	public static final String ALIAS_LAST_DATE = "lastDate";
	public static final String ALIAS_ENABLE = "是否启用0是1否";
	public static final String ALIAS_REMARK = "配置项描述";
	public static final String ALIAS_CONFIG_NAME = "配置项名称";
	
	private java.lang.Integer id;
	private java.lang.String orgId;
	private java.lang.String deviceType;
	private java.lang.String deviceId;
	private java.lang.String configKey;
	private java.lang.String configValue;
	private java.util.Date crtDate;
	private java.util.Date lastDate;
	private java.lang.String enable;
	private java.lang.String remark;
	private java.lang.String configName;

	public PrjConfig(){}
	public PrjConfig(String orgId, String deviceType, String deviceId, String configKey, String configValue, String enable){
		this(orgId, deviceType, deviceId, configKey, configValue, enable, null, null, null, null);
		Date now = new Date();
		this.setCrtDate(now);
		this.setLastDate(now);
	}
	public PrjConfig(String orgId, String deviceType, String deviceId, String configKey, String configValue, String enable,
			Date crtDate, Date lastDate, String configName, String remark){
		this.setCrtDate(crtDate);
		this.setLastDate(lastDate);
		this.setOrgId(orgId);
		this.setDeviceType(deviceType);
		if(deviceId != null && !"".equals(deviceId)){
			this.setDeviceId(deviceId);
		} else {
			this.setDeviceId(null);
		}
		this.setConfigKey(configKey);
		this.setEnable(enable);
		this.setConfigName(configName);
		this.setRemark(remark);
		this.setConfigValue(configValue);
	}
	
	
	public final static String CONFIG_ENABLE = "0";	//启用
	public final static String CONFIG_UNABLE = "1"; //不启用
	
	/*
	 * 所有配置项key
	 */
	public final static String R2K_NAV_CENTER_LOGO = "center_logo";	//触摸屏导航页中心logo
	public final static String R2k_NAV_BUTTON_LOGO = "button_logo"; //触摸屏菜单logo
	public final static String DEVICE_START_TIME = "dev_open_time"; 	//触摸屏开机时间
	public final static String DEVICE_END_TIME = "dev_close_time"; 		//触摸屏关机时间
	public final static String DEVICE_HOTSPOT = "dev_hotspot_open"; 	//触摸屏wifi设置
	
	public static final Map<Integer, String> configNameMap = CollectionUtils.buildMap(
			R2K_NAV_CENTER_LOGO, "触摸屏导航页中心logo",
			R2k_NAV_BUTTON_LOGO, "触摸屏菜单logo",
			DEVICE_START_TIME, "触摸屏开机时间",
			DEVICE_END_TIME, "触摸屏关机时间",
			DEVICE_HOTSPOT, "触摸屏wifi设置"
	);
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setOrgId(java.lang.String value) {
		this.orgId = value;
	}
	
	public java.lang.String getOrgId() {
		return this.orgId;
	}
	public void setDeviceType(java.lang.String value) {
		this.deviceType = value;
	}
	
	public java.lang.String getDeviceType() {
		return this.deviceType;
	}
	public void setDeviceId(java.lang.String value) {
		this.deviceId = value;
	}
	
	public java.lang.String getDeviceId() {
		return this.deviceId;
	}
	public void setConfigKey(java.lang.String value) {
		this.configKey = value;
	}
	
	public java.lang.String getConfigKey() {
		return this.configKey;
	}
	public void setCrtDate(java.util.Date value) {
		this.crtDate = value;
	}
	
	public java.util.Date getCrtDate() {
		return this.crtDate;
	}
	
	public void setLastDate(java.util.Date value) {
		this.lastDate = value;
	}
	
	public java.util.Date getLastDate() {
		return this.lastDate;
	}
	public void setEnable(java.lang.String value) {
		this.enable = value;
	}
	
	public java.lang.String getEnable() {
		return this.enable;
	}
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	
	public java.lang.String getRemark() {
		return this.remark;
	}
	public void setConfigName(java.lang.String value) {
		this.configName = value;
	}
	
	public java.lang.String getConfigName() {
		return this.configName;
	}
	public java.lang.String getConfigValue() {
		return configValue;
	}
	public void setConfigValue(java.lang.String configValue) {
		this.configValue = configValue;
	}
}

