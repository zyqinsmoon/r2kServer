package com.apabi.r2k.msg.service;

import com.apabi.r2k.msg.service.impl.MsgRequest;
import com.apabi.r2k.msg.service.impl.MsgResponse;


public interface MsgHandler {
	public void handlerMsg(MsgRequest msgRequest,MsgResponse msgResponse);
}
