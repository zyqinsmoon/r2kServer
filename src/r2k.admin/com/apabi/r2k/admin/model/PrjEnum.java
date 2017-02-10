package com.apabi.r2k.admin.model;

public class PrjEnum {
	
	//alias
	public static final String TABLE_ALIAS = "AuthEnum";
	public static final String ALIAS_ENUM_ID = "enumId";
	public static final String ALIAS_ENUM_NAME = "enumName";
	public static final String ALIAS_ENUM_VALUE = "enumValue";
	public static final String ALIAS_ENUM_CODE = "enumCode";
	public static final String ALIAS_CRT_DATE = "crtDate";
	public static final String ALIAS_ENUM_TYPE = "enumType";
	
	private java.lang.Integer enumId;
	private java.lang.String enumName;
	private java.lang.String enumValue;
	private java.lang.String enumCode;
	private java.util.Date crtDate;
	private java.lang.String enumType;
	private int enumSort;
	private String interfaceUrl;
	private String enumDesc;
	private String devDef;

	public static final String ENUMTYPE_RES = "AUTH_RES";				//ENUMTYPE为AUTH_RES
	public static final String ENUMTYPE_DEVICETYPE = "DEVICE_TYPE";		//ENUMTYPE为DEVICE_TYPE
	public static final String ENUMTYPE_RES_ADMIN = "admin";			//dev_def中包含admin
	
	public static final String ENUMTYPE_RES_EBOOK = "1";				//AUTH_RES:电子书授权
	public static final String ENUMTYPE_RES_PAPER = "2";				//AUTH_RES:电子报授权
	public static final String ENUMTYPE_RES_TOPIC = "3";				//AUTH_RES:报纸专题授权
	public static final String ENUMTYPE_RES_PUBLISH = "4";				//AUTH_RES:资源授权
	public static final String ENUMTYPE_RES_PICTURE = "5";				//AUTH_RES:图片授权
	
	public static final String ENUM_VALUE_BOOKS = "books";		
	public static final String ENUM_VALUE_PAPER = "newspaper";
	public static final String ENUM_VALUE_TOPIC = "topic";
	public static final String ENUM_VALUE_PUBLISH = "publish";
	public static final String ENUM_VALUE_PICTURE = "picture";
	public static final String ENUM_VALUE_NAVIGATION = "navigation";
	public static final String ENUM_VALUE_USER = "user";
	
	public void setEnumId(java.lang.Integer value) {
		this.enumId = value;
	}
	public java.lang.Integer getEnumId() {
		return this.enumId;
	}
	public void setEnumName(java.lang.String value) {
		this.enumName = value;
	}
	public java.lang.String getEnumName() {
		return this.enumName;
	}
	public void setEnumValue(java.lang.String value) {
		this.enumValue = value;
	}
	public java.lang.String getEnumValue() {
		return this.enumValue;
	}
	public void setEnumCode(java.lang.String value) {
		this.enumCode = value;
	}
	public java.lang.String getEnumCode() {
		return this.enumCode;
	}
	public void setCrtDate(java.util.Date value) {
		this.crtDate = value;
	}
	public java.util.Date getCrtDate() {
		return this.crtDate;
	}
	public void setEnumType(java.lang.String value) {
		this.enumType = value;
	}
	public java.lang.String getEnumType() {
		return this.enumType;
	}
	public synchronized int getEnumSort() {
		return enumSort;
	}
	public synchronized void setEnumSort(int enumSort) {
		this.enumSort = enumSort;
	}
	public synchronized String getInterfaceUrl() {
		return interfaceUrl;
	}
	public synchronized void setInterfaceUrl(String interfaceUrl) {
		this.interfaceUrl = interfaceUrl;
	}
	public synchronized String getEnumDesc() {
		return enumDesc;
	}
	public synchronized void setEnumDesc(String enumDesc) {
		this.enumDesc = enumDesc;
	}
	

	public String getDevDef() {
		return devDef;
	}
	public void setDevDef(String devDef) {
		this.devDef = devDef;
	}
}

