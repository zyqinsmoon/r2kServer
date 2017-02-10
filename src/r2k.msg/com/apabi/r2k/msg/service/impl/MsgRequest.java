package com.apabi.r2k.msg.service.impl;

import javax.servlet.http.HttpServletRequest;

import com.apabi.r2k.common.utils.PropertiesUtil;

public class MsgRequest {
	private String msgType;
	private Object msg;
	private HttpServletRequest  request;

	public MsgRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public String getOrgId(){
		return request.getParameter("orgid");
	}
	
	public String[] getParameterValues(String name){
		return request.getParameterValues(name);
	}

	public String getValue(String name) {
		return request.getParameter(name);
	}


	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public Object getMsg() {
		return msg;
	}

	public void setMsg(Object msg) {
		this.msg = msg;
	}

    public String getServerType(){
    	return PropertiesUtil.get("server.type");
    }

}
