package com.apabi.r2k.msg.service;

import com.apabi.r2k.msg.service.impl.MsgRequest;
import com.apabi.r2k.msg.service.impl.MsgResponse;




public interface MsgProducer {
	//获取消息接口
    public void produceMsg(MsgRequest msgRequest,MsgResponse msgResponse)throws Exception;
	
}
