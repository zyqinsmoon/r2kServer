package com.apabi.r2k.msg.model;


public class R2kMessage {
	//msg类型
	private String msgType;
	//msg
	private Object msg;
	
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


}
