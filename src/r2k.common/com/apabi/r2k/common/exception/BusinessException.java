package com.apabi.r2k.common.exception;

/**
 * @author : liangjun
 * @projectName : r2kServer
 * @className : BusinessException.java
 * @date 2014-7-24 : 下午05:00:23
 * @description : TODO 业务逻辑相关异常
 */
@SuppressWarnings("serial")
public class BusinessException extends Exception{
	
	// 异常码
	private String code;
	
	public BusinessException() {
		
	}
	
	public BusinessException(String message) {
		super(message);
	}
	
	public BusinessException(String code, String message) {
		super(message);
		this.code = code;
	}
	
	public BusinessException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public static BusinessException businessExcepion() {
		return new BusinessException("110", "业务逻辑异常");
	}
	

}
