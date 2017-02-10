package com.apabi.r2k.msg.service.impl;

import java.util.Properties;

import com.apabi.r2k.msg.service.MsgHandler;

public class MsgHandlerImpl implements MsgHandler{

	private Properties msgProp ;
	public Properties getMsgProp() {
		return msgProp;
	}
	public void setMsgProp(Properties msgProp) {
		this.msgProp = msgProp;
	}
	
	@Override
	public void handlerMsg(MsgRequest msgRequest,MsgResponse msgResponse) {
		MsgHandler msgHandler = (MsgHandler)msgProp.get(msgRequest.getMsgType());
		msgHandler.handlerMsg(msgRequest,msgResponse);
		
	}

}
