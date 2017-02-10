package com.apabi.r2k.admin.model;

public class AuthEnum {
	
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

	public static final String ENUMTYPE_RES = "AUTH_RES";		//ENUMTYPE为AUTH_RES
	
	
	
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
}

