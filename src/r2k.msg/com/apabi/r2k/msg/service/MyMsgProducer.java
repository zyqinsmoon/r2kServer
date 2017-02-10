package com.apabi.r2k.msg.service;

import com.apabi.r2k.msg.service.impl.MsgRequest;
import com.apabi.r2k.msg.service.impl.MsgResponse;

public interface MyMsgProducer {

	public MsgResponse myProduceMsg(MsgRequest msgRequest,MsgResponse msgResponse);
}
