package com.apabi.r2k.msg.service;




public interface MyMsgService {
	//获取消息接口
	public void getMyMsg(String msgtype);
	
	//处理消息接口
	public void MsgMyHandler();
	
	//
	public void replayMyMsg(String msgid);
}
