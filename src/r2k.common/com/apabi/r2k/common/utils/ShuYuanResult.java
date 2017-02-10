package com.apabi.r2k.common.utils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 书苑登录结果
 */
@XStreamAlias("Return")
public class ShuYuanResult {

	@XStreamAsAttribute
	@XStreamAlias("Code")
	private String code;
	@XStreamAsAttribute
	@XStreamAlias("Message")
	private String message;
	@XStreamAlias("token")
	private String token;
	@XStreamAlias("uid")
	private String uid;
	@XStreamAlias("orgidentifier")
	private String orgidentifier;
	@XStreamAlias("orgname")
	private String orgname;
	
	public final static String LOGOUT_CODE_SUCCESS = "0";	//注销成功
	public final static String LOGOUT_CODE_FAIL = "-1";		//注销失败
	
	public final static String LOGIN_CODE_SUCCESS = "0";	//登录成功
	
	public ShuYuanResult(){}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getOrgidentifier() {
		return orgidentifier;
	}

	public void setOrgidentifier(String orgidentifier) {
		this.orgidentifier = orgidentifier;
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
}
