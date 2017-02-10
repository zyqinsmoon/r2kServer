package com.apabi.r2k.api.exception;

import java.util.HashMap;
import java.util.Map;

public class ApiException extends Exception{

	
	private static final long serialVersionUID = -2445488256264617507L;

	private String code;
		
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public ApiException(String message) {
		super(message);
	}

	public ApiException(String code, String message) {
		super(message);
		this.code = code;
	}

	public ApiException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}
	
	/**
	 * api不存在.
	 */
	public static ApiException unknownApi() {
		return new ApiException("-1001", "API不存在");
	}
	/**
	 * 无操作权限.
	 */
	public static ApiException noPermission() {
		return new ApiException("-1002", "无操作权限");
	}
	
	
	/**
	 * 参数无效.
	 */
	public static ApiException invalidParams() {
		return new ApiException("-1003", "参数无效");
	}
	
	
	
	/**
	 * 返回操作结果失败.
	 */
	public static ApiException responseFailed(String msg) {
		return new ApiException("-1004", msg);
	}
	
	
	public static Map<String, Object> makeMode(String code,String message){
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("code", code);
		model.put("message", message);
		return model;
	}
	
	
}
